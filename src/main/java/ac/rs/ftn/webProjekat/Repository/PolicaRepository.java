package ac.rs.ftn.webProjekat.Repository;

import ac.rs.ftn.webProjekat.Entity.Polica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PolicaRepository extends JpaRepository<Polica, Long> {
}
