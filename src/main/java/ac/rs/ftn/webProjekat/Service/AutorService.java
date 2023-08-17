package ac.rs.ftn.webProjekat.Service;

import ac.rs.ftn.webProjekat.Entity.Autor;
import ac.rs.ftn.webProjekat.Repository.AutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AutorService {

    @Autowired
    private AutorRepository autorRepository;

    public Autor findById(Long id) {
        List<Autor> autori = autorRepository.findAll();
        for(Autor it : autori) {
            if(it.getId().equals(id)) {
                return it;
            }
        }
        return null;
    }

    public Autor save(Autor autor) {
       return autorRepository.save(autor);
    }
}
