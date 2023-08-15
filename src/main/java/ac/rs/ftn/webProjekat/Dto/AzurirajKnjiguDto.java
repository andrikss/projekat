package ac.rs.ftn.webProjekat.Dto;

import java.util.Date;

public class AzurirajKnjiguDto {

    private String ISBN;
    private String naslov;
    private Date datumObjavljivanja;
    private Long brojStrana;
    private String opis;
    private Long ocjena;
    private String naslovnaFotografija;
    private String emailAdresaAutora;

    public AzurirajKnjiguDto() {}

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public String getNaslov() {
        return naslov;
    }

    public void setNaslov(String naslov) {
        this.naslov = naslov;
    }

    public Date getDatumObjavljivanja() {
        return datumObjavljivanja;
    }

    public void setDatumObjavljivanja(Date datumObjavljivanja) {
        this.datumObjavljivanja = datumObjavljivanja;
    }

    public Long getBrojStrana() {
        return brojStrana;
    }

    public void setBrojStrana(Long brojStrana) {
        this.brojStrana = brojStrana;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public Long getOcjena() {
        return ocjena;
    }

    public void setOcjena(Long ocjena) {
        this.ocjena = ocjena;
    }

    public String getNaslovnaFotografija() {
        return naslovnaFotografija;
    }

    public void setNaslovnaFotografija(String naslovnaFotografija) {
        this.naslovnaFotografija = naslovnaFotografija;
    }

    public String getEmailAdresaAutora() {
        return emailAdresaAutora;
    }

    public void setEmailAdresaAutora(String emailAdresaAutora) {
        this.emailAdresaAutora = emailAdresaAutora;
    }
}
