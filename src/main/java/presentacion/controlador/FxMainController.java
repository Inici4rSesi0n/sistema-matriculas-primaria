package presentacion.controlador;

import presentacion.animaciones.AnimacionesMain;
import presentacion.dialogos.Dialogos;
import presentacion.estadousuario.SesionUsuario;
import dominio.modelo.Usuario;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author inici4rsesi0n
 */
public class FxMainController implements Initializable {

    @FXML private Button btnPortalIngreso;
    @FXML private Button btnPortalTramites;
    @FXML private Button btnPortalMatricula;
    @FXML private Button btnCerrarSesion;
    @FXML private Label lblSchoolBase;
    @FXML private Label lblConectado;
    @FXML private Label lblUsuarioConectado;
    @FXML private MenuButton menuIdioma;

    private Timeline timelineSchoolBase;
    private Timeline timelineConectado;

    private final Map<String, URL> rutasVistas = new ConcurrentHashMap<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        timelineSchoolBase = AnimacionesMain.crearTimelinePulso(lblSchoolBase);
        timelineConectado = AnimacionesMain.crearTimelinePulso(lblConectado);

        menuIdioma.getItems().addAll(
            new MenuItem("Español"),
            new MenuItem("English")
        );

        actualizarIndicadorSesion();
        precargarRutas();
    }

    private void precargarRutas() {
        rutasVistas.put("main", getClass().getResource("/fxml/main.fxml"));
        rutasVistas.put("login", getClass().getResource("/fxml/login.fxml"));
        rutasVistas.put("dashboard", getClass().getResource("/fxml/dashboard.fxml"));
    }

    @FXML
    private void handlePortalIngreso() { navegar("INGRESO"); }

    @FXML
    private void handlePortalTramites() { navegar("TRAMITES"); }

    @FXML
    private void handlePortalMatricula() { navegar("MATRICULA"); }

    private void navegar(String modo) {
        if (SesionUsuario.isSesionActiva()) {
            Usuario usuario = SesionUsuario.getUsuarioActual();
            if (tienePermiso(usuario, modo)) {
                navegarAVista("dashboard", (Stage) btnPortalIngreso.getScene().getWindow(), usuario);
            } else {
                Dialogos.M1("No tiene permisos para acceder a este portal.");
            }
        } else {
            abrirLogin(modo);
        }
    }

    private boolean tienePermiso(Usuario usuario, String modo) {
        Usuario.Rol rol = usuario.getRol();
        switch (modo) {
            case "INGRESO":
                return rol == Usuario.Rol.ADMINISTRADOR || rol == Usuario.Rol.DIRECTOR ||
                       rol == Usuario.Rol.SECRETARIO || rol == Usuario.Rol.COORDINADOR ||
                       rol == Usuario.Rol.DOCENTE || rol == Usuario.Rol.ESTUDIANTE;
            case "TRAMITES":
                return rol == Usuario.Rol.ESTUDIANTE || rol == Usuario.Rol.PADRE || rol == Usuario.Rol.SECRETARIO;
            case "MATRICULA":
                return rol == Usuario.Rol.ESTUDIANTE || rol == Usuario.Rol.PADRE || rol == Usuario.Rol.SECRETARIO;
            default: return false;
        }
    }

    private void abrirLogin(String modo) {
        URL ruta = rutasVistas.get("login");
        if (ruta == null) {
            Dialogos.M1("Ventana de login no disponible.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(ruta);
            Parent root = loader.load();
            FxLoginController controller = loader.getController();

            Stage loginStage = new Stage(StageStyle.DECORATED);
            loginStage.setTitle("Inicio de Sesión - SchoolBase");
            loginStage.setScene(new Scene(root));

            loginStage.setOnCloseRequest(event -> Platform.exit());

            controller.limpiarCampos();
            controller.setModo(modo);

            Stage mainStage = (Stage) btnPortalIngreso.getScene().getWindow();
            loginStage.setX(mainStage.getX());
            loginStage.setY(mainStage.getY());
            loginStage.setWidth(mainStage.getWidth());
            loginStage.setHeight(mainStage.getHeight());
            loginStage.setMaximized(mainStage.isMaximized());

            mainStage.hide();

            loginStage.showAndWait();

            if (controller.isLoginExitoso()) {
                actualizarIndicadorSesion();
                mainStage.close();
                navegarAVista("dashboard", loginStage, controller.getUsuarioAutenticado());
            } else {
                mainStage.setX(loginStage.getX());
                mainStage.setY(loginStage.getY());
                mainStage.setWidth(loginStage.getWidth());
                mainStage.setHeight(loginStage.getHeight());
                mainStage.setMaximized(loginStage.isMaximized());
                mainStage.show();
            }
        } catch (IOException e) {
            Dialogos.M1("No se pudo abrir la ventana de inicio de sesión.");
            Stage mainStage = (Stage) btnPortalIngreso.getScene().getWindow();
            if (!mainStage.isShowing()) {
                mainStage.show();
            }
        }
    }

    private void navegarAVista(String vistaKey, Stage ventanaActual, Usuario usuario) {
        URL ruta = rutasVistas.get(vistaKey);
        if (ruta == null) {
            Dialogos.M1("Vista no disponible: " + vistaKey);
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(ruta);
            Parent root = loader.load();
            Object controller = loader.getController();

            Stage nuevaVentana = new Stage(StageStyle.DECORATED);
            nuevaVentana.setScene(new Scene(root));

            nuevaVentana.setX(ventanaActual.getX());
            nuevaVentana.setY(ventanaActual.getY());
            nuevaVentana.setWidth(ventanaActual.getWidth());
            nuevaVentana.setHeight(ventanaActual.getHeight());

            if ("dashboard".equals(vistaKey)) {
                nuevaVentana.setTitle("SchoolBase - Panel de Control");
                FxDashboardController dashboardController = (FxDashboardController) controller;
                dashboardController.setUsuario(usuario);

                nuevaVentana.setOnCloseRequest(event -> Platform.exit());

                nuevaVentana.setOnHidden(event -> {
                    navegarAVista("main", nuevaVentana, null);
                });
            } else if ("main".equals(vistaKey)) {
                nuevaVentana.setTitle("SchoolBase - Sistema de Gestión Educativa");
            }

            ventanaActual.close();

            nuevaVentana.show();
            if (ventanaActual.isMaximized()) {
                nuevaVentana.setMaximized(true);
            }

        } catch (IOException e) {
            Dialogos.M1("No se pudo cargar la vista: " + vistaKey);
        }
    }

    @FXML
    private void handleCerrarSesion() {
        SesionUsuario.cerrarSesion();
        actualizarIndicadorSesion();
        Dialogos.M1("Sesión cerrada correctamente.");
    }

    private void actualizarIndicadorSesion() {
        boolean activa = SesionUsuario.isSesionActiva();
        btnCerrarSesion.setVisible(activa);
        if (activa) {
            lblUsuarioConectado.setText("Conectado: " + SesionUsuario.getUsuarioActual().getNombre());
        } else {
            lblUsuarioConectado.setText("");
        }
    }

    @FXML
    private void handleMouseEntered(MouseEvent event) {
        Label label = (Label) event.getSource();
        Timeline tl = (label == lblSchoolBase) ? timelineSchoolBase : timelineConectado;
        tl.stop();
        tl.playFromStart();
    }

    @FXML
    private void handleMouseExited(MouseEvent event) {
        Label label = (Label) event.getSource();
        Timeline tl = (label == lblSchoolBase) ? timelineSchoolBase : timelineConectado;
        tl.stop();
        label.setScaleX(1.0);
        label.setScaleY(1.0);
    }
}