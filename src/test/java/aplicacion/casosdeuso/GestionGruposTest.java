package aplicacion.casosdeuso;

import dominio.modelo.Docente;
import dominio.modelo.Grado;
import dominio.modelo.Grupo;
import dominio.puerto.repositorio.RepositorioGrupos;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 *
 * @author inici4rsesi0n
 */
class GestionGruposTest {

    private RepositorioGrupos repoMock;
    private GestionGrupos casoUso;
    private Grado grado;

    @BeforeEach
    void setUp() {
        repoMock = mock(RepositorioGrupos.class);
        casoUso = new GestionGrupos(repoMock);
        grado = new Grado("1er Grado", "Primaria");
    }

    @Test
    void crearGrupo_debeGuardarCuandoNoExiste() {
        when(repoMock.buscarPorNombre("1A")).thenReturn(Optional.empty());

        casoUso.crearGrupo("1A", grado);

        verify(repoMock).agregar(any(Grupo.class));
    }

    @Test
    void crearGrupo_debeLanzarExcepcionCuandoYaExiste() {
        when(repoMock.buscarPorNombre("1A")).thenReturn(Optional.of(new Grupo("1A", grado)));

        assertThrows(IllegalArgumentException.class, () -> casoUso.crearGrupo("1A", grado));
        verify(repoMock, never()).agregar(any());
    }

    @Test
    void buscarPorNombre_debeRetornarGrupoSiExiste() {
        Grupo esperado = new Grupo("1A", grado);
        when(repoMock.buscarPorNombre("1A")).thenReturn(Optional.of(esperado));

        Grupo resultado = casoUso.buscarPorNombre("1A");

        assertEquals(esperado, resultado);
    }

    @Test
    void buscarPorNombre_debeRetornarNullSiNoExiste() {
        when(repoMock.buscarPorNombre("Inexistente")).thenReturn(Optional.empty());

        Grupo resultado = casoUso.buscarPorNombre("Inexistente");

        assertNull(resultado);
    }

    @Test
    void listarTodos_debeRetornarListaCompleta() {
        List<Grupo> lista = List.of(new Grupo("1A", grado), new Grupo("1B", grado));
        when(repoMock.listarTodos()).thenReturn(lista);

        List<Grupo> resultado = casoUso.listarTodos();

        assertEquals(2, resultado.size());
    }

    @Test
    void buscarPorNombreYGrado_debeRetornarFiltrados() {
        List<Grupo> filtrados = List.of(new Grupo("1A", grado));
        when(repoMock.buscarPorNombreYGrado("1A", "1er Grado")).thenReturn(filtrados);

        List<Grupo> resultado = casoUso.buscarPorNombreYGrado("1A", "1er Grado");

        assertEquals(1, resultado.size());
    }

    @Test
    void actualizarGrupo_debeActualizarConNuevosDatos() {
        Grupo original = new Grupo("Viejo", grado);
        Grado nuevoGrado = new Grado("2do Grado", "Primaria");

        casoUso.actualizarGrupo(original, "Nuevo", nuevoGrado);

        verify(repoMock).actualizar(eq(original), any(Grupo.class));
    }

    @Test
    void eliminarGrupo_debeEliminarCorrectamente() {
        Grupo aEliminar = new Grupo("1A", grado);

        casoUso.eliminarGrupo(aEliminar);

        verify(repoMock).eliminar(aEliminar);
    }

    @Test
    void asignarTutor_debeAsignarDocenteYActualizar() {
        Grupo grupo = new Grupo("1A", grado);
        Docente tutor = new Docente("D001", "hash", "111", "Juan", "Perez", 30, "Mat", new ArrayList<>());

        casoUso.asignarTutor(grupo, tutor);

        assertEquals(tutor, grupo.getTutor());
        verify(repoMock).actualizar(grupo, grupo);
    }

    @Test
    void removerTutor_debeQuitarDocenteYActualizar() {
        Grupo grupo = new Grupo("1A", grado);
        Docente tutor = new Docente("D001", "hash", "111", "Juan", "Perez", 30, "Mat", new ArrayList<>());
        grupo.asignarTutor(tutor); // precondición: ya tiene tutor

        casoUso.removerTutor(grupo);

        assertNull(grupo.getTutor());
        verify(repoMock).actualizar(grupo, grupo);
    }
}