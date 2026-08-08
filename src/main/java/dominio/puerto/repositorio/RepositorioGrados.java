package dominio.puerto.repositorio;
import dominio.modelo.Grado;
import java.util.Optional;
/**
 *
 * @author inici4rsesi0n
 */
public interface RepositorioGrados extends Repositorio<Grado> {
    Optional<Grado> buscarPorNombre(String nombre);
}