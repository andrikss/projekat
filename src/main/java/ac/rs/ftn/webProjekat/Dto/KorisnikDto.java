package ac.rs.ftn.webProjekat.Dto;

import ac.rs.ftn.webProjekat.Entity.Korisnik;
import ac.rs.ftn.webProjekat.Entity.Polica;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class KorisnikDto {

    private Long id;

    private String ime;
    private String prezime;

    private String lozinka;

    private String emailAdresa;
    private String korisnickoIme;

    private Date datumRodjenja;
    private String opis;
    private String profilnaSlika;

    private String ulogaKorisnika;

    private List<PolicaDto> policeDto = new ArrayList<>();

    //valjda ovako
    public void setPoliceDto(List<Polica> police) {
        if (police == null) {
            return;
        }
        for (Polica p : police) {
            policeDto.add(new PolicaDto(p));
        }
    }

    public KorisnikDto() {
    }

    public KorisnikDto(Korisnik korisnik) {
        this.id = korisnik.getId();
        this.ime = korisnik.getIme();
        this.prezime = korisnik.getPrezime();
        this.emailAdresa = korisnik.getEmailAdresa();
        this.datumRodjenja = korisnik.getDatumRodjenja();
        this.korisnickoIme  = korisnik.getKorisnickoIme();
        this.opis = korisnik.getOpis();
        this.profilnaSlika = korisnik.getProfilnaSlika();
        this.ulogaKorisnika = korisnik.getUlogaKorisnika();
        setPoliceDto(korisnik.getPolice());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getUlogaKorisnika() {
        return ulogaKorisnika;
    }

    public void setUlogaKorisnika(String ulogaKorisnika) {
        this.ulogaKorisnika = ulogaKorisnika;
    }

    public List<PolicaDto> getPoliceDto() {
        return policeDto;
    }


}
