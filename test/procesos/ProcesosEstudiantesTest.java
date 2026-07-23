package procesos;
import static org.junit.jupiter.api.Assertions.*;
import modelo.repositorio.GestorDirector;
import modelo.repositorio.ListaDocentes;
import modelo.repositorio.ListaEstudiantes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
/**
 *
 * @author inici4rsesi0n
 */
public class ProcesosEstudiantesTest {

    private ListaEstudiantes listaEstudiantes;
    private ListaDocentes listaDocentes;
    private GestorDirector gestorDirector;

    @BeforeEach
    void setUp() {
        listaEstudiantes = new ListaEstudiantes();
        listaDocentes = new ListaDocentes();
        gestorDirector = new GestorDirector();
    }

    @Test
    @DisplayName("validarDatosEstudiante: código vacío retorna mensaje de error")
    void testValidarCodigoVacio() {
        String resultado = ProcesosEstudiantes.validarDatosEstudiante("", "12345678", "Juan", "Pérez", "8", "admin123".toCharArray(), "1er Grado", "A", listaEstudiantes, listaDocentes, gestorDirector);
        assertEquals("El código no puede estar vacío.", resultado);
    }

    @Test
    @DisplayName("validarDatosEstudiante: código duplicado en estudiantes retorna mensaje de error")
    void testValidarCodigoDuplicadoEstudiante() {
        listaEstudiantes.agregar(ProcesosEstudiantes.crearEstudiante(null, null, "E0001", "hash", "12345678", "Juan", "Pérez", 8));
        String resultado = ProcesosEstudiantes.validarDatosEstudiante("E0001", "12345678", "Juan", "Pérez", "8", "admin123".toCharArray(), "1er Grado", "A", listaEstudiantes, listaDocentes, gestorDirector);
        assertEquals("El código ya existe.", resultado);
    }

    @Test
    @DisplayName("validarDatosEstudiante: DNI vacío retorna mensaje de error")
    void testValidarDniVacio() {
        String resultado = ProcesosEstudiantes.validarDatosEstudiante("E0001", "", "Juan", "Pérez", "8", "admin123".toCharArray(), "1er Grado", "A", listaEstudiantes, listaDocentes, gestorDirector);
        assertEquals("El DNI no puede estar vacío.", resultado);
    }

    @Test
    @DisplayName("validarDatosEstudiante: nombre vacío retorna mensaje de error")
    void testValidarNombreVacio() {
        String resultado = ProcesosEstudiantes.validarDatosEstudiante("E0001", "12345678", "", "Pérez", "8", "admin123".toCharArray(), "1er Grado", "A", listaEstudiantes, listaDocentes, gestorDirector);
        assertEquals("El nombre no puede estar vacío.", resultado);
    }

    @Test
    @DisplayName("validarDatosEstudiante: apellido vacío retorna mensaje de error")
    void testValidarApellidoVacio() {
        String resultado = ProcesosEstudiantes.validarDatosEstudiante("E0001", "12345678", "Juan", "", "8", "admin123".toCharArray(), "1er Grado", "A", listaEstudiantes, listaDocentes, gestorDirector);
        assertEquals("El apellido no puede estar vacío.", resultado);
    }

    @Test
    @DisplayName("validarDatosEstudiante: edad vacía retorna mensaje de error")
    void testValidarEdadVacia() {
        String resultado = ProcesosEstudiantes.validarDatosEstudiante("E0001", "12345678", "Juan", "Pérez", "", "admin123".toCharArray(), "1er Grado", "A", listaEstudiantes, listaDocentes, gestorDirector);
        assertEquals("La edad no puede estar vacía.", resultado);
    }

    @Test
    @DisplayName("validarDatosEstudiante: edad no numérica retorna mensaje de error")
    void testValidarEdadNoNumerica() {
        String resultado = ProcesosEstudiantes.validarDatosEstudiante("E0001", "12345678", "Juan", "Pérez", "abc", "admin123".toCharArray(), "1er Grado", "A", listaEstudiantes, listaDocentes, gestorDirector);
        assertEquals("La edad debe ser un número válido.", resultado);
    }

    @Test
    @DisplayName("validarDatosEstudiante: contraseña vacía retorna mensaje de error")
    void testValidarPasswordVacio() {
        String resultado = ProcesosEstudiantes.validarDatosEstudiante("E0001", "12345678", "Juan", "Pérez", "8", new char[0], "1er Grado", "A", listaEstudiantes, listaDocentes, gestorDirector);
        assertEquals("La contraseña no puede estar vacía.", resultado);
    }

    @Test
    @DisplayName("validarDatosEstudiante: grado no seleccionado retorna mensaje de error")
    void testValidarGradoNulo() {
        String resultado = ProcesosEstudiantes.validarDatosEstudiante("E0001", "12345678", "Juan", "Pérez", "8", "admin123".toCharArray(), null, "A", listaEstudiantes, listaDocentes, gestorDirector);
        assertEquals("Debe seleccionar un grado.", resultado);
    }

    @Test
    @DisplayName("validarDatosEstudiante: salón no seleccionado retorna mensaje de error")
    void testValidarSalonNulo() {
        String resultado = ProcesosEstudiantes.validarDatosEstudiante("E0001", "12345678", "Juan", "Pérez", "8", "admin123".toCharArray(), "1er Grado", null, listaEstudiantes, listaDocentes, gestorDirector);
        assertEquals("Debe seleccionar un salón.", resultado);
    }

    @Test
    @DisplayName("validarDatosEstudiante: datos válidos retorna null")
    void testValidarDatosValidos() {
        String resultado = ProcesosEstudiantes.validarDatosEstudiante("E0001", "12345678", "Juan", "Pérez", "8", "admin123".toCharArray(), "1er Grado", "A", listaEstudiantes, listaDocentes, gestorDirector);
        assertNull(resultado);
    }
}