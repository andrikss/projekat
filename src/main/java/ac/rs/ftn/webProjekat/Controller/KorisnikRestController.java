package ac.rs.ftn.webProjekat.Controller;

import ac.rs.ftn.webProjekat.Dto.*;
import ac.rs.ftn.webProjekat.Entity.Autor;
import ac.rs.ftn.webProjekat.Entity.Korisnik;
import ac.rs.ftn.webProjekat.Entity.Polica;
import ac.rs.ftn.webProjekat.Entity.UlogaKorisnika;
import ac.rs.ftn.webProjekat.Service.AutorService;
import ac.rs.ftn.webProjekat.Service.KorisnikService;
import ac.rs.ftn.webProjekat.Service.PolicaService;
import jakarta.servlet.http.HttpSession;
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
            }
        }

        return new ResponseEntity<>(korisnikDtos, HttpStatus.OK);
    }

    //prikaz jednog korisnika
    @GetMapping("/{id}")
    public ResponseEntity<KorisnikDto> getKorisnikById(@PathVariable(name = "id") Long id) {
        Korisnik targetKorisnik = korisnikService.findById(id);

        if (targetKorisnik == null) {
            // korisnik sa datim id-om ne postoji
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        if (targetKorisnik.getUlogaKorisnika().equals(UlogaKorisnika.ADMINISTRATOR.toString())) {
            // taj korisnik je administrator
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        }

        KorisnikDto targetKorisnikDto = new KorisnikDto(targetKorisnik);

        // uspjesno
        return new ResponseEntity<>(targetKorisnikDto, HttpStatus.OK);
    }

    //prikaz korisnikovih polica
    @GetMapping("/{id}/police")
    public ResponseEntity<List<PolicaDto>> getPoliciesForKorisnik(@PathVariable(name = "id") Long id) {
        Korisnik targetKorisnik = korisnikService.findById(id);

        if (targetKorisnik == null) {
            //korisnik ne posotji
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        if (targetKorisnik.getUlogaKorisnika().equals(UlogaKorisnika.ADMINISTRATOR.toString())) {
            // ne moze adm
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
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
    public ResponseEntity<KorisnikDto> login(@RequestBody UlogujSeDto loginDto, HttpSession httpSession) {
        if (loginDto.getEmailAdresa() == null || loginDto.getEmailAdresa().isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        if (loginDto.getEmailAdresa().isEmpty() || loginDto.getLozinka().isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        Korisnik korisnik = korisnikService.findByEmail(loginDto.getEmailAdresa());
        if (korisnik == null || !korisnik.getLozinka().equals(loginDto.getLozinka())) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }

        httpSession.setAttribute("loggedUser", korisnik);
        KorisnikDto k = new KorisnikDto(korisnik);
        return new ResponseEntity<>(k, HttpStatus.OK);
    }

    //registruj se
    @PostMapping("/register")
    public ResponseEntity<KorisnikDto> register(@RequestBody RegistrujSeDto registracijaDto) {
        if (registracijaDto.getIme().isEmpty() || registracijaDto.getPrezime().isEmpty() ||
                registracijaDto.getKorisnickoIme().isEmpty() || registracijaDto.getEmailAdresa().isEmpty() ||
                registracijaDto.getLozinka().isEmpty() || registracijaDto.getPonovljenaLozinka().isEmpty() ||
                registracijaDto.getDatumRodjenja() == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        if (!registracijaDto.getLozinka().equals(registracijaDto.getPonovljenaLozinka())) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST); // Lozinke se ne podudaraju
        }

        Korisnik noviKorisnik = new Korisnik();
        if (korisnikService.daLiPostojiDuplikat(noviKorisnik)) {
            return new ResponseEntity<>(null, HttpStatus.CONFLICT); // Konflikt jer postoji
        }

        // Pravim novog korisnika
        noviKorisnik.setIme(registracijaDto.getIme());
        noviKorisnik.setPrezime(registracijaDto.getPrezime());
        noviKorisnik.setKorisnickoIme(registracijaDto.getKorisnickoIme());
        noviKorisnik.setEmailAdresa(registracijaDto.getEmailAdresa());
        noviKorisnik.setLozinka(registracijaDto.getLozinka());
        noviKorisnik.setDatumRodjenja(registracijaDto.getDatumRodjenja());
        noviKorisnik.setUlogaKorisnika(UlogaKorisnika.CITALAC.toString());

        korisnikService.saveKorisnik(noviKorisnik);

        KorisnikDto k = new KorisnikDto(noviKorisnik);
        return new ResponseEntity<>(k, HttpStatus.CREATED); // Kreiran - uspješno registrovan
    }


    //azuriranje korisnika
    @PutMapping("/updateKorisnik/{id}")
    public ResponseEntity<KorisnikDto> updateKorisnik(@PathVariable(name = "id") Long id, @RequestBody AzurirajKorisnikaDto azurirajKorisnikaDto, HttpSession httpSession) {
        Korisnik korisnik = korisnikService.findById(id);

        if (korisnik == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND); // ne postoji
        }

        Korisnik loggedUser = (Korisnik) httpSession.getAttribute("loggedUser");

        if (loggedUser == null || !loggedUser.getId().equals(id)) {
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN); // zabranjen pristup
        }

        // verifikacija trenutne lozinke ako korisnik mijenja mejl adresu ili lozinku
        if (!korisnik.getLozinka().equals(azurirajKorisnikaDto.getStaraLozinka())) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED); // neispravna trenutna lozinka
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
    }


    //azuriranje autora
    //basically the same samo sto ima AKTIVAN polje
    @PutMapping("/updateAutor/{id}")
    public ResponseEntity<AutorDto> updateAutor(@PathVariable(name = "id") Long id, @RequestBody AzurirajAutoraDto azurirajAutoraDto, HttpSession httpSession) {
        Autor autor = autorService.findById(id);

        if (autor == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        Autor loggedUser = (Autor) httpSession.getAttribute("loggedUser");

        //provjera
        if (!loggedUser.getId().equals(autor.getId())) {
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN); // Nema dozvolu za ažuriranje
        }

        //
        if (!azurirajAutoraDto.getStaraLozinka().equals(autor.getLozinka())) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED); // Neispravna trenutna lozinka
        }

        autor.setIme(azurirajAutoraDto.getIme());
        autor.setPrezime(azurirajAutoraDto.getPrezime());
        autor.setKorisnickoIme(azurirajAutoraDto.getKorisnickoIme());
        autor.setEmailAdresa(azurirajAutoraDto.getNovaEmailAdresa());
        autor.setLozinka(azurirajAutoraDto.getNovaLozinka()); //
        //autor.setAktivan(azurirajAutoraDto.isAktivan());
        //kontam da on to ne moze azurirat jer bilo bi glupo
        //da trazi onda zahtjev
        // ili bi bila rupa lol u sistemu
        Autor updatedAutor = autorService.save(autor);
        AutorDto autorDto = new AutorDto(updatedAutor);

        return new ResponseEntity<>(autorDto, HttpStatus.OK);
    }


    //KREIRANJE AUTORA, OVO MOZE SAMO KO???
    // ADMIINISTRATOR WOOP WOOP
    @PostMapping("/kreirajAutora")
    public ResponseEntity<AutorDto> createAutor(@RequestBody AutorDto autorDto, HttpSession httpSession) {
        //  je l administrator
        Korisnik loggedUser = (Korisnik) httpSession.getAttribute("loggedUser");
        if (loggedUser == null || !loggedUser.getUlogaKorisnika().equals(UlogaKorisnika.ADMINISTRATOR.toString())) {
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN); //ne diraj zabranjeno voce
        }

        //duplikat?
      /*  if (korisnikService.daLiPostojiDuplikat((autorDto.getEmailAdresa())) {
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        }*/


        Autor noviAutor = new Autor();
        noviAutor.setIme(autorDto.getIme());
        noviAutor.setPrezime(autorDto.getPrezime());
        noviAutor.setKorisnickoIme(autorDto.getKorisnickoIme());
        noviAutor.setEmailAdresa(autorDto.getEmailAdresa());
        noviAutor.setLozinka(autorDto.getLozinka());
        //noviAutor.setAktivan(autorDto.isAktivan());
        noviAutor.setAktivan(false);
        //specifikacije tako kazu jer mora da podnese zahtjev

        korisnikService.saveKorisnik(noviAutor);

        AutorDto createdAutorDto = new AutorDto(noviAutor);
        return new ResponseEntity<>(createdAutorDto, HttpStatus.CREATED);
    }

    @DeleteMapping("/logout/{id}")
    public ResponseEntity<String> logout(@PathVariable(name = "id") Long id, HttpSession httpSession) {
        if (httpSession.getAttribute("loggedUser") != null) {
            Korisnik loggedUser = (Korisnik) httpSession.getAttribute("loggedUser");
            if (loggedUser.getId().equals(id)) {
                httpSession.invalidate();
                return new ResponseEntity<>("Uspješno ste se odjavili.", HttpStatus.OK);
            }
        }

        return new ResponseEntity<>("Niste prijavljeni ili nemate dozvolu za odjavu.", HttpStatus.UNAUTHORIZED);
    }
}
