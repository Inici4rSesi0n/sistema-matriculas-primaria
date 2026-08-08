package aplicacion.casosdeuso;

import dominio.modelo.*;
import dominio.puerto.repositorio.RepositorioClases;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 *
 * @author inici4rsesi0n
 */
class GestionClasesTest {

    private RepositorioClases repoMock;
    private GestionClases casoUso;

    private Asignatura asignatura;
    private Docente docente;
    private Grupo grupo;
    private Aula aula;
    private PeriodoAcademico periodo;

    @BeforeEach
    void setUp() {
        repoMock = mock(RepositorioClases.class);
        casoUso = new GestionClases(repoMock);

        asignatura = new Asignatura("Matemáticas");
        docente = new Docente("D001", "hash", "123", "Juan", "Perez", 30, "Mat", new ArrayList<>());
        Grado grado = new Grado("1er Grado", "Primaria");
        grupo = new Grupo("1A", grado);
        aula = new Aula("Aula 101", 40, "Pabellón 1", "Teoría");
        periodo = new PeriodoAcademico("2026-I", "2026-01-01", "2026-12-31", "Activo");
    }

    @Test
    void crearClase_debeGuardarClaseCorrectamente() {
        casoUso.crearClase("Lunes", "08:00", "10:00", asignatura, docente, grupo, aula, periodo);
        verify(repoMock).agregar(any(Clase.class));
    }

    @Test
    void crearClase_debeLanzarExcepcionSiDiaVacio() {
        assertThrows(IllegalArgumentException.class,
                () -> casoUso.crearClase("", "08:00", "10:00", asignatura, docente, grupo, aula, periodo));
    }

    @Test
    void crearClase_debeLanzarExcepcionSiHoraInicioVacia() {
        assertThrows(IllegalArgumentException.class,
                () -> casoUso.crearClase("Lunes", "", "10:00", asignatura, docente, grupo, aula, periodo));
    }

    @Test
    void crearClase_debeLanzarExcepcionSiHoraFinVacia() {
        assertThrows(IllegalArgumentException.class,
                () -> casoUso.crearClase("Lunes", "08:00", "", asignatura, docente, grupo, aula, periodo));
    }

    @Test
    void crearClase_debeLanzarExcepcionSiAsignaturaNula() {
        assertThrows(IllegalArgumentException.class,
                () -> casoUso.crearClase("Lunes", "08:00", "10:00", null, docente, grupo, aula, periodo));
    }

    @Test
    void crearClase_debeLanzarExcepcionSiDocenteNulo() {
        assertThrows(IllegalArgumentException.class,
                () -> casoUso.crearClase("Lunes", "08:00", "10:00", asignatura, null, grupo, aula, periodo));
    }

    @Test
    void crearClase_debeLanzarExcepcionSiGrupoNulo() {
        assertThrows(IllegalArgumentException.class,
                () -> casoUso.crearClase("Lunes", "08:00", "10:00", asignatura, docente, null, aula, periodo));
    }

    @Test
    void crearClase_debeLanzarExcepcionSiAulaNula() {
        assertThrows(IllegalArgumentException.class,
                () -> casoUso.crearClase("Lunes", "08:00", "10:00", asignatura, docente, grupo, null, periodo));
    }

    @Test
    void crearClase_debeLanzarExcepcionSiPeriodoNulo() {
        assertThrows(IllegalArgumentException.class,
                () -> casoUso.crearClase("Lunes", "08:00", "10:00", asignatura, docente, grupo, aula, null));
    }

    @Test
    void listarClases_debeRetornarListaCompleta() {
        FranjaHoraria franja = new FranjaHoraria("Lunes", "08:00", "10:00");
        Clase clase = new Clase(franja, periodo, asignatura, docente, grupo, aula);
        when(repoMock.listarTodos()).thenReturn(List.of(clase));

        List<Clase> resultado = casoUso.listarClases();

        assertEquals(1, resultado.size());
        verify(repoMock).listarTodos();
    }

    @Test
    void buscarPorGrupoYGrado_debeRetornarListaFiltrada() {
        FranjaHoraria franja = new FranjaHoraria("Lunes", "08:00", "10:00");
        Clase clase = new Clase(franja, periodo, asignatura, docente, grupo, aula);
        when(repoMock.buscarPorGrupoYGrado("1A", "1er Grado")).thenReturn(List.of(clase));

        List<Clase> resultado = casoUso.buscarPorGrupoYGrado("1A", "1er Grado");

        assertEquals(1, resultado.size());
        verify(repoMock).buscarPorGrupoYGrado("1A", "1er Grado");
    }

    @Test
    void existeDocenteEnHorario_debeRetornarTrueSiExiste() {
        when(repoMock.existeDocenteEnHorario("D001", "Lunes", "08:00", "10:00")).thenReturn(true);

        boolean resultado = casoUso.existeDocenteEnHorario("D001", "Lunes", "08:00", "10:00");

        assertTrue(resultado);
    }

    @Test
    void existeDocenteEnHorario_debeRetornarFalseSiNoExiste() {
        when(repoMock.existeDocenteEnHorario("D001", "Lunes", "08:00", "10:00")).thenReturn(false);

        boolean resultado = casoUso.existeDocenteEnHorario("D001", "Lunes", "08:00", "10:00");

        assertFalse(resultado);
    }

    @Test
    void buscarClase_debeRetornarClaseSiExiste() {
        FranjaHoraria franja = new FranjaHoraria("Lunes", "08:00", "10:00");
        Clase esperada = new Clase(franja, periodo, asignatura, docente, grupo, aula);
        when(repoMock.buscarClase("1er Grado", "1A", "Lunes", "08:00", "10:00"))
                .thenReturn(Optional.of(esperada));

        Clase resultado = casoUso.buscarClase("1er Grado", "1A", "Lunes", "08:00", "10:00");

        assertEquals(esperada, resultado);
    }

    @Test
    void buscarClase_debeRetornarNullSiNoExiste() {
        when(repoMock.buscarClase("1er Grado", "1A", "Lunes", "08:00", "10:00"))
                .thenReturn(Optional.empty());

        Clase resultado = casoUso.buscarClase("1er Grado", "1A", "Lunes", "08:00", "10:00");

        assertNull(resultado);
    }

    @Test
    void actualizarClase_debeActualizarCorrectamente() {
        FranjaHoraria franjaOriginal = new FranjaHoraria("Lunes", "08:00", "10:00");
        Clase original = new Clase(franjaOriginal, periodo, asignatura, docente, grupo, aula);

        casoUso.actualizarClase(original, "Martes", "09:00", "11:00", asignatura, docente, grupo, aula, periodo);

        verify(repoMock).actualizar(eq(original), any(Clase.class));
    }

    @Test
    void eliminarClase_debeEliminarCorrectamente() {
        FranjaHoraria franja = new FranjaHoraria("Lunes", "08:00", "10:00");
        Clase aEliminar = new Clase(franja, periodo, asignatura, docente, grupo, aula);

        casoUso.eliminarClase(aEliminar);

        verify(repoMock).eliminar(aEliminar);
    }
}