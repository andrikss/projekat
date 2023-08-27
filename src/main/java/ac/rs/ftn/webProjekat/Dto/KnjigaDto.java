package ac.rs.ftn.webProjekat.Dto;

import ac.rs.ftn.webProjekat.Entity.Knjiga;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class KnjigaDto {

    private Long id;

    private String ISBN;

    private String naslov;

    private Date datumObjavljivanja;

    private Long brojStrana;

    private String opis;

    private Long ocjena;

    private Set<ZanrDto> zanrovi;

    private Set<RecenzijaDto> recenzije;

    private String emailAdresaAutora;

    private String naslovnaFotografija;

    public KnjigaDto() {
    }

    public KnjigaDto(Knjiga knjiga) {
        this.id = knjiga.getId();
        this.brojStrana = knjiga.getBrojStrana();
        this.ISBN = knjiga.getISBN();
        this.naslov = knjiga.getNaslov();
        this.opis = knjiga.getOpis();
        this.ocjena = knjiga.getOcjena();
        this.datumObjavljivanja = knjiga.getDatumObjavljivanja();
        this.recenzije = new HashSet<>();
        this.zanrovi = new HashSet<>();
        this.naslovnaFotografija = knjiga.getNaslovnaFotografija();
        this.emailAdresaAutora = knjiga.getEmailAdresaAutora();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public void setOcena(Long ocena) {
        this.ocjena = ocena;
    }

    public Set<ZanrDto> getZanrovi() {
        return zanrovi;
    }

    public void setZanrovi(Set<ZanrDto> zanrovi) {
        this.zanrovi = zanrovi;
    }

    public Set<RecenzijaDto> getRecenzije() {
        return recenzije;
    }

    public void setRecenzije(Set<RecenzijaDto> recenzije) {
        this.recenzije = recenzije;
    }

    public String getEmailAdresaAutora() {
        return emailAdresaAutora;
    }

    public void setEmailAdresaAutora(String emailAdresaAutora) {
        this.emailAdresaAutora = emailAdresaAutora;
    }

    public String getNaslovnaFotografija() {
        return naslovnaFotografija;
    }

    public void setNaslovnaFotografija(String naslovnaFotografija) {
        this.naslovnaFotografija = naslovnaFotografija;
    }

    @Override
    public String toString() {
        return "KnjigaDto{" +
                "id=" + id +
                ", ISBN='" + ISBN + '\'' +
                ", naslov='" + naslov + '\'' +
                ", datumObjavljivanja=" + datumObjavljivanja +
                ", brojStrana=" + brojStrana +
                ", opis='" + opis + '\'' +
                ", ocjena=" + ocjena +
                ", zanrovi=" + zanrovi +
                ", recenzije=" + recenzije +
                ", emailAdresaAutora='" + emailAdresaAutora + '\'' +
               // ", naslovnaFotografija='" + naslovnaFotografija + '\'' +
                '}';
    }
}
