package infraestructura.persistencia.jpa;
import infraestructura.persistencia.entidades.GradoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
/**
 *
 * @author inici4rsesi0n
 */
@Repository
public interface GradoJpaRepository extends JpaRepository<GradoEntity, Long>{
    Optional<GradoEntity> findByNombre(String nombre);
    Optional<GradoEntity> findByNombreAndNivel(String nombre, String nivel);
}
