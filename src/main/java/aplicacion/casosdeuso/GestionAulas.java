package aplicacion.casosdeuso;

import dominio.modelo.Aula;
import dominio.modelo.ModalidadAula;
import dominio.puerto.externo.LoggerPort;
import dominio.puerto.repositorio.RepositorioAulas;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * @author inici4rsesi0n
 */
@Service
public class GestionAulas {

    private final RepositorioAulas repo;
    private final LoggerPort logger;

    public GestionAulas(RepositorioAulas repo, LoggerPort logger) {
        this.repo = repo;
        this.logger = logger;
    }

    public void crearAula(String nombre, int capacidad, String ubicacion, String tipo, ModalidadAula modalidad) {
        if (nombre == null || nombre.isBlank()) throw new IllegalArgumentException("El nombre del aula no puede estar vacío.");
        if (capacidad <= 0) throw new IllegalArgumentException("La capacidad debe ser mayor a 0.");
        if (ubicacion == null || ubicacion.isBlank()) throw new IllegalArgumentException("La ubicación no puede estar vacía.");
        if (tipo == null || tipo.isBlank()) throw new IllegalArgumentException("El tipo de aula no puede estar vacío.");
        if (repo.buscarPorNombre(nombre).isPresent()) throw new IllegalArgumentException("Ya existe un aula con ese nombre.");
        repo.agregar(new Aula(nombre, capacidad, ubicacion, tipo, modalidad));
        logger.audit(String.format(
                "op=CREATE entity=AULA id=%s | capacidad=%d modalidad=%s",
                nombre, capacidad, modalidad));
    }

    public List<Aula> listarTodos() { return repo.listarTodos(); }

    public Aula buscarAula(String nombre) { return repo.buscarPorNombre(nombre).orElse(null); }

    public void actualizarAula(Aula original, String nuevoNombre, int nuevaCapacidad, String nuevaUbicacion, String nuevoTipo, ModalidadAula nuevaModalidad) {
        if (nuevoNombre == null || nuevoNombre.isBlank()) throw new IllegalArgumentException("El nuevo nombre no puede estar vacío.");
        if (nuevaCapacidad <= 0) throw new IllegalArgumentException("La nueva capacidad debe ser mayor a 0.");
        if (nuevaUbicacion == null || nuevaUbicacion.isBlank()) throw new IllegalArgumentException("La nueva ubicación no puede estar vacía.");
        if (nuevoTipo == null || nuevoTipo.isBlank()) throw new IllegalArgumentException("El nuevo tipo no puede estar vacío.");

        String antes = original.toString();
        original.setNombre(nuevoNombre);
        original.setCapacidad(nuevaCapacidad);
        original.setUbicacion(nuevaUbicacion);
        original.setTipo(nuevoTipo);
        original.setModalidad(nuevaModalidad);
        repo.actualizar(original, original);

        logger.audit(String.format(
                "op=UPDATE entity=AULA id=%s | before=%s after=%s",
                nuevoNombre, antes, original));
    }

    public void eliminarAula(Aula aula) {
        if (aula == null) throw new IllegalArgumentException("El aula no puede ser nula");
        String snapshot = aula.toString();
        repo.eliminar(aula);
        logger.audit(String.format(
                "op=DELETE entity=AULA id=%s | snapshot=%s",
                aula.getNombre(), snapshot));
    }
}
