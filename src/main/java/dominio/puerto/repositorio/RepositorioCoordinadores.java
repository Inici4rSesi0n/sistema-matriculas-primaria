package dominio.puerto.repositorio;
import dominio.modelo.CoordinadorAcademico;
import java.util.Optional;
/**
 *
 * @author inici4rsesi0n
 */
public interface RepositorioCoordinadores extends Repositorio<CoordinadorAcademico> {
    Optional<CoordinadorAcademico> buscarPorCodigo(String codigo);
}