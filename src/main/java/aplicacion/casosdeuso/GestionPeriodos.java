package aplicacion.casosdeuso;

import dominio.modelo.PeriodoAcademico;
import dominio.puerto.externo.LoggerPort;
import dominio.puerto.repositorio.RepositorioPeriodos;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * @author inici4rsesi0n
 */
@Service
public class GestionPeriodos {

    private final RepositorioPeriodos repo;
    private final LoggerPort logger;

    public GestionPeriodos(RepositorioPeriodos repo, LoggerPort logger) {
        this.repo = repo;
        this.logger = logger;
    }

    public void crearPeriodo(String nombre, String fechaInicio, String fechaFin, String estado) {
        if (nombre == null || nombre.isBlank()) throw new IllegalArgumentException("El nombre del periodo no puede estar vacío.");
        if (fechaInicio == null || fechaInicio.isBlank()) throw new IllegalArgumentException("La fecha de inicio no puede estar vacía.");
        if (fechaFin == null || fechaFin.isBlank()) throw new IllegalArgumentException("La fecha de fin no puede estar vacía.");
        if (repo.buscarPorNombre(nombre).isPresent()) throw new IllegalArgumentException("Ya existe un periodo académico con ese nombre.");

        String estadoFinal = (estado != null && !estado.isBlank()) ? estado : "Activo";
        repo.agregar(new PeriodoAcademico(nombre, fechaInicio, fechaFin, estadoFinal));

        logger.audit(String.format(
                "op=CREATE entity=PERIODO id=%s | inicio=%s fin=%s estado=%s",
                nombre, fechaInicio, fechaFin, estadoFinal));
    }

    public PeriodoAcademico buscarPorNombre(String nombre) { return repo.buscarPorNombre(nombre).orElse(null); }

    public List<PeriodoAcademico> listarTodos() { return repo.listarTodos(); }

    public void actualizarPeriodo(PeriodoAcademico original, String nuevoNombre, String nuevaFechaInicio, String nuevaFechaFin, String nuevoEstado) {
        if (nuevoNombre == null || nuevoNombre.isBlank()) throw new IllegalArgumentException("El nuevo nombre no puede estar vacío.");
        if (nuevaFechaInicio == null || nuevaFechaInicio.isBlank()) throw new IllegalArgumentException("La nueva fecha de inicio no puede estar vacía.");
        if (nuevaFechaFin == null || nuevaFechaFin.isBlank()) throw new IllegalArgumentException("La nueva fecha de fin no puede estar vacía.");

        String antes = original.toString();
        original.setNombre(nuevoNombre);
        original.setFechaInicio(nuevaFechaInicio);
        original.setFechaFin(nuevaFechaFin);
        original.setEstado(nuevoEstado);
        repo.actualizar(original, original);

        logger.audit(String.format(
                "op=UPDATE entity=PERIODO id=%s | before=%s after=%s",
                nuevoNombre, antes, original));
    }

    public void eliminarPeriodo(PeriodoAcademico periodo) {
        if (periodo == null) throw new IllegalArgumentException("El periodo no puede ser nulo");
        String snapshot = periodo.toString();
        repo.eliminar(periodo);
        logger.audit(String.format(
                "op=DELETE entity=PERIODO id=%s | snapshot=%s",
                periodo.getNombre(), snapshot));
    }
}
