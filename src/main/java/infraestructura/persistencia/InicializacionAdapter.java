package infraestructura.persistencia;

import dominio.puerto.externo.ServicioInicializacion;
import infraestructura.seguridad.UtilLimpieza;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.io.Serializable;
import java.security.KeyStore;
import java.security.SecureRandom;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Adaptador del puerto ServicioInicializacion.
 * Delega la persistencia a ManejadorPersistencia y contiene la lógica
 * de generación del keystore AES (PBKDF2 + JCEKS).
 *
 * @author inici4rsesi0n
 */
@Component
public class InicializacionAdapter implements ServicioInicializacion {

    private static final String ARCHIVO_KEYSTORE = "data/keystore.jceks";
    private static final String CLAVE_KEYSTORE = "S1st3maMatr1culas2026";
    private static final String ALIAS_CLAVE = "aes-key";
    private static final int ITERACIONES_PBKDF2 = 100000;
    private static final int LONGITUD_CLAVE_AES = 256;

    @Override
    public <T extends Serializable> T cargar(String nombreArchivo) {
        return ManejadorPersistencia.cargar(nombreArchivo);
    }

    @Override
    public void guardar(Serializable objeto, String nombreArchivo) {
        ManejadorPersistencia.guardar(objeto, nombreArchivo);
    }

    @Override
    public void recargarClave() {
        ManejadorPersistencia.recargarClave();
    }

    @Override
    public void generarKeystore(char[] contrasenaMaestra) {
        try {
            byte[] salt = new byte[16];
            new SecureRandom().nextBytes(salt);

            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            PBEKeySpec spec = new PBEKeySpec(contrasenaMaestra, salt, ITERACIONES_PBKDF2, LONGITUD_CLAVE_AES);
            SecretKey tmp = factory.generateSecret(spec);
            SecretKey claveAES = new SecretKeySpec(tmp.getEncoded(), "AES");

            KeyStore ks = KeyStore.getInstance("JCEKS");
            ks.load(null, CLAVE_KEYSTORE.toCharArray());

            KeyStore.SecretKeyEntry entry = new KeyStore.SecretKeyEntry(claveAES);
            KeyStore.ProtectionParameter prot = new KeyStore.PasswordProtection(CLAVE_KEYSTORE.toCharArray());
            ks.setEntry(ALIAS_CLAVE, entry, prot);

            try (FileOutputStream fos = new FileOutputStream(ARCHIVO_KEYSTORE)) {
                ks.store(fos, CLAVE_KEYSTORE.toCharArray());
            }
        } catch (Exception e) {
            throw new RuntimeException("No se pudo generar el almacén de claves.", e);
        } finally {
            UtilLimpieza.limpiarContraseña(contrasenaMaestra);
        }
    }
}
