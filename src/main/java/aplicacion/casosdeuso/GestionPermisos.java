package aplicacion.casosdeuso;

import dominio.modelo.Usuario;
import org.springframework.stereotype.Service;

/**
 * Servicio central de reglas de visibilidad por rol.
 * Es el ÚNICO lugar donde se define qué ve cada rol.
 *
 * Dos dimensiones de permisos:
 *   - esSeccionVisible   : secciones del dashboard (menú lateral)
 *   - puedeAccederPortal : portales de la pantalla principal (Ingreso, Trámites, Matrícula)
 *
 * @author inici4rsesi0n
 */
@Service
public class GestionPermisos {

    /** Visibilidad de secciones del dashboard (menú lateral). */
    public boolean esSeccionVisible(Usuario.Rol rol, String seccion) {
        if (rol == null || seccion == null) return false;
        switch (seccion) {
            case "MisCursos":
            case "MiHorario":
            case "Calificaciones":
                return rol == Usuario.Rol.ADMINISTRADOR
                        || rol == Usuario.Rol.DOCENTE
                        || rol == Usuario.Rol.ESTUDIANTE
                        || rol == Usuario.Rol.PADRE;

            case "SubirMaterial":
            case "RegistrarAsistencia":
                return rol == Usuario.Rol.ADMINISTRADOR
                        || rol == Usuario.Rol.DOCENTE;

            case "Tramites":
            case "Matricula":
                return rol == Usuario.Rol.ADMINISTRADOR
                        || rol == Usuario.Rol.SECRETARIO
                        || rol == Usuario.Rol.ESTUDIANTE
                        || rol == Usuario.Rol.PADRE;

            case "GestionUsuarios":
                return rol == Usuario.Rol.ADMINISTRADOR
                        || rol == Usuario.Rol.DIRECTOR;

            case "GestionAcademica":
            case "Reportes":
                return rol == Usuario.Rol.ADMINISTRADOR
                        || rol == Usuario.Rol.DIRECTOR
                        || rol == Usuario.Rol.COORDINADOR;

            case "ConfiguracionSistema":
                return rol == Usuario.Rol.ADMINISTRADOR;

            default:
                return false;
        }
    }

    /**
     * Acceso a los portales de la pantalla principal.
     * Preserva el comportamiento histórico de FxMainController.
     */
    public boolean puedeAccederPortal(Usuario.Rol rol, String portal) {
        if (rol == null || portal == null) return false;
        switch (portal) {
            case "INGRESO":
                return rol == Usuario.Rol.ADMINISTRADOR
                        || rol == Usuario.Rol.DIRECTOR
                        || rol == Usuario.Rol.SECRETARIO
                        || rol == Usuario.Rol.COORDINADOR
                        || rol == Usuario.Rol.DOCENTE
                        || rol == Usuario.Rol.ESTUDIANTE;

            case "TRAMITES":
            case "MATRICULA":
                return rol == Usuario.Rol.ESTUDIANTE
                        || rol == Usuario.Rol.PADRE
                        || rol == Usuario.Rol.SECRETARIO;

            default:
                return false;
        }
    }
}
