package aplicacion.puerto;
/**
 *
 * @author inici4rsesi0n
 */
public interface ProveedorDatosIniciales {
    String getCodigo();
    String getNombre();
    String getApellido();
    String getDni();
    char[] getContrasena();
    char[] getContrasenaMaestra();
    boolean isConfirmado();
}