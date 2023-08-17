package ac.rs.ftn.webProjekat.Controller;

import ac.rs.ftn.webProjekat.Dto.PolicaDto;
import ac.rs.ftn.webProjekat.Entity.*;
import ac.rs.ftn.webProjekat.Service.PolicaService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@RestController
@RequestMapping("/api/police")
public class PolicaRestController {

    @Autowired
    private PolicaService policaService;

    //lista polica
    // podrazumijeva se lista ulogovanog korisnika
    @GetMapping("/lista")
    public ResponseEntity<List<PolicaDto>> getPolice(HttpSession httpSession) {
        //  da li je korisnik ulogovan
        Korisnik loggedUser = (Korisnik) httpSession.getAttribute("loggedUser");
        if (loggedUser == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED); // Unauthorized status
        }

        loggedUser = policaService.findKorisnikById(loggedUser.getId());

        List<PolicaDto> police = new ArrayList<>();
        for(Polica p : loggedUser.getPolice()) {
            PolicaDto pol = new PolicaDto(p);
            police.add(pol);
        }

        return new ResponseEntity<>(police, HttpStatus.OK);
    }

    //odredjena polica
    @GetMapping("/{policaId}")
    public ResponseEntity<Polica> getPolica(@PathVariable(name = "policaId") Long policaId, HttpSession httpSession) {
        // da li je korisnik ulogovan
        Korisnik loggedUser = (Korisnik) httpSession.getAttribute("loggedUser");
        if (loggedUser == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED); // Unauthorized status
        }

        Polica polica = policaService.findById(policaId);

        if (polica == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Polica not found status
        }

        return new ResponseEntity<>(polica, HttpStatus.OK);
    }

    //neprijavljen korisnik koji
    //moze da vidi listu polica nekog korisnika
    @GetMapping("/korisnik/{korisnikId}/police")
    public ResponseEntity<List<PolicaDto>> getPoliceForKorisnik(@PathVariable(name = "korisnikId") Long korisnikId) {
        List<PolicaDto> police = new ArrayList<>();
        for (Polica p : policaService.findPolicaByKorisnikId(korisnikId))
        {
            PolicaDto pol = new PolicaDto(p);
            police.add(pol);
        }
        return new ResponseEntity<>(police, HttpStatus.OK);

    }

    //dodaj policu
    @PostMapping("/dodajPolicu")
    public ResponseEntity<String> addPolicaToLoggedUser(@RequestBody PolicaDto policaDto,
                                                              HttpSession httpSession) {
        Korisnik loggedUser = (Korisnik) httpSession.getAttribute("loggedUser");

        if (loggedUser == null) {
            return new ResponseEntity<>("No session!", HttpStatus.FORBIDDEN);
        }

        loggedUser = policaService.findKorisnikById(loggedUser.getId());

        if (loggedUser == null) {
            return new ResponseEntity<>("Nope!", HttpStatus.FORBIDDEN);
        }

        if (!policaDto.isValid()) {
            return new ResponseEntity<>("Mora postojati validno ime police", HttpStatus.FORBIDDEN);
        }

        if (loggedUser.jelPostojiPolica(policaDto.getNaziv())) {
            return new ResponseEntity<>("Vec postoji takva polica", HttpStatus.FORBIDDEN);
        }

        Polica newCustomPolica = new Polica(policaDto);
        newCustomPolica.setStavkaPolice(new HashSet<>());

        //dodajemo tom ulogovanom korisniku policu
        loggedUser.getPolice().add(newCustomPolica);

        //cuvamo prvo policu pa korisnika - logicno
        policaService.savePolica(newCustomPolica);
        policaService.saveKorisnik(loggedUser);

        return new ResponseEntity<>("Uspjesno!", HttpStatus.OK);
    }

    //brisanje police ulogovanog korisnika
    @DeleteMapping("/obrisiPolicu/{id}")
    public ResponseEntity<String> deleteCustomPolicaFromLoggedUser(@PathVariable(name = "id") Long policaId,
                                                                   HttpSession httpSession)
    {
        Korisnik loggedUser = (Korisnik) httpSession.getAttribute("loggedUser");
        if (loggedUser == null) {
            return new ResponseEntity<>("No session!", HttpStatus.FORBIDDEN);
        }

        Polica targetPolica = policaService.findById(policaId);
        if (targetPolica == null) {
            return new ResponseEntity<>("Ne postoji!", HttpStatus.BAD_REQUEST);
        }

        if (targetPolica.getTip() != TipPolice.REGULAR) {
            return new ResponseEntity<>("Ne mozes obrisati primarnu policu", HttpStatus.FORBIDDEN);
        }

        if (!loggedUser.daLiSadrziPolicu(targetPolica)) {
            return new ResponseEntity<>("Ovo nije tvoja polica!", HttpStatus.FORBIDDEN);
        }

        loggedUser.izbrisiPolicu(targetPolica);

        policaService.saveKorisnik(loggedUser);

        policaService.deletePolica(targetPolica);

        return new ResponseEntity<>("Uspjesno!", HttpStatus.OK);
    }

    //dodavanje knjige na policu
    @PostMapping("/knjiga/{knjigaId}/polica/{policaId}")
    public ResponseEntity<String> addKnjigaToPolica(@PathVariable(name="knjigaId") Long knjigaId,
                                                    @PathVariable(name="policaId") Long policaId,
                                                    HttpSession httpSession)
    {
        Korisnik loggedUser = (Korisnik) httpSession.getAttribute("loggedUser");
        if (loggedUser == null) {
            return new ResponseEntity<>("No session!", HttpStatus.FORBIDDEN);
        }

        Polica targetPolica = policaService.findById(policaId);
        if (targetPolica == null) {
            return new ResponseEntity<>("Ne postoji polica!!", HttpStatus.NOT_FOUND);
        }

        Knjiga targetKnjiga = policaService.findKnjigaById(knjigaId);
        if (targetKnjiga == null) {
            return new ResponseEntity<>("Ne postoji knjiga!", HttpStatus.NOT_FOUND);
        }

        if (!loggedUser.jelNjegovaPolica(targetPolica)) {
            return new ResponseEntity<>("Nije korisnikova polica!", HttpStatus.FORBIDDEN);
        }

        if (loggedUser.daLiJeKnjigaNaPrimarnoj(targetKnjiga)
                && !targetPolica.getTip().equals(TipPolice.REGULAR)) {
            return new ResponseEntity<>("Knjiga je vec na primarnoj polici!",HttpStatus.FORBIDDEN);
        }

        if (targetPolica.daLiPostojiKnjiga(targetKnjiga)) {
            return new ResponseEntity<>("Polica vec sadrzi knjigu!",HttpStatus.FORBIDDEN);
        }

        if (targetPolica.getTip() == TipPolice.REGULAR && !loggedUser.daLiJeKnjigaNaPrimarnoj(targetKnjiga) )
        {
            return new ResponseEntity<>("Knjiga mora biti prvo dodana na primarnoj polici", HttpStatus.FORBIDDEN);
        }

        StavkaPolice newStavka = new StavkaPolice();
        newStavka.setKnjiga(targetKnjiga);
        targetPolica.getStavkaPolice().add(newStavka);

        policaService.savePolica(targetPolica);

        return new ResponseEntity<>("Nova knjiga je dodana!", HttpStatus.CREATED);
    }

    //brisanje knjige s police




}
