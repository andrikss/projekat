package ac.rs.ftn.webProjekat.Entity;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.io.Serializable;

@Entity
@Table(name = "stavka_police")
public class StavkaPolice implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {})
    @JoinColumn(name = "stavka_police_knjiga",
            referencedColumnName = "id"
    )
    private Knjiga knjiga;

    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
    @JoinColumn(name = "stavka_police_id",
            referencedColumnName = "id",
            nullable = true
    )
    private Set<Recenzija> recenzija = new HashSet<>();

    public void izbrisiRecenziju(Recenzija r) {
        //ista prica kao i za policu
        // pravimo novi set u koji ubacujemo sve one osim
        // one koju treba izbrisati
        // ako je ne nadjemo nikom nista
        Set<Recenzija> newRecenzija = new HashSet<>();
        for (Recenzija it : recenzija) {
            if (it.getId() != r.getId()) {
                newRecenzija.add(it);
            }
        }
        //Only change if removed
        if (newRecenzija.size() != recenzija.size())
            recenzija = newRecenzija;
    }

    public boolean daLiImaRecenziju(Recenzija r) {
        return recenzija.contains(r);
    }

    // trebalo bi mozda implementirati dobavljanje recenzija po id-u
    // kao sto smo i za knjigu
    // ali to posle
    // getRecenzijaById

   /* public void removeKnjiga(Knjiga knj) {
        Set<Knjiga> newKnjige = new HashSet<>();
        for (Knjiga it : knjiga) {
            if (!it.getISBN().equals(knj.getISBN())) {
                newKnjige.add(it);
            }
        }
        knjiga = (Knjiga) newKnjige;
    }*/


    public StavkaPolice() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Knjiga getKnjiga() {
        return knjiga;
    }

    public void setKnjiga(Knjiga knjiga) {
        this.knjiga = knjiga;
    }

    public Set<Recenzija> getRecenzija() {
        return recenzija;
    }

    public void setRecenzija(Set<Recenzija> recenzija) {
        this.recenzija = recenzija;
    }

    @Override
    public String toString() {
        return "StavkaPolice{" +
                "id=" + id +
                ", knjiga=" + knjiga +
              //  ", recenzija=" + recenzija +
                '}';
    }
}
