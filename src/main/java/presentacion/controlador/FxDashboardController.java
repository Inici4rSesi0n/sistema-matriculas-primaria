package presentacion.controlador;

import aplicacion.casosdeuso.GestionAsignaturas;
import aplicacion.casosdeuso.GestionDocentes;
import aplicacion.casosdeuso.GestionEstudiantes;
import aplicacion.casosdeuso.GestionGrupos;
import aplicacion.casosdeuso.GestionPermisos;
import dominio.modelo.Usuario;
import infraestructura.configuracion.SpringContext;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author inici4rsesi0n
 */
public class FxDashboardController implements Initializable {

    @FXML private Label lblUsuarioHeader, lblBienvenida;
    @FXML private Label lblNumEstudiantes, lblNumDocentes, lblNumGrupos, lblNumAsignaturas;
    @FXML private Button btnMisCursos, btnMiHorario, btnCalificaciones, btnSubirMaterial, btnRegistrarAsistencia;
    @FXML private Button btnTramites, btnMatricula;
    @FXML private Button btnGestionUsuarios, btnGestionAcademica, btnReportes, btnConfiguracionSistema;
    @FXML private Button btnVolverInicio;
    @FXML private StackPane contenedorCentral;

    private Usuario usuario;
    private GestionPermisos gestionPermisos;
    private final Map<String, Pane> vistasCargadas = new HashMap<>();
    private final Map<String, URL> rutasVistas = new HashMap<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        gestionPermisos = SpringContext.getBean(GestionPermisos.class);
        precargarRutas();
    }

    private void precargarRutas() {
        rutasVistas.put("s_GestionUsuarios", getClass().getResource("/fxml/s_GestionUsuarios.fxml"));
        rutasVistas.put("s_GestionAcademica", getClass().getResource("/fxml/s_GestionAcademica.fxml"));
        rutasVistas.put("s_Matricula", getClass().getResource("/fxml/s_Matricula.fxml"));
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        configurarVisibilidadPorRol();
        cargarDatosIniciales();
    }

    private void configurarVisibilidadPorRol() {
        if (usuario == null) return;
        Usuario.Rol rol = usuario.getRol();

        btnMisCursos.setVisible(gestionPermisos.esSeccionVisible(rol, "MisCursos"));
        btnMiHorario.setVisible(gestionPermisos.esSeccionVisible(rol, "MiHorario"));
        btnCalificaciones.setVisible(gestionPermisos.esSeccionVisible(rol, "Calificaciones"));
        btnSubirMaterial.setVisible(gestionPermisos.esSeccionVisible(rol, "SubirMaterial"));
        btnRegistrarAsistencia.setVisible(gestionPermisos.esSeccionVisible(rol, "RegistrarAsistencia"));

        btnTramites.setVisible(gestionPermisos.esSeccionVisible(rol, "Tramites"));
        btnMatricula.setVisible(gestionPermisos.esSeccionVisible(rol, "Matricula"));

        btnGestionUsuarios.setVisible(gestionPermisos.esSeccionVisible(rol, "GestionUsuarios"));
        btnGestionAcademica.setVisible(gestionPermisos.esSeccionVisible(rol, "GestionAcademica"));
        btnReportes.setVisible(gestionPermisos.esSeccionVisible(rol, "Reportes"));
        btnConfiguracionSistema.setVisible(gestionPermisos.esSeccionVisible(rol, "ConfiguracionSistema"));

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
        lblNumEstudiantes.setText(String.valueOf(SpringContext.getBean(GestionEstudiantes.class).listarTodos().size()));
        lblNumDocentes.setText(String.valueOf(SpringContext.getBean(GestionDocentes.class).listarTodos().size()));
        lblNumGrupos.setText(String.valueOf(SpringContext.getBean(GestionGrupos.class).listarTodos().size()));
        lblNumAsignaturas.setText(String.valueOf(SpringContext.getBean(GestionAsignaturas.class).listarTodos().size()));
    }

    @FXML
    private void cargarSeccion(javafx.event.ActionEvent event) {
        Button boton = (Button) event.getSource();
        String clave = (String) boton.getUserData();
        if (clave == null) return;

        Pane vista = vistasCargadas.get(clave);
        if (vista == null) {
            URL ruta = rutasVistas.get(clave);
            if (ruta == null) {
                System.err.println("No hay ruta registrada para: " + clave);
                return;
            }
            try {
                FXMLLoader loader = new FXMLLoader(ruta);
                vista = loader.load();
                vistasCargadas.put(clave, vista);
            } catch (IOException e) {
                System.err.println("Error al cargar la vista: " + clave);
                e.printStackTrace();
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