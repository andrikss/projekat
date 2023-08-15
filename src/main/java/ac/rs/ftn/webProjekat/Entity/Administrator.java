package ac.rs.ftn.webProjekat.Entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.io.Serializable;

@Entity
@DiscriminatorValue("Administrator")
public class Administrator extends Korisnik implements Serializable {
    @Override
    public String toString() {
        return "Administrator{}" +
                super.toString() + '\'' +
                "}";
    }
}

