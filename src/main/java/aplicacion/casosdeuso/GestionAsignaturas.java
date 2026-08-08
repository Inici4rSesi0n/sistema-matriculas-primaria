package aplicacion.casosdeuso;
import dominio.modelo.Asignatura;
import dominio.puerto.repositorio.RepositorioAsignaturas;
import java.util.List;
/**
 *
 * @author inici4rsesi0n
 */
public class GestionAsignaturas {

    private final RepositorioAsignaturas repo;

    public GestionAsignaturas(RepositorioAsignaturas repo) {
        this.repo = repo;
    }

    public void crearAsignatura(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre de la asignatura no puede estar vacío.");
        }
        if (repo.buscarPorNombre(nombre).isPresent()) {
            throw new IllegalArgumentException("Ya existe una asignatura con ese nombre.");
        }
        repo.agregar(new Asignatura(nombre));
    }

    public List<Asignatura> listarTodos() {
        return repo.listarTodos();
    }

    public Asignatura buscarAsignatura(String nombre) {
        return repo.buscarPorNombre(nombre).orElse(null);
    }

    public void actualizarAsignatura(Asignatura original, String nuevoNombre) {
        if (nuevoNombre == null || nuevoNombre.isBlank()) {
            throw new IllegalArgumentException("El nuevo nombre no puede estar vacío.");
        }
        Asignatura actualizada = new Asignatura(nuevoNombre);
        repo.actualizar(original, actualizada);
    }

    public void eliminarAsignatura(Asignatura asignatura) {
        repo.eliminar(asignatura);
    }
}