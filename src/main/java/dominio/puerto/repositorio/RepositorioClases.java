package dominio.puerto.repositorio;
import dominio.modelo.Clase;
import java.util.List;
import java.util.Optional;
/**
 *
 * @author inici4rsesi0n
 */
public interface RepositorioClases extends Repositorio<Clase> {
    List<Clase> buscarPorGrupoYGrado(String nombreGrupo, String nombreGrado);
    boolean existeDocenteEnHorario(String codigoDocente, String dia, String horaInicio, String horaFin);
    Optional<Clase> buscarClase(String nombreGrado, String nombreGrupo, String dia, String horaInicio, String horaFin);
}