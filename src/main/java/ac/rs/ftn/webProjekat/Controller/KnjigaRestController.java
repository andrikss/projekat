package ac.rs.ftn.webProjekat.Controller;

import ac.rs.ftn.webProjekat.Dto.AzurirajKnjiguDto;
import ac.rs.ftn.webProjekat.Dto.KnjigaDto;
import ac.rs.ftn.webProjekat.Dto.RecenzijaDto;
import ac.rs.ftn.webProjekat.Dto.ZanrDto;
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

        //oki
        Knjiga novaKnjiga = new Knjiga(knjigaDto);

        // Postavi autora knjige na korisnika koji dodaje knjigu
        if (loggedUser.getUlogaKorisnika().equals(UlogaKorisnika.AUTOR.toString())) {
            novaKnjiga.setEmailAdresaAutora(loggedUser.getEmailAdresa());
        }

        knjigaService.save(novaKnjiga);

        return new ResponseEntity<>("Uspjesno!", HttpStatus.OK);
    }

    //ista logika i sa azuriranjem
    @PutMapping("/azurirajKnjigu/{id}")
    public ResponseEntity<String> updateKnjiga(@PathVariable(name = "id") Long id, @RequestBody AzurirajKnjiguDto knjigaDto, HttpSession httpSession) {
        Korisnik loggedUser = (Korisnik) httpSession.getAttribute("loggedUser");
        if (loggedUser == null) {
            return new ResponseEntity<>("No session!", HttpStatus.FORBIDDEN);
        }

        Knjiga targetKnjiga = knjigaService.findById(id);
        if (targetKnjiga == null) {
            return new ResponseEntity<>("Ta knjiga ne postoji", HttpStatus.NOT_FOUND);
        }

        // autor ili administrator
        if (!loggedUser.getUlogaKorisnika().equals(UlogaKorisnika.AUTOR.toString())
                && !loggedUser.getUlogaKorisnika().equals(UlogaKorisnika.ADMINISTRATOR.toString())) {
            return new ResponseEntity<>("Niste administrator ili autor", HttpStatus.FORBIDDEN);
        }

        // Ako je korisnik autor, provjeri da li je to njegova knjiga
        if (loggedUser.getUlogaKorisnika().equals(UlogaKorisnika.AUTOR.toString())) {
            if (!targetKnjiga.getEmailAdresaAutora().equals(loggedUser.getEmailAdresa())) {
                return new ResponseEntity<>("Ako si autor, mozes da azuriras samo svoju knjigu", HttpStatus.FORBIDDEN);
            }
        }

        // oki
        targetKnjiga.updateKnjiga(knjigaDto);
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

        // Session check
        Korisnik loggedUser = (Korisnik) httpSession.getAttribute("loggedUser");
        if (loggedUser == null) {
            return new ResponseEntity<>("No session!", HttpStatus.FORBIDDEN);
        }
        loggedUser = knjigaService.findKorisnikById(loggedUser.getId());

        // Check permissions
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

        // Add the genre to the book
        targetKnjiga.getZanrovi().add(targetZanr);
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
        if (targetZanr == null || targetKnjiga == null) {
            return new ResponseEntity<>("Knjiga ili zanr ne postoji!", HttpStatus.NOT_FOUND);
        }

        // Session check
        Korisnik loggedUser = (Korisnik) httpSession.getAttribute("loggedUser");
        if (loggedUser == null) {
            return new ResponseEntity<>("No session!", HttpStatus.FORBIDDEN);
        }
        loggedUser = knjigaService.findKorisnikById(loggedUser.getId());

        // Check permissions
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
