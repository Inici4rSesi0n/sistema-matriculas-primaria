package aplicacion.casosdeuso;

import dominio.modelo.Aula;
import dominio.puerto.repositorio.RepositorioAulas;
import java.util.List;

/**
 *
 * @author inici4rsesi0n
 */
public class GestionAulas {

    private final RepositorioAulas repo;

    public GestionAulas(RepositorioAulas repo) {
        this.repo = repo;
    }

    public void crearAula(String nombre, int capacidad, String ubicacion, String tipo) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del aula no puede estar vacío.");
        }
        if (capacidad <= 0) {
            throw new IllegalArgumentException("La capacidad debe ser mayor a 0.");
        }
        if (ubicacion == null || ubicacion.isBlank()) {
            throw new IllegalArgumentException("La ubicación no puede estar vacía.");
        }
        if (tipo == null || tipo.isBlank()) {
            throw new IllegalArgumentException("El tipo de aula no puede estar vacío.");
        }
        if (repo.buscarPorNombre(nombre).isPresent()) {
            throw new IllegalArgumentException("Ya existe un aula con ese nombre.");
        }
        repo.agregar(new Aula(nombre, capacidad, ubicacion, tipo));
    }

    public List<Aula> listarTodos() {
        return repo.listarTodos();
    }

    public Aula buscarAula(String nombre) {
        return repo.buscarPorNombre(nombre).orElse(null);
    }

    public void actualizarAula(Aula original, String nuevoNombre, int nuevaCapacidad,
                               String nuevaUbicacion, String nuevoTipo) {
        if (nuevoNombre == null || nuevoNombre.isBlank()) {
            throw new IllegalArgumentException("El nuevo nombre no puede estar vacío.");
        }
        if (nuevaCapacidad <= 0) {
            throw new IllegalArgumentException("La nueva capacidad debe ser mayor a 0.");
        }
        if (nuevaUbicacion == null || nuevaUbicacion.isBlank()) {
            throw new IllegalArgumentException("La nueva ubicación no puede estar vacía.");
        }
        if (nuevoTipo == null || nuevoTipo.isBlank()) {
            throw new IllegalArgumentException("El nuevo tipo no puede estar vacío.");
        }
        Aula actualizada = new Aula(nuevoNombre, nuevaCapacidad, nuevaUbicacion, nuevoTipo);
        repo.actualizar(original, actualizada);
    }

    public void eliminarAula(Aula aula) {
        repo.eliminar(aula);
    }
}