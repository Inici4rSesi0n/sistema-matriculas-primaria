package aplicacion.casosdeuso;

import dominio.modelo.Turno;
import dominio.puerto.repositorio.RepositorioTurnos;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * @author inici4rsesi0n
 */
@Service
public class GestionTurnos {

    private final RepositorioTurnos repo;

    public GestionTurnos(RepositorioTurnos repo) {
        this.repo = repo;
    }

    public void crearTurno(String nombre) {
        if (nombre == null || nombre.isBlank()) throw new IllegalArgumentException("El nombre del turno no puede estar vacío.");
        if (repo.buscarPorNombre(nombre).isPresent()) throw new IllegalArgumentException("Ya existe un turno con ese nombre.");
        repo.agregar(new Turno(nombre));
    }

    public Turno buscarPorNombre(String nombre) { return repo.buscarPorNombre(nombre).orElse(null); }
    public List<Turno> listarTodos() { return repo.listarTodos(); }

    public void actualizarTurno(Turno original, String nuevoNombre) {
        if (nuevoNombre == null || nuevoNombre.isBlank()) throw new IllegalArgumentException("El nombre del turno no puede estar vacío.");
        Turno actualizado = new Turno(nuevoNombre);
        repo.actualizar(original, actualizado);
    }

    public void eliminarTurno(Turno turno) { repo.eliminar(turno); }
}