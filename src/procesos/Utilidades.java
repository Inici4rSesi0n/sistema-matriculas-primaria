package procesos;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
/**
 *
 * @author inici4rsesi0n
 */
public class Utilidades {
    public static String generarHash(char[] contraseña){
        if(contraseña==null)return null;
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            //convertir char[] a byte[] UTF-8
            StringBuilder sb = new StringBuilder();
            for(char c:contraseña){
                sb.append(c);
            }
            byte[] hashBytes = digest.digest(sb.toString().getBytes
        (java.nio.charset.StandardCharsets.UTF_8));
            //convertir bytes a hexadecimal
            StringBuilder hexString = new StringBuilder();
            for(byte b:hashBytes){
                String hex = Integer.toHexString(0xff & b);
                if(hex.length()==1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch(NoSuchAlgorithmException e){
            throw new RuntimeException("Algoritmo SHA-256 no disponible", e);
        }
    }
    public static void limpiarContraseña(char[] pwd) {
        if (pwd != null) {java.util.Arrays.fill(pwd, '\0');}
    }
}
