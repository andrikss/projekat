package ac.rs.ftn.webProjekat.Entity;

import ac.rs.ftn.webProjekat.Dto.ZahtjevZaAktivacijuNalogaAutoraDto;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "zahtjev_za_aktivaciju_naloga_autora")
public class ZahtjevZaAktivacijuNalogaAutora implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email_adresa")
    private String emailAdresa;

    @Column(name = "telefon")
    private String telefon;

    @Column(name = "poruka")
    private String poruka;

    @Column(name = "datum")
    private Date datum;

    @Column(name = "status")
    private StatusZahtjeva status;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "autor_id",
            referencedColumnName = "id")
    private Autor autor;

    public ZahtjevZaAktivacijuNalogaAutora() {
        this.status = StatusZahtjeva.NA_CEKANJU;
    }

    public ZahtjevZaAktivacijuNalogaAutora(ZahtjevZaAktivacijuNalogaAutoraDto zahtjev) {
        this.emailAdresa = zahtjev.getEmailAdresa();
        this.telefon = zahtjev.getTelefon();
        this.datum = zahtjev.getDatum();
        this.poruka = zahtjev.getPoruka();
        this.status = StatusZahtjeva.NA_CEKANJU;
    }

    public ZahtjevZaAktivacijuNalogaAutora(ZahtjevZaAktivacijuNalogaAutora zahtjev) {
        this.status = StatusZahtjeva.NA_CEKANJU;
        this.emailAdresa = zahtjev.getEmailAdresa();
        this.telefon = zahtjev.getTelefon();
        this.datum = zahtjev.getDatum();
        this.poruka = zahtjev.getPoruka();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmailAdresa() {
        return emailAdresa;
    }

    public void setEmailAdresa(String emailAdresa) {
        this.emailAdresa = emailAdresa;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public String getPoruka() {
        return poruka;
    }

    public void setPoruka(String poruka) {
        this.poruka = poruka;
    }

    public Date getDatum() {
        return datum;
    }

    public void setDatum(Date datum) {
        this.datum = datum;
    }

    public StatusZahtjeva getStatus() {
        return status;
    }

    public void setStatus(StatusZahtjeva status) {
        this.status = status;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    @Override
    public String toString() {
        return "ZahtjevZaAktivacijuNalogaAutora{" +
                "id=" + id +
                ", emailAdresa='" + emailAdresa + '\'' +
                ", telefon='" + telefon + '\'' +
                ", poruka='" + poruka + '\'' +
                ", datum=" + datum +
                ", status=" + status +
               // ", autor=" + autor +
                '}';
    }
}
