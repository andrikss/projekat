package ac.rs.ftn.webProjekat.Service;

import ac.rs.ftn.webProjekat.Entity.Knjiga;
import ac.rs.ftn.webProjekat.Entity.StavkaPolice;
import ac.rs.ftn.webProjekat.Repository.StavkaPoliceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StavkaPoliceService {

    @Autowired
    private StavkaPoliceRepository stavkaPoliceRepository;

    public List<StavkaPolice> findAllStavka() {
        return stavkaPoliceRepository.findAll();
    }

    public StavkaPolice findById(Long id) {
        List<StavkaPolice> stavke = stavkaPoliceRepository.findAll();
        for (StavkaPolice it : stavke) {
            if (it.getId().equals(id)) {
                return it;
            }
        }
        return null;
    }

    public StavkaPolice saveStavka(StavkaPolice st) {
        return stavkaPoliceRepository.save(st);
    }

    public void deleteStavka(StavkaPolice st) {
       stavkaPoliceRepository.delete(st);
    }

    public void deleteStavkeOfKnjiga(Knjiga knjiga) {
        List<StavkaPolice> stavke = stavkaPoliceRepository.findAll();
        for(StavkaPolice it : stavke) {
            if(it.getKnjiga().getId().equals(knjiga.getId())) {
                deleteStavka(it);
                //is save reaally necesery
                stavkaPoliceRepository.save(it);
            }
        }
    }
// ili pak ovako kao drugi, vidjecemo sta radi u controlleru
    /*
    public void deleteStavkeOfKnjiga(Knjiga knjiga) {
        List<StavkaPolice> stavke = stavkaPoliceRepository.findAll();
        List<StavkaPolice> newStavkaPoliceList = new ArrayList<>();

        for (StavkaPolice it : stavke) {
            if (!it.getKnjiga().getId().equals(knjiga.getId())) {
                newStavkaPoliceList.add(it);
            } else {
                deleteStavka(it);
            }
        }

        stavkaPoliceRepository.saveAll(newStavkaPoliceList);
    }

*/

}
