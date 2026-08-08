package dominio.puerto.repositorio;
import dominio.modelo.Estudiante;
import java.util.Optional;
/**
 *
 * @author inici4rsesi0n
 */
public interface RepositorioEstudiantes extends Repositorio<Estudiante> {
    Optional<Estudiante> buscarPorDni(String dni);
    Optional<Estudiante> buscarPorCodigo(String codigo);
}