package aplicacion.casosdeuso;

import dominio.modelo.Grado;
import dominio.puerto.repositorio.RepositorioGrados;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * @author inici4rsesi0n
 */
@Service
public class GestionGrados {

    private final RepositorioGrados repo;

    public GestionGrados(RepositorioGrados repo) {
        this.repo = repo;
    }

    public void crearGrado(String nombre, String nivel) {
        if (nombre == null || nombre.isBlank()) throw new IllegalArgumentException("El nombre del grado no puede estar vacío.");
        if (nivel == null || nivel.isBlank()) throw new IllegalArgumentException("El nivel del grado no puede estar vacío.");
        if (repo.buscarPorNombre(nombre).isPresent()) throw new IllegalArgumentException("Ya existe un grado con ese nombre.");
        repo.agregar(new Grado(nombre, nivel));
    }

    public Grado buscarPorNombre(String nombre) { return repo.buscarPorNombre(nombre).orElse(null); }
    public List<Grado> listarTodos() { return repo.listarTodos(); }

    public void actualizarGrado(Grado original, String nuevoNombre, String nuevoNivel) {
        if (nuevoNombre == null || nuevoNombre.isBlank()) throw new IllegalArgumentException("El nombre del grado no puede estar vacío.");
        if (nuevoNivel == null || nuevoNivel.isBlank()) throw new IllegalArgumentException("El nivel del grado no puede estar vacío.");
        Grado actualizado = new Grado(nuevoNombre, nuevoNivel);
        repo.actualizar(original, actualizado);
    }

    public void eliminarGrado(Grado grado) { repo.eliminar(grado); }
}