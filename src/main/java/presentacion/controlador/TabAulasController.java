package presentacion.controlador;

import aplicacion.casosdeuso.GestionAulas;
import dominio.modelo.Aula;
import dominio.modelo.ModalidadAula;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Controlador de la pestaña "Aulas".
 *
 * @author inici4rsesi0n
 */
@Component
@Scope("prototype")
public class TabAulasController implements Initializable {

    private final GestionAulas gestionAulas;

    @FXML private TableView<Aula> tablaAulas;
    @FXML private TableColumn<Aula, Integer> colNumAula;
    @FXML private TableColumn<Aula, String> colNombreAula, colUbicacionAula, colTipoAula;
    @FXML private TableColumn<Aula, Integer> colCapacidadAula;
    @FXML private TableColumn<Aula, ModalidadAula> colModalidadAula;
    @FXML private TableColumn<Aula, Void> colAccionesAula;
    @FXML private VBox panelFormularioAula;
    @FXML private TextField txtNombreAula, txtCapacidadAula, txtUbicacionAula, txtTipoAula;
    @FXML private ComboBox<ModalidadAula> cmbModalidadAula;
    @FXML private Button btnNuevaAula, btnGuardarAula, btnCancelarAula;

    private ObservableList<Aula> listaAulas;
    private Aula aulaEditando;

    public TabAulasController(GestionAulas gestionAulas) {
        this.gestionAulas = gestionAulas;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        configurarComboModalidad();
        configurarTabla();
        cargarAulas();
        SistemaEventBus.suscribir(TipoEvento.AULAS, this::cargarAulas);
    }

    private void configurarComboModalidad() {
        cmbModalidadAula.getItems().setAll(ModalidadAula.values());
        cmbModalidadAula.setConverter(new StringConverter<>() {
            @Override
            public String toString(ModalidadAula modalidad) {
                return modalidad != null ? modalidad.name() : "";
            }
            @Override
            public ModalidadAula fromString(String string) {
                return ModalidadAula.fromString(string);
            }
        });
        cmbModalidadAula.getSelectionModel().selectFirst();
    }

    private void configurarTabla() {
        colNumAula.setCellFactory(col -> new TableCell<>() {
            @Override protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : String.valueOf(getIndex() + 1));
            }
        });
        colNombreAula.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colCapacidadAula.setCellValueFactory(new PropertyValueFactory<>("capacidad"));
        colUbicacionAula.setCellValueFactory(new PropertyValueFactory<>("ubicacion"));
        colTipoAula.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colModalidadAula.setCellValueFactory(new PropertyValueFactory<>("modalidad"));
        configurarAcciones();
    }

    private void configurarAcciones() {
        colAccionesAula.setCellFactory(param -> new TableCell<>() {
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

    private void cargarAulas() {
        Platform.runLater(() -> {
            listaAulas = FXCollections.observableArrayList(gestionAulas.listarTodos());
            tablaAulas.setItems(listaAulas);
            tablaAulas.refresh();
        });
    }

    private void cargarEnFormulario(Aula a) {
        aulaEditando = a;
        txtNombreAula.setText(a.getNombre());
        txtCapacidadAula.setText(String.valueOf(a.getCapacidad()));
        txtUbicacionAula.setText(a.getUbicacion());
        txtTipoAula.setText(a.getTipo());
        cmbModalidadAula.setValue(a.getModalidad());
        panelFormularioAula.setVisible(true);
        panelFormularioAula.setManaged(true);
    }

    private void eliminar(Aula a) {
        if (!Dialogos.confirmar("Confirmar eliminación", "¿Está seguro de eliminar este registro?")) return;
        try {
            gestionAulas.eliminarAula(a);
            SistemaEventBus.notificar(TipoEvento.AULAS);
        } catch (IllegalArgumentException e) {
            Dialogos.info(e.getMessage());
        }
    }

    @FXML
    private void handleNuevaAula() {
        aulaEditando = null;
        txtNombreAula.clear();
        txtCapacidadAula.clear();
        txtUbicacionAula.clear();
        txtTipoAula.clear();
        cmbModalidadAula.getSelectionModel().selectFirst();
        panelFormularioAula.setVisible(true);
        panelFormularioAula.setManaged(true);
    }

    @FXML
    private void handleGuardarAula() {
        String nombre = txtNombreAula.getText().trim();
        String capacidadStr = txtCapacidadAula.getText().trim();
        String ubicacion = txtUbicacionAula.getText().trim();
        String tipo = txtTipoAula.getText().trim();
        ModalidadAula modalidad = cmbModalidadAula.getValue();

        if (nombre.isBlank() || capacidadStr.isBlank() || ubicacion.isBlank() || tipo.isBlank()) {
            Dialogos.info("Todos los campos son obligatorios.");
            return;
        }
        int capacidad;
        try {
            capacidad = Integer.parseInt(capacidadStr);
        } catch (NumberFormatException e) {
            Dialogos.info("La capacidad debe ser un número entero válido");
            return;
        }
        try {
            if (aulaEditando == null) gestionAulas.crearAula(nombre, capacidad, ubicacion, tipo, modalidad);
            else gestionAulas.actualizarAula(aulaEditando, nombre, capacidad, ubicacion, tipo, modalidad);
            SistemaEventBus.notificar(TipoEvento.AULAS);
            panelFormularioAula.setVisible(false);
            panelFormularioAula.setManaged(false);
        } catch (IllegalArgumentException e) {
            Dialogos.info(e.getMessage());
        }
    }

    @FXML
    private void handleCancelarAula() {
        panelFormularioAula.setVisible(false);
        panelFormularioAula.setManaged(false);
    }
}
