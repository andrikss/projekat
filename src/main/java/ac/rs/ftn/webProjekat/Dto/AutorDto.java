package ac.rs.ftn.webProjekat.Dto;

import ac.rs.ftn.webProjekat.Entity.Autor;

import java.util.Date;

public class AutorDto {

    private Long id;

    private boolean aktivan;
    private String ime;
    private String prezime;
    private String emailAdresa;

    private String lozinka;
    private String korisnickoIme;
    private Date datumRodjenja;
    private String opis;
    private String profilnaSlika;

    public AutorDto(Autor autor) {
        this.id = autor.getId();
        this.aktivan = autor.isAktivan();
        this.emailAdresa = autor.getEmailAdresa();
        this.ime = autor.getIme();
        this.opis = autor.getOpis();
        this.profilnaSlika = autor.getProfilnaSlika();
        this.datumRodjenja = autor.getDatumRodjenja();
        this.prezime = autor.getPrezime();
    }

    public AutorDto() {
        this.aktivan = false;
    }

    public AutorDto(boolean aktivan, String emailAdresa, String korisnickoIme, String ime, String prezime, Date datumRodjenja, String opis, String profilnaSlika) {
        this.aktivan = aktivan;
        this.ime = ime;
        this.prezime = prezime;
        this.emailAdresa = emailAdresa;
        this.korisnickoIme = korisnickoIme;
        this.datumRodjenja = datumRodjenja;
        this.opis = opis;
        this.profilnaSlika = profilnaSlika;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isAktivan() {
        return aktivan;
    }

    public void setAktivan(boolean aktivan) {
        this.aktivan = aktivan;
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

    public String getLozinka() {
        return lozinka;
    }

    public void setLozinka(String lozinka) {
        this.lozinka = lozinka;
    }
}
