package aplicacion.casosdeuso;

import dominio.modelo.Estudiante;
import dominio.modelo.Padre;
import dominio.puerto.repositorio.RepositorioPadres;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * @author inici4rsesi0n
 */
@Service
public class GestionPadres {

    private final RepositorioPadres repo;

    public GestionPadres(RepositorioPadres repo) {
        this.repo = repo;
    }

    public void agregar(Padre padre) {
        if (padre == null) throw new IllegalArgumentException("El padre no puede ser nulo");
        if (repo.buscarPorCodigo(padre.getCodigo()).isPresent()) throw new IllegalArgumentException("Ya existe un padre con ese código");
        repo.agregar(padre);
    }

    public Padre buscarPorCodigo(String codigo) { return repo.buscarPorCodigo(codigo).orElse(null); }
    public List<Padre> listarTodos() { return repo.listarTodos(); }
    public void actualizar(Padre original, Padre actualizado) { repo.actualizar(original, actualizado); }
    public void eliminar(Padre padre) { repo.eliminar(padre); }

    public void vincularEstudiante(Padre padre, Estudiante estudiante) {
        if (estudiante == null) throw new IllegalArgumentException("El estudiante no puede ser nulo");
        padre.agregarHijo(estudiante);
        repo.actualizar(padre, padre);
    }

    public void desvincularEstudiante(Padre padre, Estudiante estudiante) {
        if (estudiante == null) throw new IllegalArgumentException("El estudiante no puede ser nulo");
        padre.removerHijo(estudiante);
        repo.actualizar(padre, padre);
    }
}