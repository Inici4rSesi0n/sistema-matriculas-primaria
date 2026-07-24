package modelo.repositorio;
import static org.junit.jupiter.api.Assertions.*;
import modelo.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
/**
 *
 * @author inici4rsesi0n
 */
public class ListaClasesTest {

    private ListaClases lista;
    private Grado grado;
    private Salon salonA;
    private Salon salonB;
    private Docente docente1;
    private Docente docente2;
    private Asignatura asignatura;

    @BeforeEach
    void setUp() {
        lista = new ListaClases();
        grado = new Grado("1er Grado", "Primaria");
        salonA = new Salon("A", grado, null, 30);
        salonB = new Salon("B", grado, null, 25);
        docente1 = new Docente("Matemática", new ArrayList<>(), null, "D0001", "hash1", "11111111", "Juan", "Pérez", 35);
        docente2 = new Docente("Comunicación", new ArrayList<>(), null, "D0002", "hash2", "22222222", "María", "López", 42);
        asignatura = new Asignatura("Matemática");

        Clase c1 = new Clase("Lunes", "08:00", "09:00", asignatura, docente1, salonA);
        Clase c2 = new Clase("Martes", "09:00", "10:15", asignatura, docente1, salonA);
        Clase c3 = new Clase("Lunes", "08:00", "09:00", asignatura, docente2, salonB);
        lista.agregar(c1);
        lista.agregar(c2);
        lista.agregar(c3);
    }

    @Test
    @DisplayName("buscarPorSalonYGrado: devuelve solo las clases del aula y grado indicados")
    void testBuscarPorSalonYGrado() {
        ArrayList<Clase> resultado = lista.buscarPorSalonYGrado("A", "1er Grado");
        assertEquals(2, resultado.size());
        assertTrue(resultado.stream().allMatch(c -> c.getSalon().getAula().equals("A")));
    }

    @Test
    @DisplayName("buscarPorSalonYGrado: ordena por día y hora")
    void testBuscarPorSalonYGradoOrden() {
        ArrayList<Clase> resultado = lista.buscarPorSalonYGrado("A", "1er Grado");
        assertEquals("Lunes", resultado.get(0).getDiaSemana());
        assertEquals("Martes", resultado.get(1).getDiaSemana());
    }

    @Test
    @DisplayName("buscarPorSalonYGrado: ignora mayúsculas/minúsculas")
    void testBuscarPorSalonYGradoCaseInsensitive() {
        ArrayList<Clase> resultado = lista.buscarPorSalonYGrado("b", "1er grado");
        assertEquals(1, resultado.size());
        assertEquals("B", resultado.get(0).getSalon().getAula());
    }

    @Test
    @DisplayName("buscarPorSalonYGrado: retorna lista vacía si no hay coincidencias")
    void testBuscarPorSalonYGradoSinResultados() {
        ArrayList<Clase> resultado = lista.buscarPorSalonYGrado("C", "1er Grado");
        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("existeDocenteEnHorario: devuelve true si el docente ya tiene clase")
    void testExisteDocenteEnHorarioTrue() {
        assertTrue(lista.existeDocenteEnHorario("D0001", "Lunes", "08:00", "09:00"));
    }

    @Test
    @DisplayName("existeDocenteEnHorario: devuelve false si el docente está libre")
    void testExisteDocenteEnHorarioFalse() {
        assertFalse(lista.existeDocenteEnHorario("D0001", "Viernes", "10:45", "11:45"));
    }

    @Test
    @DisplayName("buscarClase: encuentra la posición de una clase existente")
    void testBuscarClaseExistente() {
        int pos = lista.buscarClase("1er Grado", "A", "Lunes", "08:00", "09:00");
        assertEquals(0, pos);
    }

    @Test
    @DisplayName("buscarClase: retorna -1 si no existe")
    void testBuscarClaseInexistente() {
        int pos = lista.buscarClase("1er Grado", "A", "Viernes", "10:45", "11:45");
        assertEquals(-1, pos);
    }

    @Test
    @DisplayName("agregar: aumenta el tamaño de la lista")
    void testAgregar() {
        Clase nueva = new Clase("Viernes", "10:45", "11:45", asignatura, docente1, salonA);
        lista.agregar(nueva);
        assertEquals(4, lista.getLista().size());
    }

    @Test
    @DisplayName("obtener: devuelve la clase en la posición dada")
    void testObtener() {
        Clase c = lista.obtener(0);
        assertEquals("Lunes", c.getDiaSemana());
    }

    @Test
    @DisplayName("actualizar: reemplaza la clase en la posición indicada")
    void testActualizar() {
        Clase nueva = new Clase("Viernes", "11:45", "12:30", asignatura, docente1, salonA);
        lista.actualizar(0, nueva);
        assertEquals("Viernes", lista.obtener(0).getDiaSemana());
    }

    @Test
    @DisplayName("eliminar: reduce el tamaño de la lista")
    void testEliminar() {
        lista.eliminar(0);
        assertEquals(2, lista.getLista().size());
    }
}