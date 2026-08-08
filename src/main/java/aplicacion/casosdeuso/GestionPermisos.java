package aplicacion.casosdeuso;

import dominio.modelo.Usuario;

/**
 *
 * @author inici4rsesi0n
 */
public class GestionPermisos {

    public boolean esSeccionVisible(Usuario.Rol rol, String seccion) {
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
}