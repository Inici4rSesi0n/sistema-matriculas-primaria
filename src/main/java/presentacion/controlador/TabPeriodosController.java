package presentacion.controlador;

import aplicacion.casosdeuso.GestionPeriodos;
import dominio.modelo.PeriodoAcademico;
import presentacion.dialogos.Dialogos;
import presentacion.eventos.SistemaEventBus;
import presentacion.eventos.TipoEvento;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
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
 * Controlador de la pestaña "Periodos".
 *
 * @author inici4rsesi0n
 */
@Component
@Scope("prototype")
public class TabPeriodosController implements Initializable {

    private final GestionPeriodos gestionPeriodos;

    @FXML private TableView<PeriodoAcademico> tablaPeriodos;
    @FXML private TableColumn<PeriodoAcademico, Integer> colNumPeriodo;
    @FXML private TableColumn<PeriodoAcademico, String> colNombrePeriodo, colFechaInicio, colFechaFin, colEstadoPeriodo;
    @FXML private TableColumn<PeriodoAcademico, Void> colAccionesPeriodo;
    @FXML private VBox panelFormularioPeriodo;
    @FXML private TextField txtNombrePeriodo;
    @FXML private DatePicker dateFechaInicio, dateFechaFin;
    @FXML private ComboBox<String> cmbEstadoPeriodo;
    @FXML private Button btnNuevoPeriodo, btnGuardarPeriodo, btnCancelarPeriodo;

    private ObservableList<PeriodoAcademico> listaPeriodos;
    private PeriodoAcademico periodoEditando;

    public TabPeriodosController(GestionPeriodos gestionPeriodos) {
        this.gestionPeriodos = gestionPeriodos;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        configurarComboEstado();
        configurarTabla();
        cargarPeriodos();
        SistemaEventBus.suscribir(TipoEvento.PERIODOS, this::cargarPeriodos);
    }

    private void configurarComboEstado() {
        cmbEstadoPeriodo.getItems().addAll("Activo", "Inactivo", "Culminado", "Prorrogado");
        cmbEstadoPeriodo.getSelectionModel().selectFirst();
    }

    private void configurarTabla() {
        colNumPeriodo.setCellFactory(col -> new TableCell<>() {
            @Override protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : String.valueOf(getIndex() + 1));
            }
        });
        colNombrePeriodo.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colFechaInicio.setCellValueFactory(new PropertyValueFactory<>("fechaInicio"));
        colFechaFin.setCellValueFactory(new PropertyValueFactory<>("fechaFin"));
        colEstadoPeriodo.setCellValueFactory(new PropertyValueFactory<>("estado"));
        configurarAcciones();
    }

    private void configurarAcciones() {
        colAccionesPeriodo.setCellFactory(param -> new TableCell<>() {
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

    private void cargarPeriodos() {
        Platform.runLater(() -> {
            listaPeriodos = FXCollections.observableArrayList(gestionPeriodos.listarTodos());
            tablaPeriodos.setItems(listaPeriodos);
            tablaPeriodos.refresh();
        });
    }

    private void cargarEnFormulario(PeriodoAcademico p) {
        periodoEditando = p;
        txtNombrePeriodo.setText(p.getNombre());
        dateFechaInicio.setValue(LocalDate.parse(p.getFechaInicio()));
        dateFechaFin.setValue(LocalDate.parse(p.getFechaFin()));
        cmbEstadoPeriodo.setValue(p.getEstado());
        panelFormularioPeriodo.setVisible(true);
        panelFormularioPeriodo.setManaged(true);
    }

    private void eliminar(PeriodoAcademico p) {
        if (!Dialogos.confirmar("Confirmar eliminación", "¿Está seguro de eliminar este registro?")) return;
        try {
            gestionPeriodos.eliminarPeriodo(p);
            SistemaEventBus.notificar(TipoEvento.PERIODOS);
        } catch (IllegalArgumentException e) {
            Dialogos.info(e.getMessage());
        }
    }

    @FXML
    private void handleNuevoPeriodo() {
        periodoEditando = null;
        txtNombrePeriodo.clear();
        dateFechaInicio.setValue(null);
        dateFechaFin.setValue(null);
        cmbEstadoPeriodo.getSelectionModel().selectFirst();
        panelFormularioPeriodo.setVisible(true);
        panelFormularioPeriodo.setManaged(true);
    }

    @FXML
    private void handleGuardarPeriodo() {
        String nombre = txtNombrePeriodo.getText().trim();
        LocalDate inicio = dateFechaInicio.getValue();
        LocalDate fin = dateFechaFin.getValue();
        String estado = cmbEstadoPeriodo.getValue();
        if (nombre.isBlank() || inicio == null || fin == null || estado == null) {
            Dialogos.info("Todos los campos son obligatorios.");
            return;
        }
        try {
            if (periodoEditando == null) gestionPeriodos.crearPeriodo(nombre, inicio.toString(), fin.toString(), estado);
            else gestionPeriodos.actualizarPeriodo(periodoEditando, nombre, inicio.toString(), fin.toString(), estado);
            SistemaEventBus.notificar(TipoEvento.PERIODOS);
            panelFormularioPeriodo.setVisible(false);
            panelFormularioPeriodo.setManaged(false);
        } catch (IllegalArgumentException e) {
            Dialogos.info(e.getMessage());
        }
    }

    @FXML
    private void handleCancelarPeriodo() {
        panelFormularioPeriodo.setVisible(false);
        panelFormularioPeriodo.setManaged(false);
    }
}
