package dominio.puerto.repositorio;
import dominio.modelo.Matricula;
import java.util.List;
import java.util.Optional;
/**
 *
 * @author inici4rsesi0n
 */
public interface RepositorioMatriculas extends Repositorio<Matricula> {
    Optional<Matricula> buscarPorEstudianteYPeriodo(String codigoEstudiante, String nombrePeriodo);
    List<Matricula> buscarPorGrupo(String nombreGrupo);
}