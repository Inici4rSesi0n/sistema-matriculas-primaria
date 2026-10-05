package dominio.puerto.externo;

import java.io.Serializable;

/**
 * Puerto para las operaciones de inicialización del sistema.
 * Abstrae la infraestructura de persistencia y la generación del keystore
 * para que la capa de aplicación no dependa de ella.
 *
 * @author inici4rsesi0n
 */
public interface ServicioInicializacion {

    /** Carga un objeto serializable desde el archivo indicado. */
    <T extends Serializable> T cargar(String nombreArchivo);

    /** Persiste un objeto serializable en el archivo indicado. */
    void guardar(Serializable objeto, String nombreArchivo);

    /** Recarga la clave AES desde el keystore en memoria. */
    void recargarClave();

    /**
     * Genera un nuevo keystore a partir de la contraseña maestra.
     * El adaptador limpia el array al finalizar.
     */
    void generarKeystore(char[] contrasenaMaestra);
}
