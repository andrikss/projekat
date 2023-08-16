package ac.rs.ftn.webProjekat.Service;

import ac.rs.ftn.webProjekat.Entity.Korisnik;
import ac.rs.ftn.webProjekat.Entity.Recenzija;
import ac.rs.ftn.webProjekat.Entity.StavkaPolice;
import ac.rs.ftn.webProjekat.Repository.KorisnikRepository;
import ac.rs.ftn.webProjekat.Repository.RecenzijaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RecenzijaService {

    @Autowired
    private RecenzijaRepository recenzijaRepository;

    @Autowired
    private KorisnikService korisnikService;

    @Autowired
    private StavkaPoliceService stavkaPoliceService;

    public List<Recenzija> findAll() {
        return recenzijaRepository.findAll();
    }

    public Recenzija findById(Long id) {
        List<Recenzija> recenzije = recenzijaRepository.findAll();
        for(Recenzija it : recenzije) {
            if(it.getId().equals(id)) {
                return it;
            }
        }
        return null;
    }

    public Recenzija saveRecenzija(Recenzija recenzija) {
        return recenzijaRepository.save(recenzija);
    }
    // je l mora onda i save?????

    // imamo bidirekcionu vezu mora safe delete
    public void deleteRecenzija(Recenzija recenzija) {
        List<StavkaPolice> stavke = stavkaPoliceService.findAllStavka();
        for(StavkaPolice it : stavke) {
            if(it.daLiImaRecenziju(recenzija)) {
                it.izbrisiRecenziju(recenzija);
                stavkaPoliceService.saveStavka(it);
            }
        }
        recenzijaRepository.deleteById(recenzija.getId());
    }

    //opet controller stuff

    public Korisnik findByKorisnikId(Long id) {
        return korisnikService.findById(id);
    }

    public List<Recenzija> findRecenzijeOfKnjigaId(Long id) {
        List<Recenzija> recenzije = new ArrayList<>();
        List<StavkaPolice> stavke = stavkaPoliceService.findAllStavka();
        for(StavkaPolice it : stavke) {
            if(it.getKnjiga().getId().equals(id)) {
                recenzije.add((Recenzija) it.getRecenzija());
            }
        }
        return recenzije;
    }

    public void saveStavkaPolice(StavkaPolice st) {
        stavkaPoliceService.saveStavka(st);
    }
}
