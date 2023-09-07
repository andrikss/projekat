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

    public Autor saveAutor(Autor autor) {
       return autorRepository.save(autor);
    }

    public void deleteAutor(Autor autor) {  autorRepository.delete(autor);}
    public Autor findByEmail(String email) {
        List<Autor> autori = autorRepository.findAll();
        for(Autor it : autori) {
            if(it.getEmailAdresa().equals(email)) {
                return it;
            }
        }
        return null;
    }

    public void removeAuthorFromBook(Long authorId, Long bookId) {
        // Prvo dohvatite autora iz baze
        Autor autor = autorRepository.findById(authorId).orElse(null);

        // Proverite da li je autor pronađen
        if (autor != null) {
            // Uklonite knjigu iz liste knjiga autora
            autor.getAutoroveKnjige().removeIf(knjiga -> knjiga.getId().equals(bookId));
            autorRepository.save(autor);
        }
    }

    public List<Autor> getAll() {
        return autorRepository.findAll();
    }
}
