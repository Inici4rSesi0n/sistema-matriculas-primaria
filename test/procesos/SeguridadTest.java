package procesos;
/**
 *
 * @author inici4rsesi0n
 */
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class SeguridadTest {

    @Test
    @DisplayName("generarHash devuelve formato salt:hash")
    void testGenerarHashFormato() {
        char[] password = "admin123".toCharArray();
        String resultado = Seguridad.generarHash(password);
        
        assertNotNull(resultado);
        assertTrue(resultado.contains(":"), "El hash debe contener ':'");
    }

    @Test
    @DisplayName("verificarHash acepta contraseña correcta")
    void testVerificarHashCorrecto() {
        char[] password = "admin123".toCharArray();
        String hash = Seguridad.generarHash(password);
        
        assertTrue(Seguridad.verificarHash(hash, password));
    }

    @Test
    @DisplayName("verificarHash rechaza contraseña incorrecta")
    void testVerificarHashIncorrecto() {
        char[] password = "admin123".toCharArray();
        String hash = Seguridad.generarHash(password);
        
        assertFalse(Seguridad.verificarHash(hash, "wrongpass".toCharArray()));
    }

    @Test
    @DisplayName("limpiarContraseña sobrescribe el array con ceros")
    void testLimpiarContraseña() {
        char[] password = "admin123".toCharArray();
        Seguridad.limpiarContraseña(password);
        
        for (char c : password) {
            assertEquals('\0', c);
        }
    }
}