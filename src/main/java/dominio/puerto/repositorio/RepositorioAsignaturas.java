package dominio.puerto.repositorio;
import dominio.modelo.Asignatura;
import java.util.Optional;
/**
 *
 * @author inici4rsesi0n
 */
public interface RepositorioAsignaturas extends Repositorio<Asignatura> {
    Optional<Asignatura> buscarPorNombre(String nombre);
}