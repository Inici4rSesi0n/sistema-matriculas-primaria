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
public class ProcesosDocenteTest {

    private ListaDocentes listaDocentes;
    private ListaEstudiantes listaEstudiantes;
    private GestorDirector gestorDirector;

    @BeforeEach
    void setUp() {
        listaDocentes = new ListaDocentes();
        listaEstudiantes = new ListaEstudiantes();
        gestorDirector = new GestorDirector();
    }

    @Test
    @DisplayName("validarDatosDocente: código vacío retorna mensaje de error")
    void testValidarCodigoVacio() {
        String resultado = ProcesosDocente.validarDatosDocente("", "12345678", "Juan", "Pérez", "35", "admin123".toCharArray(), "Matemática", listaDocentes, listaEstudiantes, gestorDirector);
        assertEquals("El código no puede estar vacío.", resultado);
    }

    @Test
    @DisplayName("validarDatosDocente: código duplicado en docentes retorna mensaje de error")
    void testValidarCodigoDuplicadoDocente() {
        listaDocentes.agregar(ProcesosDocente.crearDocente("Matemática", "D0001", "hash", "12345678", "Juan", "Pérez", 35));
        String resultado = ProcesosDocente.validarDatosDocente("D0001", "12345678", "Juan", "Pérez", "35", "admin123".toCharArray(), "Matemática", listaDocentes, listaEstudiantes, gestorDirector);
        assertEquals("El código ya existe.", resultado);
    }

    @Test
    @DisplayName("validarDatosDocente: DNI vacío retorna mensaje de error")
    void testValidarDniVacio() {
        String resultado = ProcesosDocente.validarDatosDocente("D0001", "", "Juan", "Pérez", "35", "admin123".toCharArray(), "Matemática", listaDocentes, listaEstudiantes, gestorDirector);
        assertEquals("El DNI no puede estar vacío.", resultado);
    }

    @Test
    @DisplayName("validarDatosDocente: nombre vacío retorna mensaje de error")
    void testValidarNombreVacio() {
        String resultado = ProcesosDocente.validarDatosDocente("D0001", "12345678", "", "Pérez", "35", "admin123".toCharArray(), "Matemática", listaDocentes, listaEstudiantes, gestorDirector);
        assertEquals("El nombre no puede estar vacío.", resultado);
    }

    @Test
    @DisplayName("validarDatosDocente: apellido vacío retorna mensaje de error")
    void testValidarApellidoVacio() {
        String resultado = ProcesosDocente.validarDatosDocente("D0001", "12345678", "Juan", "", "35", "admin123".toCharArray(), "Matemática", listaDocentes, listaEstudiantes, gestorDirector);
        assertEquals("El apellido no puede estar vacío.", resultado);
    }

    @Test
    @DisplayName("validarDatosDocente: edad vacía retorna mensaje de error")
    void testValidarEdadVacia() {
        String resultado = ProcesosDocente.validarDatosDocente("D0001", "12345678", "Juan", "Pérez", "", "admin123".toCharArray(), "Matemática", listaDocentes, listaEstudiantes, gestorDirector);
        assertEquals("La edad no puede estar vacía.", resultado);
    }

    @Test
    @DisplayName("validarDatosDocente: edad no numérica retorna mensaje de error")
    void testValidarEdadNoNumerica() {
        String resultado = ProcesosDocente.validarDatosDocente("D0001", "12345678", "Juan", "Pérez", "abc", "admin123".toCharArray(), "Matemática", listaDocentes, listaEstudiantes, gestorDirector);
        assertEquals("La edad debe ser un número válido.", resultado);
    }

    @Test
    @DisplayName("validarDatosDocente: contraseña vacía retorna mensaje de error")
    void testValidarPasswordVacio() {
        String resultado = ProcesosDocente.validarDatosDocente("D0001", "12345678", "Juan", "Pérez", "35", new char[0], "Matemática", listaDocentes, listaEstudiantes, gestorDirector);
        assertEquals("La contraseña no puede estar vacía.", resultado);
    }

    @Test
    @DisplayName("validarDatosDocente: especialidad nula retorna mensaje de error")
    void testValidarEspecialidadNula() {
        String resultado = ProcesosDocente.validarDatosDocente("D0001", "12345678", "Juan", "Pérez", "35", "admin123".toCharArray(), null, listaDocentes, listaEstudiantes, gestorDirector);
        assertEquals("Debe seleccionar una especialidad.", resultado);
    }

    @Test
    @DisplayName("validarDatosDocente: datos válidos retorna null")
    void testValidarDatosValidos() {
        String resultado = ProcesosDocente.validarDatosDocente("D0001", "12345678", "Juan", "Pérez", "35", "admin123".toCharArray(), "Matemática", listaDocentes, listaEstudiantes, gestorDirector);
        assertNull(resultado);
    }
}