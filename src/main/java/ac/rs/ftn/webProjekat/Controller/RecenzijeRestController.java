package ac.rs.ftn.webProjekat.Controller;

import ac.rs.ftn.webProjekat.Dto.KorisnikDto;
import ac.rs.ftn.webProjekat.Dto.RecenzijaDataDto;
import ac.rs.ftn.webProjekat.Dto.RecenzijaDto;
import ac.rs.ftn.webProjekat.Entity.*;
import ac.rs.ftn.webProjekat.Service.KnjigaService;
import ac.rs.ftn.webProjekat.Service.KorisnikService;
import ac.rs.ftn.webProjekat.Service.RecenzijaService;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/recenzije")
public class RecenzijeRestController {

    @Autowired
    private RecenzijaService recenzijaService;

    @Autowired
    private KnjigaService knjigaService;

    @Autowired
    private KorisnikService korisnikService;

    @GetMapping("/lista")
    public ResponseEntity<List<RecenzijaDto>> listaRecenzije()
    {
        List<Recenzija> recenzijaList = recenzijaService.findAll();
        List<RecenzijaDto> recenzijaDtoList = new ArrayList<>();
        for (Recenzija rec : recenzijaList) {
            recenzijaDtoList.add( new RecenzijaDto(rec) );
        }

        return new ResponseEntity<>(recenzijaDtoList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRecenzijaById(@PathVariable(name = "id") Long id) {
        Recenzija targetRecenzija = recenzijaService.findById(id);

        if (targetRecenzija == null) {
            return new ResponseEntity<>("Ne postoji recenzija sa tim id-om!", HttpStatus.NOT_FOUND);
        }

        RecenzijaDto recenzijaDto = new RecenzijaDto(targetRecenzija);

        // uspjesno
        return new ResponseEntity<>(recenzijaDto, HttpStatus.OK);
    }

    // nadji recenziju pomocu knjige
    @GetMapping("/knjiga/{id}/recenzije")
    public ResponseEntity<List<RecenzijaDto>> getRecenzijeByKnjigaId(@PathVariable(name = "id") Long knjigaId)
    {
        List<Recenzija> recenzijaList = recenzijaService.findRecenzijeOfKnjigaId(knjigaId);
        List<RecenzijaDto> recenzijaDtoList = new ArrayList<>();
        for (Recenzija it : recenzijaList) {
            recenzijaDtoList.add( new RecenzijaDto(it) );
        }

        return new ResponseEntity<>(recenzijaDtoList,HttpStatus.OK);
    }

    //dodaj recenziju na knjigu

    @PostMapping("/knjiga/{knjigaId}/recenzija")
    public ResponseEntity<String> addRecenzija(@PathVariable(name = "knjigaId") Long knjigaId,
                                               @RequestBody RecenzijaDataDto recenzijaDataDto,
                                               HttpSession httpSession) {

        // Session check
        Korisnik loggedUser = (Korisnik) httpSession.getAttribute("loggedUser");
      //  String x = loggedUser.getKorisnickoIme();
      //  Korisnik praviKorisnik = korisnikService.findByKorisnickoIme(x);
        if (loggedUser == null) {
            return new ResponseEntity<>("No session!", HttpStatus.FORBIDDEN);
        }


        //probavam da dodam korisnicko ime
        //Korisnik ime = knjigaService.findKorisnikById(loggedUser.getId());
        loggedUser = recenzijaService.findByKorisnikId(loggedUser.getId());

        System.out.println("KORISNIKOVE POLICE:" + loggedUser.getPolice().toString());

        Polica targetPolica = loggedUser.getPrimarnaPolicaByKnjigaId(knjigaId);
        if (targetPolica == null && targetPolica.getTip()!= TipPolice.READ) {
            return new ResponseEntity<>("Knjiga nije na korisnikovoj primarnoj polici READ!", HttpStatus.FORBIDDEN);
        }

        StavkaPolice targetStavka = targetPolica.getStavkaByKnjigaId(knjigaId);
        System.out.println("STAVKA PRIJE ADDA" +targetStavka.toString());
        Recenzija recenzija = new Recenzija(recenzijaDataDto);
        recenzija.setKorisnik(loggedUser);
        recenzijaService.saveRecenzija(recenzija);
       // System.out.println(recenzija.toString());
        targetStavka.getRecenzija().add(recenzija);
        recenzijaService.saveStavkaPolice(targetStavka);
        System.out.println("STAVKA POSLE ADDA " +targetStavka.toString());
        //knjigaService.saveStavkeOfKnjiga(targetStavka);
        // recenzija.setKorisnik(praviKorisnik);
        // Korisnik korisnik = new Korisnik();
       // korisnik.setEmailAdresa(loggedUser.getEmailAdresa());
       // korisnik.setKorisnickoIme(loggedUser.getKorisnickoIme());
       // recenzija.setKorisnik(korisnik);

        //System.out.println("KOR POLICE KONACNO" + loggedUser.getPolice().toString());

        return new ResponseEntity<>("Recenzija uspješno dodata!", HttpStatus.OK);
    }

    //azuriraj recenziju
    @PutMapping("/{id}")
    public ResponseEntity<String> updateRecenizja(@PathVariable(name = "id") Long recenzijaId,
                                                  @RequestBody RecenzijaDto recenzijaDto,
                                                  HttpSession httpSession)
    {
        Korisnik loggedUser = (Korisnik) httpSession.getAttribute("loggedUser");
        if (loggedUser == null) {
            return new ResponseEntity<>("No session!", HttpStatus.FORBIDDEN);
        }


        Recenzija targetRecenzija = recenzijaService.findById(recenzijaId);
        if (targetRecenzija == null ) {
            return new ResponseEntity<>("Recenzija nije pronadjena!", HttpStatus.NOT_FOUND);
        }

        if (targetRecenzija.getKorisnik().getId() != loggedUser.getId()) {
            return new ResponseEntity<>("Recenzija nije od strane ovog korisnika!", HttpStatus.FORBIDDEN);
        }

        System.out.println("OVO JE RECENZIJADTO: " + recenzijaDto.getTekst());
        if(recenzijaDto.getTekst() == null) {
            recenzijaDto.setTekst(targetRecenzija.getTekst());
        }

        if(recenzijaDto.getTekst().isEmpty()) {
            recenzijaDto.setTekst(targetRecenzija.getTekst());
        }
        targetRecenzija.updateRecenzija(
                recenzijaDto.getOcjena(),
                recenzijaDto.getTekst(),
                recenzijaDto.getDatumRecenzije()
        );

        recenzijaService.saveRecenzija(targetRecenzija);

        return new ResponseEntity<>("Recenzija uspjesno azurirana!", HttpStatus.OK);
    }

    // izbrisi recenziju
    // ne brise iz liste iz nekog razloga
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRecenzija(@PathVariable(name = "id") Long recenzijaId,
                                                  HttpSession httpSession)
    {
        Korisnik loggedUser = (Korisnik) httpSession.getAttribute("loggedUser");
        if (loggedUser == null) {
            return new ResponseEntity<>("No session!", HttpStatus.FORBIDDEN);
        }

        Recenzija targetRecenzija = recenzijaService.findById(recenzijaId);
        if (targetRecenzija == null) {
            return new ResponseEntity<>("Recenzija ne postoji", HttpStatus.NOT_FOUND);
        }

        if (targetRecenzija.getKorisnik().getId() != loggedUser.getId()) {
            return new ResponseEntity<>("Ovo nije vasa recenzija!", HttpStatus.FORBIDDEN);
        }

        targetRecenzija.setKorisnik(null);
        recenzijaService.saveRecenzija(targetRecenzija);
        recenzijaService.deleteRecenzija(targetRecenzija);

        return new ResponseEntity<>("Uspjesno izbrisana recenzija!", HttpStatus.OK);
    }
}
