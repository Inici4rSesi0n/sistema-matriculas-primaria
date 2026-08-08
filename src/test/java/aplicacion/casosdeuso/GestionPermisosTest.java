package aplicacion.casosdeuso;

import dominio.modelo.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author inici4rsesi0n
 */
class GestionPermisosTest {

    private GestionPermisos gestionPermisos;

    @BeforeEach
    void setUp() {
        gestionPermisos = new GestionPermisos();
    }

    @Test
    void administrador_debeVerTodasLasSecciones() {
        Usuario.Rol rol = Usuario.Rol.ADMINISTRADOR;
        assertTrue(gestionPermisos.esSeccionVisible(rol, "MisCursos"));
        assertTrue(gestionPermisos.esSeccionVisible(rol, "MiHorario"));
        assertTrue(gestionPermisos.esSeccionVisible(rol, "Calificaciones"));
        assertTrue(gestionPermisos.esSeccionVisible(rol, "SubirMaterial"));
        assertTrue(gestionPermisos.esSeccionVisible(rol, "RegistrarAsistencia"));
        assertTrue(gestionPermisos.esSeccionVisible(rol, "Tramites"));
        assertTrue(gestionPermisos.esSeccionVisible(rol, "Matricula"));
        assertTrue(gestionPermisos.esSeccionVisible(rol, "GestionUsuarios"));
        assertTrue(gestionPermisos.esSeccionVisible(rol, "GestionAcademica"));
        assertTrue(gestionPermisos.esSeccionVisible(rol, "Reportes"));
        assertTrue(gestionPermisos.esSeccionVisible(rol, "ConfiguracionSistema"));
    }

    @Test
    void docente_debeVerSoloSeccionesPermitidas() {
        Usuario.Rol rol = Usuario.Rol.DOCENTE;
        assertTrue(gestionPermisos.esSeccionVisible(rol, "MisCursos"));
        assertTrue(gestionPermisos.esSeccionVisible(rol, "SubirMaterial"));
        assertFalse(gestionPermisos.esSeccionVisible(rol, "GestionUsuarios"));
        assertFalse(gestionPermisos.esSeccionVisible(rol, "ConfiguracionSistema"));
        assertFalse(gestionPermisos.esSeccionVisible(rol, "Tramites"));
    }

    @Test
    void estudiante_debeVerSoloSeccionesPermitidas() {
        Usuario.Rol rol = Usuario.Rol.ESTUDIANTE;
        assertTrue(gestionPermisos.esSeccionVisible(rol, "Calificaciones"));
        assertTrue(gestionPermisos.esSeccionVisible(rol, "Matricula"));
        assertFalse(gestionPermisos.esSeccionVisible(rol, "SubirMaterial"));
        assertFalse(gestionPermisos.esSeccionVisible(rol, "GestionUsuarios"));
    }

    @Test
    void padre_debeVerSoloSeccionesPermitidas() {
        Usuario.Rol rol = Usuario.Rol.PADRE;
        assertTrue(gestionPermisos.esSeccionVisible(rol, "Tramites"));
        assertTrue(gestionPermisos.esSeccionVisible(rol, "Matricula"));
        assertFalse(gestionPermisos.esSeccionVisible(rol, "GestionAcademica"));
        assertFalse(gestionPermisos.esSeccionVisible(rol, "SubirMaterial"));
    }

    @Test
    void seccionInexistente_debeRetornarFalse() {
        assertFalse(gestionPermisos.esSeccionVisible(Usuario.Rol.ADMINISTRADOR, "SeccionInventada"));
    }
}