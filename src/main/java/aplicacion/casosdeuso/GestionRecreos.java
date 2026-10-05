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
    public void crearRecreo(FranjaHoraria franja, String descripcion, List<PeriodoAcademico> periodos) {
        if (franja == null) throw new IllegalArgumentException("La franja horaria es obligatoria.");
        if (periodos == null || periodos.isEmpty())
            throw new IllegalArgumentException("Debe asociar al menos un periodo al recreo.");
        Recreo nuevo = new Recreo(franja, descripcion, periodos);
        repo.agregar(nuevo);
        logger.audit(String.format(
                "op=CREATE entity=RECREO id=%s | desc=%s periodos=%d",
                franja, nuevo.getDescripcion(), periodos.size()));
    }
    public Recreo buscarPorDescripcion(String descripcion) {
        return repo.buscarPorDescripcion(descripcion).orElse(null);
    }
    public List<Recreo> listarTodos() { return repo.listarTodos(); }
    public void actualizarRecreo(Recreo original, FranjaHoraria franja, String descripcion, List<PeriodoAcademico> periodos) {
        if (franja == null) throw new IllegalArgumentException("La franja horaria es obligatoria.");
        if (periodos == null || periodos.isEmpty())
            throw new IllegalArgumentException("Debe asociar al menos un periodo al recreo.");
        String antes = original.toString();
        original.setFranja(franja);
        original.setDescripcion(descripcion);
        original.setPeriodos(periodos);
        repo.actualizar(original, original);
        logger.audit(String.format(
                "op=UPDATE entity=RECREO | before=%s after=%s",
                antes, original));
    }
    public void aplicarA(Recreo recreo, PeriodoAcademico periodo) {
        if (recreo == null || periodo == null)
            throw new IllegalArgumentException("Recreo y periodo son obligatorios.");
        recreo.aplicarA(periodo);
        repo.actualizar(recreo, recreo);

        logger.audit(String.format(
                "op=LINK entity=RECREO id=%s -> PERIODO id=%s",
                recreo.getFranja(), periodo.getNombre()));
    }
    public void removerDe(Recreo recreo, PeriodoAcademico periodo) {
        if (recreo == null || periodo == null)
            throw new IllegalArgumentException("Recreo y periodo son obligatorios.");
        recreo.removerDe(periodo);
        repo.actualizar(recreo, recreo);

        logger.audit(String.format(
                "op=UNLINK entity=RECREO id=%s -> PERIODO id=%s",
                recreo.getFranja(), periodo.getNombre()));
    }
    public void eliminarRecreo(Recreo recreo) {
        if (recreo == null) throw new IllegalArgumentException("El recreo no puede ser nulo");
        String snapshot = recreo.toString();
        repo.eliminar(recreo);
        logger.audit(String.format(
                "op=DELETE entity=RECREO | snapshot=%s", snapshot));
    }
}
