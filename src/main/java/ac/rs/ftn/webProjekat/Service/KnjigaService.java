package ac.rs.ftn.webProjekat.Service;

import ac.rs.ftn.webProjekat.Entity.*;
import ac.rs.ftn.webProjekat.Repository.KnjigaRepository;
import ac.rs.ftn.webProjekat.Repository.KorisnikRepository;
import ac.rs.ftn.webProjekat.Repository.StavkaPoliceRepository;
import ac.rs.ftn.webProjekat.Repository.ZanrRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class KnjigaService {

    @Autowired
    private KnjigaRepository knjigaRepository;

    @Autowired
    private KorisnikService korisnikService;

    @Autowired
    private ZanrService zanrService;

    @Autowired
    private StavkaPoliceService stavkaPoliceService;

    public List<Knjiga> findAllKnjiga() {
        return knjigaRepository.findAll();
    }

    public Knjiga findById(Long id) {
        List<Knjiga> knjige = knjigaRepository.findAll();
        for (Knjiga it : knjige) {
            if (it.getId().equals(id)) {
                return it;
            }
        }
        return null;
    }

    public Knjiga findByISBN(String isbn) {
        List<Knjiga> knjige = knjigaRepository.findAll();
        for (Knjiga it : knjige) {
            if (it.getISBN().equals(isbn)) {
                return it;
            }
        }
        return null;
    }

    public Knjiga findByNaslov(String naslov) {
        List<Knjiga> knjige = knjigaRepository.findAll();
        for (Knjiga it : knjige) {
            if (it.getNaslov().equals(naslov)) {
                return it;
            }
        }
        return null;
    }

    //da li knjiga ima recenziju
    //stavka police jer ima mapiranje na rec
    public boolean daLiImaRecenziju(Knjiga knjiga) {
        List<StavkaPolice> stavke = stavkaPoliceService.findAllStavka();
        for (StavkaPolice it : stavke) {
            if (it.getKnjiga().getId().equals(knjiga.getId())) {
                if (it.getRecenzija().size() > 0) { // je l validna
                    return true;
                }
            }
        }
        return false;
    }

    //dobavi recenziju
    public Set<Recenzija> getRecenzija(Knjiga knjiga) {
        List<StavkaPolice> stavke = stavkaPoliceService.findAllStavka();
        for(StavkaPolice it : stavke) {
            if(it.getKnjiga().getId().equals(knjiga.getId())) {
                return it.getRecenzija();
            }
        }
        return null;
    }

    public void poveziZanrSaKnjigom(Knjiga knjiga, List<String> zanrNaziv)
    {
        List<Zanr> zanrList = zanrService.findAllZanr();

        for (Zanr it : zanrList) {

            for (String jt : zanrNaziv) {
                if (it.getNaziv().equals(jt)) {
                    knjiga.getZanrovi().add(it);
                    break;
                }
            }
        }
    }

    public Knjiga save(Knjiga knjiga) { return knjigaRepository.save(knjiga); }
    public void delete(Knjiga knjiga) { knjigaRepository.delete(knjiga);}

    // ove funkcije na dole nisu vezane direktno za knjigu
    // ali ce mi biti potrebne u controlleru

    public Korisnik findKorisnikById(Long id) { return korisnikService.findById(id);}

    public Zanr findZanrById(Long id) { return zanrService.findZanrById(id); }

    public Autor findAutorByEmail(String email) {
        Autor autor = (Autor) korisnikService.findByEmail(email);
        if(autor != null) {
            if(autor.getUlogaKorisnika().equals(UlogaKorisnika.AUTOR.toString())) {
                return autor;
            }
        }
        return null;
    }

    //pravimo listu autora
    // ulazimo u jednog autora i listamo njegove knjige
    // ako pronadjemo knjigu sa istim isbnom
    // nasli smo i autora
    public Autor findAutorByISBN(String isbn) {
        List<Autor> autori = korisnikService.findAllAutor();
        for(Autor it : autori) {
            for(Knjiga x : it.getAutoroveKnjige()) {
                if(x.getISBN().equals(isbn)) {
                    return it;
                }
            }
        }
        return null;
    }

    //naredne funkcije sluze za
    //apdejtanje brisnaje i sacuvavanje knjiga
    // jer npr kad brisem
    // morma da izbrisem i zanr i stavku i autora
    // kad apdjetam/cuvam isto
    public void saveAutorOfKnjiga(Autor autor) {
        korisnikService.saveKorisnik(autor);
    }

    public void deleteKnjigaOfAutor(Knjiga knjiga) {
        korisnikService.deleteKnjigaOfAutor(knjiga);
    }

    public void saveStavkeOfKnjiga(StavkaPolice stavka) {
        stavkaPoliceService.saveStavka(stavka);
    }
    public void deleteStavkeOfKnjiga(Knjiga knjiga) {
        stavkaPoliceService.deleteStavkeOfKnjiga(knjiga);
    }

    public void saveZanrOfKnjiga(Zanr zanr) {
        zanrService.saveZanr(zanr);
    }
    public void deleteZanrOfKnjiga(Knjiga knjiga) {
        zanrService.deleteZanrOfKnjiga(knjiga);
    }

    public List<Knjiga> findKnjigeThatContainInISBN(String ISBN_substring) {
        List<Knjiga> ret = new ArrayList<>();
        List<Knjiga> knjigaList = knjigaRepository.findAll();
        for (Knjiga k : knjigaList) {
            if (k.getISBN().contains(ISBN_substring)) {
                ret.add(k);
            }
        }
        return ret;
    }

    public List<Knjiga> findKnjigeThatContainInNaslov(String naslov_substring) {
        List<Knjiga> ret = new ArrayList<>();
        List<Knjiga> knjigaList = knjigaRepository.findAll();
        for (Knjiga k : knjigaList) {
            if (k.getNaslov().contains(naslov_substring)) {
                ret.add(k);
            }
        }
        return ret;
    }

}
