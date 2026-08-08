package aplicacion.casosdeuso;
import dominio.modelo.Usuario;
import dominio.puerto.externo.HashProvider;
import dominio.puerto.repositorio.*;
/**
 *
 * @author inici4rsesi0n
 */
public class AutenticarUsuario {

    private final RepositorioAdministradores repoAdmin;
    private final RepositorioDirectores repoDir;
    private final RepositorioDocentes repoDoc;
    private final RepositorioEstudiantes repoEst;
    private final RepositorioSecretarios repoSec;
    private final RepositorioCoordinadores repoCoord;
    private final RepositorioPadres repoPad;
    private final HashProvider hashProvider;

    public AutenticarUsuario(RepositorioAdministradores repoAdmin,
                             RepositorioDirectores repoDir,
                             RepositorioDocentes repoDoc,
                             RepositorioEstudiantes repoEst,
                             RepositorioSecretarios repoSec,
                             RepositorioCoordinadores repoCoord,
                             RepositorioPadres repoPad,
                             HashProvider hashProvider) {
        this.repoAdmin = repoAdmin;
        this.repoDir = repoDir;
        this.repoDoc = repoDoc;
        this.repoEst = repoEst;
        this.repoSec = repoSec;
        this.repoCoord = repoCoord;
        this.repoPad = repoPad;
        this.hashProvider = hashProvider;
    }

    public Usuario ejecutar(String codigo, char[] contraseña) {
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("El código no puede estar vacío.");
        }
        if (contraseña == null) {
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
            return usuario;
        }
        return null;
    }
}