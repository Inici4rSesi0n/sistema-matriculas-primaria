package dominio.puerto.repositorio;
import dominio.modelo.Docente;
import java.util.Optional;
/**
 *
 * @author inici4rsesi0n
 */
public interface RepositorioDocentes extends Repositorio<Docente> {
    Optional<Docente> buscarPorDni(String dni);
    Optional<Docente> buscarPorCodigo(String codigo);
}