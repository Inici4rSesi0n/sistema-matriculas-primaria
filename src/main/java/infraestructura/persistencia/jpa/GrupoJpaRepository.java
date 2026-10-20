package infraestructura.persistencia.jpa;
import infraestructura.persistencia.entidades.GrupoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
/**
 *
 * @author inici4rsesi0n
 */
@Repository
public interface GrupoJpaRepository extends JpaRepository<GrupoEntity, Long>{
    Optional<GrupoEntity> findByNombre(String nombre);
    List<GrupoEntity> findByNombreAndGradoNombre(String nombre, String gradoNombre);
}
