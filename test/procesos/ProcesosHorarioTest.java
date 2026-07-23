package procesos;
import static org.junit.jupiter.api.Assertions.*;
import modelo.*;
import modelo.repositorio.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
/**
 *
 * @author inici4rsesi0n
 */
public class ProcesosHorarioTest {
    private ListaClases listaClases;
    private CatalogoGrados catalogoGrados;
    private ListaSalones listaSalones;
    private Grado grado;
    private Salon salon;
    private Docente docente;
    private Asignatura asignatura;

    @BeforeEach
    void setUp() {
        listaClases = new ListaClases();
        catalogoGrados = new CatalogoGrados();
        listaSalones = new ListaSalones();
        grado = new Grado("1er Grado", "Primaria");
        salon = new Salon("A", grado, null, 30);
        listaSalones.agregar(salon);
        docente = new Docente("Matematica", new ArrayList<>(), null, "D0001", "hash", "12345678", "Juan", "Perez", 35);
        asignatura = new Asignatura("Matematica");
    }

    @Test
    @DisplayName("validarEventoDatos: evento duplicado en el mismo salon retorna mensaje de error")
    void testValidarEventoDuplicadoSalon() {
        Clase existente = new Clase("Lunes", "08:00", "09:00", asignatura, docente, salon);
        listaClases.agregar(existente);

        String resultado = ProcesosHorario.validarEventoDatos(
                "Lunes", "08:00", "09:00", "A", "1er Grado", "D0001", listaClases
        );
        assertTrue(resultado.startsWith("Ya existe un evento"));
    }

    @Test
    @DisplayName("validarEventoDatos: docente duplicado en el mismo horario retorna mensaje de error")
    void testValidarEventoDocenteDuplicado() {
        Salon otroSalon = new Salon("B", grado, null, 30);
        listaSalones.agregar(otroSalon);
        Clase existente = new Clase("Lunes", "08:00", "09:00", asignatura, docente, otroSalon);
        listaClases.agregar(existente);

        String resultado = ProcesosHorario.validarEventoDatos(
                "Lunes", "08:00", "09:00", "A", "1er Grado", "D0001", listaClases
        );
        assertTrue(resultado.startsWith("El docente ya tiene una clase"));
    }

    @Test
    @DisplayName("validarEventoDatos: horario libre retorna null")
    void testValidarEventoLibre() {
        String resultado = ProcesosHorario.validarEventoDatos(
                "Lunes", "08:00", "09:00", "A", "1er Grado", "D0001", listaClases
        );
        assertNull(resultado);
    }
}