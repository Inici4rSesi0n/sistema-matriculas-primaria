package dominio.puerto.repositorio;
import dominio.modelo.Recreo;
import java.util.Optional;
/**
 *
 * @author inici4rsesi0n
 */
public interface RepositorioRecreos extends Repositorio<Recreo> {
    Optional<Recreo> buscarPorDescripcion(String descripcion);
}