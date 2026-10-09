package presentacion.controlador;

import aplicacion.casosdeuso.GestionGrados;
import aplicacion.casosdeuso.GestionGrupos;
import dominio.modelo.Grado;
import dominio.modelo.Grupo;
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

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Controlador de la pestaña "Grupos".
 * También gestiona el combo de grados, exclusivo de esta pestaña.
 *
 * @author inici4rsesi0n
 */
@Component
@Scope("prototype")
public class TabGruposController implements Initializable {

    private final GestionGrupos gestionGrupos;
    private final GestionGrados gestionGrados;

    @FXML private TableView<Grupo> tablaGrupos;
    @FXML private TableColumn<Grupo, Integer> colNumGrupo;
    @FXML private TableColumn<Grupo, String> colNombreGrupo, colGradoGrupo;
    @FXML private TableColumn<Grupo, Void> colAccionesGrupo;
    @FXML private VBox panelFormularioGrupo;
    @FXML private TextField txtNombreGrupo;
    @FXML private ComboBox<String> cmbGradoGrupo;
    @FXML private Button btnNuevoGrupo, btnGuardarGrupo, btnCancelarGrupo;

    private ObservableList<Grupo> listaGrupos;
    private Grupo grupoEditando;

    public TabGruposController(GestionGrupos gestionGrupos, GestionGrados gestionGrados) {
        this.gestionGrupos = gestionGrupos;
        this.gestionGrados = gestionGrados;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        configurarTabla();
        recargarComboGrados();
        cargarGrupos();

        SistemaEventBus.suscribir(TipoEvento.GRUPOS, this::cargarGrupos);
        SistemaEventBus.suscribir(TipoEvento.GRADOS, this::recargarComboGrados);
    }

    private void configurarTabla() {
        colNumGrupo.setCellFactory(col -> new TableCell<>() {
            @Override protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : String.valueOf(getIndex() + 1));
            }
        });
        colNombreGrupo.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colGradoGrupo.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(
                cell.getValue().getGrado() != null ? cell.getValue().getGrado().getNombre() : ""));
        configurarAcciones();
    }

    private void configurarAcciones() {
        colAccionesGrupo.setCellFactory(param -> new TableCell<>() {
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

    private void cargarGrupos() {
        Platform.runLater(() -> {
            listaGrupos = FXCollections.observableArrayList(gestionGrupos.listarTodos());
            tablaGrupos.setItems(listaGrupos);
            tablaGrupos.refresh();
        });
    }

    private void recargarComboGrados() {
        cmbGradoGrupo.getItems().clear();
        cmbGradoGrupo.getItems().add("Seleccione un grado");
        for (Grado g : gestionGrados.listarTodos()) {
            cmbGradoGrupo.getItems().add(formatearGrado(g));
        }
        cmbGradoGrupo.getSelectionModel().selectFirst();
    }

    private void cargarEnFormulario(Grupo g) {
        grupoEditando = g;
        recargarComboGrados();
        txtNombreGrupo.setText(g.getNombre());
        cmbGradoGrupo.setValue(g.getGrado() != null ? formatearGrado(g.getGrado()) : null);
        panelFormularioGrupo.setVisible(true);
        panelFormularioGrupo.setManaged(true);
    }

    private void eliminar(Grupo g) {
        if (!Dialogos.confirmar("Confirmar eliminación", "¿Está seguro de eliminar este registro?")) return;
        try {
            gestionGrupos.eliminarGrupo(g);
            SistemaEventBus.notificar(TipoEvento.GRUPOS);
        } catch (IllegalArgumentException e) {
            Dialogos.info(e.getMessage());
        }
    }

    @FXML
    private void handleNuevoGrupo() {
        grupoEditando = null;
        recargarComboGrados();
        txtNombreGrupo.clear();
        panelFormularioGrupo.setVisible(true);
        panelFormularioGrupo.setManaged(true);
    }

    @FXML
    private void handleGuardarGrupo() {
        String nombre = txtNombreGrupo.getText().trim();
        String gradoNombre = cmbGradoGrupo.getValue();
        if (nombre.isBlank() || gradoNombre == null || gradoNombre.startsWith("Seleccione")) {
            Dialogos.info("Todos los campos son obligatorios.");
            return;
        }
        Grado grado = parsearGrado(gradoNombre);
        if (grado == null) {
            Dialogos.info("El grado seleccionado no existe.");
            return;
        }
        try {
            if (grupoEditando == null) gestionGrupos.crearGrupo(nombre, grado);
            else gestionGrupos.actualizarGrupo(grupoEditando, nombre, grado);
            SistemaEventBus.notificar(TipoEvento.GRUPOS);
            panelFormularioGrupo.setVisible(false);
            panelFormularioGrupo.setManaged(false);
        } catch (IllegalArgumentException e) {
            Dialogos.info(e.getMessage());
        }
    }

    @FXML
    private void handleCancelarGrupo() {
        panelFormularioGrupo.setVisible(false);
        panelFormularioGrupo.setManaged(false);
    }

    /** Formato: "Nombre (Nivel)" para eliminar ambigüedad. */
    private String formatearGrado(Grado g) {
        if (g == null) return "";
        String nivel = (g.getNivel() != null) ? g.getNivel() : "Sin nivel";
        return g.getNombre() + " (" + nivel + ")";
    }

    /** Parsea "1er Grado (Primaria)" y busca por nombre + nivel. */
    private Grado parsearGrado(String texto) {
        if (texto == null) return null;
        int idx = texto.lastIndexOf(" (");
        if (idx < 0) return gestionGrados.buscarPorNombre(texto);
        String nombre = texto.substring(0, idx).trim();
        String nivel = texto.substring(idx + 2, texto.length() - 1).trim();
        return gestionGrados.buscarPorNombreYNivel(nombre, nivel);
    }
}
