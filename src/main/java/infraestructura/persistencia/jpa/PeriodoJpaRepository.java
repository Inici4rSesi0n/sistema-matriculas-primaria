package infraestructura.persistencia.jpa;
import infraestructura.persistencia.entidades.PeriodoAcademicoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
/**
 *
 * @author inici4rsesi0n
 */
@Repository
public interface PeriodoJpaRepository extends JpaRepository<PeriodoAcademicoEntity, Long>{
    Optional<PeriodoAcademicoEntity> findByNombre(String nombre);
}
