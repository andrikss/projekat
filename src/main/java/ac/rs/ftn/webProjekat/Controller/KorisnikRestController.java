package ac.rs.ftn.webProjekat.Controller;

import ac.rs.ftn.webProjekat.Dto.*;
import ac.rs.ftn.webProjekat.Entity.*;
import ac.rs.ftn.webProjekat.Service.AutorService;
import ac.rs.ftn.webProjekat.Service.KorisnikService;
import ac.rs.ftn.webProjekat.Service.PolicaService;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/korisnici")
public class KorisnikRestController {

    @Autowired
    private KorisnikService korisnikService;

    @Autowired
    private AutorService autorService;

    @Autowired
    private PolicaService policaService;

    //lista korisnika
    @GetMapping("/lista")
    public ResponseEntity<List<KorisnikDto>> getAllKorisnici() {
        List<Korisnik> korisnici = korisnikService.findAllKorisnik();

        List<KorisnikDto> korisnikDtos = new ArrayList<>();
        for (Korisnik korisnik : korisnici) {
            if (!korisnik.getUlogaKorisnika().equals(UlogaKorisnika.ADMINISTRATOR.toString())) {
                korisnikDtos.add(new KorisnikDto(korisnik));
                System.out.println("OVO JE KORISNIK: " + korisnik.toString());
            }
        }

        return new ResponseEntity<>(korisnikDtos, HttpStatus.OK);
    }

    //lista autora
    @GetMapping("/listaAutora")
    public ResponseEntity<List<AutorDto>> getAllAutori() {
        List<Autor> autori = korisnikService.findAllAutor();

        List<AutorDto> autorDtos = new ArrayList<>();
        for (Autor autor : autori) {
            if(autor.isAktivan()) {
                autorDtos.add(new AutorDto(autor));
            }
        }

        return new ResponseEntity<>(autorDtos, HttpStatus.OK);
    }

    @GetMapping("/korisnik")
    public ResponseEntity<KorisnikDto> getThisSessionUserInfo(HttpSession httpSession)
    {
        Korisnik loggedKorisnik = (Korisnik) httpSession.getAttribute("loggedUser");
        if (loggedKorisnik == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        loggedKorisnik = korisnikService.findById(loggedKorisnik.getId());

        KorisnikDto loggedUserDto = new KorisnikDto(loggedKorisnik);
        return new ResponseEntity<KorisnikDto>(loggedUserDto, HttpStatus.OK);
    }

    //prikaz jednog korisnika
    @GetMapping("/{id}")
    public ResponseEntity<?> getKorisnikById(@PathVariable(name = "id") Long id) {
        Korisnik targetKorisnik = korisnikService.findById(id);

        if (targetKorisnik == null) {
            // korisnik sa datim id-om ne postoji
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        if (targetKorisnik.getUlogaKorisnika().equals(UlogaKorisnika.ADMINISTRATOR.toString())) {
            String errorMessage = "Taj korisnik je administrator!";
            return new ResponseEntity<String>(errorMessage, HttpStatus.FORBIDDEN);
        }

        KorisnikDto targetKorisnikDto = new KorisnikDto(targetKorisnik);

        // uspjesno
        return new ResponseEntity<>(targetKorisnikDto, HttpStatus.OK);
    }

    //prikaz jednog autora
    @GetMapping("/autor/{id}")
    public ResponseEntity<?> getAutorById(@PathVariable(name = "id") Long id) {
        Korisnik targetKorisnik = korisnikService.findAutorById(id);

        if (targetKorisnik == null) {
            String errorMessage = "Taj korisnik ne postoji ili nije autor.";
            return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
        }

        Autor targetAutor = (Autor) targetKorisnik;
        AutorDto a = new AutorDto(targetAutor);
        return new ResponseEntity<>(a, HttpStatus.OK);
    }


    //prikaz korisnikovih polica
    @GetMapping("/{id}/police")
    public ResponseEntity<?> getPoliciesForKorisnik(@PathVariable(name = "id") Long id) {
        Korisnik targetKorisnik = korisnikService.findById(id);

        if (targetKorisnik == null) {
            //korisnik ne posotji
            return new ResponseEntity<>("Taj korisnik ne postoji!", HttpStatus.NOT_FOUND);
        }

        if (targetKorisnik.getUlogaKorisnika().equals(UlogaKorisnika.ADMINISTRATOR.toString())) {
            // ne moze adm
            return new ResponseEntity<>("Taj korisnik je administrator!", HttpStatus.FORBIDDEN);
        }

        List<Polica> korisnikovePolice = policaService.findPolicaByKorisnikId(targetKorisnik.getId());

        List<PolicaDto> policaDtos = new ArrayList<>();
        for (Polica polica : korisnikovePolice) {
            policaDtos.add(new PolicaDto(polica));
        }

        // oki
        return new ResponseEntity<>(policaDtos, HttpStatus.OK);
    }

    //prijavi se
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UlogujSeDto loginDto, HttpSession httpSession) {
        if (loginDto.getEmailAdresa() == null || loginDto.getEmailAdresa().isEmpty()) {
            return new ResponseEntity<>("Unesite email adresu!", HttpStatus.BAD_REQUEST);
        }

        if (loginDto.getEmailAdresa().isEmpty() || loginDto.getLozinka().isEmpty()) {
            return new ResponseEntity<>("Unesite lozinku", HttpStatus.BAD_REQUEST);
        }

        Korisnik korisnik = korisnikService.findByEmail(loginDto.getEmailAdresa());
        if (korisnik == null || !korisnik.getLozinka().equals(loginDto.getLozinka())) {
            return new ResponseEntity<>("Netacna lozinka ili mejl!", HttpStatus.UNAUTHORIZED);
        }

        httpSession.setAttribute("loggedUser", korisnik);
        KorisnikDto k = new KorisnikDto(korisnik);
        return new ResponseEntity<>(k, HttpStatus.OK);
    }

    //registruj se

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegistrujSeDto registracijaDto, HttpSession httpSession) {
        if (registracijaDto.getIme().isEmpty() || registracijaDto.getPrezime().isEmpty() ||
                registracijaDto.getKorisnickoIme().isEmpty() || registracijaDto.getEmailAdresa().isEmpty() ||
                registracijaDto.getLozinka().isEmpty() || registracijaDto.getPonovljenaLozinka().isEmpty() ||
                registracijaDto.getDatumRodjenja() == null) {
            return new ResponseEntity<>("Niste popunili sva polja", HttpStatus.BAD_REQUEST);
        }

        if (!registracijaDto.getLozinka().equals(registracijaDto.getPonovljenaLozinka())) {
            return new ResponseEntity<>("Lozinke se ne podudaraju", HttpStatus.BAD_REQUEST); // Lozinke se ne podudaraju
        }

        Korisnik noviKorisnik = new Korisnik(registracijaDto);
        System.out.println(noviKorisnik.getPolice().toString());

        if (korisnikService.daLiPostojiDuplikat(noviKorisnik)) {
            return new ResponseEntity<>("Korisnik vec postoji!", HttpStatus.CONFLICT); // Konflikt jer postoji
        }

        // Pravim novog korisnika
        noviKorisnik.setIme(registracijaDto.getIme());
        noviKorisnik.setPrezime(registracijaDto.getPrezime());
        noviKorisnik.setKorisnickoIme(registracijaDto.getKorisnickoIme());
        noviKorisnik.setEmailAdresa(registracijaDto.getEmailAdresa());
        noviKorisnik.setLozinka(registracijaDto.getLozinka());
        noviKorisnik.setDatumRodjenja(registracijaDto.getDatumRodjenja());
        noviKorisnik.setOpis(registracijaDto.getOpis());
        noviKorisnik.setUlogaKorisnika(UlogaKorisnika.CITALAC.toString());

        korisnikService.saveKorisnik(noviKorisnik);
        Korisnik kor = korisnikService.findByEmail(noviKorisnik.getEmailAdresa());
        httpSession.setAttribute("loggedUser", kor);
        KorisnikDto k = new KorisnikDto(kor);
        return new ResponseEntity<>(k, HttpStatus.CREATED); // Kreiran - uspješno registrovan
    }

    //azuriranje korisnika po idu
    //azuriranje korisnika
   /* @PutMapping("/updateKorisnik/{id}")
    public ResponseEntity<?> updateKorisnik(@PathVariable(name = "id") Long id, @RequestBody AzurirajKorisnikaDto azurirajKorisnikaDto, HttpSession httpSession) {
        Korisnik korisnik = korisnikService.findById(id);

        if (korisnik == null) {
            return new ResponseEntity<>("Korisnik ne postoji", HttpStatus.NOT_FOUND); // ne postoji
        }

        Korisnik loggedUser = (Korisnik) httpSession.getAttribute("loggedUser");

        if (loggedUser == null || !loggedUser.getId().equals(id)) {
            return new ResponseEntity<>("Ne mozete azurirati acc ako se prije niste ulogovali na nj!", HttpStatus.FORBIDDEN); // zabranjen pristup
        }

        // verifikacija trenutne lozinke ako korisnik mijenja mejl adresu ili lozinku
        if (!korisnik.getLozinka().equals(azurirajKorisnikaDto.getStaraLozinka())) {
            return new ResponseEntity<>("Lozinke nisu okej!", HttpStatus.UNAUTHORIZED); // neispravna trenutna lozinka
        }

        // azuriranje korisnikovih informacija
        korisnik.setIme(azurirajKorisnikaDto.getIme());
        korisnik.setPrezime(azurirajKorisnikaDto.getPrezime());
        korisnik.setDatumRodjenja(azurirajKorisnikaDto.getDatumRodjenja());
        korisnik.setProfilnaSlika(azurirajKorisnikaDto.getProfilnaSlika());
        korisnik.setOpis(azurirajKorisnikaDto.getOpis());

        // azuriranje mejl adrese i lozinke samo ako
        if (!azurirajKorisnikaDto.getNovaEmailAdresa().isEmpty()) {
            korisnik.setEmailAdresa(azurirajKorisnikaDto.getNovaEmailAdresa());
        }

        if (!azurirajKorisnikaDto.getNovaLozinka().isEmpty()) {
            korisnik.setLozinka(azurirajKorisnikaDto.getNovaLozinka());
        }

        korisnik = korisnikService.saveKorisnik(korisnik);

        KorisnikDto k = new KorisnikDto(korisnik);
        return new ResponseEntity<>(k, HttpStatus.OK);
    }*/

    //azuriranje korisnika
    @PostMapping("/updateKorisnik")
    public ResponseEntity<?> updateKorisnikProfile(@RequestBody AzurirajKorisnikaDto azurirajKorisnikaDto, HttpSession httpSession) {

        Korisnik korisnik = (Korisnik) httpSession.getAttribute("loggedUser");
        if (korisnik == null) {
            return new ResponseEntity<>("Korisnik ne postoji", HttpStatus.NOT_FOUND); // ne postoji
        }
        if (!korisnik.getLozinka().equals(azurirajKorisnikaDto.getStaraLozinka())) {
            return new ResponseEntity<>("Lozinke nisu okej!", HttpStatus.UNAUTHORIZED); // neispravna trenutna lozinka
        }

        if(azurirajKorisnikaDto.getIme() != null) {
            korisnik.setIme(azurirajKorisnikaDto.getIme());
        }

        if(azurirajKorisnikaDto.getPrezime() != null) {
            korisnik.setPrezime(azurirajKorisnikaDto.getPrezime());
        }

        if(azurirajKorisnikaDto.getDatumRodjenja() !=null) {
            korisnik.setDatumRodjenja(azurirajKorisnikaDto.getDatumRodjenja());
        }

        if(azurirajKorisnikaDto.getProfilnaSlika() != null) {
            korisnik.setProfilnaSlika(azurirajKorisnikaDto.getProfilnaSlika());
        }

        if(azurirajKorisnikaDto.getOpis() != null) {
            korisnik.setOpis(azurirajKorisnikaDto.getOpis());
        }

        if(azurirajKorisnikaDto.getKorisnickoIme() != null) {
            korisnik.setKorisnickoIme(azurirajKorisnikaDto.getKorisnickoIme());
        }

        if (azurirajKorisnikaDto.getNovaEmailAdresa() != null) {
            korisnik.setEmailAdresa(azurirajKorisnikaDto.getNovaEmailAdresa());
        }

        if (azurirajKorisnikaDto.getNovaLozinka() != null) {
            korisnik.setLozinka(azurirajKorisnikaDto.getNovaLozinka());
        }
        korisnik = korisnikService.saveKorisnik(korisnik);

        KorisnikDto k = new KorisnikDto(korisnik);
        return new ResponseEntity<>(k, HttpStatus.OK);
    }


    //azuriranje korisnika od administratora
    @PostMapping("/updateKorisnik/{id}")
    public ResponseEntity<?> updateKorisnikProfile(@RequestBody AzurirajKorisnikaDto azurirajKorisnikaDto,
                                                   @PathVariable Long id,
                                                   HttpSession httpSession) {

        Korisnik korisnik = korisnikService.findById(id);

        if (korisnik == null) {
            return new ResponseEntity<>("Korisnik ne postoji", HttpStatus.NOT_FOUND); // ne postoji
        }

        if(azurirajKorisnikaDto.getIme() != null) {
            korisnik.setIme(azurirajKorisnikaDto.getIme());
        }

        if(azurirajKorisnikaDto.getPrezime() != null) {
            korisnik.setPrezime(azurirajKorisnikaDto.getPrezime());
        }

        if(azurirajKorisnikaDto.getDatumRodjenja() !=null) {
            korisnik.setDatumRodjenja(azurirajKorisnikaDto.getDatumRodjenja());
        }

        if(azurirajKorisnikaDto.getProfilnaSlika() != null) {
            korisnik.setProfilnaSlika(azurirajKorisnikaDto.getProfilnaSlika());
        }

        if(azurirajKorisnikaDto.getOpis() != null) {
            korisnik.setOpis(azurirajKorisnikaDto.getOpis());
        }

        if(azurirajKorisnikaDto.getKorisnickoIme() != null) {
            korisnik.setKorisnickoIme(azurirajKorisnikaDto.getKorisnickoIme());
        }

        if (azurirajKorisnikaDto.getNovaEmailAdresa() != null) {
            korisnik.setEmailAdresa(azurirajKorisnikaDto.getNovaEmailAdresa());
        }

        if (azurirajKorisnikaDto.getNovaLozinka() != null) {
            korisnik.setLozinka(azurirajKorisnikaDto.getNovaLozinka());
        }
        korisnik = korisnikService.saveKorisnik(korisnik);

        KorisnikDto k = new KorisnikDto(korisnik);
        return new ResponseEntity<>(k, HttpStatus.OK);
    }
    //azuriranje autora
    //basically the same samo sto ima AKTIVAN polje
    @PostMapping("/updateAutor/{id}")
    public ResponseEntity<?> updateAutor(@PathVariable(name = "id") Long id, @RequestBody AutorDto autorDto, HttpSession httpSession) {
        Korisnik loggedUser = (Korisnik) httpSession.getAttribute("loggedUser");

        if (loggedUser == null) {
            return new ResponseEntity<>("No session!", HttpStatus.FORBIDDEN);
        }

        if (!loggedUser.getUlogaKorisnika().equals(UlogaKorisnika.ADMINISTRATOR.toString())) {
            return new ResponseEntity<>("User is not of type ADMINISTRATOR!", HttpStatus.FORBIDDEN);
        }

        Autor autor = (Autor) korisnikService.findById(id);
        //provjera
        if (autor == null) {
            return new ResponseEntity<>("Autor nije pronadjen!", HttpStatus.NOT_FOUND);
        }

       /* if (autor.isAktivan()) {
            return new ResponseEntity<>("Autor je aktivan i ne moze se modifikovati!", HttpStatus.FORBIDDEN);
        }*/

        if (autorDto.getEmailAdresa() != null &&
                !autorDto.getEmailAdresa().equals(autor.getEmailAdresa()) &&
                korisnikService.findByEmail(autorDto.getEmailAdresa()) != null)
        {
            return new ResponseEntity<>("Autor sa email adresom vec postoji", HttpStatus.FORBIDDEN);
        }

        if (autorDto.getKorisnickoIme() != null && !autorDto.getKorisnickoIme().equals(autor.getKorisnickoIme())
                && korisnikService.findByKorisnickoIme(autorDto.getKorisnickoIme()) != null)
        {
            return new ResponseEntity<>("Autor sa korisnickim imenom vec postoji", HttpStatus.FORBIDDEN);
        }

        autor.setIme(autorDto.getIme());
        autor.setPrezime(autorDto.getPrezime());
        autor.setKorisnickoIme(autorDto.getKorisnickoIme());
        autor.setEmailAdresa(autorDto.getEmailAdresa());
        autor.setDatumRodjenja(autorDto.getDatumRodjenja());
        autor.setOpis(autorDto.getOpis());

        korisnikService.saveKorisnik(autor);
        return new ResponseEntity<>("Uspjesno!", HttpStatus.OK);
    }


    //KREIRANJE AUTORA, OVO MOZE SAMO KO???
    // ADMIINISTRATOR WOOP WOOP
    @PostMapping("/kreirajAutora")
    public ResponseEntity<?> createAutor(@RequestBody AutorDto autorDto, HttpSession httpSession) {
        //  je l administrator
        Korisnik loggedUser = (Korisnik) httpSession.getAttribute("loggedUser");
        if (loggedUser == null || !loggedUser.getUlogaKorisnika().equals(UlogaKorisnika.ADMINISTRATOR.toString())) {
            return new ResponseEntity<>("Niste administrator!", HttpStatus.FORBIDDEN); //ne diraj zabranjeno voce
        }

        if (korisnikService.findByEmail(autorDto.getEmailAdresa()) != null) {
            return new ResponseEntity<>("Email address is already in use!", HttpStatus.BAD_REQUEST);
        }

        if (korisnikService.findByKorisnickoIme(autorDto.getKorisnickoIme()) != null) {
            return new ResponseEntity<>("Username is already in use!", HttpStatus.BAD_REQUEST);
        }
        //duplikat?
       /* if (korisnikService.daLiPostojiDuplikat((autorDto.getEmailAdresa())) {
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        }*/

        System.out.println(autorDto.toString());
        Autor noviAutor = new Autor();
        noviAutor.setIme(autorDto.getIme());
        noviAutor.setPrezime(autorDto.getPrezime());
        noviAutor.setKorisnickoIme(autorDto.getKorisnickoIme());
        noviAutor.setEmailAdresa(autorDto.getEmailAdresa());
        noviAutor.setLozinka(autorDto.getLozinka());
        noviAutor.setDatumRodjenja(autorDto.getDatumRodjenja());
        noviAutor.setAktivan(false);
        noviAutor.setOpis(autorDto.getOpis());
        //specifikacije tako kazu jer mora da podnese zahtjev

        korisnikService.saveKorisnik(noviAutor);

        AutorDto createdAutorDto = new AutorDto(noviAutor);
        return new ResponseEntity<>(createdAutorDto, HttpStatus.CREATED);
    }

    //azuriraj autora
    @PostMapping("/updateAutor")
    public ResponseEntity<?> updateAutorProfile(@RequestBody AzurirajKorisnikaDto azurirajKorisnikaDto, HttpSession httpSession) {

        Autor autor = (Autor) httpSession.getAttribute("loggedUser");
        if (autor == null) {
            return new ResponseEntity<>("Korisnik ne postoji", HttpStatus.NOT_FOUND); // ne postoji
        }
        System.out.println(autor.getLozinka());
        System.out.println(azurirajKorisnikaDto.getStaraLozinka());
        if (!autor.getLozinka().equals(azurirajKorisnikaDto.getStaraLozinka())) {
            return new ResponseEntity<>("Lozinke nisu okej!", HttpStatus.UNAUTHORIZED); // neispravna trenutna lozinka
        }

        if(azurirajKorisnikaDto.getIme() != null) {
            autor.setIme(azurirajKorisnikaDto.getIme());
        }

        if(azurirajKorisnikaDto.getPrezime() != null) {
            autor.setPrezime(azurirajKorisnikaDto.getPrezime());
        }

        if(azurirajKorisnikaDto.getDatumRodjenja() !=null) {
            autor.setDatumRodjenja(azurirajKorisnikaDto.getDatumRodjenja());
        }

        if(azurirajKorisnikaDto.getProfilnaSlika() != null) {
            autor.setProfilnaSlika(azurirajKorisnikaDto.getProfilnaSlika());
        }

        if(azurirajKorisnikaDto.getOpis() != null) {
            autor.setOpis(azurirajKorisnikaDto.getOpis());
        }

        if(azurirajKorisnikaDto.getKorisnickoIme() != null) {
            autor.setKorisnickoIme(azurirajKorisnikaDto.getKorisnickoIme());
        }

        if (azurirajKorisnikaDto.getNovaEmailAdresa() != null && !azurirajKorisnikaDto.getNovaEmailAdresa().isEmpty()) {
            autor.setEmailAdresa(azurirajKorisnikaDto.getNovaEmailAdresa());
        }


        if (azurirajKorisnikaDto.getNovaLozinka() != null) {
            autor.setLozinka(azurirajKorisnikaDto.getNovaLozinka());
        }
        autor = autorService.saveAutor(autor);

        AutorDto a = new AutorDto(autor);
        return new ResponseEntity<>(a, HttpStatus.OK);
    }
    @GetMapping("/logout")
    public ResponseEntity<String> Logout( HttpSession httpSession) {
        Korisnik loggedKorisnik = (Korisnik) httpSession.getAttribute("loggedUser");

        if (loggedKorisnik == null)
            return new ResponseEntity<String>("Session does not exist", HttpStatus.FORBIDDEN);

        //httpSession.setAttribute("loggedUser",null);
        httpSession.invalidate();
        return new ResponseEntity<String>("Successfully logged out", HttpStatus.OK);
    }

    @PutMapping("/aktivirajAutora/{id}")
    public ResponseEntity<String> activateAutorAccount(
            @PathVariable(name = "id") Long autorId,
            @RequestBody AutorDto autorDto,
            HttpSession httpSession) {
        Korisnik loggedUser = (Korisnik) httpSession.getAttribute("loggedUser");
        if (loggedUser == null) {
            return new ResponseEntity<>("Nema sesije!", HttpStatus.FORBIDDEN);
        }

        if (!loggedUser.getUlogaKorisnika().equals(UlogaKorisnika.ADMINISTRATOR.toString())) {
            return new ResponseEntity<>("Korisnik nije administrator!", HttpStatus.FORBIDDEN);
        }

        Autor targetAutor = (Autor )korisnikService.findAutorById(autorId);
        if (targetAutor == null) {
            return new ResponseEntity<>("Autor nije pronadjen!", HttpStatus.NOT_FOUND);
        }

        if (targetAutor.isAktivan()) {
            return new ResponseEntity<>("Autor je već aktivan i ne moze se mijenjati!", HttpStatus.FORBIDDEN);
        }

        targetAutor.setAktivan(true);
        korisnikService.saveKorisnik(targetAutor);

        return new ResponseEntity<>("Nalog autora je aktiviran!", HttpStatus.OK);
    }

}
