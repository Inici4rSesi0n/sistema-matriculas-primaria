package aplicacion.casosdeuso;

import dominio.modelo.Docente;
import dominio.modelo.Grado;
import dominio.modelo.Grupo;
import dominio.puerto.externo.LoggerPort;
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
    private final LoggerPort logger;

    public GestionGrupos(RepositorioGrupos repo, RepositorioDocentes repoDocentes, LoggerPort logger) {
        this.repo = repo;
        this.repoDocentes = repoDocentes;
        this.logger = logger;
    }

    public void crearGrupo(String nombre, Grado grado) {
        if (nombre == null || nombre.isBlank()) throw new IllegalArgumentException("El nombre del grupo no puede estar vacío.");
        if (grado == null) throw new IllegalArgumentException("El grado no puede ser nulo.");
        if (repo.buscarPorNombre(nombre).isPresent()) throw new IllegalArgumentException("Ya existe un grupo con ese nombre.");

        Grupo nuevo = new Grupo(nombre, grado);
        repo.agregar(nuevo);

        logger.audit(String.format(
                "op=CREATE entity=GRUPO id=%s | grado=%s",
                nombre, (grado.getNombre() != null ? grado.getNombre() : "Sin grado")));
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

        String antes = original.toString();

        original.setNombre(nuevoNombre);
        original.setGrado(nuevoGrado);
        repo.actualizar(original, original);

        logger.audit(String.format(
                "op=UPDATE entity=GRUPO id=%s | before=%s after=%s",
                original.getNombre(), antes, original));
    }

    public void eliminarGrupo(Grupo grupo) {
        if (grupo == null) throw new IllegalArgumentException("El grupo no puede ser nulo.");

        String snapshot = grupo.toString();
        repo.eliminar(grupo);

        logger.audit(String.format(
                "op=DELETE entity=GRUPO id=%s | snapshot=%s",
                grupo.getNombre(), snapshot));
    }

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

        String detalleAnterior = (tutorAnterior != null && !tutorAnterior.equals(tutor))
                ? String.format(" previous=%s", tutorAnterior.getCodigo())
                : "";
        logger.audit(String.format(
                "op=LINK entity=GRUPO id=%s -> DOCENTE id=%s | role=TUTOR%s",
                grupo.getNombre(), tutor.getCodigo(), detalleAnterior));
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

        logger.audit(String.format(
                "op=UNLINK entity=GRUPO id=%s -> DOCENTE id=%s | role=TUTOR",
                grupo.getNombre(), tutorAnterior.getCodigo()));
    }
}
