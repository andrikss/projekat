package ac.rs.ftn.webProjekat.Entity;

public enum UlogaKorisnika {

    CITALAC("Citalac"),
    AUTOR("Autor"),
    ADMINISTRATOR("Administrator");

    // da bih lakse koristila

    private final String text;

    UlogaKorisnika(final String text) {
        this.text = text;
    }
    @Override
    public String toString() {
        return text;
    }


}
