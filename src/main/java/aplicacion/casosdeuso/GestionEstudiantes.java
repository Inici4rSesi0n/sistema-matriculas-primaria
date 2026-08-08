package aplicacion.casosdeuso;

import dominio.modelo.Estudiante;
import dominio.puerto.repositorio.RepositorioEstudiantes;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * @author inici4rsesi0n
 */
@Service
public class GestionEstudiantes {

    private final RepositorioEstudiantes repo;

    public GestionEstudiantes(RepositorioEstudiantes repo) {
        this.repo = repo;
    }

    public void agregar(Estudiante estudiante) {
        if (estudiante == null) throw new IllegalArgumentException("El estudiante no puede ser nulo");
        if (repo.buscarPorCodigo(estudiante.getCodigo()).isPresent()) throw new IllegalArgumentException("Ya existe un estudiante con ese código");
        if (repo.buscarPorDni(estudiante.getDni()).isPresent()) throw new IllegalArgumentException("Ya existe un estudiante con ese DNI");
        repo.agregar(estudiante);
    }

    public Estudiante buscarPorCodigo(String codigo) { return repo.buscarPorCodigo(codigo).orElse(null); }
    public Estudiante buscarPorDni(String dni) { return repo.buscarPorDni(dni).orElse(null); }
    public List<Estudiante> listarTodos() { return repo.listarTodos(); }
    public void actualizar(Estudiante original, Estudiante actualizado) { repo.actualizar(original, actualizado); }
    public void eliminar(Estudiante estudiante) { repo.eliminar(estudiante); }
}