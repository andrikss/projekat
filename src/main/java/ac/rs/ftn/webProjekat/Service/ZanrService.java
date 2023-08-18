package ac.rs.ftn.webProjekat.Service;

import ac.rs.ftn.webProjekat.Dto.ZanrDto;
import ac.rs.ftn.webProjekat.Entity.Knjiga;
import ac.rs.ftn.webProjekat.Entity.Zanr;
import ac.rs.ftn.webProjekat.Repository.ZanrRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ZanrService {

    @Autowired
    private ZanrRepository zanrRepository;

    public List<Zanr> findAllZanr() {
        return zanrRepository.findAll();
    }

    public Zanr findZanrById(Long id) {
        List<Zanr> zanrovi = zanrRepository.findAll();
        for(Zanr it : zanrovi) {
            if(it.getId().equals(id)) {
                return it;
            }
        }
        return null;
    }

    public Zanr saveZanr(Zanr zanr) {
        return zanrRepository.save(zanr);
    }

    public void deleteZanr(Zanr zanr) {
        zanrRepository.delete(zanr);
    }

    public boolean daLiPostojiZanr(ZanrDto zanr) {
        List<Zanr> zanrovi = zanrRepository.findAll();
        for(Zanr it : zanrovi) {
            if(it.getNaziv().equals(zanr.getNaziv())) {
                return true;
            }
        }
        return false;
    }

    public void deleteZanrOfKnjiga(Knjiga knjiga) {
        List<Zanr> zanrovi = knjiga.getZanrovi().stream().toList(); //jer je set
        for(Zanr it : zanrovi) {
            it.izbrisiKnjigu(knjiga);
            zanrRepository.save(it);
            // pretty jednostavno
            // izbrise knjigu pomocu f-je u entity
            // sacuva preko repository jpa
        }
    }

}
