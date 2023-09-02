package ac.rs.ftn.webProjekat.Repository;

import ac.rs.ftn.webProjekat.Entity.Knjiga;
import ac.rs.ftn.webProjekat.Entity.StavkaPolice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StavkaPoliceRepository extends JpaRepository<StavkaPolice, Long> {
    List<StavkaPolice> findByKnjiga(Knjiga knjiga);
}
