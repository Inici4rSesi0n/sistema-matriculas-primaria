package dominio.puerto.repositorio;
import dominio.modelo.Padre;
import java.util.Optional;
/**
 *
 * @author inici4rsesi0n
 */
public interface RepositorioPadres extends Repositorio<Padre> {
    Optional<Padre> buscarPorCodigo(String codigo);
}