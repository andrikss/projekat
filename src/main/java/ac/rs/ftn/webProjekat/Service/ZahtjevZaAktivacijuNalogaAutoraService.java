package ac.rs.ftn.webProjekat.Service;

import ac.rs.ftn.webProjekat.Entity.Autor;
import ac.rs.ftn.webProjekat.Entity.Korisnik;
import ac.rs.ftn.webProjekat.Entity.ZahtjevZaAktivacijuNalogaAutora;
import ac.rs.ftn.webProjekat.Repository.ZahtjevRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ZahtjevZaAktivacijuNalogaAutoraService {

    @Autowired
    private ZahtjevRepository zahtjevRepository;

    @Autowired
    private AutorService autorService;
    @Autowired
    private KorisnikService korisnikService;

    public List<ZahtjevZaAktivacijuNalogaAutora> findAll() {
        return zahtjevRepository.findAll();
    }

    public String generisiKorisnickoIme(String emailAdresa) {
        int atIndex = emailAdresa.indexOf('@');
        if (atIndex != -1) {
            String korisnickoIme = emailAdresa.substring(0, atIndex);
            return korisnickoIme;
        } else {
            return emailAdresa;
        }
    }
    public ZahtjevZaAktivacijuNalogaAutora findById(Long id) {
        List<ZahtjevZaAktivacijuNalogaAutora> zahtjevi = zahtjevRepository.findAll();
        for(ZahtjevZaAktivacijuNalogaAutora it : zahtjevi) {
            if(it.getId().equals(id)) {
                return  it;
            }
        }
        return null;
    }

    public ZahtjevZaAktivacijuNalogaAutora saveZahtjev(ZahtjevZaAktivacijuNalogaAutora zahtjev) {
        return zahtjevRepository.save(zahtjev);
    }

    public void deleteZahtjev(ZahtjevZaAktivacijuNalogaAutora zahtjev) {
        zahtjevRepository.delete(zahtjev);
    }

    // controller

    public Korisnik findKorisnikById(Long Id) {
        return korisnikService.findById(Id);
    }

    public Korisnik findKorisnikByEmail(String email) {
        return korisnikService.findByEmail(email);
    }

    public Korisnik saveKorisnik(Korisnik korisnik) {
        return korisnikService.saveKorisnik(korisnik);
    }

    public Autor saveAutor(Autor autor) { return  autorService.saveAutor(autor); }
}
