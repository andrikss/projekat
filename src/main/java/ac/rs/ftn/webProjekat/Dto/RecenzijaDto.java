package ac.rs.ftn.webProjekat.Dto;

import ac.rs.ftn.webProjekat.Entity.Recenzija;

import java.util.Date;

public class RecenzijaDto {

    private Long id;
    private Long ocjena;
    private String tekst;
    private Date datumRecenzije;
    private String korisnik;

    public RecenzijaDto() {}

    public RecenzijaDto(Recenzija recenzija) {
        this.id = recenzija.getId();
        this.ocjena = recenzija.getOcjena();
        this.tekst = recenzija.getTekst();
        this.datumRecenzije = recenzija.getDatumRecenzije();
        this.korisnik = recenzija.getKorisnik().getKorisnickoIme();
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

    public String getKorisnik() {
        return korisnik;
    }

    public void setKorisnik(String korisnik) {
        this.korisnik = korisnik;
    }
}
