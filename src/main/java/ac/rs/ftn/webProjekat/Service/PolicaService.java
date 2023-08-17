package ac.rs.ftn.webProjekat.Service;

import ac.rs.ftn.webProjekat.Entity.Knjiga;
import ac.rs.ftn.webProjekat.Entity.Korisnik;
import ac.rs.ftn.webProjekat.Entity.Polica;
import ac.rs.ftn.webProjekat.Entity.StavkaPolice;
import ac.rs.ftn.webProjekat.Repository.PolicaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class PolicaService {

    @Autowired
    private PolicaRepository policaRepository;

    @Autowired
    private KorisnikService korisnikService;

    @Autowired
    private KnjigaService knjigaService;

    @Autowired
    private StavkaPoliceService stavkaPoliceService;

    public List<Polica> findAllPolica() {
        return policaRepository.findAll();
    }

    public Polica findById(Long id) {
        List<Polica> police = policaRepository.findAll();
        for(Polica it : police) {
            if(it.getId().equals(id)) {
                return it;
            }
        }
        return null;
    }

    // cuvanje i brisannje polica se mora odraditi
    // na nesto drukciji nacin
    // prvo cuvamo sve stavke police
    // pa onda policu
    // isto je i sa brisnajem
    // cascsade type all lol

    public Polica savePolica(Polica polica) {
        for(StavkaPolice it : polica.getStavkaPolice()) {
            stavkaPoliceService.saveStavka(it);
        }
        return policaRepository.save(polica);
    }

    public void deletePolica(Polica polica) {
        for(StavkaPolice it : polica.getStavkaPolice()) {
            stavkaPoliceService.deleteStavka(it);
        }
        polica.setStavkaPolice(null); // classic c thing hahahh mora na null
        policaRepository.delete(polica);
    }

    //opet stvari koje ce mi trebati u kontroleru

    public List<Polica> findPolicaByKorisnikId(Long id) {
        Korisnik korisnik = korisnikService.findById(id);
        if(korisnik != null) {
            return korisnik.getPolice().stream().toList();
        }
        return null;
    }

    public Korisnik saveKorisnik(Korisnik korisnik) {
        return korisnikService.saveKorisnik(korisnik);
    }

    public Korisnik findKorisnikById(Long id) {
        return korisnikService.findById(id);
    }

    public Knjiga findKnjigaById(Long id) {
        return knjigaService.findById(id);
    }

    public Knjiga findKnjigaByIsbn(String isbn) {
        return knjigaService.findByISBN(isbn);
    }

    public void deleteStavkaPolice(StavkaPolice st) {
        stavkaPoliceService.deleteStavka(st);
    }

    public void deleteStavkeOfKnjiga(Polica polica, Knjiga knjiga) {
        List<StavkaPolice> stavke = new ArrayList<>();
        for(StavkaPolice it : polica.getStavkaPolice()) {
            if(it.getKnjiga().getId().equals(knjiga.getId())) {
                stavkaPoliceService.deleteStavka(it);
            } else {
                stavke.add(it);
            }
        }
        polica.setStavkaPolice((Set<StavkaPolice>) stavke);
    }
}


