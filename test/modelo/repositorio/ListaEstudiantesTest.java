package modelo.repositorio;
import static org.junit.jupiter.api.Assertions.*;
import modelo.Estudiante;
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
public class ListaEstudiantesTest {

    private ListaEstudiantes lista;
    private Estudiante est1;
    private Estudiante est2;
    private Estudiante est3;
    private Grado grado1;
    private Grado grado2;

    @BeforeEach
    void setUp() {
        lista = new ListaEstudiantes();
        grado1 = new Grado("1er Grado", "Primaria");
        grado2 = new Grado("2do Grado", "Primaria");
        Salon salonA = new Salon("A", grado1, null, 30);
        Salon salonB = new Salon("B", grado2, null, 25);
        est1 = new Estudiante(salonA, grado1, "E001", "hash1", "11111111", "Ana", "Pérez", 7);
        est2 = new Estudiante(salonA, grado1, "E002", "hash2", "22222222", "Luis", "Gómez", 8);
        est3 = new Estudiante(salonB, grado2, "E003", "hash3", "33333333", "Mía", "Rojas", 7);
        lista.agregar(est1);
        lista.agregar(est2);
        lista.agregar(est3);
    }

    @Test
    @DisplayName("buscarPorDNI: encuentra un estudiante existente")
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
    @DisplayName("buscarPorCODIGO: encuentra un estudiante existente")
    void testBuscarPorCodigoExistente() {
        int pos = lista.buscarPorCODIGO("E002");
        assertEquals(1, pos);
    }

    @Test
    @DisplayName("buscarPorCODIGO: retorna -1 si no existe")
    void testBuscarPorCodigoInexistente() {
        int pos = lista.buscarPorCODIGO("E999");
        assertEquals(-1, pos);
    }

    @Test
    @DisplayName("buscarPorGrado: devuelve solo los estudiantes del grado indicado")
    void testBuscarPorGrado() {
        ArrayList<Estudiante> resultado = lista.buscarPorGrado("1er Grado");
        assertEquals(2, resultado.size());
        assertTrue(resultado.stream().allMatch(e -> e.getSalon().getGrado().getNom().equals("1er Grado")));
    }

    @Test
    @DisplayName("buscarPorGrado: ignora mayúsculas/minúsculas")
    void testBuscarPorGradoCaseInsensitive() {
        ArrayList<Estudiante> resultado = lista.buscarPorGrado("2do grado");
        assertEquals(1, resultado.size());
        assertEquals("E003", resultado.get(0).getCod());
    }

    @Test
    @DisplayName("buscarPorGrado: retorna lista vacía si no hay coincidencias")
    void testBuscarPorGradoSinResultados() {
        ArrayList<Estudiante> resultado = lista.buscarPorGrado("3er Grado");
        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("agregar: aumenta el tamaño de la lista")
    void testAgregar() {
        Estudiante nuevo = new Estudiante(null, null, "E004", "hash4", "44444444", "Sara", "López", 7);
        lista.agregar(nuevo);
        assertEquals(4, lista.getLista().size());
    }

    @Test
    @DisplayName("obtener: devuelve el estudiante en la posición dada")
    void testObtener() {
        Estudiante e = lista.obtener(0);
        assertEquals("E001", e.getCod());
    }

    @Test
    @DisplayName("actualizar: reemplaza el estudiante en la posición indicada")
    void testActualizar() {
        Estudiante nuevo = new Estudiante(null, null, "E005", "hash5", "55555555", "Pedro", "Soto", 8);
        lista.actualizar(1, nuevo);
        assertEquals("E005", lista.obtener(1).getCod());
    }

    @Test
    @DisplayName("eliminar: reduce el tamaño de la lista")
    void testEliminar() {
        lista.eliminar(0);
        assertEquals(2, lista.getLista().size());
        assertEquals("E002", lista.obtener(0).getCod());
    }
}