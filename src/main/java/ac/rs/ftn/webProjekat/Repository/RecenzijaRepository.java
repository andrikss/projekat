package ac.rs.ftn.webProjekat.Repository;

import ac.rs.ftn.webProjekat.Entity.Recenzija;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecenzijaRepository extends JpaRepository<Recenzija, Long> {
}
