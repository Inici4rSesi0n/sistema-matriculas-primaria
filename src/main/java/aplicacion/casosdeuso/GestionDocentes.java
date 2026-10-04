package aplicacion.casosdeuso;

import dominio.modelo.Asignatura;
import dominio.modelo.Docente;
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
public class GestionDocentes {

    private final RepositorioDocentes repo;
    private final RepositorioGrupos repoGrupos;

    public GestionDocentes(RepositorioDocentes repo, RepositorioGrupos repoGrupos) {
        this.repo = repo;
        this.repoGrupos = repoGrupos;
    }

    public void agregar(Docente docente) {
        if (docente == null) throw new IllegalArgumentException("El docente no puede ser nulo");
        if (repo.buscarPorCodigo(docente.getCodigo()).isPresent()) throw new IllegalArgumentException("Ya existe un docente con ese código");
        if (repo.buscarPorDni(docente.getDni()).isPresent()) throw new IllegalArgumentException("Ya existe un docente con ese DNI");
        repo.agregar(docente);
    }

    public Docente buscarPorCodigo(String codigo) { return repo.buscarPorCodigo(codigo).orElse(null); }
    public Docente buscarPorDni(String dni) { return repo.buscarPorDni(dni).orElse(null); }
    public List<Docente> listarTodos() { return repo.listarTodos(); }
    public void actualizar(Docente original, Docente actualizado) { repo.actualizar(original, actualizado); }
    public void eliminar(Docente docente) { repo.eliminar(docente); }

    public void agregarAsignatura(Docente docente, Asignatura asignatura) {
        if (asignatura == null) throw new IllegalArgumentException("La asignatura no puede ser nula");
        docente.agregarAsignatura(asignatura);
        repo.actualizar(docente, docente);
    }

    public void removerAsignatura(Docente docente, Asignatura asignatura) {
        if (asignatura == null) throw new IllegalArgumentException("La asignatura no puede ser nula");
        docente.removerAsignatura(asignatura);
        repo.actualizar(docente, docente);
    }

    /**
     * Asigna una tutoría a un docente sobre un grupo.
     * Persiste tanto el grupo (con su nuevo tutor) como el docente (con su nueva tutoría).
     */
    public void asignarTutoria(Docente docente, Grupo grupo) {
        if (docente == null || grupo == null) throw new IllegalArgumentException("Docente y grupo son obligatorios");

        Docente tutorAnterior = grupo.getTutor();
        grupo.asignarTutor(docente);

        if (tutorAnterior != null && !tutorAnterior.equals(docente)) {
            repo.actualizar(tutorAnterior, tutorAnterior);
        }
        repoGrupos.actualizar(grupo, grupo);
        repo.actualizar(docente, docente);
    }
}
