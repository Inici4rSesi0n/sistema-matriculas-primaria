package aplicacion.casosdeuso;
import dominio.modelo.Clase;
import dominio.modelo.Evento;
import dominio.modelo.Grupo;
import dominio.modelo.PeriodoAcademico;
import dominio.modelo.Recreo;
import dominio.puerto.repositorio.RepositorioClases;
import dominio.puerto.repositorio.RepositorioRecreos;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
/**
 *
 * @author inici4rsesi0n
 */
public class GestionHorario {

    private final RepositorioClases repoClases;
    private final RepositorioRecreos repoRecreos;

    public GestionHorario(RepositorioClases repoClases, RepositorioRecreos repoRecreos) {
        this.repoClases = repoClases;
        this.repoRecreos = repoRecreos;
    }

    public List<Evento> obtenerHorarioCompleto(Grupo grupo, PeriodoAcademico periodo) {
        if (grupo == null) {
            throw new IllegalArgumentException("El grupo no puede ser nulo.");
        }
        if (periodo == null) {
            throw new IllegalArgumentException("El periodo académico no puede ser nulo.");
        }
        List<Evento> eventos = new ArrayList<>();
        List<Clase> clases = obtenerClasesDelGrupo(grupo, periodo);
        eventos.addAll(clases);
        List<Recreo> recreos = obtenerRecreosDelPeriodo(periodo);
        eventos.addAll(recreos);
        eventos.sort(Comparator.comparing(Evento::getDiaSemana)
                .thenComparing(Evento::getHoraInicio));
        return eventos;
    }

    private List<Clase> obtenerClasesDelGrupo(Grupo grupo, PeriodoAcademico periodo) {
        List<Clase> resultado = new ArrayList<>();
        for (Clase c : repoClases.listarTodos()) {
            if (c.getGrupo() != null && c.getGrupo().equals(grupo)
                    && c.getPeriodo() != null && c.getPeriodo().equals(periodo)) {
                resultado.add(c);
            }
        }
        return resultado;
    }

    private List<Recreo> obtenerRecreosDelPeriodo(PeriodoAcademico periodo) {
        List<Recreo> resultado = new ArrayList<>();
        for (Recreo r : repoRecreos.listarTodos()) {
            if (r.getPeriodo() != null && r.getPeriodo().equals(periodo)) {
                resultado.add(r);
            }
        }
        return resultado;
    }
}