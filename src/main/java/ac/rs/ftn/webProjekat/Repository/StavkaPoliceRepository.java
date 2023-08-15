package ac.rs.ftn.webProjekat.Repository;

import ac.rs.ftn.webProjekat.Entity.StavkaPolice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StavkaPoliceRepository extends JpaRepository<StavkaPolice, Long> {
}
