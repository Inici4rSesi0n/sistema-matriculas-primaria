package aplicacion.casosdeuso;

import dominio.modelo.Estudiante;
import dominio.puerto.repositorio.RepositorioEstudiantes;
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
class GestionEstudiantesTest {

    private RepositorioEstudiantes repoMock;
    private GestionEstudiantes casoUso;

    @BeforeEach
    void setUp() {
        repoMock = mock(RepositorioEstudiantes.class);
        casoUso = new GestionEstudiantes(repoMock);
    }

    @Test
    void agregar_debeGuardarEstudianteCuandoNoExiste() {
        Estudiante estudiante = new Estudiante("E001", "hash", "111", "Est", "Uno", 15);
        when(repoMock.buscarPorCodigo("E001")).thenReturn(Optional.empty());
        when(repoMock.buscarPorDni("111")).thenReturn(Optional.empty());

        casoUso.agregar(estudiante);

        verify(repoMock).agregar(estudiante);
    }

    @Test
    void agregar_debeLanzarExcepcionSiCodigoDuplicado() {
        Estudiante existente = new Estudiante("E001", "hash", "111", "Est", "Uno", 15);
        Estudiante nuevo = new Estudiante("E001", "hash2", "222", "Est", "Dos", 16);
        when(repoMock.buscarPorCodigo("E001")).thenReturn(Optional.of(existente));

        assertThrows(IllegalArgumentException.class, () -> casoUso.agregar(nuevo));
        verify(repoMock, never()).agregar(any());
    }

    @Test
    void agregar_debeLanzarExcepcionSiDniDuplicado() {
        Estudiante existente = new Estudiante("E002", "hash", "111", "Est", "Dos", 16);
        Estudiante nuevo = new Estudiante("E003", "hash2", "111", "Est", "Tres", 17);
        when(repoMock.buscarPorCodigo("E003")).thenReturn(Optional.empty());
        when(repoMock.buscarPorDni("111")).thenReturn(Optional.of(existente));

        assertThrows(IllegalArgumentException.class, () -> casoUso.agregar(nuevo));
        verify(repoMock, never()).agregar(any());
    }

    @Test
    void buscarPorCodigo_debeRetornarEstudianteSiExiste() {
        Estudiante esperado = new Estudiante("E001", "hash", "111", "Est", "Uno", 15);
        when(repoMock.buscarPorCodigo("E001")).thenReturn(Optional.of(esperado));

        Estudiante resultado = casoUso.buscarPorCodigo("E001");
        assertEquals(esperado, resultado);
    }

    @Test
    void buscarPorCodigo_debeRetornarNullSiNoExiste() {
        when(repoMock.buscarPorCodigo("E999")).thenReturn(Optional.empty());

        Estudiante resultado = casoUso.buscarPorCodigo("E999");
        assertNull(resultado);
    }

    @Test
    void buscarPorDni_debeRetornarEstudianteSiExiste() {
        Estudiante esperado = new Estudiante("E001", "hash", "111", "Est", "Uno", 15);
        when(repoMock.buscarPorDni("111")).thenReturn(Optional.of(esperado));

        Estudiante resultado = casoUso.buscarPorDni("111");
        assertEquals(esperado, resultado);
    }

    @Test
    void buscarPorDni_debeRetornarNullSiNoExiste() {
        when(repoMock.buscarPorDni("000")).thenReturn(Optional.empty());

        Estudiante resultado = casoUso.buscarPorDni("000");
        assertNull(resultado);
    }

    @Test
    void listarTodos_debeRetornarListaCompleta() {
        List<Estudiante> lista = List.of(
                new Estudiante("E001", "hash1", "111", "Est1", "Uno", 15),
                new Estudiante("E002", "hash2", "222", "Est2", "Dos", 16)
        );
        when(repoMock.listarTodos()).thenReturn(lista);

        List<Estudiante> resultado = casoUso.listarTodos();
        assertEquals(2, resultado.size());
        verify(repoMock).listarTodos();
    }

    @Test
    void actualizar_debeActualizarEstudiante() {
        Estudiante original = new Estudiante("E001", "hash", "111", "Est", "Uno", 15);
        Estudiante actualizado = new Estudiante("E001", "hashNuevo", "999", "EstMod", "UnoMod", 16);

        casoUso.actualizar(original, actualizado);

        verify(repoMock).actualizar(original, actualizado);
    }

    @Test
    void eliminar_debeEliminarEstudiante() {
        Estudiante aEliminar = new Estudiante("E001", "hash", "111", "Est", "Uno", 15);

        casoUso.eliminar(aEliminar);

        verify(repoMock).eliminar(aEliminar);
    }
}