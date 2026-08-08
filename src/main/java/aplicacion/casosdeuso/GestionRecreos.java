package aplicacion.casosdeuso;

import dominio.modelo.FranjaHoraria;
import dominio.modelo.PeriodoAcademico;
import dominio.modelo.Recreo;
import dominio.puerto.repositorio.RepositorioRecreos;
import java.util.List;

/**
 *
 * @author inici4rsesi0n
 */
public class GestionRecreos {

    private final RepositorioRecreos repo;

    public GestionRecreos(RepositorioRecreos repo) {
        this.repo = repo;
    }

    public void crearRecreo(FranjaHoraria franja, String descripcion, PeriodoAcademico periodo) {
        if (franja == null || periodo == null) {
            throw new IllegalArgumentException("La franja horaria y el periodo son obligatorios.");
        }
        Recreo recreo = new Recreo(franja, descripcion, periodo);
        repo.agregar(recreo);
    }

    public Recreo buscarPorDescripcion(String descripcion) {
        return repo.buscarPorDescripcion(descripcion).orElse(null);
    }

    public List<Recreo> listarTodos() {
        return repo.listarTodos();
    }

    public void actualizarRecreo(Recreo original, FranjaHoraria franja, String descripcion, PeriodoAcademico periodo) {
        if (franja == null || periodo == null) {
            throw new IllegalArgumentException("La franja horaria y el periodo son obligatorios.");
        }
        Recreo actualizado = new Recreo(franja, descripcion, periodo);
        repo.actualizar(original, actualizado);
    }

    public void eliminarRecreo(Recreo recreo) {
        repo.eliminar(recreo);
    }
}