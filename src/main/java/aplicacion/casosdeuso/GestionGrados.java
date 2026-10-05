package aplicacion.casosdeuso;

import dominio.modelo.Grado;
import dominio.puerto.externo.LoggerPort;
import dominio.puerto.repositorio.RepositorioGrados;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * @author inici4rsesi0n
 */
@Service
public class GestionGrados {

    private final RepositorioGrados repo;
    private final LoggerPort logger;

    public GestionGrados(RepositorioGrados repo, LoggerPort logger) {
        this.repo = repo;
        this.logger = logger;
    }

    public void crearGrado(String nombre, String nivel) {
        if (nombre == null || nombre.isBlank()) throw new IllegalArgumentException("El nombre del grado no puede estar vacío.");
        if (nivel == null || nivel.isBlank()) throw new IllegalArgumentException("El nivel del grado no puede estar vacío.");
        if (repo.buscarPorNombre(nombre).isPresent()) throw new IllegalArgumentException("Ya existe un grado con ese nombre.");
        repo.agregar(new Grado(nombre, nivel));
        logger.audit(String.format(
                "op=CREATE entity=GRADO id=%s | nivel=%s", nombre, nivel));
    }

    public Grado buscarPorNombre(String nombre) { return repo.buscarPorNombre(nombre).orElse(null); }

    public List<Grado> listarTodos() { return repo.listarTodos(); }

    public void actualizarGrado(Grado original, String nuevoNombre, String nuevoNivel) {
        if (nuevoNombre == null || nuevoNombre.isBlank()) throw new IllegalArgumentException("El nombre del grado no puede estar vacío.");
        if (nuevoNivel == null || nuevoNivel.isBlank()) throw new IllegalArgumentException("El nivel del grado no puede estar vacío.");

        String antes = original.toString();
        original.setNombre(nuevoNombre);
        original.setNivel(nuevoNivel);
        repo.actualizar(original, original);

        logger.audit(String.format(
                "op=UPDATE entity=GRADO id=%s | before=%s after=%s",
                nuevoNombre, antes, original));
    }

    public void eliminarGrado(Grado grado) {
        if (grado == null) throw new IllegalArgumentException("El grado no puede ser nulo");
        String snapshot = grado.toString();
        repo.eliminar(grado);
        logger.audit(String.format(
                "op=DELETE entity=GRADO id=%s | snapshot=%s",
                grado.getNombre(), snapshot));
    }
}
