package aplicacion.casosdeuso;

import dominio.modelo.Grado;
import dominio.puerto.externo.LoggerPort;
import dominio.puerto.repositorio.RepositorioGrados;
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
class GestionGradosTest {

    private RepositorioGrados repoMock;
    private LoggerPort loggerMock;
    private GestionGrados casoUso;

    @BeforeEach
    void setUp() {
        repoMock = mock(RepositorioGrados.class);
        loggerMock = mock(LoggerPort.class);
        casoUso = new GestionGrados(repoMock, loggerMock);
    }

    @Test
    void crearGrado_debeGuardarCuandoNoExiste() {
        when(repoMock.buscarPorNombre("1er Grado")).thenReturn(Optional.empty());
        casoUso.crearGrado("1er Grado", "Primaria");
        verify(repoMock).agregar(any(Grado.class));
    }

    @Test
    void crearGrado_debeFallarSiNombreVacio() {
        assertThrows(IllegalArgumentException.class, () -> casoUso.crearGrado("", "Primaria"));
        verify(repoMock, never()).agregar(any());
    }

    @Test
    void crearGrado_debeFallarSiNivelVacio() {
        assertThrows(IllegalArgumentException.class, () -> casoUso.crearGrado("1er Grado", ""));
        verify(repoMock, never()).agregar(any());
    }

    @Test
    void crearGrado_debeFallarSiDuplicado() {
        when(repoMock.buscarPorNombre("1er Grado")).thenReturn(Optional.of(new Grado("1er Grado", "Primaria")));
        assertThrows(IllegalArgumentException.class, () -> casoUso.crearGrado("1er Grado", "Primaria"));
        verify(repoMock, never()).agregar(any());
    }

    @Test
    void buscarPorNombre_debeRetornarSiExiste() {
        Grado esperado = new Grado("1er Grado", "Primaria");
        when(repoMock.buscarPorNombre("1er Grado")).thenReturn(Optional.of(esperado));
        assertEquals(esperado, casoUso.buscarPorNombre("1er Grado"));
    }

    @Test
    void buscarPorNombre_debeRetornarNullSiNoExiste() {
        when(repoMock.buscarPorNombre("X")).thenReturn(Optional.empty());
        assertNull(casoUso.buscarPorNombre("X"));
    }

    @Test
    void listarTodos_debeRetornarListaCompleta() {
        List<Grado> lista = List.of(
                new Grado("1er Grado", "Primaria"),
                new Grado("2do Grado", "Primaria"));
        when(repoMock.listarTodos()).thenReturn(lista);
        assertEquals(2, casoUso.listarTodos().size());
    }

    @Test
    void actualizarGrado_debeModificarMismaReferencia() {
        Grado original = new Grado("1er Grado", "Primaria");
        casoUso.actualizarGrado(original, "2do Grado", "Secundaria");
        assertEquals("2do Grado", original.getNombre());
        assertEquals("Secundaria", original.getNivel());
        verify(repoMock).actualizar(original, original);
    }

    @Test
    void eliminarGrado_debeEliminarCorrectamente() {
        Grado aEliminar = new Grado("1er Grado", "Primaria");
        casoUso.eliminarGrado(aEliminar);
        verify(repoMock).eliminar(aEliminar);
    }
}
