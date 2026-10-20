package infraestructura.persistencia.jpa;
import infraestructura.persistencia.entidades.AsignaturaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
/**
 *
 * @author inici4rsesi0n
 */
@Repository
public interface AsignaturaJpaRepository extends JpaRepository<AsignaturaEntity, Long>{
    Optional<AsignaturaEntity> findByNombre(String nombre);
}
