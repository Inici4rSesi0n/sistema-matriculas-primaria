package aplicacion.casosdeuso;

import dominio.modelo.Director;
import dominio.puerto.repositorio.RepositorioDirectores;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * @author inici4rsesi0n
 */
@Service
public class GestionDirectores {

    private final RepositorioDirectores repo;

    public GestionDirectores(RepositorioDirectores repo) {
        this.repo = repo;
    }

    public void agregar(Director director) {
        if (director == null) throw new IllegalArgumentException("El director no puede ser nulo");
        if (repo.buscarPorCodigo(director.getCodigo()).isPresent()) throw new IllegalArgumentException("Ya existe un director con ese código");
        repo.agregar(director);
    }

    public Director buscarPorCodigo(String codigo) { return repo.buscarPorCodigo(codigo).orElse(null); }
    public List<Director> listarTodos() { return repo.listarTodos(); }
    public void actualizar(Director original, Director actualizado) { repo.actualizar(original, actualizado); }
    public void eliminar(Director director) { repo.eliminar(director); }
}