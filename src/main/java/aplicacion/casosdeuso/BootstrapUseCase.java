package aplicacion.casosdeuso;

import aplicacion.puerto.ProveedorDatosIniciales;
import dominio.modelo.Administrador;
import dominio.modelo.FranjaHoraria;
import dominio.modelo.Grado;
import dominio.modelo.PeriodoAcademico;
import dominio.modelo.Recreo;
import dominio.modelo.Turno;
import dominio.puerto.externo.HashProvider;
import dominio.puerto.externo.LoggerPort;
import dominio.puerto.externo.ServicioInicializacion;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author inici4rsesi0n
 */
@Service
public class BootstrapUseCase {

    private static final String ARCHIVO_ADMINISTRADORES = "administradores.bin";
    private static final String ARCHIVO_TURNOS = "turnos.bin";
    private static final String ARCHIVO_GRADOS = "grados.bin";
    private static final String ARCHIVO_RECREOS = "recreos.bin";
    private static final String ARCHIVO_PERIODOS = "periodos.bin";

    private final HashProvider hashProvider;
    private final LoggerPort logger;
    private final ServicioInicializacion inicializacion;

    public BootstrapUseCase(HashProvider hashProvider,
                            LoggerPort logger,
                            ServicioInicializacion inicializacion) {
        this.hashProvider = hashProvider;
        this.logger = logger;
        this.inicializacion = inicializacion;
    }

    public void inicializarSistema(ProveedorDatosIniciales proveedor) {
        List<Administrador> administradores = inicializacion.cargar(ARCHIVO_ADMINISTRADORES);

        if (administradores == null || administradores.isEmpty()) {
            logger.info("No se encontraron administradores. Iniciando configuración inicial del sistema.");
            if (proveedor == null) {
                throw new IllegalArgumentException("El proveedor de datos del administrador no puede ser nulo");
            }

            Administrador admin = crearAdministrador(proveedor);
            logger.info("Administrador {} creado correctamente.", admin.getCodigo());

            try {
                inicializacion.generarKeystore(proveedor.getContrasenaMaestra());
                inicializacion.recargarClave();
                logger.info("Keystore generado y clave recargada.");
            } catch (RuntimeException e) {
                logger.error("Error al generar el keystore", e);
                throw e;
            }

            administradores = new ArrayList<>();
            administradores.add(admin);
            inicializacion.guardar(new ArrayList<>(administradores), ARCHIVO_ADMINISTRADORES);

            inicializarCatalogos();
            logger.info("Catálogos inicializados exitosamente.");
        } else {
            logger.info("Sistema ya inicializado. Se encontraron {} administradores.", administradores.size());
        }
    }

    private Administrador crearAdministrador(ProveedorDatosIniciales proveedor) {
        String codigo = proveedor.getCodigo();
        String nombre = proveedor.getNombre();
        String apellido = proveedor.getApellido();
        String dni = proveedor.getDni();
        char[] contrasena = proveedor.getContrasena();

        if (codigo == null || codigo.isBlank()
                || nombre == null || nombre.isBlank()
                || apellido == null || apellido.isBlank()
                || dni == null || dni.isBlank()
                || contrasena == null) {
            throw new IllegalArgumentException("Todos los campos del administrador son obligatorios");
        }

        // hashProvider.generarHash limpia el array internamente
        String hash = hashProvider.generarHash(contrasena);

        return new Administrador(codigo, hash, dni, nombre, apellido, 30);
    }

    private void inicializarCatalogos() {
        inicializarTurnos();
        inicializarGrados();
        inicializarRecreos();
        inicializarPeriodos();
    }

    private void inicializarTurnos() {
        List<Turno> turnos = inicializacion.cargar(ARCHIVO_TURNOS);
        if (turnos == null || turnos.isEmpty()) {
            turnos = new ArrayList<>();
            turnos.add(new Turno("Mañana"));
            turnos.add(new Turno("Tarde"));
            inicializacion.guardar(new ArrayList<>(turnos), ARCHIVO_TURNOS);
        }
    }

    private void inicializarGrados() {
        List<Grado> grados = inicializacion.cargar(ARCHIVO_GRADOS);
        if (grados == null || grados.isEmpty()) {
            grados = new ArrayList<>();
            String[] nombres = {"1er Grado", "2do Grado", "3er Grado",
                                "4to Grado", "5to Grado", "6to Grado"};
            for (String nombre : nombres) {
                grados.add(new Grado(nombre, "Primaria"));
            }
            inicializacion.guardar(new ArrayList<>(grados), ARCHIVO_GRADOS);
        }
    }

    private void inicializarRecreos() {
        List<Recreo> recreos = inicializacion.cargar(ARCHIVO_RECREOS);
        if (recreos == null || recreos.isEmpty()) {
            recreos = new ArrayList<>();
            PeriodoAcademico periodo = new PeriodoAcademico("2026-I", "2026-01-01", "2026-12-31", "Activo");
            FranjaHoraria franja = new FranjaHoraria("Lunes", "10:15", "10:45");
            recreos.add(new Recreo(franja, "Recreo", periodo));
            inicializacion.guardar(new ArrayList<>(recreos), ARCHIVO_RECREOS);
        }
    }

    private void inicializarPeriodos() {
        List<PeriodoAcademico> periodos = inicializacion.cargar(ARCHIVO_PERIODOS);
        if (periodos == null || periodos.isEmpty()) {
            periodos = new ArrayList<>();
            periodos.add(new PeriodoAcademico("2026-I", "2026-01-01", "2026-12-31", "Activo"));
            inicializacion.guardar(new ArrayList<>(periodos), ARCHIVO_PERIODOS);
        }
    }
}
