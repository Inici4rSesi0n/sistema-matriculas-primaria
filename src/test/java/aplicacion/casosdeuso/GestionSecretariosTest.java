package aplicacion.casosdeuso;

import dominio.modelo.Secretario;
import dominio.puerto.repositorio.RepositorioSecretarios;
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
class GestionSecretariosTest {

    private RepositorioSecretarios repoMock;
    private GestionSecretarios casoUso;

    @BeforeEach
    void setUp() {
        repoMock = mock(RepositorioSecretarios.class);
        casoUso = new GestionSecretarios(repoMock);
    }

    @Test
    void agregar_debeGuardarSecretarioCuandoNoExiste() {
        Secretario secretario = new Secretario("S001", "hash", "111", "Sec", "Uno", 30);
        when(repoMock.buscarPorCodigo("S001")).thenReturn(Optional.empty());

        casoUso.agregar(secretario);

        verify(repoMock).agregar(secretario);
    }

    @Test
    void agregar_debeLanzarExcepcionSiCodigoDuplicado() {
        Secretario existente = new Secretario("S001", "hash", "111", "Sec", "Uno", 30);
        Secretario nuevo = new Secretario("S001", "hash2", "222", "Sec", "Dos", 35);
        when(repoMock.buscarPorCodigo("S001")).thenReturn(Optional.of(existente));

        assertThrows(IllegalArgumentException.class, () -> casoUso.agregar(nuevo));
        verify(repoMock, never()).agregar(any());
    }

    @Test
    void agregar_debeLanzarExcepcionSiSecretarioNulo() {
        assertThrows(IllegalArgumentException.class, () -> casoUso.agregar(null));
    }

    @Test
    void buscarPorCodigo_debeRetornarSecretarioSiExiste() {
        Secretario esperado = new Secretario("S001", "hash", "111", "Sec", "Uno", 30);
        when(repoMock.buscarPorCodigo("S001")).thenReturn(Optional.of(esperado));

        Secretario resultado = casoUso.buscarPorCodigo("S001");

        assertEquals(esperado, resultado);
    }

    @Test
    void buscarPorCodigo_debeRetornarNullSiNoExiste() {
        when(repoMock.buscarPorCodigo("S999")).thenReturn(Optional.empty());

        Secretario resultado = casoUso.buscarPorCodigo("S999");

        assertNull(resultado);
    }

    @Test
    void listarTodos_debeRetornarListaCompleta() {
        List<Secretario> lista = List.of(
                new Secretario("S001", "hash1", "111", "Sec1", "Uno", 30),
                new Secretario("S002", "hash2", "222", "Sec2", "Dos", 35)
        );
        when(repoMock.listarTodos()).thenReturn(lista);

        List<Secretario> resultado = casoUso.listarTodos();

        assertEquals(2, resultado.size());
        verify(repoMock).listarTodos();
    }

    @Test
    void actualizar_debeActualizarSecretario() {
        Secretario original = new Secretario("S001", "hash", "111", "Sec", "Uno", 30);
        Secretario actualizado = new Secretario("S001", "hashNuevo", "999", "SecMod", "UnoMod", 40);

        casoUso.actualizar(original, actualizado);

        verify(repoMock).actualizar(original, actualizado);
    }

    @Test
    void eliminar_debeEliminarSecretario() {
        Secretario aEliminar = new Secretario("S001", "hash", "111", "Sec", "Uno", 30);

        casoUso.eliminar(aEliminar);

        verify(repoMock).eliminar(aEliminar);
    }
}