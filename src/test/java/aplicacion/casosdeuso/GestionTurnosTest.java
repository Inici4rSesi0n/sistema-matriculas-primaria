package aplicacion.casosdeuso;

import dominio.modelo.Turno;
import dominio.puerto.repositorio.RepositorioTurnos;
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
class GestionTurnosTest {

    private RepositorioTurnos repoMock;
    private GestionTurnos casoUso;

    @BeforeEach
    void setUp() {
        repoMock = mock(RepositorioTurnos.class);
        casoUso = new GestionTurnos(repoMock);
    }

    @Test
    void crearTurno_debeGuardarCuandoNoExiste() {
        when(repoMock.buscarPorNombre("Mañana")).thenReturn(Optional.empty());

        casoUso.crearTurno("Mañana");

        verify(repoMock).agregar(any(Turno.class));
    }

    @Test
    void crearTurno_debeLanzarExcepcionCuandoYaExiste() {
        when(repoMock.buscarPorNombre("Mañana")).thenReturn(Optional.of(new Turno("Mañana")));

        assertThrows(IllegalArgumentException.class, () -> casoUso.crearTurno("Mañana"));
        verify(repoMock, never()).agregar(any());
    }

    @Test
    void listarTodos_debeRetornarListaCompleta() {
        List<Turno> lista = List.of(new Turno("Mañana"), new Turno("Tarde"));
        when(repoMock.listarTodos()).thenReturn(lista);

        List<Turno> resultado = casoUso.listarTodos();

        assertEquals(2, resultado.size());
        verify(repoMock).listarTodos();
    }

    @Test
    void buscarPorNombre_debeRetornarTurnoSiExiste() {
        Turno esperado = new Turno("Noche");
        when(repoMock.buscarPorNombre("Noche")).thenReturn(Optional.of(esperado));

        Turno resultado = casoUso.buscarPorNombre("Noche");

        assertEquals(esperado, resultado);
    }

    @Test
    void buscarPorNombre_debeRetornarNullSiNoExiste() {
        when(repoMock.buscarPorNombre("Inexistente")).thenReturn(Optional.empty());

        Turno resultado = casoUso.buscarPorNombre("Inexistente");

        assertNull(resultado);
    }

    @Test
    void actualizarTurno_debeActualizarConNuevoNombre() {
        Turno original = new Turno("Mañana");
        String nuevoNombre = "Matutino";

        casoUso.actualizarTurno(original, nuevoNombre);

        verify(repoMock).actualizar(eq(original), any(Turno.class));
    }

    @Test
    void eliminarTurno_debeEliminarCorrectamente() {
        Turno aEliminar = new Turno("Tarde");

        casoUso.eliminarTurno(aEliminar);

        verify(repoMock).eliminar(aEliminar);
    }
}