package ac.rs.ftn.webProjekat.Entity;

import ac.rs.ftn.webProjekat.Dto.RecenzijaDataDto;
import ac.rs.ftn.webProjekat.Dto.RecenzijaDto;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "recenzija")
public class Recenzija implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ocjena")
    private Long ocjena;
    @Column(name = "tekst")
    private String tekst;
    @Column(name = "datumRecenzije")
    private Date datumRecenzije;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {})
    @JoinColumn(name = "korisnik_id",
            referencedColumnName = "id"
    )
    private Korisnik korisnik;

    public void updateRecenzija(Long ocjena, String tekst, Date datumRecenzije) {
        this.ocjena = ocjena;
        this.tekst = tekst;
        this.datumRecenzije = datumRecenzije;
    }

    //equals metoda???? da li treba mozda

    public Recenzija() {

    }
    public Recenzija(RecenzijaDataDto recenzijaDataDto) {
        this.ocjena = recenzijaDataDto.getOcjena();
        this.tekst = recenzijaDataDto.getTekst();
        this.datumRecenzije = recenzijaDataDto.getDatumRecenzije();
    }

    public Recenzija(Long ocjena, String tekst,Date datumRecenzije) {
        this.ocjena = ocjena;
        this.tekst = tekst;
        this.datumRecenzije = datumRecenzije;
    }

    public Recenzija(RecenzijaDto recenzijaDto) {
        this.ocjena = recenzijaDto.getOcjena();
        this.datumRecenzije = recenzijaDto.getDatumRecenzije();
        this.tekst = recenzijaDto.getTekst();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOcjena() {
        return ocjena;
    }

    public void setOcjena(Long ocjena) {
        this.ocjena = ocjena;
    }

    public String getTekst() {
        return tekst;
    }

    public void setTekst(String tekst) {
        this.tekst = tekst;
    }

    public Date getDatumRecenzije() {
        return datumRecenzije;
    }

    public void setDatumRecenzije(Date datumRecenzije) {
        this.datumRecenzije = datumRecenzije;
    }

    public Korisnik getKorisnik() {
        return korisnik;
    }

    public void setKorisnik(Korisnik korisnik) {
        this.korisnik = korisnik;
    }

    @Override
    public String toString() {
        return "Recenzija{" +
                "id=" + id +
                ", ocjena=" + ocjena +
                ", tekst='" + tekst + '\'' +
                ", datumRecenzije=" + datumRecenzije +
               // ", korisnik=" + korisnik +
                '}';
    }

    public boolean equals(RecenzijaDto recenzijaDto) {
        //this.datumRecenzije == recenzijaDto.getDatumRecenzije()   //Date are still messy so either make a class that casts them or dont use them at all
        if (this.ocjena != recenzijaDto.getOcjena())
            return false;
        if (this.tekst == null && recenzijaDto.getTekst() == null)
            return true;
        if (this.tekst != null && recenzijaDto.getTekst() != null &&
                this.tekst.equals(recenzijaDto.getTekst()))
        {
            return true;
        }
        return false;
    }
}
