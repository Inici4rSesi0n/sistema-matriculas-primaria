package aplicacion.casosdeuso;

import dominio.modelo.PeriodoAcademico;
import dominio.puerto.repositorio.RepositorioPeriodos;
import java.util.List;

/**
 *
 * @author inici4rsesi0n
 */
public class GestionPeriodos {

    private final RepositorioPeriodos repo;

    public GestionPeriodos(RepositorioPeriodos repo) {
        this.repo = repo;
    }

    public void crearPeriodo(String nombre, String fechaInicio, String fechaFin, String estado) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del periodo no puede estar vacío.");
        }
        if (fechaInicio == null || fechaInicio.isBlank()) {
            throw new IllegalArgumentException("La fecha de inicio no puede estar vacía.");
        }
        if (fechaFin == null || fechaFin.isBlank()) {
            throw new IllegalArgumentException("La fecha de fin no puede estar vacía.");
        }
        if (repo.buscarPorNombre(nombre).isPresent()) {
            throw new IllegalArgumentException("Ya existe un periodo académico con ese nombre.");
        }
        repo.agregar(new PeriodoAcademico(nombre, fechaInicio, fechaFin,
                (estado != null && !estado.isBlank()) ? estado : "Activo"));
    }

    public PeriodoAcademico buscarPorNombre(String nombre) {
        return repo.buscarPorNombre(nombre).orElse(null);
    }

    public List<PeriodoAcademico> listarTodos() {
        return repo.listarTodos();
    }

    public void actualizarPeriodo(PeriodoAcademico original, String nuevoNombre,
                                  String nuevaFechaInicio, String nuevaFechaFin,
                                  String nuevoEstado) {
        if (nuevoNombre == null || nuevoNombre.isBlank()) {
            throw new IllegalArgumentException("El nuevo nombre no puede estar vacío.");
        }
        if (nuevaFechaInicio == null || nuevaFechaInicio.isBlank()) {
            throw new IllegalArgumentException("La nueva fecha de inicio no puede estar vacía.");
        }
        if (nuevaFechaFin == null || nuevaFechaFin.isBlank()) {
            throw new IllegalArgumentException("La nueva fecha de fin no puede estar vacía.");
        }
        PeriodoAcademico actualizado = new PeriodoAcademico(nuevoNombre, nuevaFechaInicio, nuevaFechaFin,
                (nuevoEstado != null && !nuevoEstado.isBlank()) ? nuevoEstado : "Activo");
        repo.actualizar(original, actualizado);
    }

    public void eliminarPeriodo(PeriodoAcademico periodo) {
        repo.eliminar(periodo);
    }
}