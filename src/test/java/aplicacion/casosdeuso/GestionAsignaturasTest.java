package aplicacion.casosdeuso;

import dominio.modelo.Asignatura;
import dominio.puerto.repositorio.RepositorioAsignaturas;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 *
 * @author inici4rsesi0n
 */
class GestionAsignaturasTest {

    private RepositorioAsignaturas repoMock;
    private GestionAsignaturas casoUso;

    @BeforeEach
    void setUp() {
        repoMock = mock(RepositorioAsignaturas.class);
        casoUso = new GestionAsignaturas(repoMock);
    }

    @Test
    void crearAsignatura_debeGuardarCuandoNoExiste() {
        when(repoMock.buscarPorNombre("Matemáticas")).thenReturn(Optional.empty());

        casoUso.crearAsignatura("Matemáticas");

        verify(repoMock).agregar(any(Asignatura.class));
    }

    @Test
    void crearAsignatura_debeLanzarExcepcionCuandoYaExiste() {
        when(repoMock.buscarPorNombre("Matemáticas")).thenReturn(Optional.of(new Asignatura("Matemáticas")));

        assertThrows(IllegalArgumentException.class, () -> casoUso.crearAsignatura("Matemáticas"));
        verify(repoMock, never()).agregar(any());
    }

    @Test
    void listarAsignaturas_debeRetornarTodas() {
        List<Asignatura> lista = List.of(new Asignatura("A1"), new Asignatura("A2"));
        when(repoMock.listarTodos()).thenReturn(lista);

        List<Asignatura> resultado = casoUso.listarTodos();

        assertEquals(2, resultado.size());
        verify(repoMock).listarTodos();
    }

    @Test
    void buscarAsignatura_debeRetornarAsignaturaSiExiste() {
        Asignatura esperada = new Asignatura("Historia");
        when(repoMock.buscarPorNombre("Historia")).thenReturn(Optional.of(esperada));

        Asignatura resultado = casoUso.buscarAsignatura("Historia");

        assertEquals(esperada, resultado);
    }

    @Test
    void buscarAsignatura_debeRetornarNullSiNoExiste() {
        when(repoMock.buscarPorNombre("Inexistente")).thenReturn(Optional.empty());

        Asignatura resultado = casoUso.buscarAsignatura("Inexistente");

        assertNull(resultado);
    }

    @Test
    void actualizarAsignatura_debeActualizarConNuevoNombre() {
        Asignatura original = new Asignatura("Ciencias");
        String nuevoNombre = "Ciencias Naturales";

        casoUso.actualizarAsignatura(original, nuevoNombre);

        ArgumentCaptor<Asignatura> captor = ArgumentCaptor.forClass(Asignatura.class);
        verify(repoMock).actualizar(eq(original), captor.capture());
        assertEquals(nuevoNombre, captor.getValue().getNombre());
    }

    @Test
    void eliminarAsignatura_debeEliminarCorrectamente() {
        Asignatura aEliminar = new Asignatura("Arte");

        casoUso.eliminarAsignatura(aEliminar);

        verify(repoMock).eliminar(aEliminar);
    }
}