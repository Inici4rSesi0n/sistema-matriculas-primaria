package dominio.puerto.repositorio;
import dominio.modelo.Turno;
import java.util.Optional;
/**
 *
 * @author inici4rsesi0n
 */
public interface RepositorioTurnos extends Repositorio<Turno> {
    Optional<Turno> buscarPorNombre(String nombre);
}