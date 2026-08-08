package aplicacion.casosdeuso;

import dominio.modelo.Administrador;
import dominio.puerto.repositorio.RepositorioAdministradores;
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
class GestionAdministradoresTest {

    private RepositorioAdministradores repoMock;
    private GestionAdministradores casoUso;

    @BeforeEach
    void setUp() {
        repoMock = mock(RepositorioAdministradores.class);
        casoUso = new GestionAdministradores(repoMock);
    }

    @Test
    void agregar_debeGuardarAdministradorCuandoNoExiste() {
        Administrador admin = new Administrador("A001", "hash", "111", "Admin", "Uno", 30);
        when(repoMock.buscarPorCodigo("A001")).thenReturn(Optional.empty());

        casoUso.agregar(admin);

        verify(repoMock).agregar(admin);
    }

    @Test
    void agregar_debeLanzarExcepcionSiCodigoDuplicado() {
        Administrador existente = new Administrador("A001", "hash", "111", "Admin", "Uno", 30);
        Administrador nuevo = new Administrador("A001", "hash2", "222", "Admin", "Dos", 35);
        when(repoMock.buscarPorCodigo("A001")).thenReturn(Optional.of(existente));

        assertThrows(IllegalArgumentException.class, () -> casoUso.agregar(nuevo));
        verify(repoMock, never()).agregar(any());
    }

    @Test
    void agregar_debeLanzarExcepcionSiAdministradorNulo() {
        assertThrows(IllegalArgumentException.class, () -> casoUso.agregar(null));
    }

    @Test
    void buscarPorCodigo_debeRetornarAdministradorSiExiste() {
        Administrador esperado = new Administrador("A001", "hash", "111", "Admin", "Uno", 30);
        when(repoMock.buscarPorCodigo("A001")).thenReturn(Optional.of(esperado));

        Administrador resultado = casoUso.buscarPorCodigo("A001");

        assertEquals(esperado, resultado);
    }

    @Test
    void buscarPorCodigo_debeRetornarNullSiNoExiste() {
        when(repoMock.buscarPorCodigo("B999")).thenReturn(Optional.empty());

        Administrador resultado = casoUso.buscarPorCodigo("B999");

        assertNull(resultado);
    }

    @Test
    void listarTodos_debeRetornarListaCompleta() {
        List<Administrador> lista = List.of(
                new Administrador("A001", "hash1", "111", "Admin1", "Uno", 30),
                new Administrador("A002", "hash2", "222", "Admin2", "Dos", 35)
        );
        when(repoMock.listarTodos()).thenReturn(lista);

        List<Administrador> resultado = casoUso.listarTodos();

        assertEquals(2, resultado.size());
        verify(repoMock).listarTodos();
    }

    @Test
    void actualizar_debeActualizarAdministrador() {
        Administrador original = new Administrador("A001", "hash", "111", "Admin", "Uno", 30);
        Administrador actualizado = new Administrador("A001", "hashNuevo", "999", "AdminMod", "UnoMod", 40);

        casoUso.actualizar(original, actualizado);

        verify(repoMock).actualizar(original, actualizado);
    }

    @Test
    void eliminar_debeEliminarAdministrador() {
        Administrador aEliminar = new Administrador("A001", "hash", "111", "Admin", "Uno", 30);

        casoUso.eliminar(aEliminar);

        verify(repoMock).eliminar(aEliminar);
    }
}