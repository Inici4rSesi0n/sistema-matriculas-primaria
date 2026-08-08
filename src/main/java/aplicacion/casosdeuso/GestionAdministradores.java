package aplicacion.casosdeuso;

import dominio.modelo.Administrador;
import dominio.puerto.repositorio.RepositorioAdministradores;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * @author inici4rsesi0n
 */
@Service
public class GestionAdministradores {

    private final RepositorioAdministradores repo;

    public GestionAdministradores(RepositorioAdministradores repo) {
        this.repo = repo;
    }

    public void agregar(Administrador administrador) {
        if (administrador == null) {
            throw new IllegalArgumentException("El administrador no puede ser nulo");
        }
        if (repo.buscarPorCodigo(administrador.getCodigo()).isPresent()) {
            throw new IllegalArgumentException("Ya existe un administrador con ese código");
        }
        repo.agregar(administrador);
    }

    public Administrador buscarPorCodigo(String codigo) {
        return repo.buscarPorCodigo(codigo).orElse(null);
    }

    public List<Administrador> listarTodos() {
        return repo.listarTodos();
    }

    public void actualizar(Administrador original, Administrador actualizado) {
        repo.actualizar(original, actualizado);
    }

    public void eliminar(Administrador administrador) {
        repo.eliminar(administrador);
    }
}