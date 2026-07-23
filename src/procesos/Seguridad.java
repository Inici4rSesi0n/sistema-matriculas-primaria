package procesos;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;
/**
 *
 * @author inici4rsesi0n
 */
public class Seguridad {
    private static final SecureRandom random = new SecureRandom();

    private static String generarSalt() {
        byte[] saltBytes = new byte[16];
        random.nextBytes(saltBytes);
        return Base64.getEncoder().encodeToString(saltBytes);
    }
    private static String hashConSalt(String salt, char[] contraseña) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            String input = salt + new String(contraseña);
            byte[] hashBytes = digest.digest(input.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 no disponible", e);
        }
    }

    public static String generarHash(char[] contraseña) {
        if (contraseña == null) return null;
        String salt = generarSalt();
        return salt + ":" + hashConSalt(salt, contraseña);
    }
    public static boolean verificarHash(String hashAlmacenado, char[] contraseña) {
        if (hashAlmacenado == null || contraseña == null) return false;
        int pos = hashAlmacenado.indexOf(':');
        String salt = pos != -1 ? hashAlmacenado.substring(0, pos) : "";
        String hashOriginal = pos != -1 ? hashAlmacenado.substring(pos + 1) : hashAlmacenado;
        String hashCalculado = pos != -1 ? hashConSalt(salt, contraseña) : hashSinSalt(contraseña);
        return hashOriginal.equals(hashCalculado);
    }
    private static String hashSinSalt(char[] contraseña) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            String input = new String(contraseña);
            byte[] hashBytes = digest.digest(input.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 no disponible", e);
        }
    }
    public static void limpiarContraseña(char[] pwd) {
        if (pwd != null) {
            Arrays.fill(pwd, '\0');
        }
    }
}