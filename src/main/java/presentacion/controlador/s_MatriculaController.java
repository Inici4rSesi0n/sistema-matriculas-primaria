package presentacion.controlador;

import presentacion.dialogos.Dialogos;
import infraestructura.configuracion.SpringContext;
import aplicacion.casosdeuso.GestionMatriculas;
import aplicacion.casosdeuso.GestionEstudiantes;
import aplicacion.casosdeuso.GestionPeriodos;
import aplicacion.casosdeuso.GestionGrupos;
import dominio.modelo.Estudiante;
import dominio.modelo.Grupo;
import dominio.modelo.Matricula;
import dominio.modelo.PeriodoAcademico;
import dominio.modelo.EstadoMatricula;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

/**
 *
 * @author inici4rsesi0n
 */
public class s_MatriculaController implements Initializable {

    @FXML private TableView<Matricula> tablaMatriculas;
    @FXML private TableColumn<Matricula, Integer> colNumMatricula;
    @FXML private TableColumn<Matricula, String> colEstudiante, colPeriodo, colGrupo, colFecha;
    @FXML private TableColumn<Matricula, EstadoMatricula> colEstado;
    @FXML private TableColumn<Matricula, Void> colAccionesMatricula;

    @FXML private VBox panelFormularioMatricula;
    @FXML private ComboBox<String> cmbEstudianteMatricula, cmbPeriodoMatricula, cmbGrupoMatricula;
    @FXML private ComboBox<EstadoMatricula> cmbEstadoMatricula;
    @FXML private TextField txtFechaMatricula;
    @FXML private Button btnNuevaMatricula, btnGuardarMatricula, btnCancelarMatricula;

    private ObservableList<Matricula> listaMatriculas;
    private Matricula matriculaEditando;

    private GestionMatriculas gestionMatriculas;
    private GestionEstudiantes gestionEstudiantes;
    private GestionPeriodos gestionPeriodos;
    private GestionGrupos gestionGrupos;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        gestionMatriculas = SpringContext.getBean(GestionMatriculas.class);
        gestionEstudiantes = SpringContext.getBean(GestionEstudiantes.class);
        gestionPeriodos = SpringContext.getBean(GestionPeriodos.class);
        gestionGrupos = SpringContext.getBean(GestionGrupos.class);

        configurarTabla();
        configurarColumnaAcciones();
        cargarDatosIniciales();
    }

    private void configurarTabla() {
        colNumMatricula.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : String.valueOf(getIndex() + 1));
            }
        });
        colEstudiante.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(
                cell.getValue().getEstudiante() != null ? cell.getValue().getEstudiante().getCodigo() : ""));
        colPeriodo.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(
                cell.getValue().getPeriodo() != null ? cell.getValue().getPeriodo().getNombre() : ""));
        colGrupo.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(
                cell.getValue().getGrupo() != null ? cell.getValue().getGrupo().getNombre() : ""));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
    }

    private void configurarColumnaAcciones() {
        colAccionesMatricula.setCellFactory(param -> new TableCell<>() {
            private final Button btnEditar = new Button("Editar");
            private final Button btnEliminar = new Button("Eliminar");
            private final HBox hbox = new HBox(10, btnEditar, btnEliminar);
            {
                btnEditar.getStyleClass().add("boton-tabla-editar");
                btnEliminar.getStyleClass().add("boton-tabla-eliminar");
                btnEditar.setOnAction(e -> {
                    Matricula m = getTableView().getItems().get(getIndex());
                    cargarEnFormulario(m);
                });
                btnEliminar.setOnAction(e -> {
                    Matricula m = getTableView().getItems().get(getIndex());
                    eliminarMatricula(m);
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : hbox);
            }
        });
    }

    private void cargarDatosIniciales() {
        configurarComboEstado();
        cargarMatriculas();
    }

    private void configurarComboEstado() {
        cmbEstadoMatricula.getItems().setAll(EstadoMatricula.values());
        cmbEstadoMatricula.setConverter(new StringConverter<>() {
            @Override
            public String toString(EstadoMatricula estado) {
                return estado != null ? estado.name() : "";
            }

            @Override
            public EstadoMatricula fromString(String string) {
                return EstadoMatricula.fromString(string);
            }
        });
        cmbEstadoMatricula.getSelectionModel().selectFirst();
    }

    private void recargarCombos() {
        cmbEstudianteMatricula.getItems().clear();
        cmbEstudianteMatricula.getItems().add("Seleccione un estudiante");
        for (Estudiante e : gestionEstudiantes.listarTodos()) {
            cmbEstudianteMatricula.getItems().add(e.getCodigo() + " - " + e.getNombreCompleto());
        }

        cmbPeriodoMatricula.getItems().clear();
        cmbPeriodoMatricula.getItems().add("Seleccione un periodo");
        for (PeriodoAcademico p : gestionPeriodos.listarTodos()) {
            cmbPeriodoMatricula.getItems().add(p.getNombre());
        }

        cmbGrupoMatricula.getItems().clear();
        cmbGrupoMatricula.getItems().add("Seleccione un grupo");
        for (Grupo g : gestionGrupos.listarTodos()) {
            cmbGrupoMatricula.getItems().add(g.getNombre());
        }
    }

    private void cargarMatriculas() {
        listaMatriculas = FXCollections.observableArrayList(gestionMatriculas.listarTodas());
        tablaMatriculas.setItems(listaMatriculas);
    }

    @FXML
    private void handleNuevaMatricula() {
        matriculaEditando = null;
        recargarCombos();
        limpiarFormulario();
        panelFormularioMatricula.setVisible(true);
        panelFormularioMatricula.setManaged(true);
    }

    @FXML
    private void handleGuardarMatricula() {
        String estudianteSeleccionado = cmbEstudianteMatricula.getValue();
        String periodoSeleccionado = cmbPeriodoMatricula.getValue();
        String grupoSeleccionado = cmbGrupoMatricula.getValue();
        String fecha = txtFechaMatricula.getText().trim();
        EstadoMatricula estado = cmbEstadoMatricula.getValue();

        if (estudianteSeleccionado == null || estudianteSeleccionado.startsWith("Seleccione")
                || periodoSeleccionado == null || periodoSeleccionado.startsWith("Seleccione")
                || grupoSeleccionado == null || grupoSeleccionado.startsWith("Seleccione")
                || estado == null) {
            Dialogos.M1("Todos los campos son obligatorios.");
            return;
        }

        try {
            String codigoEstudiante = estudianteSeleccionado.split(" - ")[0];
            Estudiante estudiante = gestionEstudiantes.buscarPorCodigo(codigoEstudiante);
            PeriodoAcademico periodo = gestionPeriodos.buscarPorNombre(periodoSeleccionado);
            Grupo grupo = gestionGrupos.buscarPorNombre(grupoSeleccionado);

            if (estudiante == null || periodo == null || grupo == null) {
                Dialogos.M1("Los datos seleccionados no son válidos.");
                return;
            }

            if (matriculaEditando == null) {
                gestionMatriculas.matricularEstudiante(estudiante, periodo, grupo, null, fecha, estado);
            } else {
                gestionMatriculas.actualizarEstado(matriculaEditando, estado);
                matriculaEditando.setFecha(fecha);
                gestionMatriculas.actualizarEstado(matriculaEditando, matriculaEditando.getEstado());
            }
            cargarMatriculas();
            limpiarFormulario();
        } catch (IllegalArgumentException e) {
            Dialogos.M1(e.getMessage());
        }
    }

    @FXML
    private void handleCancelarMatricula() {
        limpiarFormulario();
    }

    private void cargarEnFormulario(Matricula matricula) {
        matriculaEditando = matricula;
        recargarCombos();
        cmbEstudianteMatricula.setValue(matricula.getEstudiante().getCodigo() + " - " + matricula.getEstudiante().getNombreCompleto());
        cmbPeriodoMatricula.setValue(matricula.getPeriodo().getNombre());
        cmbGrupoMatricula.setValue(matricula.getGrupo().getNombre());
        txtFechaMatricula.setText(matricula.getFecha());
        cmbEstadoMatricula.setValue(matricula.getEstado());
        panelFormularioMatricula.setVisible(true);
        panelFormularioMatricula.setManaged(true);
    }

    private void eliminarMatricula(Matricula matricula) {
        int respuesta = Dialogos.M3("Confirmar eliminación",
                "¿Está seguro de eliminar la matrícula de " + matricula.getEstudiante().getCodigo() + "?");
        if (respuesta == 0) {
            try {
                gestionMatriculas.eliminarMatricula(matricula);
                cargarMatriculas();
            } catch (IllegalArgumentException e) {
                Dialogos.M1(e.getMessage());
            }
        }
    }

    private void limpiarFormulario() {
        matriculaEditando = null;
        cmbEstudianteMatricula.getSelectionModel().selectFirst();
        cmbPeriodoMatricula.getSelectionModel().selectFirst();
        cmbGrupoMatricula.getSelectionModel().selectFirst();
        cmbEstadoMatricula.getSelectionModel().selectFirst();
        txtFechaMatricula.clear();
        panelFormularioMatricula.setVisible(false);
        panelFormularioMatricula.setManaged(false);
    }
}