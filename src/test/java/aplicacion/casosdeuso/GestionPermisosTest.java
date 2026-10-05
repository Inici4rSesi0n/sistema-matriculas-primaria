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

    // ============ esSeccionVisible (dashboard) ============

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

    @Test
    void esSeccionVisible_conRolNulo_debeRetornarFalse() {
        assertFalse(gestionPermisos.esSeccionVisible(null, "MisCursos"));
    }

    @Test
    void esSeccionVisible_conSeccionNula_debeRetornarFalse() {
        assertFalse(gestionPermisos.esSeccionVisible(Usuario.Rol.ADMINISTRADOR, null));
    }

    // ============ puedeAccederPortal (pantalla principal) ============

    @Test
    void puedeAccederPortal_ingreso_paraRolesAutorizados() {
        assertTrue(gestionPermisos.puedeAccederPortal(Usuario.Rol.ADMINISTRADOR, "INGRESO"));
        assertTrue(gestionPermisos.puedeAccederPortal(Usuario.Rol.DIRECTOR, "INGRESO"));
        assertTrue(gestionPermisos.puedeAccederPortal(Usuario.Rol.SECRETARIO, "INGRESO"));
        assertTrue(gestionPermisos.puedeAccederPortal(Usuario.Rol.COORDINADOR, "INGRESO"));
        assertTrue(gestionPermisos.puedeAccederPortal(Usuario.Rol.DOCENTE, "INGRESO"));
        assertTrue(gestionPermisos.puedeAccederPortal(Usuario.Rol.ESTUDIANTE, "INGRESO"));
    }

    @Test
    void puedeAccederPortal_ingreso_padreNoAutorizado() {
        assertFalse(gestionPermisos.puedeAccederPortal(Usuario.Rol.PADRE, "INGRESO"));
    }

    @Test
    void puedeAccederPortal_tramites_paraRolesAutorizados() {
        assertTrue(gestionPermisos.puedeAccederPortal(Usuario.Rol.ESTUDIANTE, "TRAMITES"));
        assertTrue(gestionPermisos.puedeAccederPortal(Usuario.Rol.PADRE, "TRAMITES"));
        assertTrue(gestionPermisos.puedeAccederPortal(Usuario.Rol.SECRETARIO, "TRAMITES"));
    }

    @Test
    void puedeAccederPortal_tramites_administradorNoAutorizado() {
        assertFalse(gestionPermisos.puedeAccederPortal(Usuario.Rol.ADMINISTRADOR, "TRAMITES"));
        assertFalse(gestionPermisos.puedeAccederPortal(Usuario.Rol.DOCENTE, "TRAMITES"));
        assertFalse(gestionPermisos.puedeAccederPortal(Usuario.Rol.DIRECTOR, "TRAMITES"));
    }

    @Test
    void puedeAccederPortal_matricula_paraRolesAutorizados() {
        assertTrue(gestionPermisos.puedeAccederPortal(Usuario.Rol.ESTUDIANTE, "MATRICULA"));
        assertTrue(gestionPermisos.puedeAccederPortal(Usuario.Rol.PADRE, "MATRICULA"));
        assertTrue(gestionPermisos.puedeAccederPortal(Usuario.Rol.SECRETARIO, "MATRICULA"));
    }

    @Test
    void puedeAccederPortal_matricula_administradorNoAutorizado() {
        assertFalse(gestionPermisos.puedeAccederPortal(Usuario.Rol.ADMINISTRADOR, "MATRICULA"));
        assertFalse(gestionPermisos.puedeAccederPortal(Usuario.Rol.DOCENTE, "MATRICULA"));
    }

    @Test
    void puedeAccederPortal_portalInexistente_debeRetornarFalse() {
        assertFalse(gestionPermisos.puedeAccederPortal(Usuario.Rol.ADMINISTRADOR, "PORTAL_RARO"));
    }

    @Test
    void puedeAccederPortal_conRolNulo_debeRetornarFalse() {
        assertFalse(gestionPermisos.puedeAccederPortal(null, "INGRESO"));
    }

    @Test
    void puedeAccederPortal_conPortalNulo_debeRetornarFalse() {
        assertFalse(gestionPermisos.puedeAccederPortal(Usuario.Rol.ADMINISTRADOR, null));
    }
}
