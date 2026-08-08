package dominio.puerto.repositorio;
import dominio.modelo.Administrador;
import java.util.Optional;

/**
 *
 * @author inici4rsesi0n
 */
public interface RepositorioAdministradores extends Repositorio<Administrador> {
    Optional<Administrador> buscarPorCodigo(String codigo);
}