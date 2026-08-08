package aplicacion.casosdeuso;

import dominio.modelo.Secretario;
import dominio.puerto.repositorio.RepositorioSecretarios;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * @author inici4rsesi0n
 */
@Service
public class GestionSecretarios {

    private final RepositorioSecretarios repo;

    public GestionSecretarios(RepositorioSecretarios repo) {
        this.repo = repo;
    }

    public void agregar(Secretario secretario) {
        if (secretario == null) throw new IllegalArgumentException("El secretario no puede ser nulo");
        if (repo.buscarPorCodigo(secretario.getCodigo()).isPresent()) throw new IllegalArgumentException("Ya existe un secretario con ese código");
        repo.agregar(secretario);
    }

    public Secretario buscarPorCodigo(String codigo) { return repo.buscarPorCodigo(codigo).orElse(null); }
    public List<Secretario> listarTodos() { return repo.listarTodos(); }
    public void actualizar(Secretario original, Secretario actualizado) { repo.actualizar(original, actualizado); }
    public void eliminar(Secretario secretario) { repo.eliminar(secretario); }
}