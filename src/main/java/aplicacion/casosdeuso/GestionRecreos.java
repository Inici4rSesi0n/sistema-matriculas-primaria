package aplicacion.casosdeuso;

import dominio.modelo.FranjaHoraria;
import dominio.modelo.PeriodoAcademico;
import dominio.modelo.Recreo;
import dominio.puerto.externo.LoggerPort;
import dominio.puerto.repositorio.RepositorioRecreos;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * @author inici4rsesi0n
 */
@Service
public class GestionRecreos {

    private final RepositorioRecreos repo;
    private final LoggerPort logger;

    public GestionRecreos(RepositorioRecreos repo, LoggerPort logger) {
        this.repo = repo;
        this.logger = logger;
    }

    public void crearRecreo(FranjaHoraria franja, String descripcion, PeriodoAcademico periodo) {
        if (franja == null || periodo == null) throw new IllegalArgumentException("La franja horaria y el periodo son obligatorios.");
        Recreo nuevo = new Recreo(franja, descripcion, periodo);
        repo.agregar(nuevo);
        logger.audit(String.format(
                "op=CREATE entity=RECREO id=%s | %s periodo=%s",
                franja, descripcion, periodo.getNombre()));
    }

    public Recreo buscarPorDescripcion(String descripcion) { return repo.buscarPorDescripcion(descripcion).orElse(null); }

    public List<Recreo> listarTodos() { return repo.listarTodos(); }

    public void actualizarRecreo(Recreo original, FranjaHoraria franja, String descripcion, PeriodoAcademico periodo) {
        if (franja == null || periodo == null) throw new IllegalArgumentException("La franja horaria y el periodo son obligatorios.");

        String antes = original.toString();
        original.setFranja(franja);
        original.setPeriodo(periodo);
        original.setDescripcion(descripcion);
        repo.actualizar(original, original);

        logger.audit(String.format(
                "op=UPDATE entity=RECREO | before=%s after=%s",
                antes, original));
    }

    public void eliminarRecreo(Recreo recreo) {
        if (recreo == null) throw new IllegalArgumentException("El recreo no puede ser nulo");
        String snapshot = recreo.toString();
        repo.eliminar(recreo);
        logger.audit(String.format(
                "op=DELETE entity=RECREO | snapshot=%s", snapshot));
    }
}
