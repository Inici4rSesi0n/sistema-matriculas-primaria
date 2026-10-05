package aplicacion.casosdeuso;

import dominio.modelo.*;
import dominio.puerto.repositorio.RepositorioClases;
import dominio.puerto.repositorio.RepositorioRecreos;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author inici4rsesi0n
 */
@Service
public class GestionHorario {

    private final RepositorioClases repoClases;
    private final RepositorioRecreos repoRecreos;

    public GestionHorario(RepositorioClases repoClases, RepositorioRecreos repoRecreos) {
        this.repoClases = repoClases;
        this.repoRecreos = repoRecreos;
    }

    public List<Evento> obtenerHorarioCompleto(Grupo grupo, PeriodoAcademico periodo) {
        if (grupo == null) throw new IllegalArgumentException("El grupo no puede ser nulo.");
        if (periodo == null) throw new IllegalArgumentException("El periodo académico no puede ser nulo.");

        List<Evento> eventos = new ArrayList<>();
        eventos.addAll(obtenerClasesDelGrupo(grupo, periodo));
        eventos.addAll(obtenerRecreosDelPeriodo(periodo));

        eventos.sort(Comparator.comparing(Evento::getDiaSemana)
                .thenComparing(Evento::getHoraInicio));
        return eventos;
    }

    private List<Clase> obtenerClasesDelGrupo(Grupo grupo, PeriodoAcademico periodo) {
        return repoClases.listarTodos().stream()
                .filter(c -> grupo.equals(c.getGrupo()))
                .filter(c -> periodo.equals(c.getPeriodo()))
                .toList();
    }

    private List<Recreo> obtenerRecreosDelPeriodo(PeriodoAcademico periodo) {
        return repoRecreos.listarTodos().stream()
                .filter(r -> r.aplicaEn(periodo))
                .toList();
    }
}
