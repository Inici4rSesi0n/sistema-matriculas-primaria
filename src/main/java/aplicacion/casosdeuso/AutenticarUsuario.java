package aplicacion.casosdeuso;

import dominio.modelo.Usuario;
import dominio.puerto.externo.HashProvider;
import dominio.puerto.externo.LoggerPort;
import dominio.puerto.repositorio.*;
import org.springframework.stereotype.Service;

/**
 *
 * @author inici4rsesi0n
 */
@Service
public class AutenticarUsuario {

    private final RepositorioAdministradores repoAdmin;
    private final RepositorioDirectores repoDir;
    private final RepositorioDocentes repoDoc;
    private final RepositorioEstudiantes repoEst;
    private final RepositorioSecretarios repoSec;
    private final RepositorioCoordinadores repoCoord;
    private final RepositorioPadres repoPad;
    private final HashProvider hashProvider;
    private final LoggerPort logger;

    public AutenticarUsuario(RepositorioAdministradores repoAdmin,
                             RepositorioDirectores repoDir,
                             RepositorioDocentes repoDoc,
                             RepositorioEstudiantes repoEst,
                             RepositorioSecretarios repoSec,
                             RepositorioCoordinadores repoCoord,
                             RepositorioPadres repoPad,
                             HashProvider hashProvider,
                             LoggerPort logger) {
        this.repoAdmin = repoAdmin;
        this.repoDir = repoDir;
        this.repoDoc = repoDoc;
        this.repoEst = repoEst;
        this.repoSec = repoSec;
        this.repoCoord = repoCoord;
        this.repoPad = repoPad;
        this.hashProvider = hashProvider;
        this.logger = logger;
    }

    public Usuario ejecutar(String codigo, char[] contraseña) {
        if (codigo == null || codigo.isBlank()) {
            logger.warn("Intento de autenticación con código vacío.");
            throw new IllegalArgumentException("El código no puede estar vacío.");
        }
        if (contraseña == null) {
            logger.warn("Intento de autenticación con contraseña nula para código: {}", codigo);
            throw new IllegalArgumentException("La contraseña no puede ser nula.");
        }

        Usuario usuario = null;

        usuario = repoAdmin.buscarPorCodigo(codigo).orElse(null);
        if (usuario == null) usuario = repoDir.buscarPorCodigo(codigo).orElse(null);
        if (usuario == null) usuario = repoDoc.buscarPorCodigo(codigo).orElse(null);
        if (usuario == null) usuario = repoEst.buscarPorCodigo(codigo).orElse(null);
        if (usuario == null) usuario = repoSec.buscarPorCodigo(codigo).orElse(null);
        if (usuario == null) usuario = repoCoord.buscarPorCodigo(codigo).orElse(null);
        if (usuario == null) usuario = repoPad.buscarPorCodigo(codigo).orElse(null);

        if (usuario != null && hashProvider.verificarHash(usuario.getHashContrasena(), contraseña)) {
            logger.info("Usuario {} autenticado exitosamente.", codigo);
            return usuario;
        }

        if (usuario != null) {
            logger.warn("Contraseña incorrecta para usuario: {}", codigo);
        } else {
            logger.warn("Código de usuario no encontrado: {}", codigo);
        }
        return null;
    }
}