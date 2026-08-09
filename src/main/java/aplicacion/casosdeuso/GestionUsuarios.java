package aplicacion.casosdeuso;
import dominio.modelo.*;
import dominio.puerto.externo.LoggerPort;
import dominio.puerto.repositorio.*;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author inici4rsesi0n
 */
@Service
public class GestionUsuarios {

    private final RepositorioAdministradores repoAdmin;
    private final RepositorioDirectores repoDir;
    private final RepositorioDocentes repoDoc;
    private final RepositorioEstudiantes repoEst;
    private final RepositorioSecretarios repoSec;
    private final RepositorioCoordinadores repoCoord;
    private final RepositorioPadres repoPad;
    private final LoggerPort logger;

    public GestionUsuarios(RepositorioAdministradores repoAdmin, RepositorioDirectores repoDir,
                           RepositorioDocentes repoDoc, RepositorioEstudiantes repoEst,
                           RepositorioSecretarios repoSec, RepositorioCoordinadores repoCoord,
                           RepositorioPadres repoPad, LoggerPort logger) {
        this.repoAdmin = repoAdmin;
        this.repoDir = repoDir;
        this.repoDoc = repoDoc;
        this.repoEst = repoEst;
        this.repoSec = repoSec;
        this.repoCoord = repoCoord;
        this.repoPad = repoPad;
        this.logger = logger;
    }

    public List<Usuario> listarTodos() {
        logger.debug("Listando todos los usuarios del sistema.");
        List<Usuario> todos = new ArrayList<>();
        todos.addAll(repoAdmin.listarTodos());
        todos.addAll(repoDir.listarTodos());
        todos.addAll(repoDoc.listarTodos());
        todos.addAll(repoEst.listarTodos());
        todos.addAll(repoSec.listarTodos());
        todos.addAll(repoCoord.listarTodos());
        todos.addAll(repoPad.listarTodos());
        logger.debug("Total de usuarios encontrados: {}", todos.size());
        return todos;
    }

    public Usuario buscarPorCodigo(String codigo) {
        Usuario u = repoAdmin.buscarPorCodigo(codigo).orElse(null);
        if (u == null) u = repoDir.buscarPorCodigo(codigo).orElse(null);
        if (u == null) u = repoDoc.buscarPorCodigo(codigo).orElse(null);
        if (u == null) u = repoEst.buscarPorCodigo(codigo).orElse(null);
        if (u == null) u = repoSec.buscarPorCodigo(codigo).orElse(null);
        if (u == null) u = repoCoord.buscarPorCodigo(codigo).orElse(null);
        if (u == null) u = repoPad.buscarPorCodigo(codigo).orElse(null);
        return u;
    }

    public boolean existeDni(String dni, Usuario excluir) {
        return listarTodos().stream().anyMatch(u -> u.getDni().equalsIgnoreCase(dni) && !u.equals(excluir));
    }

    public void agregarUsuario(String codigo, String hash, String dni, String nombre, String apellido,
                               int edad, Usuario.Rol rol, String especialidad) {
        validarUnicidad(codigo, dni, null);
        switch (rol) {
            case ADMINISTRADOR -> repoAdmin.agregar(new Administrador(codigo, hash, dni, nombre, apellido, edad));
            case DIRECTOR -> repoDir.agregar(new Director(codigo, hash, dni, nombre, apellido, edad));
            case SECRETARIO -> repoSec.agregar(new Secretario(codigo, hash, dni, nombre, apellido, edad));
            case COORDINADOR -> repoCoord.agregar(new CoordinadorAcademico(codigo, hash, dni, nombre, apellido, edad));
            case DOCENTE -> {
                String esp = (especialidad != null && !especialidad.isBlank()) ? especialidad : "Sin asignar";
                repoDoc.agregar(new Docente(codigo, hash, dni, nombre, apellido, edad, esp, new ArrayList<>()));
            }
            case ESTUDIANTE -> repoEst.agregar(new Estudiante(codigo, hash, dni, nombre, apellido, edad));
            case PADRE -> repoPad.agregar(new Padre(codigo, hash, dni, nombre, apellido, edad, new ArrayList<>()));
        }
        logger.info("Usuario {} con rol {} creado exitosamente.", codigo, rol);
    }

    public void actualizarUsuario(Usuario usuario) {
        switch (usuario.getRol()) {
            case ADMINISTRADOR -> repoAdmin.actualizar((Administrador) usuario, (Administrador) usuario);
            case DIRECTOR -> repoDir.actualizar((Director) usuario, (Director) usuario);
            case DOCENTE -> repoDoc.actualizar((Docente) usuario, (Docente) usuario);
            case ESTUDIANTE -> repoEst.actualizar((Estudiante) usuario, (Estudiante) usuario);
            case SECRETARIO -> repoSec.actualizar((Secretario) usuario, (Secretario) usuario);
            case COORDINADOR -> repoCoord.actualizar((CoordinadorAcademico) usuario, (CoordinadorAcademico) usuario);
            case PADRE -> repoPad.actualizar((Padre) usuario, (Padre) usuario);
        }
        logger.info("Usuario {} ({}) actualizado.", usuario.getCodigo(), usuario.getRol());
    }

    public void eliminarUsuario(Usuario usuario) {
        switch (usuario.getRol()) {
            case ADMINISTRADOR -> repoAdmin.eliminar((Administrador) usuario);
            case DIRECTOR -> repoDir.eliminar((Director) usuario);
            case DOCENTE -> repoDoc.eliminar((Docente) usuario);
            case ESTUDIANTE -> repoEst.eliminar((Estudiante) usuario);
            case SECRETARIO -> repoSec.eliminar((Secretario) usuario);
            case COORDINADOR -> repoCoord.eliminar((CoordinadorAcademico) usuario);
            case PADRE -> repoPad.eliminar((Padre) usuario);
        }
        logger.warn("Usuario {} ({}) eliminado del sistema.", usuario.getCodigo(), usuario.getRol());
    }

    private void validarUnicidad(String codigo, String dni, Usuario excluir) {
        if (buscarPorCodigo(codigo) != null) {
            throw new IllegalArgumentException("Ya existe un usuario con el código " + codigo);
        }
        if (existeDni(dni, excluir)) {
            throw new IllegalArgumentException("Ya existe un usuario con el DNI " + dni);
        }
    }
}