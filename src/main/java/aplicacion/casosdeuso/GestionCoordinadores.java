package aplicacion.casosdeuso;

import dominio.modelo.CoordinadorAcademico;
import dominio.puerto.repositorio.RepositorioCoordinadores;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * @author inici4rsesi0n
 */
@Service
public class GestionCoordinadores {

    private final RepositorioCoordinadores repo;

    public GestionCoordinadores(RepositorioCoordinadores repo) {
        this.repo = repo;
    }

    public void agregar(CoordinadorAcademico coordinador) {
        if (coordinador == null) throw new IllegalArgumentException("El coordinador no puede ser nulo");
        if (repo.buscarPorCodigo(coordinador.getCodigo()).isPresent()) throw new IllegalArgumentException("Ya existe un coordinador con ese código");
        repo.agregar(coordinador);
    }

    public CoordinadorAcademico buscarPorCodigo(String codigo) { return repo.buscarPorCodigo(codigo).orElse(null); }
    public List<CoordinadorAcademico> listarTodos() { return repo.listarTodos(); }
    public void actualizar(CoordinadorAcademico original, CoordinadorAcademico actualizado) { repo.actualizar(original, actualizado); }
    public void eliminar(CoordinadorAcademico coordinador) { repo.eliminar(coordinador); }
}