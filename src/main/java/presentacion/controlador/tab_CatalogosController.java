package presentacion.controlador;
import presentacion.eventos.SistemaEventBus;
import presentacion.eventos.TipoEvento;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import presentacion.dialogos.Dialogos;
import aplicacion.casosdeuso.GestionGrados;
import aplicacion.casosdeuso.GestionGrupos;
import aplicacion.casosdeuso.GestionPeriodos;
import aplicacion.casosdeuso.GestionRecreos;
import dominio.modelo.FranjaHoraria;
import dominio.modelo.Grado;
import dominio.modelo.Grupo;
import dominio.modelo.PeriodoAcademico;
import dominio.modelo.Recreo;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
/**
 *
 * @author inici4rsesi0n
 */
@Component
@Scope("prototype")
public class tab_CatalogosController implements Initializable {
    @FXML private TabPane tabPaneCatalogos;
    @FXML private Tab tabAsignaturas, tabPeriodos, tabAulas, tabGrupos, tabRecreos, tabGrados;
    @FXML private VBox panelFormularioAula;
    @FXML private TextField txtNombreAula, txtCapacidadAula, txtUbicacionAula, txtTipoAula;
    @FXML private Button btnNuevaAula, btnGuardarAula, btnCancelarAula;
    @FXML private TableView<Grupo> tablaGrupos;
    @FXML private TableColumn<Grupo, Integer> colNumGrupo;
    @FXML private TableColumn<Grupo, String> colNombreGrupo, colGradoGrupo;
    @FXML private TableColumn<Grupo, Void> colAccionesGrupo;
    @FXML private VBox panelFormularioGrupo;
    @FXML private TextField txtNombreGrupo;
    @FXML private ComboBox<String> cmbGradoGrupo;
    @FXML private Button btnNuevoGrupo, btnGuardarGrupo, btnCancelarGrupo;
    @FXML private TableView<Recreo> tablaRecreos;
    @FXML private TableColumn<Recreo, Integer> colNumRecreo;
    @FXML private TableColumn<Recreo, String> colDiaRecreo, colInicioRecreo, colFinRecreo, colDescripcionRecreo;
    @FXML private TableColumn<Recreo, Void> colAccionesRecreo;
    @FXML private VBox panelFormularioRecreo;
    @FXML private ComboBox<String> cmbDiaRecreo, cmbPeriodoRecreo;
    @FXML private ListView<PeriodoAcademico> listViewPeriodosRecreo;
    @FXML private TextField txtInicioRecreo, txtFinRecreo, txtDescripcionRecreo;
    @FXML private Button btnNuevoRecreo, btnGuardarRecreo, btnCancelarRecreo;
    private ObservableList<Grupo> listaGrupos;
    private ObservableList<Recreo> listaRecreos;
    private ObservableList<PeriodoAcademico> periodosRecreoTemporal;
    private Grupo grupoEditando;
    private Recreo recreoEditando;
    private final GestionPeriodos gestionPeriodos;
    private final GestionGrupos gestionGrupos;
    private final GestionRecreos gestionRecreos;
    private final GestionGrados gestionGrados;
    public tab_CatalogosController(GestionPeriodos gestionPeriodos,
                                   GestionGrupos gestionGrupos,
                                   GestionRecreos gestionRecreos,
                                   GestionGrados gestionGrados) {
        this.gestionPeriodos = gestionPeriodos;
        this.gestionGrupos = gestionGrupos;
        this.gestionRecreos = gestionRecreos;
        this.gestionGrados = gestionGrados;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        configurarCombos();
        configurarTablas();
        cargarDatos();
        SistemaEventBus.suscribir(TipoEvento.PERIODOS, this::recargarComboPeriodosRecreo);
        SistemaEventBus.suscribir(TipoEvento.GRUPOS, this::cargarGrupos);
        SistemaEventBus.suscribir(TipoEvento.RECREOS, this::cargarRecreos);
        SistemaEventBus.suscribir(TipoEvento.GRADOS, this::recargarComboGrados);
        periodosRecreoTemporal = FXCollections.observableArrayList();
        listViewPeriodosRecreo.setItems(periodosRecreoTemporal);
        configurarCellFactoryPeriodosRecreo();
    }

    private void configurarCombos() {
        cmbDiaRecreo.getItems().addAll("Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo");
        cmbDiaRecreo.getSelectionModel().selectFirst();

        recargarComboGrados();
        recargarComboPeriodosRecreo();
    }

    private void configurarTablas() {
        configurarTablaGrupos();
        configurarTablaRecreos();
    }

    private void configurarTablaGrupos() {
        colNumGrupo.setCellFactory(col -> new TableCell<>() {
            @Override protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : String.valueOf(getIndex() + 1));
            }
        });
        colNombreGrupo.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colGradoGrupo.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(
                cell.getValue().getGrado() != null ? cell.getValue().getGrado().getNombre() : ""));
        configurarAcciones(colAccionesGrupo, "grupo");
    }

    private void configurarTablaRecreos() {
        colNumRecreo.setCellFactory(col -> new TableCell<>() {
            @Override protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : String.valueOf(getIndex() + 1));
            }
        });
        colDiaRecreo.setCellValueFactory(new PropertyValueFactory<>("diaSemana"));
        colInicioRecreo.setCellValueFactory(new PropertyValueFactory<>("horaInicio"));
        colFinRecreo.setCellValueFactory(new PropertyValueFactory<>("horaFin"));
        colDescripcionRecreo.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(
                cell.getValue().getDescripcion()));
        configurarAcciones(colAccionesRecreo, "recreo");
    }

    private <T> void configurarAcciones(TableColumn<T, Void> columna, String tipo) {
        columna.setCellFactory(param -> new TableCell<>() {
            private final Button btnEditar = new Button("Editar");
            private final Button btnEliminar = new Button("Eliminar");
            private final HBox hbox = new HBox(10, btnEditar, btnEliminar);
            {
                btnEditar.getStyleClass().add("boton-tabla-editar");
                btnEliminar.getStyleClass().add("boton-tabla-eliminar");
                btnEditar.setOnAction(e -> cargarEnFormulario(getTableView().getItems().get(getIndex()), tipo));
                btnEliminar.setOnAction(e -> eliminar(getTableView().getItems().get(getIndex()), tipo));
            }
            @Override protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : hbox);
            }
        });
    }

    private void cargarEnFormulario(Object obj, String tipo) {
        switch (tipo) {
            case "grupo" -> {
                Grupo g = (Grupo) obj;
                grupoEditando = g;
                recargarComboGrados();
                txtNombreGrupo.setText(g.getNombre());
                cmbGradoGrupo.setValue(g.getGrado() != null ? formatearGrado(g.getGrado()) : null);
                panelFormularioGrupo.setVisible(true);
                panelFormularioGrupo.setManaged(true);
            }
            case "recreo" -> {
                Recreo r = (Recreo) obj;
                recreoEditando = r;
                recargarComboPeriodosRecreo();
                periodosRecreoTemporal.setAll(r.getPeriodos());
                cmbDiaRecreo.setValue(r.getDiaSemana());
                txtInicioRecreo.setText(r.getHoraInicio());
                txtFinRecreo.setText(r.getHoraFin());
                txtDescripcionRecreo.setText(r.getDescripcion());
                panelFormularioRecreo.setVisible(true);
                panelFormularioRecreo.setManaged(true);
            }
        }
    }

    private void eliminar(Object obj, String tipo) {
        if (!Dialogos.confirmar("Confirmar eliminación", "¿Está seguro de eliminar este registro?")) return;
        try {
            switch (tipo) {
                case "periodo"    -> { gestionPeriodos.eliminarPeriodo((PeriodoAcademico) obj); SistemaEventBus.notificar(TipoEvento.PERIODOS); }
                case "grupo"      -> { gestionGrupos.eliminarGrupo((Grupo) obj); SistemaEventBus.notificar(TipoEvento.GRUPOS); }
                case "recreo"     -> { gestionRecreos.eliminarRecreo((Recreo) obj); SistemaEventBus.notificar(TipoEvento.RECREOS); }
            }
            cargarDatos();
        } catch (IllegalArgumentException e) {
            Dialogos.info(e.getMessage());
        }
    }

    private void recargarComboGrados() {
        cmbGradoGrupo.getItems().clear();
        cmbGradoGrupo.getItems().add("Seleccione un grado");
        for (Grado g : gestionGrados.listarTodos()) {
            cmbGradoGrupo.getItems().add(formatearGrado(g));
        }
        cmbGradoGrupo.getSelectionModel().selectFirst();
    }

    private void recargarComboPeriodosRecreo() {
        cmbPeriodoRecreo.getItems().clear();
        cmbPeriodoRecreo.getItems().add("Seleccione un periodo");
        for (PeriodoAcademico p : gestionPeriodos.listarTodos()) {
            cmbPeriodoRecreo.getItems().add(p.getNombre());
        }
        cmbPeriodoRecreo.getSelectionModel().selectFirst();
    }

    private void cargarDatos() {
        cargarGrupos();
        cargarRecreos();
    }

    private void cargarGrupos() {
        listaGrupos = FXCollections.observableArrayList(gestionGrupos.listarTodos());
        tablaGrupos.setItems(listaGrupos);
        tablaGrupos.refresh();
    }
    private void cargarRecreos() {
        listaRecreos = FXCollections.observableArrayList(gestionRecreos.listarTodos());
        tablaRecreos.setItems(listaRecreos);
        tablaRecreos.refresh();
    }

    @FXML private void handleNuevoGrupo() { grupoEditando = null; recargarComboGrados(); txtNombreGrupo.clear(); panelFormularioGrupo.setVisible(true); panelFormularioGrupo.setManaged(true); }
    @FXML private void handleGuardarGrupo() {
        String nombre = txtNombreGrupo.getText().trim();
        String gradoNombre = cmbGradoGrupo.getValue();
        if (nombre.isBlank() || gradoNombre == null || gradoNombre.startsWith("Seleccione")) { Dialogos.info("Todos los campos son obligatorios."); return; }
        Grado grado = parsearGrado(gradoNombre);
        if (grado == null) { Dialogos.info("El grado seleccionado no existe."); return; }
        try {
            if (grupoEditando == null) gestionGrupos.crearGrupo(nombre, grado);
            else gestionGrupos.actualizarGrupo(grupoEditando, nombre, grado);
            SistemaEventBus.notificar(TipoEvento.GRUPOS);
            panelFormularioGrupo.setVisible(false);
            panelFormularioGrupo.setManaged(false);
        } catch (IllegalArgumentException e) { Dialogos.info(e.getMessage()); }
    }
    @FXML private void handleCancelarGrupo() { panelFormularioGrupo.setVisible(false); panelFormularioGrupo.setManaged(false); }
    @FXML private void handleNuevoRecreo() {
        recreoEditando = null;
        periodosRecreoTemporal.clear();
        recargarComboPeriodosRecreo();
        cmbDiaRecreo.getSelectionModel().selectFirst();
        txtInicioRecreo.clear();
        txtFinRecreo.clear();
        txtDescripcionRecreo.clear();
        panelFormularioRecreo.setVisible(true);
        panelFormularioRecreo.setManaged(true);
    }
    @FXML private void handleGuardarRecreo() {
        String dia = cmbDiaRecreo.getValue();
        String inicio = txtInicioRecreo.getText().trim();
        String fin = txtFinRecreo.getText().trim();
        String desc = txtDescripcionRecreo.getText().trim();
        if (dia == null || inicio.isBlank() || fin.isBlank()) {
            Dialogos.info("Todos los campos son obligatorios.");
            return;
        }
        if (periodosRecreoTemporal.isEmpty()) {
            Dialogos.info("Debe asociar al menos un periodo al recreo.");
            return;
        }
        if (!inicio.matches("([01]\\d|2[0-3]):[0-5]\\d") || !fin.matches("([01]\\d|2[0-3]):[0-5]\\d")) {
            Dialogos.info("Formato de hora inválido. Use HH:mm (ej. 08:00, 14:30).");
            return;
        }
        FranjaHoraria franja = new FranjaHoraria(dia, inicio, fin);
        java.util.List<PeriodoAcademico> periodos = new java.util.ArrayList<>(periodosRecreoTemporal);
        try {
            if (recreoEditando == null) {
                gestionRecreos.crearRecreo(franja, desc.isBlank() ? "Recreo" : desc, periodos);
            } else {
                gestionRecreos.actualizarRecreo(recreoEditando, franja, desc.isBlank() ? "Recreo" : desc, periodos);
            }
            SistemaEventBus.notificar(TipoEvento.RECREOS);
            panelFormularioRecreo.setVisible(false);
            panelFormularioRecreo.setManaged(false);
        } catch (IllegalArgumentException e) {
            Dialogos.info(e.getMessage());
        }
    }
    @FXML private void handleCancelarRecreo() { panelFormularioRecreo.setVisible(false); panelFormularioRecreo.setManaged(false); }
    private String formatearGrado(Grado g) {
        if (g == null) return "";
        String nivel = (g.getNivel() != null) ? g.getNivel() : "Sin nivel";
        return g.getNombre() + " (" + nivel + ")";
    }
    private Grado parsearGrado(String texto) {
        if (texto == null) return null;
        int idx = texto.lastIndexOf(" (");
        if (idx < 0) return gestionGrados.buscarPorNombre(texto);
        String nombre = texto.substring(0, idx).trim();
        String nivel = texto.substring(idx + 2, texto.length() - 1).trim();
        return gestionGrados.buscarPorNombreYNivel(nombre, nivel);
    }

    public TabPane getTabPane() {
        return tabPaneCatalogos;
    }
    private void configurarCellFactoryPeriodosRecreo() {
        listViewPeriodosRecreo.setPlaceholder(
                new javafx.scene.control.Label("Sin periodos asociados — use '+ Añadir'"));
        listViewPeriodosRecreo.setCellFactory(lv -> new javafx.scene.control.ListCell<>() {
            @Override
            protected void updateItem(PeriodoAcademico item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    javafx.scene.control.Label lbl = new javafx.scene.control.Label(item.getNombre());
                    lbl.setStyle("-fx-font-size: 12px; -fx-text-fill: #333;");

                    javafx.scene.control.Button btnQuitar = new javafx.scene.control.Button("✕");
                    btnQuitar.getStyleClass().add("boton-tabla-eliminar");
                    btnQuitar.setStyle("-fx-font-size: 10px; -fx-padding: 2 8 2 8; -fx-cursor: hand;");
                    btnQuitar.setOnAction(e -> periodosRecreoTemporal.remove(item));

                    javafx.scene.layout.HBox hbox = new javafx.scene.layout.HBox(10);
                    hbox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
                    hbox.setMaxWidth(Double.MAX_VALUE);

                    javafx.scene.layout.Region spacer = new javafx.scene.layout.Region();
                    javafx.scene.layout.HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);

                    hbox.getChildren().addAll(lbl, spacer, btnQuitar);
                    hbox.prefWidthProperty().bind(listViewPeriodosRecreo.widthProperty().subtract(35));

                    setGraphic(hbox);
                    setStyle("-fx-padding: 4 8 4 8;");
                }
            }
        });
    }
    @FXML
    private void handleAgregarPeriodoRecreo() {
        String nombrePeriodo = cmbPeriodoRecreo.getValue();
        if (nombrePeriodo == null || nombrePeriodo.startsWith("Seleccione")) return;

        PeriodoAcademico periodo = gestionPeriodos.buscarPorNombre(nombrePeriodo);
        if (periodo == null) {
            Dialogos.info("Periodo no encontrado.");
            return;
        }
        if (periodosRecreoTemporal.contains(periodo)) {
            Dialogos.info("El periodo ya está asociado.");
            return;
        }
        periodosRecreoTemporal.add(periodo);
    }
}
