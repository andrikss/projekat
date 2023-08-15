package ac.rs.ftn.webProjekat.Repository;

import ac.rs.ftn.webProjekat.Entity.ZahtjevZaAktivacijuNalogaAutora;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ZahtjevRepository extends JpaRepository<ZahtjevZaAktivacijuNalogaAutora, Long> {
}
