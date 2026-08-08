package infraestructura.persistencia;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.KeyStore;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;

/**
 *
 * @author inici4rsesi0n
 */
public class ManejadorPersistencia {

    private static final String KEYSTORE_FILE_NAME = "keystore.jceks";
    private static final String KEYSTORE_PASSWORD = "S1st3maMatr1culas2026";
    private static final String KEY_ALIAS = "aes-key";
    private static final int GCM_IV_LENGTH = 12;
    private static final int GCM_TAG_LENGTH = 128;
    private static String directorioTrabajo = "data";
    private static boolean cifradoActivo = true;
    private static SecretKey claveAES;

    static {
        File dir = new File(directorioTrabajo);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        cargarClaveDesdeKeystore();
    }

    public static void setDirectorioTrabajo(String directorio) {
        if (directorio == null || directorio.isBlank()) {
            throw new IllegalArgumentException("El directorio de trabajo no puede ser nulo o vacío.");
        }
        directorioTrabajo = directorio;
        File dir = new File(directorioTrabajo);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        claveAES = null;
        cargarClaveDesdeKeystore();
    }

    public static void setCifradoActivo(boolean activo) {
        cifradoActivo = activo;
        if (activo) {
            cargarClaveDesdeKeystore();
        }
    }

    public static void recargarClave() {
        cargarClaveDesdeKeystore();
    }

    private static void cargarClaveDesdeKeystore() {
        File keystoreFile = new File(directorioTrabajo, KEYSTORE_FILE_NAME);
        if (!keystoreFile.exists()) {
            claveAES = null;
            return;
        }
        try (FileInputStream fis = new FileInputStream(keystoreFile)) {
            KeyStore ks = KeyStore.getInstance("JCEKS");
            ks.load(fis, KEYSTORE_PASSWORD.toCharArray());

            KeyStore.ProtectionParameter protParam =
                    new KeyStore.PasswordProtection(KEYSTORE_PASSWORD.toCharArray());
            KeyStore.SecretKeyEntry entry = (KeyStore.SecretKeyEntry) ks.getEntry(KEY_ALIAS, protParam);

            if (entry != null) {
                claveAES = entry.getSecretKey();
            }
        } catch (Exception e) {
            claveAES = null;
        }
    }

    public static boolean isCifradoActivo() {
        return cifradoActivo && claveAES != null;
    }

    public static <T extends Serializable> T cargar(String nombreArchivo) {
        File file = new File(directorioTrabajo, nombreArchivo);
        if (!file.exists()) {
            return null;
        }
        try (FileInputStream fis = new FileInputStream(file);
             ObjectInputStream ois = isCifradoActivo()
                     ? new ObjectInputStream(crearInputStreamDescifrado(fis))
                     : new ObjectInputStream(fis)) {
            return (T) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("No se pudo cargar el archivo: " + nombreArchivo, e);
        }
    }

    public static void guardar(Serializable objeto, String nombreArchivo) {
        File file = new File(directorioTrabajo, nombreArchivo);
        try (FileOutputStream fos = new FileOutputStream(file);
             ObjectOutputStream oos = isCifradoActivo()
                     ? new ObjectOutputStream(crearOutputStreamCifrado(fos))
                     : new ObjectOutputStream(fos)) {
            oos.writeObject(objeto);
        } catch (IOException e) {
            throw new RuntimeException("No se pudo guardar el archivo: " + nombreArchivo, e);
        }
    }

    private static CipherOutputStream crearOutputStreamCifrado(FileOutputStream fos) throws IOException {
        try {
            byte[] iv = new byte[GCM_IV_LENGTH];
            new SecureRandom().nextBytes(iv);
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
            cipher.init(Cipher.ENCRYPT_MODE, claveAES, spec);
            fos.write(iv);
            return new CipherOutputStream(fos, cipher);
        } catch (Exception e) {
            throw new IOException("Error al configurar el cifrado.", e);
        }
    }

    private static CipherInputStream crearInputStreamDescifrado(FileInputStream fis) throws IOException {
        try {
            byte[] iv = new byte[GCM_IV_LENGTH];
            if (fis.read(iv) != GCM_IV_LENGTH) {
                throw new IOException("El archivo está corrupto o no contiene un IV válido.");
            }
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
            cipher.init(Cipher.DECRYPT_MODE, claveAES, spec);
            return new CipherInputStream(fis, cipher);
        } catch (Exception e) {
            throw new IOException("Error al configurar el descifrado.", e);
        }
    }
}