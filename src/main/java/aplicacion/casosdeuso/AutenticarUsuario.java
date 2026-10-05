package aplicacion.casosdeuso;

import aplicacion.servicio.BuscadorUsuario;
import dominio.modelo.Usuario;
import dominio.puerto.externo.HashProvider;
import dominio.puerto.externo.LoggerPort;
import org.springframework.stereotype.Service;

/**
 *
 * @author inici4rsesi0n
 */
@Service
public class AutenticarUsuario {

    private final BuscadorUsuario buscadorUsuario;
    private final HashProvider hashProvider;
    private final LoggerPort logger;

    public AutenticarUsuario(BuscadorUsuario buscadorUsuario,
                             HashProvider hashProvider,
                             LoggerPort logger) {
        this.buscadorUsuario = buscadorUsuario;
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

        Usuario usuario = buscadorUsuario.buscarPorCodigo(codigo);

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
