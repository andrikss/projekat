package ac.rs.ftn.webProjekat.Dto;

import ac.rs.ftn.webProjekat.Entity.Zanr;

public class ZanrDto {

    private Long id;
    private String naziv;

    public ZanrDto() {}

    public ZanrDto(Zanr zanr) {
        this.id = zanr.getId();
        this.naziv = zanr.getNaziv();
    }

    public ZanrDto(String naziv) {
        this.naziv = naziv;
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
}
