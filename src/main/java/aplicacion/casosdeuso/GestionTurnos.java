package aplicacion.casosdeuso;

import dominio.modelo.Turno;
import dominio.puerto.externo.LoggerPort;
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
    private final LoggerPort logger;

    public GestionTurnos(RepositorioTurnos repo, LoggerPort logger) {
        this.repo = repo;
        this.logger = logger;
    }

    public void crearTurno(String nombre) {
        if (nombre == null || nombre.isBlank()) throw new IllegalArgumentException("El nombre del turno no puede estar vacío.");
        if (repo.buscarPorNombre(nombre).isPresent()) throw new IllegalArgumentException("Ya existe un turno con ese nombre.");
        repo.agregar(new Turno(nombre));
        logger.audit(String.format("op=CREATE entity=TURNO id=%s", nombre));
    }

    public Turno buscarPorNombre(String nombre) { return repo.buscarPorNombre(nombre).orElse(null); }

    public List<Turno> listarTodos() { return repo.listarTodos(); }

    public void actualizarTurno(Turno original, String nuevoNombre) {
        if (nuevoNombre == null || nuevoNombre.isBlank()) throw new IllegalArgumentException("El nombre del turno no puede estar vacío.");

        String antes = original.getNombre();
        original.setNombre(nuevoNombre);
        repo.actualizar(original, original);

        logger.audit(String.format(
                "op=UPDATE entity=TURNO id=%s | before=%s after=%s",
                nuevoNombre, antes, nuevoNombre));
    }

    public void eliminarTurno(Turno turno) {
        if (turno == null) throw new IllegalArgumentException("El turno no puede ser nulo");
        String nombre = turno.getNombre();
        repo.eliminar(turno);
        logger.audit(String.format("op=DELETE entity=TURNO id=%s", nombre));
    }
}
