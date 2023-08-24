package ac.rs.ftn.webProjekat.Dto;

import java.util.Date;

public class RegistrujSeDto {

    private String emailAdresa;
    private String korisnickoIme;
    private String ime;
    private String prezime;
    private String lozinka;
    private String ponovljenaLozinka;
    private Date datumRodjenja;
    private String opis;
    private String profilnaSlika;

    public RegistrujSeDto() {
    }

    public RegistrujSeDto(String emailAdresa, String korisnickoIme, String ime, String prezime, String lozinka, String ponovljenaLozinka, Date datumRodjenja, String opis, String profilnaSlika) {
        this.emailAdresa = emailAdresa;
        this.korisnickoIme = korisnickoIme;
        this.ime = ime;
        this.prezime = prezime;
        this.lozinka = lozinka;
        this.ponovljenaLozinka = ponovljenaLozinka;
        this.datumRodjenja = datumRodjenja;
        this.opis = opis;
        this.profilnaSlika = profilnaSlika;
    }

    public String getEmailAdresa() {
        return emailAdresa;
    }

    public void setEmailAdresa(String emailAdresa) {
        this.emailAdresa = emailAdresa;
    }

    public String getKorisnickoIme() {
        return korisnickoIme;
    }

    public void setKorisnickoIme(String korisnickoIme) {
        this.korisnickoIme = korisnickoIme;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public String getLozinka() {
        return lozinka;
    }

    public void setLozinka(String lozinka) {
        this.lozinka = lozinka;
    }

    public String getPonovljenaLozinka() {
        return ponovljenaLozinka;
    }

    public void setPonovljenaLozinka(String ponovljenaLozinka) {
        this.ponovljenaLozinka = ponovljenaLozinka;
    }

    public Date getDatumRodjenja() {
        return datumRodjenja;
    }

    public void setDatumRodjenja(Date datumRodjenja) {
        this.datumRodjenja = datumRodjenja;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public String getProfilnaSlika() {
        return profilnaSlika;
    }

    public void setProfilnaSlika(String profilnaSlika) {
        this.profilnaSlika = profilnaSlika;
    }
}
