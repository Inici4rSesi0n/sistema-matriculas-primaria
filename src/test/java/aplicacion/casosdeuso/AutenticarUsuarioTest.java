package aplicacion.casosdeuso;

import dominio.modelo.Docente;
import dominio.modelo.Secretario;
import dominio.modelo.Usuario;
import dominio.puerto.externo.HashProvider;
import dominio.puerto.externo.LoggerPort;
import dominio.puerto.repositorio.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 *
 * @author inici4rsesi0n
 */
class AutenticarUsuarioTest {

    private RepositorioAdministradores repoAdmin;
    private RepositorioDirectores repoDir;
    private RepositorioDocentes repoDoc;
    private RepositorioEstudiantes repoEst;
    private RepositorioSecretarios repoSec;
    private RepositorioCoordinadores repoCoord;
    private RepositorioPadres repoPad;
    private HashProvider hashProvider;
    private LoggerPort loggerPort;

    private AutenticarUsuario autenticarUsuario;

    @BeforeEach
    void setUp() {
        repoAdmin = mock(RepositorioAdministradores.class);
        repoDir = mock(RepositorioDirectores.class);
        repoDoc = mock(RepositorioDocentes.class);
        repoEst = mock(RepositorioEstudiantes.class);
        repoSec = mock(RepositorioSecretarios.class);
        repoCoord = mock(RepositorioCoordinadores.class);
        repoPad = mock(RepositorioPadres.class);
        hashProvider = mock(HashProvider.class);
        loggerPort = mock(LoggerPort.class);

        autenticarUsuario = new AutenticarUsuario(repoAdmin, repoDir, repoDoc,
                repoEst, repoSec, repoCoord, repoPad, hashProvider, loggerPort);
    }

    @Test
    void ejecutar_debeAutenticarDocenteCorrectamente() {
        String codigo = "D001";
        char[] contraseña = "secreta".toCharArray();
        Docente docente = new Docente(codigo, "hash", "123", "Juan", "Perez", 30, "Mat", new ArrayList<>());

        when(repoAdmin.buscarPorCodigo(codigo)).thenReturn(Optional.empty());
        when(repoDir.buscarPorCodigo(codigo)).thenReturn(Optional.empty());
        when(repoDoc.buscarPorCodigo(codigo)).thenReturn(Optional.of(docente));
        when(hashProvider.verificarHash("hash", contraseña)).thenReturn(true);

        Usuario resultado = autenticarUsuario.ejecutar(codigo, contraseña);

        assertNotNull(resultado);
        assertEquals(docente, resultado);
        verify(repoEst, never()).buscarPorCodigo(anyString());
    }

    @Test
    void ejecutar_debeFallarSiCodigoNoExiste() {
        String codigo = "XXX";
        char[] contraseña = "secreta".toCharArray();

        when(repoAdmin.buscarPorCodigo(codigo)).thenReturn(Optional.empty());
        when(repoDir.buscarPorCodigo(codigo)).thenReturn(Optional.empty());
        when(repoDoc.buscarPorCodigo(codigo)).thenReturn(Optional.empty());
        when(repoEst.buscarPorCodigo(codigo)).thenReturn(Optional.empty());
        when(repoSec.buscarPorCodigo(codigo)).thenReturn(Optional.empty());
        when(repoCoord.buscarPorCodigo(codigo)).thenReturn(Optional.empty());
        when(repoPad.buscarPorCodigo(codigo)).thenReturn(Optional.empty());

        Usuario resultado = autenticarUsuario.ejecutar(codigo, contraseña);

        assertNull(resultado);
        verify(hashProvider, never()).verificarHash(anyString(), any());
    }

    @Test
    void ejecutar_debeFallarSiContraseñaIncorrecta() {
        String codigo = "D001";
        char[] contraseña = "incorrecta".toCharArray();
        Docente docente = new Docente(codigo, "hash", "123", "Ana", "Lopez", 28, "Ing", new ArrayList<>());

        when(repoAdmin.buscarPorCodigo(codigo)).thenReturn(Optional.empty());
        when(repoDir.buscarPorCodigo(codigo)).thenReturn(Optional.empty());
        when(repoDoc.buscarPorCodigo(codigo)).thenReturn(Optional.of(docente));
        when(hashProvider.verificarHash("hash", contraseña)).thenReturn(false);

        Usuario resultado = autenticarUsuario.ejecutar(codigo, contraseña);

        assertNull(resultado);
    }

    @Test
    void ejecutar_debeBuscarEnTodosLosRepositoriosHastaEncontrar() {
        String codigo = "S001";
        char[] contraseña = "pass".toCharArray();
        Secretario secretario = mock(Secretario.class);
        when(secretario.getHashContrasena()).thenReturn("hashS");

        when(repoAdmin.buscarPorCodigo(codigo)).thenReturn(Optional.empty());
        when(repoDir.buscarPorCodigo(codigo)).thenReturn(Optional.empty());
        when(repoDoc.buscarPorCodigo(codigo)).thenReturn(Optional.empty());
        when(repoEst.buscarPorCodigo(codigo)).thenReturn(Optional.empty());
        when(repoSec.buscarPorCodigo(codigo)).thenReturn(Optional.of(secretario));
        when(hashProvider.verificarHash("hashS", contraseña)).thenReturn(true);

        Usuario resultado = autenticarUsuario.ejecutar(codigo, contraseña);

        assertNotNull(resultado);
        verify(repoCoord, never()).buscarPorCodigo(codigo);
        verify(repoPad, never()).buscarPorCodigo(codigo);
    }

    @Test
    void ejecutar_debeLanzarExcepcionSiCodigoVacio() {
        assertThrows(IllegalArgumentException.class,
                () -> autenticarUsuario.ejecutar("", "pass".toCharArray()));
    }

    @Test
    void ejecutar_debeLanzarExcepcionSiContraseñaNula() {
        assertThrows(IllegalArgumentException.class,
                () -> autenticarUsuario.ejecutar("D001", null));
    }
}