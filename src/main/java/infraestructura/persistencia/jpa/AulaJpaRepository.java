package infraestructura.persistencia.jpa;
import infraestructura.persistencia.entidades.AulaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
/**
 *
 * @author inici4rsesi0n
 */
@Repository
public interface AulaJpaRepository extends JpaRepository<AulaEntity, Long>{
    Optional<AulaEntity> findByNombre(String nombre);
}
