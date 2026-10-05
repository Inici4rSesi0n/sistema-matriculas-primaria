package aplicacion.casosdeuso;

import dominio.modelo.FranjaHoraria;
import dominio.modelo.PeriodoAcademico;
import dominio.modelo.Recreo;
import dominio.puerto.externo.LoggerPort;
import dominio.puerto.repositorio.RepositorioRecreos;
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
class GestionRecreosTest {

    private RepositorioRecreos repoMock;
    private LoggerPort loggerMock;
    private GestionRecreos casoUso;
    private FranjaHoraria franja;
    private PeriodoAcademico periodo;

    @BeforeEach
    void setUp() {
        repoMock = mock(RepositorioRecreos.class);
        loggerMock = mock(LoggerPort.class);
        casoUso = new GestionRecreos(repoMock, loggerMock);
        franja = new FranjaHoraria("Lunes", "10:00", "10:30");
        periodo = new PeriodoAcademico("2026-I", "2026-01-01", "2026-12-31", "Activo");
    }

    @Test
    void crearRecreo_debeGuardarRecreoCorrectamente() {
        casoUso.crearRecreo(franja, "Recreo", periodo);
        verify(repoMock).agregar(any(Recreo.class));
    }

    @Test
    void buscarPorDescripcion_debeRetornarRecreoSiExiste() {
        Recreo esperado = new Recreo(franja, "Recreo", periodo);
        when(repoMock.buscarPorDescripcion("Recreo")).thenReturn(Optional.of(esperado));
        assertEquals(esperado, casoUso.buscarPorDescripcion("Recreo"));
    }

    @Test
    void buscarPorDescripcion_debeRetornarNullSiNoExiste() {
        when(repoMock.buscarPorDescripcion("Inexistente")).thenReturn(Optional.empty());
        assertNull(casoUso.buscarPorDescripcion("Inexistente"));
    }

    @Test
    void listarTodos_debeRetornarListaCompleta() {
        List<Recreo> lista = List.of(
                new Recreo(franja, "Recreo", periodo),
                new Recreo(new FranjaHoraria("Martes", "10:00", "10:30"), "Recreo", periodo));
        when(repoMock.listarTodos()).thenReturn(lista);
        assertEquals(2, casoUso.listarTodos().size());
        verify(repoMock).listarTodos();
    }

    @Test
    void actualizarRecreo_debeActualizarCorrectamente() {
        Recreo original = new Recreo(franja, "Viejo", periodo);
        FranjaHoraria nuevaFranja = new FranjaHoraria("Lunes", "11:00", "11:30");
        casoUso.actualizarRecreo(original, nuevaFranja, "Nuevo", periodo);
        verify(repoMock).actualizar(eq(original), any(Recreo.class));
    }

    @Test
    void eliminarRecreo_debeEliminarCorrectamente() {
        Recreo aEliminar = new Recreo(franja, "Recreo", periodo);
        casoUso.eliminarRecreo(aEliminar);
        verify(repoMock).eliminar(aEliminar);
    }

    @Test
    void actualizarRecreo_debeModificarMismaReferencia() {
        PeriodoAcademico p = new PeriodoAcademico("2026-I", "2026-01-01", "2026-12-31", "Activo");
        FranjaHoraria f = new FranjaHoraria("Lunes", "10:00", "10:30");
        Recreo original = new Recreo(f, "Recreo", p);

        FranjaHoraria nuevaFranja = new FranjaHoraria("Martes", "11:00", "11:30");
        casoUso.actualizarRecreo(original, nuevaFranja, "Descanso", p);

        assertEquals("Descanso", original.getDescripcion());
        assertEquals("Martes", original.getDiaSemana());
        verify(repoMock).actualizar(original, original);
    }
}
