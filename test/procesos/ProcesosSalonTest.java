package procesos;
import static org.junit.jupiter.api.Assertions.*;
import modelo.Grado;
import modelo.Salon;
import modelo.repositorio.ListaSalones;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import modelo.Docente;
/**
 *
 * @author inici4rsesi0n
 */
public class ProcesosSalonTest {

    private ListaSalones listaSalones;

    @BeforeEach
    void setUp() {
        listaSalones = new ListaSalones();
    }

    @Test
    @DisplayName("validarDatosSalon: aula vacía retorna mensaje de error")
    void testValidarAulaVacia() {
        String resultado = ProcesosSalon.validarDatosSalon("  ", "1er Grado", "30", listaSalones);
        assertEquals("El nombre del salón no puede estar vacío.", resultado);
    }

    @Test
    @DisplayName("validarDatosSalon: grado nulo retorna mensaje de error")
    void testValidarGradoNulo() {
        String resultado = ProcesosSalon.validarDatosSalon("A", null, "30", listaSalones);
        assertEquals("Debe seleccionar un grado.", resultado);
    }

    @Test
    @DisplayName("validarDatosSalon: salón duplicado retorna mensaje de error")
    void testValidarSalonDuplicado() {
        Grado grado = new Grado("1er Grado", "Primaria");
        Salon existente = new Salon("A", grado, null, 25);
        listaSalones.agregar(existente);
        String resultado = ProcesosSalon.validarDatosSalon("A", "1er Grado", "30", listaSalones);
        assertTrue(resultado.startsWith("Ya existe el salón"));
    }

    @Test
    @DisplayName("validarDatosSalon: cupo máximo vacío retorna mensaje de error")
    void testValidarCupoMaxVacio() {
        String resultado = ProcesosSalon.validarDatosSalon("A", "1er Grado", "", listaSalones);
        assertEquals("El cupo máximo no puede estar vacío.", resultado);
    }

    @Test
    @DisplayName("validarDatosSalon: cupo máximo no numérico retorna mensaje de error")
    void testValidarCupoMaxNoNumerico() {
        String resultado = ProcesosSalon.validarDatosSalon("A", "1er Grado", "abc", listaSalones);
        assertEquals("El cupo máximo debe ser un número válido.", resultado);
    }

    @Test
    @DisplayName("validarDatosSalon: datos válidos retorna null")
    void testValidarDatosValidos() {
        String resultado = ProcesosSalon.validarDatosSalon("B", "1er Grado", "30", listaSalones);
        assertNull(resultado);
    }

    @Test
    @DisplayName("crearSalon: construye correctamente un objeto Salon")
    void testCrearSalon() {
        Grado grado = new Grado("2do Grado", "Primaria");
        Docente tutor = new Docente("Matemática", new ArrayList<>(), null,
                                    "D0001", "hash", "12345678", "Juan", "Pérez", 35);
        Salon salon = ProcesosSalon.crearSalon("A", 30, grado, tutor);
        assertEquals("A", salon.getAula());
        assertEquals(30, salon.getCupoMax());
        assertEquals(grado, salon.getGrado());
        assertEquals(tutor, salon.getTutor());
    }
}