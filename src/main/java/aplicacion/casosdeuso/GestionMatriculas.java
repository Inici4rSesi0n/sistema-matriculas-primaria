package aplicacion.casosdeuso;

import dominio.modelo.*;
import dominio.puerto.repositorio.RepositorioMatriculas;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * @author inici4rsesi0n
 */
@Service
public class GestionMatriculas {

    private final RepositorioMatriculas repo;

    public GestionMatriculas(RepositorioMatriculas repo) {
        this.repo = repo;
    }

    public void matricularEstudiante(Estudiante estudiante, PeriodoAcademico periodo, Grupo grupo, List<Asignatura> asignaturas, String fecha, EstadoMatricula estado) {
        if (estudiante == null || periodo == null || grupo == null) throw new IllegalArgumentException("Estudiante, periodo y grupo son obligatorios.");
        if (repo.buscarPorEstudianteYPeriodo(estudiante.getCodigo(), periodo.getNombre()).isPresent()) throw new IllegalArgumentException("El estudiante ya está matriculado en ese periodo.");
        repo.agregar(new Matricula(estudiante, periodo, grupo, asignaturas, fecha, estado));
    }

    public List<Matricula> listarTodas() { return repo.listarTodos(); }
    public Matricula buscarMatricula(String codigoEstudiante, String nombrePeriodo) { return repo.buscarPorEstudianteYPeriodo(codigoEstudiante, nombrePeriodo).orElse(null); }
    public List<Matricula> listarPorGrupo(String nombreGrupo) { return repo.buscarPorGrupo(nombreGrupo); }

    public void actualizarEstado(Matricula matricula, EstadoMatricula nuevoEstado) {
        if (nuevoEstado == null) throw new IllegalArgumentException("El estado no puede ser nulo.");
        matricula.setEstado(nuevoEstado);
        repo.actualizar(matricula, matricula);
    }

    public void eliminarMatricula(Matricula matricula) { repo.eliminar(matricula); }
}