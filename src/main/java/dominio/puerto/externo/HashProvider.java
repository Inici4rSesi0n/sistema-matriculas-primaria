package dominio.puerto.externo;
/**
 *
 * @author inici4rsesi0n
 */
public interface HashProvider {
    String generarHash(char[] contraseña);
    boolean verificarHash(String hash, char[] contraseña);
}