package ac.rs.ftn.webProjekat.Dto;

import ac.rs.ftn.webProjekat.Entity.Knjiga;
import ac.rs.ftn.webProjekat.Entity.Polica;
import ac.rs.ftn.webProjekat.Entity.TipPolice;

import java.util.ArrayList;
import java.util.List;

public class PolicaDto {

    private Long id;
    private String naziv;

    private TipPolice tip;

    private List<KnjigaDto> knjige;

    public PolicaDto() {
    }

    public boolean isValid() {
        if (naziv != null && !naziv.isEmpty()) {
            return true;
        }
        return false;
    }
    public PolicaDto(Polica polica) {
        this.id = polica.getId();
        this.naziv = polica.getNaziv();
        this.tip = polica.getTip();
        this.knjige = new ArrayList<>();
        if (polica.getKnjige() != null) {
            for (Knjiga knjiga : polica.getKnjige()) {
                this.knjige.add(new KnjigaDto(knjiga));
            }
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public TipPolice getTip() {
        return tip;
    }

    public void setTip(TipPolice tip) {
        this.tip = tip;
    }

    public List<KnjigaDto> getKnjige() {
        return knjige;
    }

    public void setKnjige(List<KnjigaDto> knjige) {
        this.knjige = knjige;
    }
}
