package ac.rs.ftn.webProjekat.Entity;

import ac.rs.ftn.webProjekat.Dto.AzurirajKnjiguDto;
import ac.rs.ftn.webProjekat.Dto.KnjigaDto;
import jakarta.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.io.Serializable;

@Entity
@Table(name = "knjiga")
public class Knjiga implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "naslov")
    private String naslov;

    @Column(name = "naslovna_fotografija")
    private String naslovnaFotografija;

    @Column(unique = true, name="isbn")
    private String ISBN;

    @Column(name = "datum_objavljivanja")
    private Date datumObjavljivanja;
    @Column(name = "broj_strana")
    private Long brojStrana;

    @Column(name = "email_adresa_autora")
    private String emailAdresaAutora;

    @Column(name = "opis")
    private String opis;

    //vise knjiga vise zanrova
    @ManyToMany(fetch = FetchType.EAGER, cascade = {})
    @JoinTable(name = "knjiga_zanr",
            joinColumns = @JoinColumn(name = "knjiga_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "zanr_id", referencedColumnName = "id")
    )
    private Set<Zanr> zanrovi = new HashSet<>();

    @ManyToMany(mappedBy = "autoroveKnjige", fetch = FetchType.EAGER, cascade = {})
    private Set<Autor> autori = new HashSet<>();

    @Column(name = "ocjena")
    private Long ocjena;

    public void updateKnjiga(String naslov, String naslovnaFotografija, String ISBN, Date datumObjavljivanja, Long brojStrana, String opis, Long ocjena, String emailAdresaAutora) {
        this.naslov = naslov;
        this.naslovnaFotografija = naslovnaFotografija;
        this.ISBN = ISBN;
        this.datumObjavljivanja = datumObjavljivanja;
        this.brojStrana = brojStrana;
        this.opis = opis;
        this.ocjena = ocjena;
        this.emailAdresaAutora = emailAdresaAutora;
    }

    //treba pogledati logiku azurirajKnjigaDto cini mi se d a je isto kao KNjigaDTO

     public void updateKnjiga(AzurirajKnjiguDto updateKnjigaDto) {
        this.ISBN = updateKnjigaDto.getISBN();
        this.naslov = updateKnjigaDto.getNaslov();
        this.opis = updateKnjigaDto.getOpis();
        this.brojStrana = updateKnjigaDto.getBrojStrana();
        this.ocjena = updateKnjigaDto.getOcjena();
        this.naslovnaFotografija = updateKnjigaDto.getNaslovnaFotografija();
        this.datumObjavljivanja = updateKnjigaDto.getDatumObjavljivanja();
        this.emailAdresaAutora = updateKnjigaDto.getEmailAdresaAutora();
    }

    public boolean daLiImaZanr(Zanr zanr) {
        for(Zanr z: this.zanrovi) {
            if (z.getId() == zanr.getId()) {
                return true;
            }
        }
        return false;
    }

    // nisam sigurna za brisanje je l tacno
    public void izbrisiZanr(Zanr zanr) {
        Set<Zanr> zanrovi = new HashSet<>();
        for (Zanr z : this.zanrovi) {
            if(z.getId() != zanr.getId()) {
                zanrovi.add(z);
            }
        }
        this.zanrovi = zanrovi;
    }

    public Knjiga() {
    }

    public Knjiga(KnjigaDto knjigaDto) {
        this.naslov = knjigaDto.getNaslov();
        this.ISBN = knjigaDto.getISBN();
        this.datumObjavljivanja = knjigaDto.getDatumObjavljivanja();
        this.brojStrana = knjigaDto.getBrojStrana();
        this.opis = knjigaDto.getOpis();
        this.ocjena = knjigaDto.getOcjena();
        this.emailAdresaAutora = knjigaDto.getEmailAdresaAutora();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNaslov() {
        return naslov;
    }

    public void setNaslov(String naslov) {
        this.naslov = naslov;
    }

    public String getNaslovnaFotografija() {
        return naslovnaFotografija;
    }

    public void setNaslovnaFotografija(String naslovnaFotografija) {
        this.naslovnaFotografija = naslovnaFotografija;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
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

    public Set<Zanr> getZanrovi() {
        return zanrovi;
    }

    public void setZanrovi(Set<Zanr> zanrovi) {
        this.zanrovi = zanrovi;
    }

    public Long getOcjena() {
        return ocjena;
    }

    public void setOcjena(Long ocjena) {
        this.ocjena = ocjena;
    }

    @Override
    public String toString() {
        return "Knjiga{" +
                "id=" + id +
                ", naslov='" + naslov + '\'' +
                ", naslovnaFotografija='" + naslovnaFotografija + '\'' +
                ", ISBN='" + ISBN + '\'' +
                ", datumObjavljivanja=" + datumObjavljivanja +
                ", brojStrana=" + brojStrana +
                ", opis='" + opis + '\'' +
                ", zanrovi=" + zanrovi +
                ", ocjena=" + ocjena +
                '}';
    }

    public String getEmailAdresaAutora() {
        return emailAdresaAutora;
    }

    public void setEmailAdresaAutora(String emailAdresaAutora) {
        this.emailAdresaAutora = emailAdresaAutora;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Knjiga knjiga = (Knjiga) o;
        return Objects.equals(id, knjiga.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
