package ac.rs.ftn.webProjekat.Entity;

import ac.rs.ftn.webProjekat.Dto.PolicaDto;
import jakarta.persistence.*;

import java.util.*;
import java.io.Serializable;


@Entity
@Table(name = "polica")
public class Polica implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "naziv")
    private String naziv;

    @Column(name = "tip")
    private TipPolice tip;

    /*@ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
    @JoinColumn(name = "korisnik_id")
    private Korisnik korisnik;*/
    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
    @JoinColumn(referencedColumnName = "id",
            name = "polica_id"
    )
    private Set<StavkaPolice> stavkaPolice;

    public boolean daLiPostojiKnjiga(Knjiga knjiga) {
        for (StavkaPolice st : stavkaPolice) {
            if (st.getKnjiga().getISBN().equals(knjiga.getISBN())) {
                return true;
            }
        }
        return false;
    }

    public StavkaPolice getStavkaByKnjigaISBN(Knjiga knjiga) {
        for (StavkaPolice s : stavkaPolice) {
            if (s.getKnjiga().getISBN().equals(knjiga.getISBN())) {
                return s;
            }
        }
        return null;
    }

    public StavkaPolice getStavkaByKnjigaId(Long knjigaId) {
        for (StavkaPolice s : stavkaPolice) {
            if (s.getKnjiga().getId().equals(knjigaId)) {
                return s;
            }
        }
        return null;
    }

    public StavkaPolice getStavkaByKnjiga(Knjiga knjiga) {
        for (StavkaPolice st : stavkaPolice) {
            if (st.getKnjiga().getISBN().equals(knjiga.getISBN())) {
                return st;
            }
        }
        return null;
    }

    // provjeriti da li valja GETKNJIGE metoda
    public List<Knjiga> getKnjige() {
        if (stavkaPolice == null || stavkaPolice.isEmpty()) {
            return null;
        }

        List<Knjiga> knjige = new ArrayList<>();

        for (StavkaPolice s : stavkaPolice) {
            Knjiga knjiga = s.getKnjiga();
            if (!knjige.contains(knjiga)) {
                knjige.add(knjiga);
            }
        }

        return knjige;
    }


    //izbrisi stavku police, nsinam sigurna za ovo
    public void removeStavkaPolice(StavkaPolice stavka) {
        Set<StavkaPolice> newStavkaPolice = new HashSet<>();

        //bukvalno pravimo novi set gdje dodajemo sve one knjige
        // koje ne treba da se izbrisu, tj. sve sem one jedne
        // ako je uopste ima
        // ako je nema, tj ako je ista velicina seta 1. i 2.
        // onda nista
        for (StavkaPolice s : stavkaPolice) {
            if (s.getId() != stavka.getId()) {
                newStavkaPolice.add(s);
            }
        }

        if (newStavkaPolice.size() != stavkaPolice.size())
            stavkaPolice = newStavkaPolice;
    }

    public Polica(PolicaDto policaDto) {
        this.id = policaDto.getId();
        this.naziv = policaDto.getNaziv();
       // this.korisnik = policaDto.getKorisnik();
        this.tip = TipPolice.REGULAR;
    }
    public Polica() {
        this.tip = TipPolice.REGULAR;
    }

    public Polica(String naziv) {
        //this.stavkaPolice = new StavkaPolice();
        this.naziv = naziv;
        this.tip = TipPolice.REGULAR;
    }
    public Polica(String naziv, TipPolice tip) {
        this.naziv = naziv;
        this.tip = tip;
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

    public TipPolice getTip() {
        return tip;
    }

    public void setTip(TipPolice tip) {
        this.tip = tip;
    }

    public Set<StavkaPolice> getStavkaPolice() {
        return stavkaPolice;
    }

    public void setStavkaPolice(Set<StavkaPolice> stavkaPolice) {
        this.stavkaPolice = stavkaPolice;
    }

    /*public Korisnik getKorisnik() {
        return korisnik;
    }

    public void setKorisnik(Korisnik korisnik) {
        this.korisnik = korisnik;
    }*/

    @Override
    public String toString() {
        return "Polica{" +
                "id=" + id +
                ", naziv='" + naziv + '\'' +
                ", tip=" + tip +
                ", stavkaPolice=" + stavkaPolice +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Polica polica = (Polica) o;
        return Objects.equals(id, polica.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}