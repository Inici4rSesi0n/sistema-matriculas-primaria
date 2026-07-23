package procesos;
import static org.junit.jupiter.api.Assertions.*;
import modelo.Director;
import modelo.Docente;
import modelo.Estudiante;
import modelo.repositorio.GestorDirector;
import modelo.repositorio.ListaDocentes;
import modelo.repositorio.ListaEstudiantes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
/**
 *
 * @author inici4rsesi0n
 */
public class ProcesosLoginTest {

    private GestorDirector gestorDirector;
    private ListaDocentes listaDocentes;
    private ListaEstudiantes listaEstudiantes;

    @BeforeEach
    void setUp() {
        listaDocentes = new ListaDocentes();
        listaEstudiantes = new ListaEstudiantes();
        gestorDirector = new GestorDirector();
    }

    @Test
    @DisplayName("validarCredenciales: código vacío retorna mensaje de error")
    void testValidarCredencialesCodigoVacio() {
        String resultado = ProcesosLogin.validarCredenciales("", "admin123".toCharArray());
        assertEquals("Ingrese su código.", resultado);
    }

    @Test
    @DisplayName("validarCredenciales: contraseña vacía retorna mensaje de error")
    void testValidarCredencialesPasswordVacio() {
        String resultado = ProcesosLogin.validarCredenciales("C22251227", new char[0]);
        assertEquals("Ingrese su contraseña.", resultado);
    }

    @Test
    @DisplayName("validarCredenciales: credenciales válidas retorna null")
    void testValidarCredencialesValidas() {
        String resultado = ProcesosLogin.validarCredenciales("C22251227", "admin123".toCharArray());
        assertNull(resultado);
    }

    @Test
    @DisplayName("autenticar: director con credenciales correctas retorna Director")
    void testAutenticarDirectorCorrecto() {
        char[] password = "admin123".toCharArray();
        modelo.Usuario usuario = ProcesosLogin.autenticar("C22251227", password, gestorDirector, listaDocentes, listaEstudiantes);
        assertNotNull(usuario);
        assertTrue(usuario instanceof Director);
    }

    @Test
    @DisplayName("autenticar: docente registrado con credenciales correctas retorna Docente")
    void testAutenticarDocenteCorrecto() {
        char[] password = "docente123".toCharArray();
        String hash = Seguridad.generarHash(password);
        Docente docente = new Docente("Matemática", new ArrayList<>(), null, "D0001", hash, "12345678", "Juan", "Pérez", 35);
        listaDocentes.agregar(docente);

        modelo.Usuario usuario = ProcesosLogin.autenticar("D0001", password, gestorDirector, listaDocentes, listaEstudiantes);
        assertNotNull(usuario);
        assertTrue(usuario instanceof Docente);
    }

    @Test
    @DisplayName("autenticar: estudiante registrado con credenciales correctas retorna Estudiante")
    void testAutenticarEstudianteCorrecto() {
        char[] password = "estudiante123".toCharArray();
        String hash = Seguridad.generarHash(password);
        Estudiante estudiante = new Estudiante(null, null, "E0001", hash, "87654321", "María", "López", 8);
        listaEstudiantes.agregar(estudiante);

        modelo.Usuario usuario = ProcesosLogin.autenticar("E0001", password, gestorDirector, listaDocentes, listaEstudiantes);
        assertNotNull(usuario);
        assertTrue(usuario instanceof Estudiante);
    }

    @Test
    @DisplayName("autenticar: código inexistente retorna null")
    void testAutenticarCodigoInexistente() {
        modelo.Usuario usuario = ProcesosLogin.autenticar("Z9999", "admin123".toCharArray(), gestorDirector, listaDocentes, listaEstudiantes);
        assertNull(usuario);
    }

    @Test
    @DisplayName("autenticar: contraseña incorrecta retorna null")
    void testAutenticarPasswordIncorrecto() {
        modelo.Usuario usuario = ProcesosLogin.autenticar("C22251227", "wrongpass".toCharArray(), gestorDirector, listaDocentes, listaEstudiantes);
        assertNull(usuario);
    }
}