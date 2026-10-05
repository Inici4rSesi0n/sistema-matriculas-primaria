package presentacion.controlador;

import aplicacion.casosdeuso.GestionTurnos;
import dominio.modelo.Turno;
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
 * Controlador de la pestaña "Turnos".
 *
 * @author inici4rsesi0n
 */
@Component
@Scope("prototype")
public class TabTurnosController implements Initializable {

    private final GestionTurnos gestionTurnos;

    @FXML private TableView<Turno> tablaTurnos;
    @FXML private TableColumn<Turno, Integer> colNumTurno;
    @FXML private TableColumn<Turno, String> colNombreTurno;
    @FXML private TableColumn<Turno, Void> colAccionesTurno;
    @FXML private VBox panelFormularioTurno;
    @FXML private TextField txtNombreTurno;
    @FXML private Button btnNuevoTurno, btnGuardarTurno, btnCancelarTurno;

    private ObservableList<Turno> listaTurnos;
    private Turno turnoEditando;

    public TabTurnosController(GestionTurnos gestionTurnos) {
        this.gestionTurnos = gestionTurnos;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        configurarTabla();
        cargarTurnos();
        SistemaEventBus.suscribir(TipoEvento.TURNOS, this::cargarTurnos);
    }

    private void configurarTabla() {
        colNumTurno.setCellFactory(col -> new TableCell<>() {
            @Override protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : String.valueOf(getIndex() + 1));
            }
        });
        colNombreTurno.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        configurarAcciones();
    }

    private void configurarAcciones() {
        colAccionesTurno.setCellFactory(param -> new TableCell<>() {
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

    private void cargarTurnos() {
        Platform.runLater(() -> {
            listaTurnos = FXCollections.observableArrayList(gestionTurnos.listarTodos());
            tablaTurnos.setItems(listaTurnos);
            tablaTurnos.refresh();
        });
    }

    private void cargarEnFormulario(Turno t) {
        turnoEditando = t;
        txtNombreTurno.setText(t.getNombre());
        panelFormularioTurno.setVisible(true);
        panelFormularioTurno.setManaged(true);
    }

    private void eliminar(Turno t) {
        if (!Dialogos.confirmar("Confirmar eliminación", "¿Está seguro de eliminar este registro?")) return;
        try {
            gestionTurnos.eliminarTurno(t);
            SistemaEventBus.notificar(TipoEvento.TURNOS);
        } catch (IllegalArgumentException e) {
            Dialogos.info(e.getMessage());
        }
    }

    @FXML
    private void handleNuevoTurno() {
        turnoEditando = null;
        txtNombreTurno.clear();
        panelFormularioTurno.setVisible(true);
        panelFormularioTurno.setManaged(true);
    }

    @FXML
    private void handleGuardarTurno() {
        String nombre = txtNombreTurno.getText().trim();
        if (nombre.isBlank()) {
            Dialogos.info("El nombre no puede estar vacío.");
            return;
        }
        try {
            if (turnoEditando == null) gestionTurnos.crearTurno(nombre);
            else gestionTurnos.actualizarTurno(turnoEditando, nombre);
            SistemaEventBus.notificar(TipoEvento.TURNOS);
            panelFormularioTurno.setVisible(false);
            panelFormularioTurno.setManaged(false);
        } catch (IllegalArgumentException e) {
            Dialogos.info(e.getMessage());
        }
    }

    @FXML
    private void handleCancelarTurno() {
        panelFormularioTurno.setVisible(false);
        panelFormularioTurno.setManaged(false);
    }
}
