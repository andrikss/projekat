package ac.rs.ftn.webProjekat.Repository;

import ac.rs.ftn.webProjekat.Entity.Knjiga;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KnjigaRepository extends JpaRepository<Knjiga, Long> {
}
