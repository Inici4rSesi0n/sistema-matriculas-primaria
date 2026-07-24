package modelo.repositorio;
import static org.junit.jupiter.api.Assertions.*;
import modelo.Asignatura;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
/**
 *
 * @author inici4rsesi0n
 */
public class ListaAsignaturasTest {

    private ListaAsignaturas lista;
    private Asignatura asignatura1;
    private Asignatura asignatura2;

    @BeforeEach
    void setUp() {
        lista = new ListaAsignaturas();
        asignatura1 = new Asignatura("Matemática");
        asignatura2 = new Asignatura("Comunicación");
        lista.agregar(asignatura1);
        lista.agregar(asignatura2);
    }

    @Test
    @DisplayName("agregar: aumenta el tamaño de la lista")
    void testAgregar() {
        Asignatura nueva = new Asignatura("Inglés");
        lista.agregar(nueva);
        assertEquals(3, lista.getLista().size());
    }

    @Test
    @DisplayName("obtener: devuelve la asignatura en la posición dada")
    void testObtener() {
        Asignatura a = lista.obtener(0);
        assertEquals("Matemática", a.getNom());
    }

    @Test
    @DisplayName("obtener: lanza excepción si el índice no es válido")
    void testObtenerIndiceInvalido() {
        assertThrows(IndexOutOfBoundsException.class, () -> lista.obtener(5));
    }

    @Test
    @DisplayName("buscarPorNombre: encuentra una asignatura existente")
    void testBuscarPorNombreExistente() {
        int pos = lista.buscarPorNombre("Comunicación");
        assertEquals(1, pos);
    }

    @Test
    @DisplayName("buscarPorNombre: retorna -1 si no existe")
    void testBuscarPorNombreInexistente() {
        int pos = lista.buscarPorNombre("Física");
        assertEquals(-1, pos);
    }

    @Test
    @DisplayName("buscarPorNombre: ignora mayúsculas/minúsculas")
    void testBuscarPorNombreCaseInsensitive() {
        int pos = lista.buscarPorNombre("matemática");
        assertEquals(0, pos);
    }

    @Test
    @DisplayName("actualizar: reemplaza la asignatura en la posición indicada")
    void testActualizar() {
        Asignatura nueva = new Asignatura("Arte");
        lista.actualizar(0, nueva);
        assertEquals("Arte", lista.obtener(0).getNom());
    }

    @Test
    @DisplayName("actualizar: lanza excepción si el índice no es válido")
    void testActualizarIndiceInvalido() {
        Asignatura nueva = new Asignatura("Arte");
        assertThrows(IndexOutOfBoundsException.class, () -> lista.actualizar(5, nueva));
    }

    @Test
    @DisplayName("eliminar: reduce el tamaño de la lista")
    void testEliminar() {
        lista.eliminar(0);
        assertEquals(1, lista.getLista().size());
        assertEquals("Comunicación", lista.obtener(0).getNom());
    }

    @Test
    @DisplayName("eliminar: lanza excepción si el índice no es válido")
    void testEliminarIndiceInvalido() {
        assertThrows(IndexOutOfBoundsException.class, () -> lista.eliminar(5));
    }
}