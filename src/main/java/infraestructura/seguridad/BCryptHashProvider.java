package infraestructura.seguridad;
import dominio.puerto.externo.HashProvider;
import org.mindrot.jbcrypt.BCrypt;
/**
 *
 * @author inici4rsesi0n
 */
public class BCryptHashProvider implements HashProvider {
    private static final int FACTOR_COSTO = 12;
    @Override
    public String generarHash(char[] contraseña) {
        if (contraseña == null) return null;
        String hash = BCrypt.hashpw(new String(contraseña), BCrypt.gensalt(FACTOR_COSTO));
        UtilLimpieza.limpiarContraseña(contraseña);
        return hash;
    }
    @Override
    public boolean verificarHash(String hashAlmacenado, char[] contraseña) {
        if (hashAlmacenado == null || contraseña == null) return false;
        try {
            return BCrypt.checkpw(new String(contraseña), hashAlmacenado);
        } catch (IllegalArgumentException e) {
            return false;
        } finally {
            UtilLimpieza.limpiarContraseña(contraseña);
        }
    }
}