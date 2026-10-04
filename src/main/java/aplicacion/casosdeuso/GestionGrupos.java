package aplicacion.casosdeuso;

import dominio.modelo.Docente;
import dominio.modelo.Grado;
import dominio.modelo.Grupo;
import dominio.puerto.repositorio.RepositorioDocentes;
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
    private final RepositorioDocentes repoDocentes;

    public GestionGrupos(RepositorioGrupos repo, RepositorioDocentes repoDocentes) {
        this.repo = repo;
        this.repoDocentes = repoDocentes;
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

    /**
     * Actualiza nombre y grado del grupo preservando tutor y estudiantes.
     * Modifica el objeto in-place para no perder las asociaciones.
     */
    public void actualizarGrupo(Grupo original, String nuevoNombre, Grado nuevoGrado) {
        if (nuevoNombre == null || nuevoNombre.isBlank()) throw new IllegalArgumentException("El nuevo nombre no puede estar vacío.");
        if (nuevoGrado == null) throw new IllegalArgumentException("El nuevo grado no puede ser nulo.");
        original.setNombre(nuevoNombre);
        original.setGrado(nuevoGrado);
        repo.actualizar(original, original);
    }

    public void eliminarGrupo(Grupo grupo) { repo.eliminar(grupo); }

    /**
     * Asigna un tutor al grupo y persiste tanto el grupo como los docentes afectados.
     * Si el grupo ya tenía tutor, el tutor anterior también se persiste (queda sin tutoría).
     */
    public void asignarTutor(Grupo grupo, Docente tutor) {
        if (grupo == null || tutor == null) throw new IllegalArgumentException("Grupo y tutor son obligatorios.");

        Docente tutorAnterior = grupo.getTutor();
        grupo.asignarTutor(tutor);

        if (tutorAnterior != null && !tutorAnterior.equals(tutor)) {
            repoDocentes.actualizar(tutorAnterior, tutorAnterior);
        }
        repoDocentes.actualizar(tutor, tutor);
        repo.actualizar(grupo, grupo);
    }

    /**
     * Remueve el tutor del grupo y persiste el grupo y el docente afectado.
     */
    public void removerTutor(Grupo grupo) {
        if (grupo == null) throw new IllegalArgumentException("El grupo no puede ser nulo.");

        Docente tutorAnterior = grupo.getTutor();
        if (tutorAnterior == null) return;

        grupo.asignarTutor(null);
        repoDocentes.actualizar(tutorAnterior, tutorAnterior);
        repo.actualizar(grupo, grupo);
    }
}
