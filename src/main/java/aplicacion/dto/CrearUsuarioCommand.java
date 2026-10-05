package aplicacion.dto;
import dominio.modelo.Usuario;
/**
 *
 * @author inici4rsesi0n
 */
public record CrearUsuarioCommand(
        String codigo,
        String hash,
        String dni,
        String nombre,
        String apellido,
        int edad,
        Usuario.Rol rol,
        String especialidad
) {
}
