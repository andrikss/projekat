package ac.rs.ftn.webProjekat.Entity;

public enum StatusZahtjeva {

    NA_CEKANJU("Na_cekanju"),
    ODOBREN("Odobren"),
    ODBIJEN("Odbijen");

    private final String text;

    StatusZahtjeva(final String text) { this.text = text; }

    public String toString() { return text; }
}
