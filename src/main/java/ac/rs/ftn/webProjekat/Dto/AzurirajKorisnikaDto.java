package ac.rs.ftn.webProjekat.Dto;

import java.util.Date;

public class AzurirajKorisnikaDto {

    private String ime;
    private String prezime;

    private String korisnickoIme;
    private String staraLozinka;
    private String novaLozinka;

    private String lozinka;
    private String staraEmailAdresa;
    private String novaEmailAdresa;
    private Date datumRodjenja;
    private String opis;
    private String profilnaSlika;

    public AzurirajKorisnikaDto() {
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

    public String getStaraLozinka() {
        return staraLozinka;
    }

    public void setStaraLozinka(String staraLozinka) {
        this.staraLozinka = staraLozinka;
    }

    public String getNovaLozinka() {
        return novaLozinka;
    }

    public void setNovaLozinka(String novaLozinka) {
        this.novaLozinka = novaLozinka;
    }

    public String getStaraEmailAdresa() {
        return staraEmailAdresa;
    }

    public void setStaraEmailAdresa(String staraEmailAdresa) {
        this.staraEmailAdresa = staraEmailAdresa;
    }

    public String getNovaEmailAdresa() {
        return novaEmailAdresa;
    }

    public void setNovaEmailAdresa(String novaEmailAdresa) {
        this.novaEmailAdresa = novaEmailAdresa;
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

    public String getKorisnickoIme() {
        return korisnickoIme;
    }

    public void setKorisnickoIme(String korisnickoIme) {
        this.korisnickoIme = korisnickoIme;
    }

    public String getLozinka() {
        return lozinka;
    }

    public void setLozinka(String lozinka) {
        this.lozinka = lozinka;
    }
}
