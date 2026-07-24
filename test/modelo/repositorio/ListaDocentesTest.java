package modelo.repositorio;
import static org.junit.jupiter.api.Assertions.*;
import modelo.Docente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
/**
 *
 * @author inici4rsesi0n
 */
public class ListaDocentesTest {

    private ListaDocentes lista;
    private Docente docente1;
    private Docente docente2;

    @BeforeEach
    void setUp() {
        lista = new ListaDocentes();
        docente1 = new Docente("Matemática", new ArrayList<>(), null, "D0001", "hash1", "11111111", "Juan", "Pérez", 35);
        docente2 = new Docente("Comunicación", new ArrayList<>(), null, "D0002", "hash2", "22222222", "María", "López", 42);
        lista.agregar(docente1);
        lista.agregar(docente2);
    }

    @Test
    @DisplayName("agregar: aumenta el tamaño de la lista")
    void testAgregar() {
        Docente nuevo = new Docente("Inglés", new ArrayList<>(), null, "D0003", "hash3", "33333333", "Rosa", "Díaz", 29);
        lista.agregar(nuevo);
        assertEquals(3, lista.getLista().size());
    }

    @Test
    @DisplayName("obtener: devuelve el docente en la posición dada")
    void testObtener() {
        Docente d = lista.obtener(0);
        assertEquals("D0001", d.getCod());
    }

    @Test
    @DisplayName("obtener: lanza excepción si el índice no es válido")
    void testObtenerIndiceInvalido() {
        assertThrows(IndexOutOfBoundsException.class, () -> lista.obtener(5));
    }

    @Test
    @DisplayName("buscarPorDNI: encuentra un docente existente")
    void testBuscarPorDniExistente() {
        int pos = lista.buscarPorDNI("22222222");
        assertEquals(1, pos);
    }

    @Test
    @DisplayName("buscarPorDNI: retorna -1 si no existe")
    void testBuscarPorDniInexistente() {
        int pos = lista.buscarPorDNI("99999999");
        assertEquals(-1, pos);
    }

    @Test
    @DisplayName("buscarPorCODIGO: encuentra un docente existente")
    void testBuscarPorCodigoExistente() {
        int pos = lista.buscarPorCODIGO("D0002");
        assertEquals(1, pos);
    }

    @Test
    @DisplayName("buscarPorCODIGO: retorna -1 si no existe")
    void testBuscarPorCodigoInexistente() {
        int pos = lista.buscarPorCODIGO("D9999");
        assertEquals(-1, pos);
    }

    @Test
    @DisplayName("actualizar: reemplaza el docente en la posición indicada")
    void testActualizar() {
        Docente nuevo = new Docente("Arte", new ArrayList<>(), null, "D0004", "hash4", "44444444", "Luis", "Torres", 45);
        lista.actualizar(0, nuevo);
        assertEquals("D0004", lista.obtener(0).getCod());
    }

    @Test
    @DisplayName("actualizar: lanza excepción si el índice no es válido")
    void testActualizarIndiceInvalido() {
        Docente nuevo = new Docente("Arte", new ArrayList<>(), null, "D0004", "hash4", "44444444", "Luis", "Torres", 45);
        assertThrows(IndexOutOfBoundsException.class, () -> lista.actualizar(5, nuevo));
    }

    @Test
    @DisplayName("eliminar: reduce el tamaño de la lista")
    void testEliminar() {
        lista.eliminar(0);
        assertEquals(1, lista.getLista().size());
        assertEquals("D0002", lista.obtener(0).getCod());
    }

    @Test
    @DisplayName("eliminar: lanza excepción si el índice no es válido")
    void testEliminarIndiceInvalido() {
        assertThrows(IndexOutOfBoundsException.class, () -> lista.eliminar(5));
    }
}