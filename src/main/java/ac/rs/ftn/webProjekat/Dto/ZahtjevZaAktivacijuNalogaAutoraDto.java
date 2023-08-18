package ac.rs.ftn.webProjekat.Dto;

import ac.rs.ftn.webProjekat.Entity.StatusZahtjeva;
import ac.rs.ftn.webProjekat.Entity.ZahtjevZaAktivacijuNalogaAutora;

import java.util.Date;

public class ZahtjevZaAktivacijuNalogaAutoraDto {

    private Long id;
    private String emailAdresa;
    private String telefon;
    private String poruka;
    private Date datum;
    private String autorEmailAdresa;
    private StatusZahtjeva status;

    public boolean isValid() {
        if (autorEmailAdresa == null || autorEmailAdresa.isEmpty()
                || emailAdresa == null || emailAdresa.isEmpty())
        {
            return false;
        }
        return true;
    }
    public ZahtjevZaAktivacijuNalogaAutoraDto() {
    }

    public ZahtjevZaAktivacijuNalogaAutoraDto(ZahtjevZaAktivacijuNalogaAutora zahtjev) {
        this.id = zahtjev.getId();
        this.autorEmailAdresa = zahtjev.getAutor().getEmailAdresa();
        this.poruka = zahtjev.getPoruka();
        this.datum = zahtjev.getDatum();
        this.telefon = zahtjev.getTelefon();
        this.emailAdresa = zahtjev.getEmailAdresa();
        this.status = zahtjev.getStatus();
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

    public String getAutorEmailAdresa() {
        return autorEmailAdresa;
    }

    public void setAutorEmailAdresa(String autorEmailAdresa) {
        this.autorEmailAdresa = autorEmailAdresa;
    }

    public StatusZahtjeva getStatus() {
        return status;
    }

    public void setStatus(StatusZahtjeva status) {
        this.status = status;
    }
}
