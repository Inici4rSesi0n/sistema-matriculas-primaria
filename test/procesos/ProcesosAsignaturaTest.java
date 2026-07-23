package procesos;
import static org.junit.jupiter.api.Assertions.*;
import modelo.repositorio.ListaAsignaturas;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
/**
 *
 * @author inici4rsesi0n
 */
public class ProcesosAsignaturaTest {
    private ListaAsignaturas lista;
    @BeforeEach
    void setUp() {
        lista = new ListaAsignaturas();
    }
    @Test
    @DisplayName("validarDatosAsignatura: nombre vacío retorna mensaje de error")
    void testValidarNombreVacio() {
        String resultado = ProcesosAsignatura.validarDatosAsignatura("", lista);
        assertEquals("El nombre de la asignatura no puede estar vacío.", resultado);
    }
    @Test
    @DisplayName("validarDatosAsignatura: nombre duplicado retorna mensaje de error")
    void testValidarNombreDuplicado() {
        lista.agregar(ProcesosAsignatura.crearAsignatura("Matemática"));
        String resultado = ProcesosAsignatura.validarDatosAsignatura("Matemática", lista);
        assertTrue(resultado.startsWith("La asignatura 'Matemática' ya existe."));
    }
    @Test
    @DisplayName("validarDatosAsignatura: nombre válido retorna null")
    void testValidarNombreValido() {
        String resultado = ProcesosAsignatura.validarDatosAsignatura("Comunicación", lista);
        assertNull(resultado);
    }
    @Test
    @DisplayName("crearAsignatura: construye correctamente un objeto Asignatura")
    void testCrearAsignatura() {
        modelo.Asignatura a = ProcesosAsignatura.crearAsignatura("Inglés");
        assertNotNull(a);
        assertEquals("Inglés", a.getNom());
    }
}