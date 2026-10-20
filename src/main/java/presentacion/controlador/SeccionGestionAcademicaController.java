package presentacion.controlador;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.StackPane;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class SeccionGestionAcademicaController implements Initializable {

    @FXML private ComboBox<String> cmbCategoria;
    @FXML private StackPane contenedorCategorias;

    @FXML private TabCatalogosController tabPaneCatalogosController;
    @FXML private TabAsignacionesController tabPaneAsignacionesController;
    @FXML private TabTutoriasController tabPaneTutoriasController;
    @FXML private TabCoordinacionController tabPaneCoordinacionController;
    @FXML private TabHorariosController tabPaneHorariosController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        configurarCategoria();
    }

    private void configurarCategoria() {
        cmbCategoria.getItems().addAll("Catálogos", "Asig. Docente", "Asig. Tutor", "Coordinación", "Horarios");
        cmbCategoria.getSelectionModel().selectFirst();

        cmbCategoria.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (tabPaneCatalogosController == null) return;

            boolean cat = "Catálogos".equals(newVal);
            boolean asig = "Asig. Docente".equals(newVal);
            boolean tut = "Asig. Tutor".equals(newVal);
            boolean coord = "Coordinación".equals(newVal);
            boolean hor = "Horarios".equals(newVal);

            tabPaneCatalogosController.getTabPane().setVisible(cat);
            tabPaneCatalogosController.getTabPane().setManaged(cat);

            if (tabPaneAsignacionesController != null) {
                tabPaneAsignacionesController.getRoot().setVisible(asig);
                tabPaneAsignacionesController.getRoot().setManaged(asig);
            }
            if (tabPaneTutoriasController != null) {
                tabPaneTutoriasController.getRoot().setVisible(tut);
                tabPaneTutoriasController.getRoot().setManaged(tut);
            }
            if (tabPaneCoordinacionController != null) {
                tabPaneCoordinacionController.getRoot().setVisible(coord);
                tabPaneCoordinacionController.getRoot().setManaged(coord);
            }
            if (tabPaneHorariosController != null) {
                tabPaneHorariosController.getRoot().setVisible(hor);
                tabPaneHorariosController.getRoot().setManaged(hor);
            }
        });

        // Ocultar todos excepto catálogos al inicio
        if (tabPaneAsignacionesController != null) {
            tabPaneAsignacionesController.getRoot().setVisible(false);
            tabPaneAsignacionesController.getRoot().setManaged(false);
        }
        if (tabPaneTutoriasController != null) {
            tabPaneTutoriasController.getRoot().setVisible(false);
            tabPaneTutoriasController.getRoot().setManaged(false);
        }
        if (tabPaneCoordinacionController != null) {
            tabPaneCoordinacionController.getRoot().setVisible(false);
            tabPaneCoordinacionController.getRoot().setManaged(false);
        }
        if (tabPaneHorariosController != null) {
            tabPaneHorariosController.getRoot().setVisible(false);
            tabPaneHorariosController.getRoot().setManaged(false);
        }
    }
}
