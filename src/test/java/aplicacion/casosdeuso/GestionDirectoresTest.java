package aplicacion.casosdeuso;

import dominio.modelo.Director;
import dominio.puerto.repositorio.RepositorioDirectores;
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
class GestionDirectoresTest {

    private RepositorioDirectores repoMock;
    private GestionDirectores casoUso;

    @BeforeEach
    void setUp() {
        repoMock = mock(RepositorioDirectores.class);
        casoUso = new GestionDirectores(repoMock);
    }

    @Test
    void agregar_debeGuardarDirectorCuandoNoExiste() {
        Director director = new Director("D001", "hash", "111", "Dir", "Uno", 40);
        when(repoMock.buscarPorCodigo("D001")).thenReturn(Optional.empty());

        casoUso.agregar(director);

        verify(repoMock).agregar(director);
    }

    @Test
    void agregar_debeLanzarExcepcionSiCodigoDuplicado() {
        Director existente = new Director("D001", "hash", "111", "Dir", "Uno", 40);
        Director nuevo = new Director("D001", "hash2", "222", "Dir", "Dos", 45);
        when(repoMock.buscarPorCodigo("D001")).thenReturn(Optional.of(existente));

        assertThrows(IllegalArgumentException.class, () -> casoUso.agregar(nuevo));
        verify(repoMock, never()).agregar(any());
    }

    @Test
    void agregar_debeLanzarExcepcionSiDirectorNulo() {
        assertThrows(IllegalArgumentException.class, () -> casoUso.agregar(null));
    }

    @Test
    void buscarPorCodigo_debeRetornarDirectorSiExiste() {
        Director esperado = new Director("D001", "hash", "111", "Dir", "Uno", 40);
        when(repoMock.buscarPorCodigo("D001")).thenReturn(Optional.of(esperado));

        Director resultado = casoUso.buscarPorCodigo("D001");

        assertEquals(esperado, resultado);
    }

    @Test
    void buscarPorCodigo_debeRetornarNullSiNoExiste() {
        when(repoMock.buscarPorCodigo("D999")).thenReturn(Optional.empty());

        Director resultado = casoUso.buscarPorCodigo("D999");

        assertNull(resultado);
    }

    @Test
    void listarTodos_debeRetornarListaCompleta() {
        List<Director> lista = List.of(
                new Director("D001", "hash1", "111", "Dir1", "Uno", 40),
                new Director("D002", "hash2", "222", "Dir2", "Dos", 45)
        );
        when(repoMock.listarTodos()).thenReturn(lista);

        List<Director> resultado = casoUso.listarTodos();

        assertEquals(2, resultado.size());
        verify(repoMock).listarTodos();
    }

    @Test
    void actualizar_debeActualizarDirector() {
        Director original = new Director("D001", "hash", "111", "Dir", "Uno", 40);
        Director actualizado = new Director("D001", "hashNuevo", "999", "DirMod", "UnoMod", 50);

        casoUso.actualizar(original, actualizado);

        verify(repoMock).actualizar(original, actualizado);
    }

    @Test
    void eliminar_debeEliminarDirector() {
        Director aEliminar = new Director("D001", "hash", "111", "Dir", "Uno", 40);

        casoUso.eliminar(aEliminar);

        verify(repoMock).eliminar(aEliminar);
    }
}