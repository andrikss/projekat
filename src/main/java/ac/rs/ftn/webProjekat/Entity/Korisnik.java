package ac.rs.ftn.webProjekat.Entity;

import ac.rs.ftn.webProjekat.Dto.AzurirajKorisnikaDto;
import ac.rs.ftn.webProjekat.Dto.RegistrujSeDto;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "korisnik")
@DiscriminatorValue("Citalac")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "uloga_korisnika", discriminatorType = DiscriminatorType.STRING)
public class Korisnik implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ime")
    private String ime;
    @Column(name = "prezime")
    private String prezime;

    @Column(name = "korisnicko_ime", unique = true, nullable = false)
    private String korisnickoIme;

    @Column(name = "email_adresa", unique = true, nullable = false)
    private String emailAdresa;

    @Column(name = "lozinka")
    private String lozinka;
    @Column(name = "datum_rodjenja")
    private Date datumRodjenja;

    @Column(name = "profilna_slika")
    @Temporal(TemporalType.DATE)
    private String profilnaSlika;

    @Column(name = "opis")
    private String opis;

    @Column(name = "uloga_korisnika", insertable = false, updatable = false)
    private String ulogaKorisnika;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Polica> police = new HashSet<>();

    //pravljenje primarnih polica
    public void napraviPrimarnePolice() {
        police.add(new Polica("WantToRead",TipPolice.WANT_TO_READ));
        police.add(new Polica("CurrentlyReading",TipPolice.CURRENTLY_READING));
        police.add(new Polica("Read",TipPolice.READ));
    }

    // dobavi policu po nazivu
    public Polica getPolicaByNaziv(String naziv) {
        for (Polica it : police) {
            if (it.getNaziv().equals(naziv)) {
                return it;
            }
        }
        return null;
    }

    //treba da se implementira DA LI JE KNJIGA NA PRIMARNOJ POLICI
    public boolean daLiJeKnjigaNaPrimarnoj(Knjiga knjiga) {
        for (Polica p : this.police) {
            if (p.getTip() == TipPolice.WANT_TO_READ || p.getTip() == TipPolice.CURRENTLY_READING || p.getTip() == TipPolice.READ) {
                // Ako je tip Police primaran, pravimo listu i tražimo da li knjiga postoji u njoj
                Set<StavkaPolice> stavkePolice = p.getStavkaPolice();
                for (StavkaPolice s : stavkePolice) {
                    if (s.getKnjiga().getISBN().equals(knjiga.getISBN())) {
                        return true; // Knjiga je pronađena na polici, vraćamo true
                    }
                }
            }
        }
        return false; // Knjiga nije pronađena na nijednoj primarnoj polici, vraćamo false
    }

    //dobavljanje PRIMARNE police po id-u knjige
    public Polica getPrimarnaPolicaByKnjigaId(Long knjigaId) {
        for (Polica p : this.police) {
            if (p.getTip() == TipPolice.WANT_TO_READ || p.getTip() == TipPolice.CURRENTLY_READING || p.getTip() == TipPolice.READ) {
                for (StavkaPolice st : p.getStavkaPolice()) {
                    if (st.getKnjiga().getId() == knjigaId) {
                        return p;
                    }
                }
            }
        }
        return null;
    }

    //da li sadrzi policu
    public boolean daLiSadrziPolicu(Polica polica) {
        for (Polica it : police) {
            if (it.getId() == polica.getId()) {
                return true;
            }
        }
        return false;
    }

    //da li sadrzi policu sa istim nazivom
    public boolean daLiJeIstiNazivPolice(String policaName) {
        for (Polica it : police) {
            if (it.getNaziv().equals(policaName)) {
                return true;
            }
        }
        return false;
    }

    public void izbrisiPolicu(Polica polica) {
        //ista logika kao i svuda
        Set<Polica> newPolice = new HashSet<>();
        for (Polica it : police) {
            if (it.getId() != polica.getId()) {
                newPolice.add(it);
            }
        }
        if (newPolice.size() != police.size())
            police = newPolice;
    }

    //dobavljanje police po id-u knjige

    public Polica getPolicaByKnjigaId(Long knjigaId) {
        for (Polica p : this.police) {
                for (StavkaPolice st : p.getStavkaPolice()) {
                    if (st.getKnjiga().getId() == knjigaId) {
                        return p;
                    }
                }
        }
        return null;
    }

    public boolean daLiJeDuplikat(Korisnik korisnik) {
        if (this.emailAdresa.equals(korisnik.emailAdresa) || this.korisnickoIme.equals(korisnik.korisnickoIme))
        {
            return true;
        }
        return false;
    }

    // apdejt korisnika, mozda bi trebalo dodati if
    public void updateKorisnik( String ime, String prezime, String opis, Date datumRodjenja, String profilnaSlika) {
        this.ime = ime;
        this.prezime = prezime;
        this.opis = opis;
        this.profilnaSlika = profilnaSlika;
        this.datumRodjenja = datumRodjenja;
    }

    // isto samo preko dto
    public void updateKorisnik(AzurirajKorisnikaDto updateKorisnikProfileDto) {
        this.ime = updateKorisnikProfileDto.getIme();
        this.opis = updateKorisnikProfileDto.getOpis();
        this.prezime = updateKorisnikProfileDto.getPrezime();
        this.profilnaSlika = updateKorisnikProfileDto.getProfilnaSlika();
        this.datumRodjenja = updateKorisnikProfileDto.getDatumRodjenja();
    }

    // RAZLICITE VRSTE KONSTRUKTORA:

    public Korisnik(String ulogaKorisnika, String emailAdresa, String korisnickoIme, String ime, String prezime, String lozinka, Date datumRodjenja, String opis) {
        this.ulogaKorisnika = ulogaKorisnika;
        this.emailAdresa = emailAdresa;
        this.korisnickoIme = korisnickoIme;
        this.ime = ime;
        this.prezime = prezime;
        this.lozinka = lozinka;
        this.datumRodjenja = datumRodjenja;
        this.opis = opis;
        napraviPrimarnePolice();
    }

    public Korisnik() {
        this.ulogaKorisnika = UlogaKorisnika.CITALAC.toString();
        napraviPrimarnePolice();
    }
    //ovo jedino ne smije biti null
    public Korisnik(String emailAdresa, String ime, UlogaKorisnika ulogaKorisnika) {
        this.emailAdresa = emailAdresa;
        this.ime = ime;
        this.ulogaKorisnika = ulogaKorisnika.toString();
        napraviPrimarnePolice();
    }

    public Korisnik(String emailAdresa, String ime) {
        this.emailAdresa = emailAdresa;
        this.ime = ime;
        this.ulogaKorisnika = UlogaKorisnika.CITALAC.toString();
        napraviPrimarnePolice();
    }
    public Korisnik(RegistrujSeDto reg) {
        this.ulogaKorisnika = UlogaKorisnika.CITALAC.toString();
        this.emailAdresa = reg.getEmailAdresa();
        this.ime = reg.getIme();
        this.korisnickoIme = reg.getIme();
        this.prezime = reg.getPrezime();
        this.korisnickoIme = reg.getKorisnickoIme();
        this.lozinka = reg.getLozinka();
        this.opis = reg.getOpis();
        this.datumRodjenja = reg.getDatumRodjenja();
        napraviPrimarnePolice();     //uvijek
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

    public String getKorisnickoIme() {
        return korisnickoIme;
    }

    public void setKorisnickoIme(String korisnickoIme) {
        this.korisnickoIme = korisnickoIme;
    }

    public String getEmailAdresa() {
        return emailAdresa;
    }

    public void setEmailAdresa(String emailAdresa) {
        this.emailAdresa = emailAdresa;
    }

    public String getLozinka() {
        return lozinka;
    }

    public void setLozinka(String lozinka) {
        this.lozinka = lozinka;
    }

    public Date getDatumRodjenja() {
        return datumRodjenja;
    }

    public void setDatumRodjenja(Date datumRodjenja) {
        this.datumRodjenja = datumRodjenja;
    }

    public String getProfilnaSlika() {
        return profilnaSlika;
    }

    public void setProfilnaSlika(String profilnaSlika) {
        this.profilnaSlika = profilnaSlika;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public String getUlogaKorisnika() {
        return ulogaKorisnika;
    }

    public void setUlogaKorisnika(String ulogaKorisnika) {
        this.ulogaKorisnika = ulogaKorisnika;
    }

    public Set<Polica> getPolice() {
        return police;
    }

    public void setPolice(Set<Polica> police) {
        this.police = police;
    }

    @Override
    public String toString() {
        return "Korisnik{" +
                "id=" + id +
                ", ime='" + ime + '\'' +
                ", prezime='" + prezime + '\'' +
                ", korisnickoIme='" + korisnickoIme + '\'' +
                ", emailAdresa='" + emailAdresa + '\'' +
                ", lozinka='" + lozinka + '\'' +
                ", datumRodjenja=" + datumRodjenja +
                ", profilnaSlika='" + profilnaSlika + '\'' +
                ", opis='" + opis + '\'' +
                ", ulogaKorisnika='" + ulogaKorisnika + '\'' +
                ", police=" + police +
                '}';
    }


}
