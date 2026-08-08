package presentacion.controlador;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.StackPane;
/**
 *
 * @author inici4rsesi0n
 */
public class s_GestionAcademicaController implements Initializable {

    @FXML private ComboBox<String> cmbCategoria;
    @FXML private StackPane contenedorCategorias;

    @FXML private tab_CatalogosController tabPaneCatalogosController;
    @FXML private tab_AsignacionesController tabPaneAsignacionesController;
    @FXML private tab_HorariosController tabPaneHorariosController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        configurarCategoria();
    }

    private void configurarCategoria() {
        cmbCategoria.getItems().addAll("Catálogos", "Asignaciones", "Horarios");
        cmbCategoria.getSelectionModel().selectFirst();

        cmbCategoria.valueProperty().addListener((obs, oldVal, newVal) -> {
            boolean cat = "Catálogos".equals(newVal);
            boolean asig = "Asignaciones".equals(newVal);
            boolean hor = "Horarios".equals(newVal);

            tabPaneCatalogosController.getTabPane().setVisible(cat);
            tabPaneCatalogosController.getTabPane().setManaged(cat);

            tabPaneAsignacionesController.getTabPane().setVisible(asig);
            tabPaneAsignacionesController.getTabPane().setManaged(asig);

            tabPaneHorariosController.getTabPane().setVisible(hor);
            tabPaneHorariosController.getTabPane().setManaged(hor);
        });

        tabPaneAsignacionesController.getTabPane().setVisible(false);
        tabPaneAsignacionesController.getTabPane().setManaged(false);
        tabPaneHorariosController.getTabPane().setVisible(false);
        tabPaneHorariosController.getTabPane().setManaged(false);
    }
}