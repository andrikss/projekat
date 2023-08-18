package ac.rs.ftn.webProjekat.Controller;

import ac.rs.ftn.webProjekat.Dto.ZanrDto;
import ac.rs.ftn.webProjekat.Entity.Korisnik;
import ac.rs.ftn.webProjekat.Entity.UlogaKorisnika;
import ac.rs.ftn.webProjekat.Entity.Zanr;
import ac.rs.ftn.webProjekat.Service.ZanrService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/zanrovi")
public class ZanrRestController {

    @Autowired
    private ZanrService zanrService;

    // lista zanrova
    @GetMapping("/lista")
    public ResponseEntity<List<ZanrDto>> listaZanrova()
    {
        List<Zanr> zanrList = zanrService.findAllZanr();
        List<ZanrDto> zanrDtoList = new ArrayList<>();

        for (Zanr zanr : zanrList) {
            zanrDtoList.add(new ZanrDto(zanr));
        }

        return new ResponseEntity<>( zanrDtoList, HttpStatus.OK);
    }

    //zanr po id-u
    @GetMapping("/{zanrId}")
    public ResponseEntity<ZanrDto> getZanrById(@PathVariable Long zanrId) {
        Zanr zanr = zanrService.findZanrById(zanrId);

        if (zanr == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        ZanrDto zanrDto = new ZanrDto(zanr);
        return new ResponseEntity<>(zanrDto, HttpStatus.OK);
    }

    //dodavanje zanra
    @PostMapping("/dodajZanr")
    public ResponseEntity<String> addZanr(@RequestBody ZanrDto zanrDto, HttpSession httpSession) {

        if (zanrDto.getNaziv() == null) {
            return new ResponseEntity<>("Niste upisali ime zanra!", HttpStatus.BAD_REQUEST);
        }

        Korisnik loggedUser = (Korisnik) httpSession.getAttribute("loggedUser");
        if (loggedUser == null) {
            return new ResponseEntity<>("No session!", HttpStatus.FORBIDDEN);
        }
        //loggedUser = korisnikService.findById(loggedUser.getId());

        if (!loggedUser.getUlogaKorisnika().equals(UlogaKorisnika.ADMINISTRATOR.toString())) {
            return new ResponseEntity<>("Niste administrator!", HttpStatus.FORBIDDEN);
        }

        if (zanrService.daLiPostojiZanr(zanrDto)) {
            return new ResponseEntity<>("Zanr vec postoji!",HttpStatus.CONFLICT);
        }


        Zanr newZanr = new Zanr(zanrDto);

        zanrService.saveZanr(newZanr);
        return new ResponseEntity<>("Uspjesno!", HttpStatus.OK);
    }

    //apdejtuj zanr? je l treba

    @DeleteMapping("/{zanrId}")
    public ResponseEntity<String> deleteZanr(@PathVariable Long zanrId, HttpSession httpSession) {
        Korisnik loggedUser = (Korisnik) httpSession.getAttribute("loggedUser");

        if (loggedUser == null) {
            return new ResponseEntity<>("No session!", HttpStatus.FORBIDDEN);
        }

        if (!loggedUser.getUlogaKorisnika().equals(UlogaKorisnika.ADMINISTRATOR.toString())) {
            return new ResponseEntity<>("Niste administrator!", HttpStatus.FORBIDDEN);
        }

        Zanr zanr = zanrService.findZanrById(zanrId);

        if (zanr == null) {
            return new ResponseEntity<>("Zanr ne postoji!", HttpStatus.NOT_FOUND);
        }

        zanrService.deleteZanr(zanr);
        return new ResponseEntity<>("Zanr uspjesno obrisan!", HttpStatus.OK);
    }

}
