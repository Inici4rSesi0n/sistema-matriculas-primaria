package presentacion.controlador;

import presentacion.dialogos.Dialogos;
import infraestructura.configuracion.SpringContext;
import aplicacion.casosdeuso.GestionAsignaturas;
import aplicacion.casosdeuso.GestionAulas;
import aplicacion.casosdeuso.GestionGrados;
import aplicacion.casosdeuso.GestionGrupos;
import aplicacion.casosdeuso.GestionPeriodos;
import aplicacion.casosdeuso.GestionRecreos;
import aplicacion.casosdeuso.GestionTurnos;
import dominio.modelo.Asignatura;
import dominio.modelo.Aula;
import dominio.modelo.FranjaHoraria;
import dominio.modelo.Grado;
import dominio.modelo.Grupo;
import dominio.modelo.PeriodoAcademico;
import dominio.modelo.Recreo;
import dominio.modelo.Turno;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 *
 * @author inici4rsesi0n
 */
public class tab_CatalogosController implements Initializable {

    @FXML private TabPane tabPaneCatalogos;
    @FXML private Tab tabAsignaturas, tabPeriodos, tabAulas, tabGrupos, tabRecreos, tabGrados, tabTurnos;

    @FXML private TableView<Asignatura> tablaAsignaturas;
    @FXML private TableColumn<Asignatura, Integer> colNumAsignatura;
    @FXML private TableColumn<Asignatura, String> colNombreAsignatura;
    @FXML private TableColumn<Asignatura, Void> colAccionesAsignatura;
    @FXML private VBox panelFormularioAsignatura;
    @FXML private TextField txtNombreAsignatura;
    @FXML private Button btnNuevaAsignatura, btnGuardarAsignatura, btnCancelarAsignatura;

    @FXML private TableView<PeriodoAcademico> tablaPeriodos;
    @FXML private TableColumn<PeriodoAcademico, Integer> colNumPeriodo;
    @FXML private TableColumn<PeriodoAcademico, String> colNombrePeriodo, colFechaInicio, colFechaFin, colEstadoPeriodo;
    @FXML private TableColumn<PeriodoAcademico, Void> colAccionesPeriodo;
    @FXML private VBox panelFormularioPeriodo;
    @FXML private TextField txtNombrePeriodo;
    @FXML private DatePicker dateFechaInicio, dateFechaFin;
    @FXML private ComboBox<String> cmbEstadoPeriodo;
    @FXML private Button btnNuevoPeriodo, btnGuardarPeriodo, btnCancelarPeriodo;

    @FXML private TableView<Aula> tablaAulas;
    @FXML private TableColumn<Aula, Integer> colNumAula;
    @FXML private TableColumn<Aula, String> colNombreAula, colUbicacionAula, colTipoAula;
    @FXML private TableColumn<Aula, Integer> colCapacidadAula;
    @FXML private TableColumn<Aula, Void> colAccionesAula;
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
    @FXML private TextField txtInicioRecreo, txtFinRecreo, txtDescripcionRecreo;
    @FXML private Button btnNuevoRecreo, btnGuardarRecreo, btnCancelarRecreo;

    @FXML private TableView<Grado> tablaGrados;
    @FXML private TableColumn<Grado, Integer> colNumGrado;
    @FXML private TableColumn<Grado, String> colNombreGrado, colNivelGrado;
    @FXML private TableColumn<Grado, Void> colAccionesGrado;
    @FXML private VBox panelFormularioGrado;
    @FXML private TextField txtNombreGrado, txtNivelGrado;
    @FXML private Button btnNuevoGrado, btnGuardarGrado, btnCancelarGrado;

    @FXML private TableView<Turno> tablaTurnos;
    @FXML private TableColumn<Turno, Integer> colNumTurno;
    @FXML private TableColumn<Turno, String> colNombreTurno;
    @FXML private TableColumn<Turno, Void> colAccionesTurno;
    @FXML private VBox panelFormularioTurno;
    @FXML private TextField txtNombreTurno;
    @FXML private Button btnNuevoTurno, btnGuardarTurno, btnCancelarTurno;

    private ObservableList<Asignatura> listaAsignaturas;
    private ObservableList<PeriodoAcademico> listaPeriodos;
    private ObservableList<Aula> listaAulas;
    private ObservableList<Grupo> listaGrupos;
    private ObservableList<Recreo> listaRecreos;
    private ObservableList<Grado> listaGrados;
    private ObservableList<Turno> listaTurnos;

    private Asignatura asignaturaEditando;
    private PeriodoAcademico periodoEditando;
    private Aula aulaEditando;
    private Grupo grupoEditando;
    private Recreo recreoEditando;
    private Grado gradoEditando;
    private Turno turnoEditando;

    private GestionAsignaturas gestionAsignaturas;
    private GestionPeriodos gestionPeriodos;
    private GestionAulas gestionAulas;
    private GestionGrupos gestionGrupos;
    private GestionRecreos gestionRecreos;
    private GestionGrados gestionGrados;
    private GestionTurnos gestionTurnos;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        gestionAsignaturas = SpringContext.getBean(GestionAsignaturas.class);
        gestionPeriodos = SpringContext.getBean(GestionPeriodos.class);
        gestionAulas = SpringContext.getBean(GestionAulas.class);
        gestionGrupos = SpringContext.getBean(GestionGrupos.class);
        gestionRecreos = SpringContext.getBean(GestionRecreos.class);
        gestionGrados = SpringContext.getBean(GestionGrados.class);
        gestionTurnos = SpringContext.getBean(GestionTurnos.class);

        configurarCombos();
        configurarTablaAsignaturas();
        configurarTablaPeriodos();
        configurarTablaAulas();
        configurarTablaGrupos();
        configurarTablaRecreos();
        configurarTablaGrados();
        configurarTablaTurnos();
        cargarDatos();
    }

    private void configurarCombos() {
        cmbEstadoPeriodo.getItems().addAll("Activo", "Inactivo", "Culminado", "Prorrogado");
        cmbEstadoPeriodo.getSelectionModel().selectFirst();

        cmbDiaRecreo.getItems().addAll("Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo");
        cmbDiaRecreo.getSelectionModel().selectFirst();

        recargarComboGrados();
        recargarComboPeriodosRecreo();
    }

    private void recargarComboGrados() {
        cmbGradoGrupo.getItems().clear();
        cmbGradoGrupo.getItems().add("Seleccione un grado");
        for (Grado g : gestionGrados.listarTodos()) {
            cmbGradoGrupo.getItems().add(g.getNombre());
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
        cargarAsignaturas();
        cargarPeriodos();
        cargarAulas();
        cargarGrupos();
        cargarRecreos();
        cargarGrados();
        cargarTurnos();
    }

    private void configurarTablaAsignaturas() {
        colNumAsignatura.setCellFactory(col -> new TableCell<>() {
            @Override protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : String.valueOf(getIndex() + 1));
            }
        });
        colNombreAsignatura.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        configurarAcciones(colAccionesAsignatura, "asignatura");
    }

    private void configurarTablaPeriodos() {
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
        configurarAcciones(colAccionesPeriodo, "periodo");
    }

    private void configurarTablaAulas() {
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
        configurarAcciones(colAccionesAula, "aula");
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

    private void configurarTablaGrados() {
        colNumGrado.setCellFactory(col -> new TableCell<>() {
            @Override protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : String.valueOf(getIndex() + 1));
            }
        });
        colNombreGrado.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colNivelGrado.setCellValueFactory(new PropertyValueFactory<>("nivel"));
        configurarAcciones(colAccionesGrado, "grado");
    }

    private void configurarTablaTurnos() {
        colNumTurno.setCellFactory(col -> new TableCell<>() {
            @Override protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : String.valueOf(getIndex() + 1));
            }
        });
        colNombreTurno.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        configurarAcciones(colAccionesTurno, "turno");
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
            case "asignatura" -> {
                Asignatura a = (Asignatura) obj;
                asignaturaEditando = a;
                txtNombreAsignatura.setText(a.getNombre());
                panelFormularioAsignatura.setVisible(true);
                panelFormularioAsignatura.setManaged(true);
            }
            case "periodo" -> {
                PeriodoAcademico p = (PeriodoAcademico) obj;
                periodoEditando = p;
                txtNombrePeriodo.setText(p.getNombre());
                dateFechaInicio.setValue(LocalDate.parse(p.getFechaInicio()));
                dateFechaFin.setValue(LocalDate.parse(p.getFechaFin()));
                cmbEstadoPeriodo.setValue(p.getEstado());
                panelFormularioPeriodo.setVisible(true);
                panelFormularioPeriodo.setManaged(true);
            }
            case "aula" -> {
                Aula a = (Aula) obj;
                aulaEditando = a;
                txtNombreAula.setText(a.getNombre());
                txtCapacidadAula.setText(String.valueOf(a.getCapacidad()));
                txtUbicacionAula.setText(a.getUbicacion());
                txtTipoAula.setText(a.getTipo());
                panelFormularioAula.setVisible(true);
                panelFormularioAula.setManaged(true);
            }
            case "grupo" -> {
                Grupo g = (Grupo) obj;
                grupoEditando = g;
                recargarComboGrados();
                txtNombreGrupo.setText(g.getNombre());
                cmbGradoGrupo.setValue(g.getGrado() != null ? g.getGrado().getNombre() : null);
                panelFormularioGrupo.setVisible(true);
                panelFormularioGrupo.setManaged(true);
            }
            case "recreo" -> {
                Recreo r = (Recreo) obj;
                recreoEditando = r;
                recargarComboPeriodosRecreo();
                cmbDiaRecreo.setValue(r.getDiaSemana());
                cmbPeriodoRecreo.setValue(r.getPeriodo() != null ? r.getPeriodo().getNombre() : null);
                txtInicioRecreo.setText(r.getHoraInicio());
                txtFinRecreo.setText(r.getHoraFin());
                txtDescripcionRecreo.setText(r.getDescripcion());
                panelFormularioRecreo.setVisible(true);
                panelFormularioRecreo.setManaged(true);
            }
            case "grado" -> {
                Grado g = (Grado) obj;
                gradoEditando = g;
                txtNombreGrado.setText(g.getNombre());
                txtNivelGrado.setText(g.getNivel());
                panelFormularioGrado.setVisible(true);
                panelFormularioGrado.setManaged(true);
            }
            case "turno" -> {
                Turno t = (Turno) obj;
                turnoEditando = t;
                txtNombreTurno.setText(t.getNombre());
                panelFormularioTurno.setVisible(true);
                panelFormularioTurno.setManaged(true);
            }
        }
    }

    private void eliminar(Object obj, String tipo) {
        String mensaje = "¿Está seguro de eliminar este registro?";
        int respuesta = Dialogos.M3("Confirmar eliminación", mensaje);
        if (respuesta != 0) return;
        try {
            switch (tipo) {
                case "asignatura" -> gestionAsignaturas.eliminarAsignatura((Asignatura) obj);
                case "periodo" -> gestionPeriodos.eliminarPeriodo((PeriodoAcademico) obj);
                case "aula" -> gestionAulas.eliminarAula((Aula) obj);
                case "grupo" -> gestionGrupos.eliminarGrupo((Grupo) obj);
                case "recreo" -> gestionRecreos.eliminarRecreo((Recreo) obj);
                case "grado" -> gestionGrados.eliminarGrado((Grado) obj);
                case "turno" -> gestionTurnos.eliminarTurno((Turno) obj);
            }
            cargarDatos();
        } catch (IllegalArgumentException e) {
            Dialogos.M1(e.getMessage());
        }
    }

    private void cargarAsignaturas() {
        listaAsignaturas = FXCollections.observableArrayList(gestionAsignaturas.listarTodos());
        tablaAsignaturas.setItems(listaAsignaturas);
    }
    private void cargarPeriodos() {
        listaPeriodos = FXCollections.observableArrayList(gestionPeriodos.listarTodos());
        tablaPeriodos.setItems(listaPeriodos);
    }
    private void cargarAulas() {
        listaAulas = FXCollections.observableArrayList(gestionAulas.listarTodos());
        tablaAulas.setItems(listaAulas);
    }
    private void cargarGrupos() {
        listaGrupos = FXCollections.observableArrayList(gestionGrupos.listarTodos());
        tablaGrupos.setItems(listaGrupos);
    }
    private void cargarRecreos() {
        listaRecreos = FXCollections.observableArrayList(gestionRecreos.listarTodos());
        tablaRecreos.setItems(listaRecreos);
    }
    private void cargarGrados() {
        listaGrados = FXCollections.observableArrayList(gestionGrados.listarTodos());
        tablaGrados.setItems(listaGrados);
    }
    private void cargarTurnos() {
        listaTurnos = FXCollections.observableArrayList(gestionTurnos.listarTodos());
        tablaTurnos.setItems(listaTurnos);
    }

    @FXML private void handleNuevaAsignatura() { asignaturaEditando = null; txtNombreAsignatura.clear(); panelFormularioAsignatura.setVisible(true); panelFormularioAsignatura.setManaged(true); }
    @FXML private void handleGuardarAsignatura() {
        String nombre = txtNombreAsignatura.getText().trim();
        if (nombre.isBlank()) { Dialogos.M1("El nombre no puede estar vacío."); return; }
        try {
            if (asignaturaEditando == null) gestionAsignaturas.crearAsignatura(nombre);
            else gestionAsignaturas.actualizarAsignatura(asignaturaEditando, nombre);
            cargarAsignaturas();
            panelFormularioAsignatura.setVisible(false);
            panelFormularioAsignatura.setManaged(false);
        } catch (IllegalArgumentException e) { Dialogos.M1(e.getMessage()); }
    }
    @FXML private void handleCancelarAsignatura() { panelFormularioAsignatura.setVisible(false); panelFormularioAsignatura.setManaged(false); }

    @FXML private void handleNuevoPeriodo() { periodoEditando = null; txtNombrePeriodo.clear(); dateFechaInicio.setValue(null); dateFechaFin.setValue(null); cmbEstadoPeriodo.getSelectionModel().selectFirst(); panelFormularioPeriodo.setVisible(true); panelFormularioPeriodo.setManaged(true); }
    @FXML private void handleGuardarPeriodo() {
        String nombre = txtNombrePeriodo.getText().trim();
        LocalDate inicio = dateFechaInicio.getValue(), fin = dateFechaFin.getValue();
        String estado = cmbEstadoPeriodo.getValue();
        if (nombre.isBlank() || inicio == null || fin == null || estado == null) { Dialogos.M1("Todos los campos son obligatorios."); return; }
        try {
            if (periodoEditando == null) gestionPeriodos.crearPeriodo(nombre, inicio.toString(), fin.toString(), estado);
            else gestionPeriodos.actualizarPeriodo(periodoEditando, nombre, inicio.toString(), fin.toString(), estado);
            cargarPeriodos();
            panelFormularioPeriodo.setVisible(false);
            panelFormularioPeriodo.setManaged(false);
        } catch (IllegalArgumentException e) { Dialogos.M1(e.getMessage()); }
    }
    @FXML private void handleCancelarPeriodo() { panelFormularioPeriodo.setVisible(false); panelFormularioPeriodo.setManaged(false); }

    @FXML private void handleNuevaAula() { aulaEditando = null; txtNombreAula.clear(); txtCapacidadAula.clear(); txtUbicacionAula.clear(); txtTipoAula.clear(); panelFormularioAula.setVisible(true); panelFormularioAula.setManaged(true); }
    @FXML private void handleGuardarAula() {
        String nombre = txtNombreAula.getText().trim(), capacidadStr = txtCapacidadAula.getText().trim(), ubicacion = txtUbicacionAula.getText().trim(), tipo = txtTipoAula.getText().trim();
        if (nombre.isBlank() || capacidadStr.isBlank() || ubicacion.isBlank() || tipo.isBlank()) { Dialogos.M1("Todos los campos son obligatorios."); return; }
        int capacidad = Integer.parseInt(capacidadStr);
        try {
            if (aulaEditando == null) gestionAulas.crearAula(nombre, capacidad, ubicacion, tipo);
            else gestionAulas.actualizarAula(aulaEditando, nombre, capacidad, ubicacion, tipo);
            cargarAulas();
            panelFormularioAula.setVisible(false);
            panelFormularioAula.setManaged(false);
        } catch (IllegalArgumentException e) { Dialogos.M1(e.getMessage()); }
    }
    @FXML private void handleCancelarAula() { panelFormularioAula.setVisible(false); panelFormularioAula.setManaged(false); }

    @FXML private void handleNuevoGrupo() { grupoEditando = null; recargarComboGrados(); txtNombreGrupo.clear(); panelFormularioGrupo.setVisible(true); panelFormularioGrupo.setManaged(true); }
    @FXML private void handleGuardarGrupo() {
        String nombre = txtNombreGrupo.getText().trim();
        String gradoNombre = cmbGradoGrupo.getValue();
        if (nombre.isBlank() || gradoNombre == null || gradoNombre.startsWith("Seleccione")) { Dialogos.M1("Todos los campos son obligatorios."); return; }
        Grado grado = gestionGrados.buscarPorNombre(gradoNombre);
        if (grado == null) { Dialogos.M1("El grado seleccionado no existe."); return; }
        try {
            if (grupoEditando == null) gestionGrupos.crearGrupo(nombre, grado);
            else gestionGrupos.actualizarGrupo(grupoEditando, nombre, grado);
            cargarGrupos();
            panelFormularioGrupo.setVisible(false);
            panelFormularioGrupo.setManaged(false);
        } catch (IllegalArgumentException e) { Dialogos.M1(e.getMessage()); }
    }
    @FXML private void handleCancelarGrupo() { panelFormularioGrupo.setVisible(false); panelFormularioGrupo.setManaged(false); }

    @FXML private void handleNuevoRecreo() { recreoEditando = null; recargarComboPeriodosRecreo(); cmbDiaRecreo.getSelectionModel().selectFirst(); txtInicioRecreo.clear(); txtFinRecreo.clear(); txtDescripcionRecreo.clear(); panelFormularioRecreo.setVisible(true); panelFormularioRecreo.setManaged(true); }
    @FXML private void handleGuardarRecreo() {
        String dia = cmbDiaRecreo.getValue(), periodoNombre = cmbPeriodoRecreo.getValue(), inicio = txtInicioRecreo.getText().trim(), fin = txtFinRecreo.getText().trim(), desc = txtDescripcionRecreo.getText().trim();
        if (dia == null || periodoNombre == null || periodoNombre.startsWith("Seleccione") || inicio.isBlank() || fin.isBlank()) { Dialogos.M1("Todos los campos son obligatorios."); return; }
        if (!inicio.matches("([01]\\d|2[0-3]):[0-5]\\d") || !fin.matches("([01]\\d|2[0-3]):[0-5]\\d")) { Dialogos.M1("Formato de hora inválido. Use HH:mm (ej. 08:00, 14:30)."); return; }
        PeriodoAcademico periodo = gestionPeriodos.buscarPorNombre(periodoNombre);
        if (periodo == null) { Dialogos.M1("Periodo no encontrado."); return; }
        FranjaHoraria franja = new FranjaHoraria(dia, inicio, fin);
        try {
            if (recreoEditando == null) gestionRecreos.crearRecreo(franja, desc.isBlank() ? "Recreo" : desc, periodo);
            else gestionRecreos.actualizarRecreo(recreoEditando, franja, desc.isBlank() ? "Recreo" : desc, periodo);
            cargarRecreos();
            panelFormularioRecreo.setVisible(false);
            panelFormularioRecreo.setManaged(false);
        } catch (IllegalArgumentException e) { Dialogos.M1(e.getMessage()); }
    }
    @FXML private void handleCancelarRecreo() { panelFormularioRecreo.setVisible(false); panelFormularioRecreo.setManaged(false); }

    @FXML private void handleNuevoGrado() { gradoEditando = null; txtNombreGrado.clear(); txtNivelGrado.clear(); panelFormularioGrado.setVisible(true); panelFormularioGrado.setManaged(true); }
    @FXML private void handleGuardarGrado() {
        String nombre = txtNombreGrado.getText().trim(), nivel = txtNivelGrado.getText().trim();
        if (nombre.isBlank() || nivel.isBlank()) { Dialogos.M1("El nombre y el nivel son obligatorios."); return; }
        try {
            if (gradoEditando == null) gestionGrados.crearGrado(nombre, nivel);
            else gestionGrados.actualizarGrado(gradoEditando, nombre, nivel);
            cargarGrados();
            panelFormularioGrado.setVisible(false);
            panelFormularioGrado.setManaged(false);
        } catch (IllegalArgumentException e) { Dialogos.M1(e.getMessage()); }
    }
    @FXML private void handleCancelarGrado() { panelFormularioGrado.setVisible(false); panelFormularioGrado.setManaged(false); }

    @FXML private void handleNuevoTurno() { turnoEditando = null; txtNombreTurno.clear(); panelFormularioTurno.setVisible(true); panelFormularioTurno.setManaged(true); }
    @FXML private void handleGuardarTurno() {
        String nombre = txtNombreTurno.getText().trim();
        if (nombre.isBlank()) { Dialogos.M1("El nombre no puede estar vacío."); return; }
        try {
            if (turnoEditando == null) gestionTurnos.crearTurno(nombre);
            else gestionTurnos.actualizarTurno(turnoEditando, nombre);
            cargarTurnos();
            panelFormularioTurno.setVisible(false);
            panelFormularioTurno.setManaged(false);
        } catch (IllegalArgumentException e) { Dialogos.M1(e.getMessage()); }
    }
    @FXML private void handleCancelarTurno() { panelFormularioTurno.setVisible(false); panelFormularioTurno.setManaged(false); }

    public TabPane getTabPane() {
        return tabPaneCatalogos;
    }
}