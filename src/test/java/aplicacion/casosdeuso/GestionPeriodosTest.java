package aplicacion.casosdeuso;

import dominio.modelo.PeriodoAcademico;
import dominio.puerto.repositorio.RepositorioPeriodos;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 *
 * @author inici4rsesi0n
 */
class GestionPeriodosTest {

    private RepositorioPeriodos repoMock;
    private GestionPeriodos casoUso;

    @BeforeEach
    void setUp() {
        repoMock = mock(RepositorioPeriodos.class);
        casoUso = new GestionPeriodos(repoMock);
    }

    @Test
    void crearPeriodo_debeGuardarCuandoNoExiste() {
        when(repoMock.buscarPorNombre("2026-I")).thenReturn(Optional.empty());

        casoUso.crearPeriodo("2026-I", "2026-01-01", "2026-12-31", "Activo");

        verify(repoMock).agregar(any(PeriodoAcademico.class));
    }

    @Test
    void crearPeriodo_debeLanzarExcepcionCuandoYaExiste() {
        when(repoMock.buscarPorNombre("2026-I"))
                .thenReturn(Optional.of(new PeriodoAcademico("2026-I", "2026-01-01", "2026-12-31", "Activo")));

        assertThrows(IllegalArgumentException.class,
                () -> casoUso.crearPeriodo("2026-I", "2026-01-01", "2026-12-31", "Activo"));
        verify(repoMock, never()).agregar(any());
    }

    @Test
    void buscarPorNombre_debeRetornarPeriodoSiExiste() {
        PeriodoAcademico esperado = new PeriodoAcademico("2026-I", "2026-01-01", "2026-12-31", "Activo");
        when(repoMock.buscarPorNombre("2026-I")).thenReturn(Optional.of(esperado));

        PeriodoAcademico resultado = casoUso.buscarPorNombre("2026-I");

        assertEquals(esperado, resultado);
    }

    @Test
    void buscarPorNombre_debeRetornarNullSiNoExiste() {
        when(repoMock.buscarPorNombre("2030-X")).thenReturn(Optional.empty());

        PeriodoAcademico resultado = casoUso.buscarPorNombre("2030-X");

        assertNull(resultado);
    }

    @Test
    void listarTodos_debeRetornarListaCompleta() {
        List<PeriodoAcademico> lista = List.of(
                new PeriodoAcademico("2026-I", "2026-01-01", "2026-12-31", "Activo"),
                new PeriodoAcademico("2026-II", "2026-07-01", "2026-12-31", "Activo")
        );
        when(repoMock.listarTodos()).thenReturn(lista);

        List<PeriodoAcademico> resultado = casoUso.listarTodos();

        assertEquals(2, resultado.size());
        verify(repoMock).listarTodos();
    }

    @Test
    void actualizarPeriodo_debeActualizarConNuevosDatos() {
        PeriodoAcademico original = new PeriodoAcademico("2026-I", "2026-01-01", "2026-06-30", "Activo");
        String nuevoNombre = "2026-I-Mod";
        String nuevaFechaInicio = "2026-02-01";
        String nuevaFechaFin = "2026-07-31";
        String nuevoEstado = "Culminado";

        casoUso.actualizarPeriodo(original, nuevoNombre, nuevaFechaInicio, nuevaFechaFin, nuevoEstado);

        verify(repoMock).actualizar(eq(original), any(PeriodoAcademico.class));
    }

    @Test
    void eliminarPeriodo_debeEliminarCorrectamente() {
        PeriodoAcademico aEliminar = new PeriodoAcademico("2026-I", "2026-01-01", "2026-12-31", "Activo");

        casoUso.eliminarPeriodo(aEliminar);

        verify(repoMock).eliminar(aEliminar);
    }
}