package dominio.puerto.repositorio;
import dominio.modelo.Director;
import java.util.Optional;
/**
 *
 * @author inici4rsesi0n
 */
public interface RepositorioDirectores extends Repositorio<Director> {
    Optional<Director> buscarPorCodigo(String codigo);
}