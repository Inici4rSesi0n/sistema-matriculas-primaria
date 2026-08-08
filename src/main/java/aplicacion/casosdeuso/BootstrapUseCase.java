package aplicacion.casosdeuso;

import aplicacion.puerto.ProveedorDatosIniciales;
import dominio.modelo.Administrador;
import dominio.modelo.FranjaHoraria;
import dominio.modelo.Grado;
import dominio.modelo.PeriodoAcademico;
import dominio.modelo.Recreo;
import dominio.modelo.Turno;
import dominio.puerto.externo.HashProvider;
import infraestructura.persistencia.ManejadorPersistencia;
import infraestructura.seguridad.UtilLimpieza;
import java.io.FileOutputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author inici4rsesi0n
 */
public class BootstrapUseCase {

    private static final String ARCHIVO_ADMINISTRADORES = "administradores.bin";
    private static final String ARCHIVO_TURNOS = "turnos.bin";
    private static final String ARCHIVO_GRADOS = "grados.bin";
    private static final String ARCHIVO_RECREOS = "recreos.bin";
    private static final String ARCHIVO_PERIODOS = "periodos.bin";

    private static final String ARCHIVO_KEYSTORE = "data/keystore.jceks";
    private static final String CLAVE_KEYSTORE = "S1st3maMatr1culas2026";
    private static final String ALIAS_CLAVE = "aes-key";

    private static final int ITERACIONES_PBKDF2 = 100000;
    private static final int LONGITUD_CLAVE_AES = 256;

    private final HashProvider hashProvider;

    public BootstrapUseCase(HashProvider hashProvider) {
        this.hashProvider = hashProvider;
    }

    public void inicializarSistema(ProveedorDatosIniciales proveedor) {
        List<Administrador> administradores = ManejadorPersistencia.cargar(ARCHIVO_ADMINISTRADORES);

        if (administradores == null || administradores.isEmpty()) {
            if (proveedor == null) {
                throw new IllegalArgumentException("El proveedor de datos del administrador no puede ser nulo");
            }

            Administrador admin = crearAdministrador(proveedor);

            generarKeystore(proveedor.getContrasenaMaestra());
            ManejadorPersistencia.recargarClave();

            administradores = new ArrayList<>();
            administradores.add(admin);
            ManejadorPersistencia.guardar(new ArrayList<>(administradores), ARCHIVO_ADMINISTRADORES);

            inicializarCatalogos();
        }
    }

    private Administrador crearAdministrador(ProveedorDatosIniciales proveedor) {
        String codigo = proveedor.getCodigo();
        String nombre = proveedor.getNombre();
        String apellido = proveedor.getApellido();
        String dni = proveedor.getDni();
        char[] contraseña = proveedor.getContrasena();

        if (codigo == null || codigo.isBlank()
                || nombre == null || nombre.isBlank()
                || apellido == null || apellido.isBlank()
                || dni == null || dni.isBlank()
                || contraseña == null) {
            throw new IllegalArgumentException("Todos los campos del administrador son obligatorios");
        }

        String hash = hashProvider.generarHash(contraseña);
        UtilLimpieza.limpiarContraseña(contraseña);

        return new Administrador(codigo, hash, dni, nombre, apellido, 30);
    }

    private void generarKeystore(char[] contrasenaMaestra) {
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

    private void inicializarCatalogos() {
        inicializarTurnos();
        inicializarGrados();
        inicializarRecreos();
        inicializarPeriodos();
    }

    private void inicializarTurnos() {
        List<Turno> turnos = ManejadorPersistencia.cargar(ARCHIVO_TURNOS);
        if (turnos == null || turnos.isEmpty()) {
            turnos = new ArrayList<>();
            turnos.add(new Turno("Mañana"));
            turnos.add(new Turno("Tarde"));
            ManejadorPersistencia.guardar(new ArrayList<>(turnos), ARCHIVO_TURNOS);
        }
    }

    private void inicializarGrados() {
        List<Grado> grados = ManejadorPersistencia.cargar(ARCHIVO_GRADOS);
        if (grados == null || grados.isEmpty()) {
            grados = new ArrayList<>();
            String[] nombres = {"1er Grado", "2do Grado", "3er Grado",
                                "4to Grado", "5to Grado", "6to Grado"};
            for (String nombre : nombres) {
                grados.add(new Grado(nombre, "Primaria"));
            }
            ManejadorPersistencia.guardar(new ArrayList<>(grados), ARCHIVO_GRADOS);
        }
    }

    private void inicializarRecreos() {
        List<Recreo> recreos = ManejadorPersistencia.cargar(ARCHIVO_RECREOS);
        if (recreos == null || recreos.isEmpty()) {
            recreos = new ArrayList<>();
            PeriodoAcademico periodo = new PeriodoAcademico("2026-I", "2026-01-01", "2026-12-31", "Activo");
            FranjaHoraria franja = new FranjaHoraria("Lunes", "10:15", "10:45");
            recreos.add(new Recreo(franja, "Recreo", periodo));
            ManejadorPersistencia.guardar(new ArrayList<>(recreos), ARCHIVO_RECREOS);
        }
    }

    private void inicializarPeriodos() {
        List<PeriodoAcademico> periodos = ManejadorPersistencia.cargar(ARCHIVO_PERIODOS);
        if (periodos == null || periodos.isEmpty()) {
            periodos = new ArrayList<>();
            periodos.add(new PeriodoAcademico("2026-I", "2026-01-01", "2026-12-31", "Activo"));
            ManejadorPersistencia.guardar(new ArrayList<>(periodos), ARCHIVO_PERIODOS);
        }
    }
}