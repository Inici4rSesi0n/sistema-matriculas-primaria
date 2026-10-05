package presentacion.controlador;
import aplicacion.casosdeuso.GestionAsignaturas;
import aplicacion.casosdeuso.GestionDocentes;
import aplicacion.casosdeuso.GestionEstudiantes;
import aplicacion.casosdeuso.GestionGrupos;
import aplicacion.casosdeuso.GestionPermisos;
import aplicacion.casosdeuso.GestionPermisos.SeccionMenu;
import dominio.modelo.Usuario;
import infraestructura.configuracion.SpringFxmlLoader;
import presentacion.dialogos.Dialogos;
import presentacion.eventos.SistemaEventBus;
import presentacion.eventos.TipoEvento;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
/**
 *
 * @author inici4rsesi0n
 */
@Component
@Scope("prototype")
public class DashboardController implements Initializable {

    private final GestionPermisos gestionPermisos;
    private final GestionEstudiantes gestionEstudiantes;
    private final GestionDocentes gestionDocentes;
    private final GestionGrupos gestionGrupos;
    private final GestionAsignaturas gestionAsignaturas;
    private final SpringFxmlLoader fxmlLoader;

    @FXML private Label lblUsuarioHeader, lblBienvenida;
    @FXML private Label lblNumEstudiantes, lblNumDocentes, lblNumGrupos, lblNumAsignaturas;
    @FXML private Button btnMisCursos, btnMiHorario, btnCalificaciones, btnSubirMaterial, btnRegistrarAsistencia;
    @FXML private Button btnTramites, btnMatricula;
    @FXML private Button btnGestionUsuarios, btnGestionAcademica, btnReportes, btnConfiguracionSistema;
    @FXML private Button btnVolverInicio;
    @FXML private StackPane contenedorCentral;

    private Usuario usuario;
    private final Map<String, Pane> vistasCargadas = new HashMap<>();
    private final Map<String, String> rutasVistas = new HashMap<>();

    public DashboardController(GestionPermisos gestionPermisos,
                                 GestionEstudiantes gestionEstudiantes,
                                 GestionDocentes gestionDocentes,
                                 GestionGrupos gestionGrupos,
                                 GestionAsignaturas gestionAsignaturas,
                                 SpringFxmlLoader fxmlLoader) {
        this.gestionPermisos = gestionPermisos;
        this.gestionEstudiantes = gestionEstudiantes;
        this.gestionDocentes = gestionDocentes;
        this.gestionGrupos = gestionGrupos;
        this.gestionAsignaturas = gestionAsignaturas;
        this.fxmlLoader = fxmlLoader;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        precargarRutas();

        SistemaEventBus.suscribir(TipoEvento.ESTUDIANTES, this::actualizarTarjetas);
        SistemaEventBus.suscribir(TipoEvento.DOCENTES, this::actualizarTarjetas);
        SistemaEventBus.suscribir(TipoEvento.GRUPOS, this::actualizarTarjetas);
        SistemaEventBus.suscribir(TipoEvento.ASIGNATURAS, this::actualizarTarjetas);
    }

    private void precargarRutas() {
        rutasVistas.put("s_GestionUsuarios", "/fxml/s_GestionUsuarios.fxml");
        rutasVistas.put("s_GestionAcademica", "/fxml/s_GestionAcademica.fxml");
        rutasVistas.put("s_Matricula", "/fxml/s_Matricula.fxml");
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        configurarVisibilidadPorRol();
        cargarDatosIniciales();
    }

    private void configurarVisibilidadPorRol() {
        if (usuario == null) return;
        Usuario.Rol rol = usuario.getRol();

        btnMisCursos.setVisible(gestionPermisos.esSeccionVisible(rol, SeccionMenu.MIS_CURSOS));
        btnMiHorario.setVisible(gestionPermisos.esSeccionVisible(rol, SeccionMenu.MI_HORARIO));
        btnCalificaciones.setVisible(gestionPermisos.esSeccionVisible(rol, SeccionMenu.CALIFICACIONES));
        btnSubirMaterial.setVisible(gestionPermisos.esSeccionVisible(rol, SeccionMenu.SUBIR_MATERIAL));
        btnRegistrarAsistencia.setVisible(gestionPermisos.esSeccionVisible(rol, SeccionMenu.REGISTRAR_ASISTENCIA));
        btnTramites.setVisible(gestionPermisos.esSeccionVisible(rol, SeccionMenu.TRAMITES));
        btnMatricula.setVisible(gestionPermisos.esSeccionVisible(rol, SeccionMenu.MATRICULA));
        btnGestionUsuarios.setVisible(gestionPermisos.esSeccionVisible(rol, SeccionMenu.GESTION_USUARIOS));
        btnGestionAcademica.setVisible(gestionPermisos.esSeccionVisible(rol, SeccionMenu.GESTION_ACADEMICA));
        btnReportes.setVisible(gestionPermisos.esSeccionVisible(rol, SeccionMenu.REPORTES));
        btnConfiguracionSistema.setVisible(gestionPermisos.esSeccionVisible(rol, SeccionMenu.CONFIGURACION_SISTEMA));

        lblUsuarioHeader.setText(usuario.getNombre() + " " + usuario.getApellido());
        lblBienvenida.setText("Bienvenido, " + usuario.getNombre() + ". Rol: " + rol);
    }

    private void cargarDatosIniciales() {
        if (usuario == null) return;
        actualizarTarjetas();
        if (usuario.getRol() != Usuario.Rol.ADMINISTRADOR && usuario.getRol() != Usuario.Rol.DIRECTOR) {
            ((GridPane) lblNumEstudiantes.getParent().getParent()).setVisible(false);
            lblBienvenida.setText("Bienvenido, " + usuario.getNombre());
        }
    }
    private void actualizarTarjetas() {
        if (usuario == null) return;
        Platform.runLater(() -> {
            lblNumEstudiantes.setText(String.valueOf(gestionEstudiantes.listarTodos().size()));
            lblNumDocentes.setText(String.valueOf(gestionDocentes.listarTodos().size()));
            lblNumGrupos.setText(String.valueOf(gestionGrupos.listarTodos().size()));
            lblNumAsignaturas.setText(String.valueOf(gestionAsignaturas.listarTodos().size()));
        });
    }

    @FXML
    private void cargarSeccion(javafx.event.ActionEvent event) {
        Button boton = (Button) event.getSource();
        String clave = (String) boton.getUserData();
        if (clave == null) return;

        Pane vista = vistasCargadas.get(clave);
        if (vista == null) {
            String ruta = rutasVistas.get(clave);
            if (ruta == null) {
                Dialogos.info("Sección no disponible: " + clave);
                return;
            }
            try {
                FXMLLoader loader = fxmlLoader.crearLoader(ruta);
                vista = loader.load();
                vistasCargadas.put(clave, vista);
            } catch (IOException e) {
                Dialogos.info("No se pudo cargar la sección: " + clave);
                return;
            }
        }
        contenedorCentral.getChildren().setAll(vista);
        actualizarTarjetas();
    }

    @FXML
    private void handleVolverInicio() {
        cerrarVentana();
    }

    private void cerrarVentana() {
        if (btnVolverInicio != null && btnVolverInicio.getScene() != null) {
            Stage stage = (Stage) btnVolverInicio.getScene().getWindow();
            if (stage != null) {
                stage.close();
            }
        }
    }
}
