package modelo.repositorio;
import static org.junit.jupiter.api.Assertions.*;
import modelo.Grado;
import modelo.Salon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
/**
 *
 * @author inici4rsesi0n
 */
public class ListaSalonesTest {

    private ListaSalones lista;
    private Grado grado1;
    private Grado grado2;
    private Salon salonA;
    private Salon salonB;
    private Salon salonC;

    @BeforeEach
    void setUp() {
        lista = new ListaSalones();
        grado1 = new Grado("1er Grado", "Primaria");
        grado2 = new Grado("2do Grado", "Primaria");
        salonA = new Salon("A", grado1, null, 30);
        salonB = new Salon("B", grado1, null, 25);
        salonC = new Salon("A", grado2, null, 20);
        lista.agregar(salonA);
        lista.agregar(salonB);
        lista.agregar(salonC);
    }

    @Test
    @DisplayName("agregar: aumenta el tamaño de la lista")
    void testAgregar() {
        assertEquals(3, lista.getLista().size());
        Salon nuevo = new Salon("C", grado1, null, 15);
        lista.agregar(nuevo);
        assertEquals(4, lista.getLista().size());
    }

    @Test
    @DisplayName("obtener: devuelve el salón en la posición dada")
    void testObtener() {
        Salon s = lista.obtener(0);
        assertEquals("A", s.getAula());
        assertEquals(grado1, s.getGrado());
    }

    @Test
    @DisplayName("obtener: lanza excepción si el índice no es válido")
    void testObtenerIndiceInvalido() {
        assertThrows(IndexOutOfBoundsException.class, () -> lista.obtener(5));
    }

    @Test
    @DisplayName("buscarPorGrado: devuelve solo los salones del grado indicado")
    void testBuscarPorGrado() {
        ArrayList<Salon> resultado = lista.buscarPorGrado("1er Grado");
        assertEquals(2, resultado.size());
        assertTrue(resultado.stream().allMatch(s -> s.getGrado().getNom().equals("1er Grado")));
    }

    @Test
    @DisplayName("buscarPorGrado: ignora mayúsculas/minúsculas")
    void testBuscarPorGradoCaseInsensitive() {
        ArrayList<Salon> resultado = lista.buscarPorGrado("2do grado");
        assertEquals(1, resultado.size());
        assertEquals("A", resultado.get(0).getAula());
    }

    @Test
    @DisplayName("buscarPorGrado: retorna lista vacía si no hay coincidencias")
    void testBuscarPorGradoSinResultados() {
        ArrayList<Salon> resultado = lista.buscarPorGrado("3er Grado");
        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("actualizar: reemplaza el salón en la posición indicada")
    void testActualizar() {
        Salon nuevo = new Salon("D", grado1, null, 10);
        lista.actualizar(1, nuevo);
        assertEquals("D", lista.obtener(1).getAula());
    }

    @Test
    @DisplayName("actualizar: lanza excepción si el índice no es válido")
    void testActualizarIndiceInvalido() {
        Salon nuevo = new Salon("D", grado1, null, 10);
        assertThrows(IndexOutOfBoundsException.class, () -> lista.actualizar(5, nuevo));
    }

    @Test
    @DisplayName("eliminar: reduce el tamaño de la lista")
    void testEliminar() {
        lista.eliminar(0);
        assertEquals(2, lista.getLista().size());
        assertEquals("B", lista.obtener(0).getAula()); // el siguiente se desplaza
    }

    @Test
    @DisplayName("eliminar: lanza excepción si el índice no es válido")
    void testEliminarIndiceInvalido() {
        assertThrows(IndexOutOfBoundsException.class, () -> lista.eliminar(5));
    }
}