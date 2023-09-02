package ac.rs.ftn.webProjekat.Service;

import ac.rs.ftn.webProjekat.Entity.*;
import ac.rs.ftn.webProjekat.Repository.KorisnikRepository;
import ac.rs.ftn.webProjekat.Repository.PolicaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class KorisnikService {
    @Autowired
    private KorisnikRepository korisnikRepository;

    public Korisnik findById(Long id) {
        List<Korisnik> korisnici = korisnikRepository.findAll();
        for(Korisnik it : korisnici) {
            if(it.getId().equals(id)) {
                return it;
            }
        }
        return null;
    }

    public Korisnik findByEmail(String emailAdresa) {
        List<Korisnik> korisnici = korisnikRepository.findAll();
        for(Korisnik it : korisnici) {
            if(it.getEmailAdresa().equals(emailAdresa)) {
                return it;
            }
        }
        return null;
    }

    public Korisnik findByKorisnickoIme(String ki) {
        return korisnikRepository.findByKorisnickoIme(ki);
    }

    public Korisnik saveKorisnik(Korisnik korisnik) {
        return korisnikRepository.save(korisnik);
    }

    //pronadji autora al vracam korisnika posto
    // prolazi kroz listu svih korisnika
    // nadje onog specificnog
    // ako ne izbacuje ili nije nasao id ili je nasao id
    // ali to nije autor
    public Korisnik findAutor(Long id) {
        List<Korisnik> korisnici = korisnikRepository.findAll();
        for(Korisnik it : korisnici) {
            if(it.getId() == id) { // je l isti id
                if(it.getUlogaKorisnika().equals(UlogaKorisnika.AUTOR.toString())) {
                    return it;
                }
            }
        }
        return null;
    }

    public List<Korisnik> findAllKorisnik() {
        return korisnikRepository.findAll();
    }
    //pronadji autorE
    public List<Autor> findAllAutor() {
        List<Korisnik> korisnici = korisnikRepository.findAll();
        List<Autor> autori = new ArrayList<>();
        for(Korisnik it : korisnici) {
            if(it.getUlogaKorisnika().equals(UlogaKorisnika.AUTOR.toString())) {
                autori.add((Autor) it);
            }
        }
        return autori;
    }

    public Korisnik findAutorById(Long id) {
        List<Korisnik> autori = korisnikRepository.findAll();
        for(Korisnik it : autori) {
            if(it.getId().equals(id) && it.getUlogaKorisnika().equals(UlogaKorisnika.AUTOR.toString())) {
                return it;
            }
        }
        return null;
    }

    public boolean daLiPostojiDuplikat(Korisnik korisnik) {
        List<Korisnik> korisnici = korisnikRepository.findAll();
        for(Korisnik it : korisnici) {
            if(it.daLiJeDuplikat(korisnik)) {
                return true; // postoji
            }
        }
        return false; // ne postoji
    }

    //kad brisemo knjigu moramo izbrisati i
    //od autora njegovu knjigu??
    // da li to ima logike
    // posle
    // TO DO

    public void deleteKnjigaOfAutor(Knjiga knjiga) {
        List<Korisnik> korisnici = korisnikRepository.findAll();
        for(Korisnik it : korisnici) {
            if(it.getUlogaKorisnika().equals(UlogaKorisnika.AUTOR)) {
                Autor autor = (Autor) it;
                autor.removeKnjiga(knjiga); // u autoru je izbrise
                korisnikRepository.save(autor); //sacuvaj autora
            }
        }
    }





}
