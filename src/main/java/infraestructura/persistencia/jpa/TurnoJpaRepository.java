package infraestructura.persistencia.jpa;
import infraestructura.persistencia.entidades.TurnoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
/**
 *
 * @author inici4rsesi0n
 */
@Repository
public interface TurnoJpaRepository extends JpaRepository<TurnoEntity, Long>{
    Optional<TurnoEntity> findByNombre(String nombre);
}
