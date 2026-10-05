package aplicacion.casosdeuso;

import dominio.modelo.CoordinadorAcademico;
import dominio.puerto.externo.LoggerPort;
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
    private final LoggerPort logger;

    public GestionCoordinadores(RepositorioCoordinadores repo, LoggerPort logger) {
        this.repo = repo;
        this.logger = logger;
    }

    public void agregar(CoordinadorAcademico coordinador) {
        if (coordinador == null) throw new IllegalArgumentException("El coordinador no puede ser nulo");
        if (repo.buscarPorCodigo(coordinador.getCodigo()).isPresent()) {
            throw new IllegalArgumentException("Ya existe un coordinador con ese código");
        }
        repo.agregar(coordinador);
        logger.audit(String.format(
                "op=CREATE entity=COORDINADOR id=%s | nombre=%s %s",
                coordinador.getCodigo(), coordinador.getNombre(), coordinador.getApellido()));
    }

    public CoordinadorAcademico buscarPorCodigo(String codigo) {
        return repo.buscarPorCodigo(codigo).orElse(null);
    }

    public List<CoordinadorAcademico> listarTodos() {
        return repo.listarTodos();
    }

    public void actualizar(CoordinadorAcademico original, CoordinadorAcademico actualizado) {
        if (original == null || actualizado == null) {
            throw new IllegalArgumentException("Los coordinadores no pueden ser nulos");
        }
        String antes = original.toString();
        repo.actualizar(original, actualizado);
        logger.audit(String.format(
                "op=UPDATE entity=COORDINADOR id=%s | before=%s after=%s",
                original.getCodigo(), antes, actualizado));
    }

    public void eliminar(CoordinadorAcademico coordinador) {
        if (coordinador == null) throw new IllegalArgumentException("El coordinador no puede ser nulo");
        String snapshot = coordinador.toString();
        repo.eliminar(coordinador);
        logger.audit(String.format(
                "op=DELETE entity=COORDINADOR id=%s | snapshot=%s",
                coordinador.getCodigo(), snapshot));
    }
}
