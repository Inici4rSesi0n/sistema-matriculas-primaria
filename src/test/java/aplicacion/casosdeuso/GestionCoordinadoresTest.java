package aplicacion.casosdeuso;

import dominio.modelo.CoordinadorAcademico;
import dominio.puerto.repositorio.RepositorioCoordinadores;
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
class GestionCoordinadoresTest {

    private RepositorioCoordinadores repoMock;
    private GestionCoordinadores casoUso;

    @BeforeEach
    void setUp() {
        repoMock = mock(RepositorioCoordinadores.class);
        casoUso = new GestionCoordinadores(repoMock);
    }

    @Test
    void agregar_debeGuardarCoordinadorCuandoNoExiste() {
        CoordinadorAcademico coord = new CoordinadorAcademico("C001", "hash", "111", "Coord", "Uno", 30);
        when(repoMock.buscarPorCodigo("C001")).thenReturn(Optional.empty());

        casoUso.agregar(coord);

        verify(repoMock).agregar(coord);
    }

    @Test
    void agregar_debeLanzarExcepcionSiCodigoDuplicado() {
        CoordinadorAcademico existente = new CoordinadorAcademico("C001", "hash", "111", "Coord", "Uno", 30);
        CoordinadorAcademico nuevo = new CoordinadorAcademico("C001", "hash2", "222", "Coord", "Dos", 35);
        when(repoMock.buscarPorCodigo("C001")).thenReturn(Optional.of(existente));

        assertThrows(IllegalArgumentException.class, () -> casoUso.agregar(nuevo));
        verify(repoMock, never()).agregar(any());
    }

    @Test
    void agregar_debeLanzarExcepcionSiCoordinadorNulo() {
        assertThrows(IllegalArgumentException.class, () -> casoUso.agregar(null));
    }

    @Test
    void buscarPorCodigo_debeRetornarCoordinadorSiExiste() {
        CoordinadorAcademico esperado = new CoordinadorAcademico("C001", "hash", "111", "Coord", "Uno", 30);
        when(repoMock.buscarPorCodigo("C001")).thenReturn(Optional.of(esperado));

        CoordinadorAcademico resultado = casoUso.buscarPorCodigo("C001");

        assertEquals(esperado, resultado);
    }

    @Test
    void buscarPorCodigo_debeRetornarNullSiNoExiste() {
        when(repoMock.buscarPorCodigo("C999")).thenReturn(Optional.empty());

        CoordinadorAcademico resultado = casoUso.buscarPorCodigo("C999");

        assertNull(resultado);
    }

    @Test
    void listarTodos_debeRetornarListaCompleta() {
        List<CoordinadorAcademico> lista = List.of(
                new CoordinadorAcademico("C001", "hash1", "111", "Coord1", "Uno", 30),
                new CoordinadorAcademico("C002", "hash2", "222", "Coord2", "Dos", 35)
        );
        when(repoMock.listarTodos()).thenReturn(lista);

        List<CoordinadorAcademico> resultado = casoUso.listarTodos();

        assertEquals(2, resultado.size());
        verify(repoMock).listarTodos();
    }

    @Test
    void actualizar_debeActualizarCoordinador() {
        CoordinadorAcademico original = new CoordinadorAcademico("C001", "hash", "111", "Coord", "Uno", 30);
        CoordinadorAcademico actualizado = new CoordinadorAcademico("C001", "hashNuevo", "999", "CoordMod", "UnoMod", 40);

        casoUso.actualizar(original, actualizado);

        verify(repoMock).actualizar(original, actualizado);
    }

    @Test
    void eliminar_debeEliminarCoordinador() {
        CoordinadorAcademico aEliminar = new CoordinadorAcademico("C001", "hash", "111", "Coord", "Uno", 30);

        casoUso.eliminar(aEliminar);

        verify(repoMock).eliminar(aEliminar);
    }
}