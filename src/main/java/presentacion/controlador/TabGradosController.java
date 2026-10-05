package presentacion.controlador;

import aplicacion.casosdeuso.GestionGrados;
import dominio.modelo.Grado;
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
 * Controlador de la pestaña "Grados".
 *
 * @author inici4rsesi0n
 */
@Component
@Scope("prototype")
public class TabGradosController implements Initializable {

    private final GestionGrados gestionGrados;

    @FXML private TableView<Grado> tablaGrados;
    @FXML private TableColumn<Grado, Integer> colNumGrado;
    @FXML private TableColumn<Grado, String> colNombreGrado, colNivelGrado;
    @FXML private TableColumn<Grado, Void> colAccionesGrado;
    @FXML private VBox panelFormularioGrado;
    @FXML private TextField txtNombreGrado, txtNivelGrado;
    @FXML private Button btnNuevoGrado, btnGuardarGrado, btnCancelarGrado;

    private ObservableList<Grado> listaGrados;
    private Grado gradoEditando;

    public TabGradosController(GestionGrados gestionGrados) {
        this.gestionGrados = gestionGrados;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        configurarTabla();
        cargarGrados();
        SistemaEventBus.suscribir(TipoEvento.GRADOS, this::cargarGrados);
    }

    private void configurarTabla() {
        colNumGrado.setCellFactory(col -> new TableCell<>() {
            @Override protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : String.valueOf(getIndex() + 1));
            }
        });
        colNombreGrado.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colNivelGrado.setCellValueFactory(new PropertyValueFactory<>("nivel"));
        configurarAcciones();
    }

    private void configurarAcciones() {
        colAccionesGrado.setCellFactory(param -> new TableCell<>() {
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

    private void cargarGrados() {
        Platform.runLater(() -> {
            listaGrados = FXCollections.observableArrayList(gestionGrados.listarTodos());
            tablaGrados.setItems(listaGrados);
            tablaGrados.refresh();
        });
    }

    private void cargarEnFormulario(Grado g) {
        gradoEditando = g;
        txtNombreGrado.setText(g.getNombre());
        txtNivelGrado.setText(g.getNivel());
        panelFormularioGrado.setVisible(true);
        panelFormularioGrado.setManaged(true);
    }

    private void eliminar(Grado g) {
        if (!Dialogos.confirmar("Confirmar eliminación", "¿Está seguro de eliminar este registro?")) return;
        try {
            gestionGrados.eliminarGrado(g);
            SistemaEventBus.notificar(TipoEvento.GRADOS);
        } catch (IllegalArgumentException e) {
            Dialogos.info(e.getMessage());
        }
    }

    @FXML
    private void handleNuevoGrado() {
        gradoEditando = null;
        txtNombreGrado.clear();
        txtNivelGrado.clear();
        panelFormularioGrado.setVisible(true);
        panelFormularioGrado.setManaged(true);
    }

    @FXML
    private void handleGuardarGrado() {
        String nombre = txtNombreGrado.getText().trim();
        String nivel = txtNivelGrado.getText().trim();
        if (nombre.isBlank() || nivel.isBlank()) {
            Dialogos.info("El nombre y el nivel son obligatorios.");
            return;
        }
        try {
            if (gradoEditando == null) gestionGrados.crearGrado(nombre, nivel);
            else gestionGrados.actualizarGrado(gradoEditando, nombre, nivel);
            SistemaEventBus.notificar(TipoEvento.GRADOS);
            panelFormularioGrado.setVisible(false);
            panelFormularioGrado.setManaged(false);
        } catch (IllegalArgumentException e) {
            Dialogos.info(e.getMessage());
        }
    }

    @FXML
    private void handleCancelarGrado() {
        panelFormularioGrado.setVisible(false);
        panelFormularioGrado.setManaged(false);
    }
}
