package ac.rs.ftn.webProjekat.Entity;

import ac.rs.ftn.webProjekat.Dto.ZanrDto;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "zanr")
public class Zanr implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "naziv")
    private String naziv;

    @ManyToMany(mappedBy = "zanrovi", fetch = FetchType.EAGER, cascade = {})
    private Set<Knjiga> knjige = new HashSet<>();

    public void izbrisiKnjigu(Knjiga knjiga) {
        Set<Knjiga> newKnjige = new HashSet<>();
        for (Knjiga it : knjige) {
            if (!it.getISBN().equals(knjiga.getISBN())) {
                newKnjige.add(it);
            }
        }
        if (newKnjige.size() != knjige.size())
            knjige = newKnjige;
    }

    public Zanr() {
    }

    public Zanr(String naziv) {
        this.naziv = naziv;
    }

    public Zanr(ZanrDto zanrDto) {
        this.naziv = zanrDto.getNaziv();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public Set<Knjiga> getKnjige() {
        return knjige;
    }

    public void setKnjige(Set<Knjiga> knjige) {
        this.knjige = knjige;
    }

    @Override
    public String toString() {
        return "Zanr{" +
                "id=" + id +
                ", naziv='" + naziv + '\'' +
                //", knjige=" + knjige +
                '}';
    }
}
