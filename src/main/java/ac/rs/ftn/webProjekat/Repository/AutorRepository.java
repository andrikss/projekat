package ac.rs.ftn.webProjekat.Repository;

import ac.rs.ftn.webProjekat.Entity.Administrator;
import ac.rs.ftn.webProjekat.Entity.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AutorRepository extends JpaRepository<Autor, Long> {
}
