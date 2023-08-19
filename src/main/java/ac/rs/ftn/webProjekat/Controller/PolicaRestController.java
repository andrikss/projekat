package ac.rs.ftn.webProjekat.Controller;

import ac.rs.ftn.webProjekat.Dto.PolicaDto;
import ac.rs.ftn.webProjekat.Entity.*;
import ac.rs.ftn.webProjekat.Service.PolicaService;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    @Transactional
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


        Polica newCustomPolica = new Polica(policaDto); // Automatski će generisati id

        //loggedUser.getPolice().add(newCustomPolica);
        newCustomPolica.setStavkaPolice(new HashSet<>());

       // System.out.println(policaDto.toString());
        //System.out.println(newCustomPolica.toString());
        //newCustomPolica.setKorisnik(loggedUser);
        //dodajemo tom ulogovanom korisniku policu
       // newCustomPolica.setKorisnik(loggedUser);
        //System.out.println(newCustomPolica.getKorisnik().toString());
        //newCustomPolica.setStavkaPolice(new HashSet<>());
        //newCustomPolica.setNaziv(policaDto.getNaziv());
        //newCustomPolica.setId(policaDto.getId());
        //loggedUser.getPolice().add(newCustomPolica);
        loggedUser.getPolice().add(newCustomPolica);

        for(Polica it: loggedUser.getPolice()) {
            System.out.println(it.getId());
        }
        //newCustomPolica.setKorisnik(loggedUser);
        policaService.savePolica(newCustomPolica);
        policaService.saveKorisnik(loggedUser);

        for(Polica it: loggedUser.getPolice()) {
            System.out.println(it.getId());
        }

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

        loggedUser = policaService.findKorisnikById(loggedUser.getId());
        for(Polica it: loggedUser.getPolice()) {
            System.out.println(it.getId());
        }

        boolean found = false;
        for (Polica p : loggedUser.getPolice()) {
            if (p.getId().equals(policaId)) {
                found = true;
                break; // Ako je pronađena ciljana polica, nema potrebe za daljom proverom
            }
        }

        if (!found) {
            return new ResponseEntity<>("Ovo nije tvoja polica!", HttpStatus.FORBIDDEN);
        }




        List<PolicaDto> police = new ArrayList<>();


        loggedUser.izbrisiPolicu(targetPolica);

        policaService.saveKorisnik(loggedUser);

        policaService.deletePolica(targetPolica);

        return new ResponseEntity<>("Uspjesno!", HttpStatus.OK);
    }

    //dodavanje knjige na policu
    @Transactional
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
    @DeleteMapping("/knjiga/{knjigaId}/polica/{policaId}")
    @Transactional
    public ResponseEntity<String> deleteKnjigaFromPolica(@PathVariable(name = "knjigaId") Long knjigaId,
                                                         @PathVariable(name = "policaId") Long policaId,
                                                         HttpSession httpSession) {
        Korisnik loggedUser = (Korisnik) httpSession.getAttribute("loggedUser");
        if (loggedUser == null) {
            return new ResponseEntity<>("No session!", HttpStatus.FORBIDDEN);
        }

        Polica targetPolica = policaService.findById(policaId);
        if (targetPolica == null) {
            return new ResponseEntity<>("Ne postoji polica", HttpStatus.NOT_FOUND);
        }

        Knjiga targetKnjiga = policaService.findKnjigaById(knjigaId);
        if (targetKnjiga == null) {
            return new ResponseEntity<>("Ne mogu pronaci knjgiu", HttpStatus.NOT_FOUND);
        }

        /*if (!loggedUser.jelNjegovaPolica(targetPolica)) {
            return new ResponseEntity<>("Korisnik ne posjeduje policu!", HttpStatus.FORBIDDEN);
        }*/
        boolean found = false;
        for (Polica p : loggedUser.getPolice()) {
            if (p.getId().equals(policaId)) {
                found = true;
                break; // Ako je pronađena ciljana polica, nema potrebe za daljom proverom
            }
        }

        if (!found) {
            return new ResponseEntity<>("Ovo nije tvoja polica!", HttpStatus.FORBIDDEN);
        }

        if (!targetPolica.daLiPostojiKnjiga(targetKnjiga)) {
            return new ResponseEntity<>("Ta knjiga nije na toj polici!", HttpStatus.NOT_FOUND);
        }

        List<Polica> policaList = policaService.findPolicaByKorisnikId(loggedUser.getId());

        if (targetPolica.getTip() != TipPolice.REGULAR) {
            for (Polica p : policaList) {
                if (p.getTip() != TipPolice.REGULAR) {
                    continue;
                }

                policaService.deleteStavkeOfKnjiga(p, targetKnjiga);
                policaService.savePolica(p);
            }
        }

        StavkaPolice targetStavka = targetPolica.getStavkaByKnjiga(targetKnjiga);
        if (targetStavka != null) {
            // Prvo izbrišite sve stavke police povezane sa knjigom
            targetPolica.removeStavkaPolice(targetStavka);
        }
        //targetPolica.removeStavkaPolice(targetStavka);
        //policaService.deleteKnjiga(targetKnjiga);
        policaService.savePolica(targetPolica);
        policaService.deleteStavkaPolice(targetStavka);
        //policaService.deleteStavkaPolice(targetStavka);
       // policaService.deleteKnjiga(targetKnjiga);


        return new ResponseEntity<>("Uspjesno!", HttpStatus.OK);
    }




}
