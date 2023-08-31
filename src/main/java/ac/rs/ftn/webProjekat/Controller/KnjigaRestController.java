package ac.rs.ftn.webProjekat.Controller;

import ac.rs.ftn.webProjekat.Dto.*;
import ac.rs.ftn.webProjekat.Entity.*;
import ac.rs.ftn.webProjekat.Service.AutorService;
import ac.rs.ftn.webProjekat.Service.KnjigaService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/knjige")
public class KnjigaRestController {

    @Autowired
    private KnjigaService knjigaService;

    @Autowired
    private AutorService autorService;

    //lista knjiga
    @GetMapping("/lista")
    public ResponseEntity<List<KnjigaDto>> getAllKnjigeWithDetails() {
        List<Knjiga> allBooks = knjigaService.findAllKnjiga();
        List<KnjigaDto> responseObject = new ArrayList<>();

        for (Knjiga knjiga : allBooks) {
            KnjigaDto knjigaDto = new KnjigaDto(knjiga);

            // Dodaj informacije o žanrovima
            if (knjiga.getZanrovi() != null) {
                for (Zanr zanr : knjiga.getZanrovi()) {
                    knjigaDto.getZanrovi().add(new ZanrDto(zanr));
                }
            }

            // Dodaj recenzije knjige
            Set<Recenzija> recenzije = knjigaService.getRecenzija(knjiga);
            if (recenzije != null) {
                for (Recenzija recenzija : recenzije) {
                    knjigaDto.getRecenzije().add(new RecenzijaDto(recenzija));
                }
            }

            responseObject.add(knjigaDto);
        }

        return new ResponseEntity<>(responseObject, HttpStatus.OK);
    }


    //prikaz pojedine knjige
    @GetMapping("/{id}")
    public ResponseEntity<KnjigaDto> getKnjigaWithDetails(@PathVariable(name = "id") Long id) {
        Knjiga knjiga = knjigaService.findById(id);

        if (knjiga == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        KnjigaDto knjigaDto = new KnjigaDto(knjiga);

        // Dodaj informacije o žanrovima
        if (knjiga.getZanrovi() != null) {
            for (Zanr zanr : knjiga.getZanrovi()) {
                knjigaDto.getZanrovi().add(new ZanrDto(zanr));
            }
        }

        // Dodaj recenzije knjige
        Set<Recenzija> recenzije = knjigaService.getRecenzija(knjiga);
        if (recenzije != null) {
            for (Recenzija recenzija : recenzije) {
                knjigaDto.getRecenzije().add(new RecenzijaDto(recenzija));
            }
        }

        return new ResponseEntity<>(knjigaDto, HttpStatus.OK);
    }

    //pretraga knjige
    @PostMapping("/pretraziKnjigu")
    public ResponseEntity<List<KnjigaDto>> searchKnjiga(@RequestBody NadjiKnjiguDto nadjiKnjiguDto)
    {
        List<Knjiga> knjigeWithNaslov = null;

        System.out.println(nadjiKnjiguDto.getNaslov());
        if (nadjiKnjiguDto.getNaslov() != null ) {
            knjigeWithNaslov = knjigaService.findKnjigeThatContainInNaslov(nadjiKnjiguDto.getNaslov());
        }
        System.out.println(knjigeWithNaslov);
        List<Knjiga> knjigaList = new ArrayList<>();

        if (knjigeWithNaslov != null) {
            for (Knjiga kNaslov : knjigeWithNaslov) {
                boolean AllreadyContains = false;
                for (Knjiga check : knjigaList) {
                    if (check.getISBN().equals(kNaslov.getISBN())) {
                        AllreadyContains = true;
                        break;
                    }
                }
                if (!AllreadyContains) {
                    knjigaList.add(kNaslov);
                }
            }
        }
        List<KnjigaDto> ret = new ArrayList<>();

        for (Knjiga k : knjigaList) {
            KnjigaDto kdto = new KnjigaDto(k);
            ret.add(kdto);
        }

        //No specified naslov or ISBN
        return new ResponseEntity<>(ret, HttpStatus.OK);
    }
    //dodavanje knjige
    // knjigu moze dodati ili autor ili administrator
    // administrator moze dodati bilo koju knjigu
    // autor moze dodati samo svoju

    @PostMapping("/dodajKnjigu")
    public ResponseEntity<String> addKnjiga(@RequestBody KnjigaDto knjigaDto, HttpSession httpSession) {
        Korisnik loggedUser = (Korisnik) httpSession.getAttribute("loggedUser");
        if (loggedUser == null) {
            return new ResponseEntity<>("No session!", HttpStatus.FORBIDDEN);
        }

        System.out.println("ovo je knjigaDTO" + knjigaDto);
        // autor ili administrator
        if (!loggedUser.getUlogaKorisnika().equals(UlogaKorisnika.AUTOR.toString())
                && !loggedUser.getUlogaKorisnika().equals(UlogaKorisnika.ADMINISTRATOR.toString())) {
            return new ResponseEntity<>("Nisi administrator niti autor!", HttpStatus.FORBIDDEN);
        }

        // Ako je korisnik autor, provjeri da li je to njegova knjiga
        if (loggedUser.getUlogaKorisnika().equals(UlogaKorisnika.AUTOR.toString())) {
            Autor autor = autorService.findByEmail(loggedUser.getEmailAdresa());
            if (!autor.getEmailAdresa().equals(knjigaDto.getEmailAdresaAutora())) {
                return new ResponseEntity<>("Ako si autor, mozes dodati samo svoju knjigu!", HttpStatus.FORBIDDEN);
            }
        }

        if (knjigaService.findByISBN(knjigaDto.getISBN()) != null) {
            return new ResponseEntity<>("Knjiga sa istim ISBN vec postoji!", HttpStatus.FORBIDDEN);
        }

        //oki
        Knjiga novaKnjiga = new Knjiga(knjigaDto);
        System.out.println("ovo je nova" + novaKnjiga);
        if (knjigaDto.getZanrovi() != null && !knjigaDto.getZanrovi().isEmpty()) {

            List<String> zanrovi = new ArrayList<>();
            for (ZanrDto zanrDto_it : knjigaDto.getZanrovi()) {
                zanrovi.add(zanrDto_it.getNaziv());
            }

            knjigaService.poveziZanrSaKnjigom(novaKnjiga, zanrovi);
        }
        knjigaService.save(novaKnjiga);

        if (loggedUser.getUlogaKorisnika().equals(UlogaKorisnika.AUTOR.toString())) {
            Autor loggerUserAutor = (Autor) loggedUser;
            loggerUserAutor.getAutoroveKnjige().add(novaKnjiga);
            knjigaService.saveAutorOfKnjiga(loggerUserAutor);
        }
        else {
            Autor targetAutor = (Autor) knjigaService.findAutorByEmail(knjigaDto.getEmailAdresaAutora());
            if (targetAutor != null) {
                targetAutor.getAutoroveKnjige().add(novaKnjiga);
                knjigaService.saveAutorOfKnjiga(targetAutor);
            }
        }

        return new ResponseEntity<>("Uspjesno!", HttpStatus.OK);
    }

    //ista logika i sa azuriranjem
    @PutMapping("/azurirajKnjigu/{id}")
    public ResponseEntity<String> updateKnjiga(@PathVariable(name = "id") Long id, @RequestBody AzurirajKnjiguDto azurirajKnjiguDto, HttpSession httpSession) {
        Korisnik loggedUser = (Korisnik) httpSession.getAttribute("loggedUser");
        if (loggedUser == null) {
            return new ResponseEntity<>("No session!", HttpStatus.FORBIDDEN);
        }

        loggedUser = knjigaService.findKorisnikById(loggedUser.getId());
        if (loggedUser == null) {
            return new ResponseEntity<>("No session!", HttpStatus.FORBIDDEN);
        }

        // autor ili administrator
        if (!loggedUser.getUlogaKorisnika().equals(UlogaKorisnika.AUTOR.toString())
                && !loggedUser.getUlogaKorisnika().equals(UlogaKorisnika.ADMINISTRATOR.toString())) {
            return new ResponseEntity<>("Niste administrator ili autor", HttpStatus.FORBIDDEN);
        }

        Knjiga targetKnjiga = knjigaService.findById(id);
        System.out.println("OVO JE KNJIGA" + targetKnjiga);
        if (targetKnjiga == null) {
            return new ResponseEntity<>("Ta knjiga ne postoji", HttpStatus.NOT_FOUND);
        }
        // Ako je korisnik autor, provjeri da li je to njegova knjiga
        if (loggedUser.getUlogaKorisnika().equals(UlogaKorisnika.AUTOR.toString())) {
            if (targetKnjiga.getEmailAdresaAutora() == null) {
                return new ResponseEntity<>("Email autora je ostao anoniman, samo administrator moze da pristupi!", HttpStatus.FORBIDDEN);
            }
            if (!targetKnjiga.getEmailAdresaAutora().equals(loggedUser.getEmailAdresa())) {
                return new ResponseEntity<>("Ako si autor, mozes da azuriras samo svoju knjigu", HttpStatus.FORBIDDEN);
            }
        }

        if (azurirajKnjiguDto.getISBN() != null &&
                !azurirajKnjiguDto.getISBN().isEmpty() &&
                !azurirajKnjiguDto.getISBN().equals(targetKnjiga.getISBN())) {
            //mijenja se ISBN
            if (knjigaService.findByISBN(azurirajKnjiguDto.getISBN()) != null) {
                return new ResponseEntity<>("Knjiga with specified new ISBN is already in use!", HttpStatus.BAD_REQUEST);
            }
        }

        Autor owner = (Autor) knjigaService.findAutorByISBN(targetKnjiga.getISBN());
        Autor targetAutor = (Autor) knjigaService.findAutorByEmail(azurirajKnjiguDto.getEmailAdresaAutora());
        if (targetAutor == null &&
                loggedUser.getUlogaKorisnika().equals(UlogaKorisnika.AUTOR.toString())) {
            targetAutor = (Autor) loggedUser;
        }
        if (owner == null &&
                loggedUser.getUlogaKorisnika().equals(UlogaKorisnika.AUTOR.toString())) {
            owner = (Autor) loggedUser;
        }

        if (targetAutor == null) {
            return new ResponseEntity<>("No autor to connect to!", HttpStatus.BAD_REQUEST);
        }
        System.out.println("OWNER KNJGIA:" + targetKnjiga.toString());
        owner.removeKnjiga(targetKnjiga);
        targetAutor.getAutoroveKnjige().add(targetKnjiga);
        knjigaService.saveAutorOfKnjiga(owner);
        knjigaService.saveAutorOfKnjiga(targetAutor);

        targetKnjiga.updateKnjiga(azurirajKnjiguDto);
        knjigaService.save(targetKnjiga);

        return new ResponseEntity<>("Azuriranje knjige - Uspjesno!", HttpStatus.OK);

    }

    //dodaj zanr na odredjenu knjigu
    @PutMapping("/{knjigaId}/zanr/{zanrId}")
    public ResponseEntity<String> addZanr(@PathVariable(name = "knjigaId") Long knjigaId,
                                          @PathVariable(name = "zanrId") Long zanrId,
                                          HttpSession httpSession) {
        Zanr targetZanr = knjigaService.findZanrById(zanrId);
        Knjiga targetKnjiga = knjigaService.findById(knjigaId);

        if (targetZanr == null || targetKnjiga == null) {
            return new ResponseEntity<>("Knjiga ili zanr ne postoji!", HttpStatus.NOT_FOUND);
        }

        Korisnik loggedUser = (Korisnik) httpSession.getAttribute("loggedUser");

        if (loggedUser == null) {
            return new ResponseEntity<>("No session!", HttpStatus.FORBIDDEN);
        }
        loggedUser = knjigaService.findKorisnikById(loggedUser.getId());

        if (!loggedUser.getUlogaKorisnika().equals(UlogaKorisnika.ADMINISTRATOR.toString())
                && !loggedUser.getUlogaKorisnika().equals(UlogaKorisnika.AUTOR.toString())) {
            return new ResponseEntity<>("Niste ni autor ni administrator!", HttpStatus.FORBIDDEN);
        }

        if (loggedUser.getUlogaKorisnika().equals(UlogaKorisnika.AUTOR.toString())) {
            Autor loggedAutor = (Autor) loggedUser;
            if (!loggedAutor.containsKnjigaWithISBN(targetKnjiga.getISBN())) {
                return new ResponseEntity<>("Niste autor ove knjige!", HttpStatus.FORBIDDEN);
            }
        }


        targetKnjiga.getZanrovi().add(targetZanr);
        knjigaService.saveZanrOfKnjiga(targetZanr);
        knjigaService.save(targetKnjiga);

        return new ResponseEntity<>("Uspjesno!", HttpStatus.OK);
    }

    //obirisi zanr s knjige
    @DeleteMapping("/{knjigaId}/zanr/{zanrId}")
    public ResponseEntity<String> removeZanr(@PathVariable(name = "knjigaId") Long knjigaId,
                                             @PathVariable(name = "zanrId") Long zanrId,
                                             HttpSession httpSession) {
        Zanr targetZanr = knjigaService.findZanrById(zanrId);
        Knjiga targetKnjiga = knjigaService.findById(knjigaId);

        System.out.println("OVO JE ZANR "+ targetZanr.toString());
        System.out.println("OVO JE KNJIGA" +targetKnjiga.toString());

        if (targetZanr == null || targetKnjiga == null) {
            return new ResponseEntity<>("Knjiga ili zanr ne postoji!", HttpStatus.NOT_FOUND);
        }

        Korisnik loggedUser = (Korisnik) httpSession.getAttribute("loggedUser");
        if (loggedUser == null) {
            return new ResponseEntity<>("No session!", HttpStatus.FORBIDDEN);
        }

        loggedUser = knjigaService.findKorisnikById(loggedUser.getId());


        if (!loggedUser.getUlogaKorisnika().equals(UlogaKorisnika.ADMINISTRATOR.toString())
                && !loggedUser.getUlogaKorisnika().equals(UlogaKorisnika.AUTOR.toString())) {
            return new ResponseEntity<>("Niste ni autor ni administrator!", HttpStatus.FORBIDDEN);
        }

        // If the user is an author, check if they own the book
        if (loggedUser.getUlogaKorisnika().equals(UlogaKorisnika.AUTOR.toString())) {
            Autor loggedAutor = (Autor) loggedUser;
            if (!loggedAutor.containsKnjigaWithISBN(targetKnjiga.getISBN())) {
                return new ResponseEntity<>("Niste autor ove knjige!", HttpStatus.FORBIDDEN);
            }
        }

        if (!targetKnjiga.daLiImaZanr(targetZanr)) {
            return new ResponseEntity<>("Knjiga vec nema taj zanr!", HttpStatus.OK);
        }

        // Remove the genre from the book
        targetKnjiga.izbrisiZanr(targetZanr);
        knjigaService.save(targetKnjiga);

        return new ResponseEntity<>("Uspjesno!", HttpStatus.OK);
    }


    //ne radi dobro
    // pise da je obrisana ali je ne obrise u listi
    @DeleteMapping("/obrisiKnjigu/{id}")
    public ResponseEntity<String> deleteKnjiga(@PathVariable(name = "id") Long id, HttpSession httpSession) {
        Korisnik loggedUser = (Korisnik) httpSession.getAttribute("loggedUser");
        if (loggedUser == null) {
            return new ResponseEntity<>("No session!", HttpStatus.FORBIDDEN);
        }

        Knjiga targetKnjiga = knjigaService.findById(id);
        if (targetKnjiga == null) {
            return new ResponseEntity<>("Ta knjiga ne postoji", HttpStatus.NOT_FOUND);
        }

        // Samo administrator moze da obrise knjigu
        if (!loggedUser.getUlogaKorisnika().equals(UlogaKorisnika.ADMINISTRATOR.toString())) {
            return new ResponseEntity<>("Niste administrator", HttpStatus.FORBIDDEN);
        }

        // Provjera da li knjiga ima recenzije
        Set<Recenzija> recenzije = knjigaService.getRecenzija(targetKnjiga);
        if (recenzije != null && !recenzije.isEmpty()) {
            return new ResponseEntity<>("Knjiga ima recenzije i ne moze biti obrisana", HttpStatus.FORBIDDEN);
        }

        // Obrisi knjigu
        knjigaService.deleteStavkeOfKnjiga(targetKnjiga);
        System.out.println("DEL STAVKE");

        knjigaService.deleteZanrOfKnjiga(targetKnjiga);
        System.out.println("DEL ZANR");

        knjigaService.deleteKnjigaOfAutor(targetKnjiga);
        System.out.println("DEL AUTOR");

        knjigaService.delete(targetKnjiga);
        System.out.println("Deleting knjiga with ID: " + id);

        targetKnjiga.setZanrovi(null);
        List<KnjigaDto> updatedKnjige = getAllKnjigeWithDetails().getBody();

        return new ResponseEntity<>("Knjiga je uspesno obrisana", HttpStatus.OK);
    }


}
