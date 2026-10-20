package infraestructura.persistencia.jpa;
import infraestructura.persistencia.entidades.RecreoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
/**
 *
 * @author inici4rsesi0n
 */
@Repository
public interface RecreoJpaRepository extends JpaRepository<RecreoEntity, Long>{
    Optional<RecreoEntity> findByDescripcion(String descripcion);
    Optional<RecreoEntity> findByDescripcionAndDiaSemanaAndHoraInicioAndHoraFin(String descripcion, String diaSemana, String horaInicio, String horaFin);
}
