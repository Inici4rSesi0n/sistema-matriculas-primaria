package aplicacion.casosdeuso;

import dominio.modelo.*;
import dominio.puerto.repositorio.RepositorioClases;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * @author inici4rsesi0n
 */
@Service
public class GestionClases {

    private final RepositorioClases repo;

    public GestionClases(RepositorioClases repo) {
        this.repo = repo;
    }

    public void crearClase(String diaSemana, String horaInicio, String horaFin, Asignatura asignatura, Docente docente, Grupo grupo, Aula aula, PeriodoAcademico periodo) {
        if (diaSemana == null || diaSemana.isBlank() || horaInicio == null || horaInicio.isBlank() || horaFin == null || horaFin.isBlank())
            throw new IllegalArgumentException("Día, hora de inicio y hora de fin son obligatorios.");
        if (asignatura == null || docente == null || grupo == null || aula == null || periodo == null)
            throw new IllegalArgumentException("Todos los campos son obligatorios.");
        repo.agregar(new Clase(new FranjaHoraria(diaSemana, horaInicio, horaFin), periodo, asignatura, docente, grupo, aula));
    }

    public List<Clase> listarClases() { return repo.listarTodos(); }
    public List<Clase> buscarPorGrupoYGrado(String nombreGrupo, String nombreGrado) { return repo.buscarPorGrupoYGrado(nombreGrupo, nombreGrado); }
    public boolean existeDocenteEnHorario(String codigoDocente, String dia, String horaInicio, String horaFin) { return repo.existeDocenteEnHorario(codigoDocente, dia, horaInicio, horaFin); }
    public Clase buscarClase(String nombreGrado, String nombreGrupo, String dia, String horaInicio, String horaFin) { return repo.buscarClase(nombreGrado, nombreGrupo, dia, horaInicio, horaFin).orElse(null); }

    public void actualizarClase(Clase original, String diaSemana, String horaInicio, String horaFin, Asignatura asignatura, Docente docente, Grupo grupo, Aula aula, PeriodoAcademico periodo) {
        if (diaSemana == null || diaSemana.isBlank() || horaInicio == null || horaInicio.isBlank() || horaFin == null || horaFin.isBlank())
            throw new IllegalArgumentException("Día, hora de inicio y hora de fin son obligatorios.");
        if (asignatura == null || docente == null || grupo == null || aula == null || periodo == null)
            throw new IllegalArgumentException("Todos los campos son obligatorios.");
        Clase actualizada = new Clase(new FranjaHoraria(diaSemana, horaInicio, horaFin), periodo, asignatura, docente, grupo, aula);
        repo.actualizar(original, actualizada);
    }

    public void eliminarClase(Clase clase) { repo.eliminar(clase); }
}