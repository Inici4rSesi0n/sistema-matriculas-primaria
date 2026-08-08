package aplicacion.casosdeuso;

import dominio.modelo.Docente;
import dominio.modelo.Grado;
import dominio.modelo.Grupo;
import dominio.puerto.repositorio.RepositorioGrupos;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * @author inici4rsesi0n
 */
@Service
public class GestionGrupos {

    private final RepositorioGrupos repo;

    public GestionGrupos(RepositorioGrupos repo) {
        this.repo = repo;
    }

    public void crearGrupo(String nombre, Grado grado) {
        if (nombre == null || nombre.isBlank()) throw new IllegalArgumentException("El nombre del grupo no puede estar vacío.");
        if (grado == null) throw new IllegalArgumentException("El grado no puede ser nulo.");
        if (repo.buscarPorNombre(nombre).isPresent()) throw new IllegalArgumentException("Ya existe un grupo con ese nombre.");
        repo.agregar(new Grupo(nombre, grado));
    }

    public Grupo buscarPorNombre(String nombre) { return repo.buscarPorNombre(nombre).orElse(null); }
    public List<Grupo> listarTodos() { return repo.listarTodos(); }
    public List<Grupo> buscarPorNombreYGrado(String nombreGrupo, String nombreGrado) { return repo.buscarPorNombreYGrado(nombreGrupo, nombreGrado); }

    public void actualizarGrupo(Grupo original, String nuevoNombre, Grado nuevoGrado) {
        if (nuevoNombre == null || nuevoNombre.isBlank()) throw new IllegalArgumentException("El nuevo nombre no puede estar vacío.");
        if (nuevoGrado == null) throw new IllegalArgumentException("El nuevo grado no puede ser nulo.");
        Grupo actualizado = new Grupo(nuevoNombre, nuevoGrado);
        repo.actualizar(original, actualizado);
    }

    public void eliminarGrupo(Grupo grupo) { repo.eliminar(grupo); }

    public void asignarTutor(Grupo grupo, Docente tutor) {
        if (grupo == null || tutor == null) throw new IllegalArgumentException("Grupo y tutor son obligatorios.");
        grupo.asignarTutor(tutor);
        repo.actualizar(grupo, grupo);
    }

    public void removerTutor(Grupo grupo) {
        if (grupo == null) throw new IllegalArgumentException("El grupo no puede ser nulo.");
        grupo.asignarTutor(null);
        repo.actualizar(grupo, grupo);
    }
}