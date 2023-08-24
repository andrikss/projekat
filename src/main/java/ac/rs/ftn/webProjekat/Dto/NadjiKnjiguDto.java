package ac.rs.ftn.webProjekat.Dto;

import ac.rs.ftn.webProjekat.Entity.Knjiga;

public class NadjiKnjiguDto {

    private String ISBN;

    private String naslov;


    public boolean isISBNValid() {
        if (ISBN == null || ISBN.isEmpty())
            return false;

        return true;
    }

    public boolean isNaslovValid() {
        if (naslov == null || naslov.isEmpty())
            return false;

        return true;
    }


    ///////////////////////////
    //Auto generisane metode://

    public NadjiKnjiguDto() {
    }

    public NadjiKnjiguDto(Knjiga knjiga) {
        this.ISBN = knjiga.getISBN();
        this.naslov = knjiga.getNaslov();
    }

    public String getISBN() {return ISBN;}
    public void setISBN(String ISBN) {this.ISBN = ISBN;}
    public String getNaslov() {return naslov;}
    public void setNaslov(String naslov) {this.naslov = naslov;}
}

