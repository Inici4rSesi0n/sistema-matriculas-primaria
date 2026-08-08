package aplicacion.casosdeuso;

import dominio.modelo.Grado;
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
    private GestionGrados casoUso;

    @BeforeEach
    void setUp() {
        repoMock = mock(RepositorioGrados.class);
        casoUso = new GestionGrados(repoMock);
    }

    @Test
    void crearGrado_debeGuardarCuandoNoExiste() {
        when(repoMock.buscarPorNombre("1er Grado")).thenReturn(Optional.empty());

        casoUso.crearGrado("1er Grado", "Primaria");

        verify(repoMock).agregar(any(Grado.class));
    }

    @Test
    void crearGrado_debeLanzarExcepcionCuandoYaExiste() {
        when(repoMock.buscarPorNombre("1er Grado")).thenReturn(Optional.of(new Grado("1er Grado", "Primaria")));

        assertThrows(IllegalArgumentException.class, () -> casoUso.crearGrado("1er Grado", "Primaria"));
        verify(repoMock, never()).agregar(any());
    }

    @Test
    void listarTodos_debeRetornarListaCompleta() {
        List<Grado> lista = List.of(
                new Grado("1er Grado", "Primaria"),
                new Grado("2do Grado", "Primaria")
        );
        when(repoMock.listarTodos()).thenReturn(lista);

        List<Grado> resultado = casoUso.listarTodos();

        assertEquals(2, resultado.size());
        verify(repoMock).listarTodos();
    }

    @Test
    void buscarPorNombre_debeRetornarGradoSiExiste() {
        Grado esperado = new Grado("3er Grado", "Primaria");
        when(repoMock.buscarPorNombre("3er Grado")).thenReturn(Optional.of(esperado));

        Grado resultado = casoUso.buscarPorNombre("3er Grado");

        assertEquals(esperado, resultado);
    }

    @Test
    void buscarPorNombre_debeRetornarNullSiNoExiste() {
        when(repoMock.buscarPorNombre("Inexistente")).thenReturn(Optional.empty());

        Grado resultado = casoUso.buscarPorNombre("Inexistente");

        assertNull(resultado);
    }

    @Test
    void actualizarGrado_debeActualizarConNuevosDatos() {
        Grado original = new Grado("Viejo", "Primaria");
        String nuevoNombre = "Nuevo";
        String nuevoNivel = "Secundaria";

        casoUso.actualizarGrado(original, nuevoNombre, nuevoNivel);

        verify(repoMock).actualizar(eq(original), any(Grado.class));
    }

    @Test
    void eliminarGrado_debeEliminarCorrectamente() {
        Grado aEliminar = new Grado("4to Grado", "Primaria");

        casoUso.eliminarGrado(aEliminar);

        verify(repoMock).eliminar(aEliminar);
    }
}