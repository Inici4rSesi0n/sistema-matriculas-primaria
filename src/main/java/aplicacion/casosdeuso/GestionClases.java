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

    public void crearClase(String diaSemana, String horaInicio, String horaFin,
                           Asignatura asignatura, Docente docente, Grupo grupo,
                           Aula aula, PeriodoAcademico periodo) {
        validarCamposObligatorios(diaSemana, horaInicio, horaFin, asignatura, docente, grupo, aula, periodo);
        validarConflictos(diaSemana, horaInicio, horaFin, docente, grupo, aula, null);

        FranjaHoraria franja = new FranjaHoraria(diaSemana, horaInicio, horaFin);
        repo.agregar(new Clase(franja, periodo, asignatura, docente, grupo, aula));
    }

    public List<Clase> listarClases() {
        return repo.listarTodos();
    }

    public List<Clase> buscarPorGrupoYGrado(String nombreGrupo, String nombreGrado) {
        return repo.buscarPorGrupoYGrado(nombreGrupo, nombreGrado);
    }

    public Clase buscarClase(String nombreGrado, String nombreGrupo, String dia, String horaInicio, String horaFin) {
        return repo.buscarClase(nombreGrado, nombreGrupo, dia, horaInicio, horaFin).orElse(null);
    }

    public void actualizarClase(Clase original, String diaSemana, String horaInicio, String horaFin,
                                Asignatura asignatura, Docente docente, Grupo grupo,
                                Aula aula, PeriodoAcademico periodo) {
        validarCamposObligatorios(diaSemana, horaInicio, horaFin, asignatura, docente, grupo, aula, periodo);
        validarConflictos(diaSemana, horaInicio, horaFin, docente, grupo, aula, original);

        FranjaHoraria franja = new FranjaHoraria(diaSemana, horaInicio, horaFin);
        Clase actualizada = new Clase(franja, periodo, asignatura, docente, grupo, aula);
        repo.actualizar(original, actualizada);
    }

    public void eliminarClase(Clase clase) {
        repo.eliminar(clase);
    }

    // ============= Validaciones privadas =============

    private void validarCamposObligatorios(String diaSemana, String horaInicio, String horaFin,
                                           Asignatura asignatura, Docente docente, Grupo grupo,
                                           Aula aula, PeriodoAcademico periodo) {
        if (diaSemana == null || diaSemana.isBlank()
                || horaInicio == null || horaInicio.isBlank()
                || horaFin == null || horaFin.isBlank()) {
            throw new IllegalArgumentException("Día, hora de inicio y hora de fin son obligatorios.");
        }
        if (asignatura == null || docente == null || grupo == null || aula == null || periodo == null) {
            throw new IllegalArgumentException("Todos los campos son obligatorios.");
        }
    }

    private void validarConflictos(String diaSemana, String horaInicio, String horaFin,
                                   Docente docente, Grupo grupo, Aula aula,
                                   Clase claseExcluida) {
        if (repo.existeConflictoDocente(docente.getCodigo(), diaSemana, horaInicio, horaFin, claseExcluida)) {
            throw new IllegalArgumentException(
                    "El docente " + docente.getNombreCompleto() + " ya tiene una clase en ese horario.");
        }
        if (repo.existeConflictoAula(aula.getNombre(), diaSemana, horaInicio, horaFin, claseExcluida)) {
            throw new IllegalArgumentException(
                    "El aula " + aula.getNombre() + " ya está ocupada en ese horario.");
        }
        String nombreGrado = (grupo.getGrado() != null) ? grupo.getGrado().getNombre() : "";
        if (repo.existeConflictoGrupo(grupo.getNombre(), nombreGrado, diaSemana, horaInicio, horaFin, claseExcluida)) {
            throw new IllegalArgumentException(
                    "El grupo " + grupo.getNombre() + " ya tiene una clase en ese horario.");
        }
    }
}
