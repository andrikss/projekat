package ac.rs.ftn.webProjekat.Entity;

import ac.rs.ftn.webProjekat.Dto.AutorDto;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@DiscriminatorValue("Autor")
public class Autor extends Korisnik implements Serializable {

    @Column(name = "aktivan")
    private boolean aktivan;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "autor_knjiga",
            joinColumns = @JoinColumn(name = "autor_id",referencedColumnName = "id"),   // povezuje autora
            inverseJoinColumns = @JoinColumn(name = "knjiga_id", referencedColumnName = "id")   // povezuje knjigu
    )
    private Set<Knjiga> autoroveKnjige = new HashSet<>();

    // da li sadrzi knjigu
    public boolean daLiSadrziKnjigu(Knjiga knjiga) {
        for (Knjiga it : autoroveKnjige) {
            if (it.getId() == knjiga.getId()) {
                return true;
            }
        }
        return false;
    }

    //da li sadrzi knjigu sa odredjenim ISBN- om
    public boolean containsKnjigaWithISBN(String ISBN) {
        for (Knjiga it : autoroveKnjige) {
            if (it.getISBN() == ISBN) {
                return true;
            }
        }
        return false;
    }

    // izbrisi knjigu
    public void removeKnjiga(Knjiga knjiga) {
        Set<Knjiga> newAutoroveKnjige = new HashSet<>();
        for (Knjiga it : autoroveKnjige) {
            if (!it.getISBN().equals(knjiga.getISBN())) {
                newAutoroveKnjige.add(it);
            }
        } //if treba zakomentarisati??
        if (newAutoroveKnjige.size() != autoroveKnjige.size())
        autoroveKnjige = newAutoroveKnjige;
    }

    //apdejt
    public void updateAutor(AutorDto autorDto) {
        this.setEmailAdresa(autorDto.getEmailAdresa());
        this.setKorisnickoIme(autorDto.getKorisnickoIme());
        this.setIme(autorDto.getIme());
        this.setDatumRodjenja(autorDto.getDatumRodjenja());
        this.setPrezime(autorDto.getPrezime());
        this.setOpis(autorDto.getOpis());
        this.setProfilnaSlika(autorDto.getProfilnaSlika());
    }

    public Autor() {
        super();
        this.aktivan = false;
        this.setUlogaKorisnika(UlogaKorisnika.AUTOR.toString());
    }

    public Autor(String emailAdresa, String ime) {
        super(emailAdresa, ime);
        this.aktivan = false;
        this.setUlogaKorisnika(UlogaKorisnika.AUTOR.toString());
    }


    //is this reaaaally neccessery omg ne znam ce vidimo
   /* public Autor(SignUpAutorDto signUpAutorDto) {
        super(  UlogaKorisnika.AUTOR.toString(),
                signUpAutorDto.getEmailAdresa(),
                signUpAutorDto.getKorisnickoIme(),
                signUpAutorDto.getIme(),
                signUpAutorDto.getPrezime(),
                signUpAutorDto.getLozinka(),
                signUpAutorDto.getDatumRodjenja(),
                signUpAutorDto.getOpis()
        );
        this.aktivan = false;
    }*/

    public boolean isAktivan() {
        return aktivan;
    }

    public void setAktivan(boolean aktivan) {
        this.aktivan = aktivan;
    }

    public Set<Knjiga> getAutoroveKnjige() {
        return autoroveKnjige;
    }

    public void setAutoroveKnjige(Set<Knjiga> autoroveKnjige) {
        this.autoroveKnjige = autoroveKnjige;
    }

    @Override
    public String toString() {
        return "Autor{" +
                super.toString() + '\'' +
                "aktivan=" + aktivan +
                ", autoroveKnjige=" + autoroveKnjige +
                '}';
    }
}
