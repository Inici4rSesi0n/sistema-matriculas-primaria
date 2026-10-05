package presentacion.controlador;

import aplicacion.casosdeuso.GestionAsignaturas;
import dominio.modelo.Asignatura;
import presentacion.dialogos.Dialogos;
import presentacion.eventos.SistemaEventBus;
import presentacion.eventos.TipoEvento;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 *
 * @author inici4rsesi0n
 */
@Component
@Scope("prototype")
public class TabAsignaturasController implements Initializable {

    private final GestionAsignaturas gestionAsignaturas;

    @FXML private TableView<Asignatura> tablaAsignaturas;
    @FXML private TableColumn<Asignatura, Integer> colNumAsignatura;
    @FXML private TableColumn<Asignatura, String> colNombreAsignatura;
    @FXML private TableColumn<Asignatura, Void> colAccionesAsignatura;
    @FXML private VBox panelFormularioAsignatura;
    @FXML private TextField txtNombreAsignatura;
    @FXML private Button btnNuevaAsignatura, btnGuardarAsignatura, btnCancelarAsignatura;

    private ObservableList<Asignatura> listaAsignaturas;
    private Asignatura asignaturaEditando;

    public TabAsignaturasController(GestionAsignaturas gestionAsignaturas) {
        this.gestionAsignaturas = gestionAsignaturas;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        configurarTabla();
        cargarAsignaturas();
        SistemaEventBus.suscribir(TipoEvento.ASIGNATURAS, this::cargarAsignaturas);
    }

    private void configurarTabla() {
        colNumAsignatura.setCellFactory(col -> new TableCell<>() {
            @Override protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : String.valueOf(getIndex() + 1));
            }
        });
        colNombreAsignatura.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        configurarAcciones();
    }

    private void configurarAcciones() {
        colAccionesAsignatura.setCellFactory(param -> new TableCell<>() {
            private final Button btnEditar = new Button("Editar");
            private final Button btnEliminar = new Button("Eliminar");
            private final HBox hbox = new HBox(10, btnEditar, btnEliminar);
            {
                btnEditar.getStyleClass().add("boton-tabla-editar");
                btnEliminar.getStyleClass().add("boton-tabla-eliminar");
                btnEditar.setOnAction(e -> cargarEnFormulario(getTableView().getItems().get(getIndex())));
                btnEliminar.setOnAction(e -> eliminar(getTableView().getItems().get(getIndex())));
            }
            @Override protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : hbox);
            }
        });
    }

    private void cargarAsignaturas() {
        Platform.runLater(() -> {
            listaAsignaturas = FXCollections.observableArrayList(gestionAsignaturas.listarTodos());
            tablaAsignaturas.setItems(listaAsignaturas);
            tablaAsignaturas.refresh();
        });
    }

    private void cargarEnFormulario(Asignatura a) {
        asignaturaEditando = a;
        txtNombreAsignatura.setText(a.getNombre());
        panelFormularioAsignatura.setVisible(true);
        panelFormularioAsignatura.setManaged(true);
    }

    private void eliminar(Asignatura a) {
        if (!Dialogos.confirmar("Confirmar eliminación", "¿Está seguro de eliminar este registro?")) return;
        try {
            gestionAsignaturas.eliminarAsignatura(a);
            SistemaEventBus.notificar(TipoEvento.ASIGNATURAS);
        } catch (IllegalArgumentException e) {
            Dialogos.info(e.getMessage());
        }
    }

    @FXML
    private void handleNuevaAsignatura() {
        asignaturaEditando = null;
        txtNombreAsignatura.clear();
        panelFormularioAsignatura.setVisible(true);
        panelFormularioAsignatura.setManaged(true);
    }

    @FXML
    private void handleGuardarAsignatura() {
        String nombre = txtNombreAsignatura.getText().trim();
        if (nombre.isBlank()) {
            Dialogos.info("El nombre no puede estar vacío.");
            return;
        }
        try {
            if (asignaturaEditando == null) gestionAsignaturas.crearAsignatura(nombre);
            else gestionAsignaturas.actualizarAsignatura(asignaturaEditando, nombre);
            SistemaEventBus.notificar(TipoEvento.ASIGNATURAS);
            panelFormularioAsignatura.setVisible(false);
            panelFormularioAsignatura.setManaged(false);
        } catch (IllegalArgumentException e) {
            Dialogos.info(e.getMessage());
        }
    }

    @FXML
    private void handleCancelarAsignatura() {
        panelFormularioAsignatura.setVisible(false);
        panelFormularioAsignatura.setManaged(false);
    }
}
