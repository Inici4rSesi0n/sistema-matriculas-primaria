package aplicacion.casosdeuso;

import dominio.modelo.Aula;
import dominio.puerto.repositorio.RepositorioAulas;
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
class GestionAulasTest {

    private RepositorioAulas repoMock;
    private GestionAulas casoUso;

    @BeforeEach
    void setUp() {
        repoMock = mock(RepositorioAulas.class);
        casoUso = new GestionAulas(repoMock);
    }

    @Test
    void crearAula_debeGuardarCuandoNoExiste() {
        when(repoMock.buscarPorNombre("Laboratorio")).thenReturn(Optional.empty());

        casoUso.crearAula("Laboratorio", 30, "Edificio A", "Laboratorio");

        verify(repoMock).agregar(any(Aula.class));
    }

    @Test
    void crearAula_debeLanzarExcepcionCuandoYaExiste() {
        when(repoMock.buscarPorNombre("Laboratorio")).thenReturn(Optional.of(new Aula("Laboratorio", 30, "Edificio A", "Laboratorio")));

        assertThrows(IllegalArgumentException.class,
                () -> casoUso.crearAula("Laboratorio", 30, "Edificio A", "Laboratorio"));
        verify(repoMock, never()).agregar(any());
    }

    @Test
    void listarAulas_debeRetornarTodas() {
        List<Aula> lista = List.of(
                new Aula("Aula 101", 40, "Pabellón 1", "Teoría"),
                new Aula("Lab Física", 25, "Pabellón 2", "Laboratorio")
        );
        when(repoMock.listarTodos()).thenReturn(lista);

        List<Aula> resultado = casoUso.listarTodos();

        assertEquals(2, resultado.size());
        verify(repoMock).listarTodos();
    }

    @Test
    void buscarAula_debeRetornarAulaSiExiste() {
        Aula esperada = new Aula("Aula Magna", 100, "Rectorado", "Auditorio");
        when(repoMock.buscarPorNombre("Aula Magna")).thenReturn(Optional.of(esperada));

        Aula resultado = casoUso.buscarAula("Aula Magna");

        assertEquals(esperada, resultado);
    }

    @Test
    void buscarAula_debeRetornarNullSiNoExiste() {
        when(repoMock.buscarPorNombre("Inexistente")).thenReturn(Optional.empty());

        Aula resultado = casoUso.buscarAula("Inexistente");

        assertNull(resultado);
    }

    @Test
    void actualizarAula_debeActualizarConNuevosDatos() {
        Aula original = new Aula("Aula Vieja", 20, "Pabellón 3", "Teoría");
        String nuevoNombre = "Aula Renovada";
        int nuevaCapacidad = 30;
        String nuevaUbicacion = "Pabellón 4";
        String nuevoTipo = "Taller";

        casoUso.actualizarAula(original, nuevoNombre, nuevaCapacidad, nuevaUbicacion, nuevoTipo);

        verify(repoMock).actualizar(eq(original), any(Aula.class));
    }

    @Test
    void eliminarAula_debeEliminarCorrectamente() {
        Aula aEliminar = new Aula("Aula a borrar", 15, "Pabellón 5", "Teoría");

        casoUso.eliminarAula(aEliminar);

        verify(repoMock).eliminar(aEliminar);
    }
}