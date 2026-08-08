package presentacion.controlador;

import presentacion.dialogos.Dialogos;
import infraestructura.configuracion.ProveedorInfraestructura;
import aplicacion.casosdeuso.GestionAsignaturas;
import aplicacion.casosdeuso.GestionAulas;
import aplicacion.casosdeuso.GestionClases;
import aplicacion.casosdeuso.GestionDocentes;
import aplicacion.casosdeuso.GestionGrupos;
import aplicacion.casosdeuso.GestionPeriodos;
import dominio.modelo.Asignatura;
import dominio.modelo.Aula;
import dominio.modelo.Clase;
import dominio.modelo.Docente;
import dominio.modelo.Grupo;
import dominio.modelo.PeriodoAcademico;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author inici4rsesi0n
 */
public class tab_HorariosController implements Initializable {

    @FXML private TabPane tabPaneHorarios;
    @FXML private Tab tabClases;

    @FXML private TableView<Clase> tablaClases;
    @FXML private TableColumn<Clase, Integer> colNumClase;
    @FXML private TableColumn<Clase, String> colDiaClase, colInicioClase, colFinClase, colAsignaturaClase, colDocenteClase, colGrupoClase, colAulaClase, colPeriodoClase;
    @FXML private TableColumn<Clase, Void> colAccionesClase;
    @FXML private VBox panelFormularioClase;
    @FXML private ComboBox<String> cmbDiaClase, cmbAsignaturaClase, cmbDocenteClase, cmbGrupoClase, cmbAulaClase, cmbPeriodoClase;
    @FXML private TextField txtInicioClase, txtFinClase;
    @FXML private Button btnNuevaClase, btnGuardarClase, btnCancelarClase;
    @FXML private Button btnVerHorario;

    private ObservableList<Clase> listaClases;
    private Clase claseEditando;

    private GestionClases gestionClases;
    private GestionAsignaturas gestionAsignaturas;
    private GestionDocentes gestionDocentes;
    private GestionGrupos gestionGrupos;
    private GestionAulas gestionAulas;
    private GestionPeriodos gestionPeriodos;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        gestionClases = ProveedorInfraestructura.getGestionClases();
        gestionAsignaturas = ProveedorInfraestructura.getGestionAsignaturas();
        gestionDocentes = ProveedorInfraestructura.getGestionDocentes();
        gestionGrupos = ProveedorInfraestructura.getGestionGrupos();
        gestionAulas = ProveedorInfraestructura.getGestionAulas();
        gestionPeriodos = ProveedorInfraestructura.getGestionPeriodos();

        configurarTablaClases();
        cargarClases();
    }

    private void configurarTablaClases() {
        colNumClase.setCellFactory(col -> new TableCell<>() {
            @Override protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : String.valueOf(getIndex() + 1));
            }
        });
        colDiaClase.setCellValueFactory(new PropertyValueFactory<>("diaSemana"));
        colInicioClase.setCellValueFactory(new PropertyValueFactory<>("horaInicio"));
        colFinClase.setCellValueFactory(new PropertyValueFactory<>("horaFin"));
        colAsignaturaClase.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(
                cell.getValue().getAsignatura() != null ? cell.getValue().getAsignatura().getNombre() : ""));
        colDocenteClase.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(
                cell.getValue().getDocente() != null ? cell.getValue().getDocente().getNombreCompleto() : ""));
        colGrupoClase.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(
                cell.getValue().getGrupo() != null ? cell.getValue().getGrupo().getNombre() : ""));
        colAulaClase.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(
                cell.getValue().getAula() != null ? cell.getValue().getAula().getNombre() : ""));
        colPeriodoClase.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(
                cell.getValue().getPeriodo() != null ? cell.getValue().getPeriodo().getNombre() : ""));
        configurarAcciones();
    }

    private void configurarAcciones() {
        colAccionesClase.setCellFactory(param -> new TableCell<>() {
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

    private void cargarClases() {
        listaClases = FXCollections.observableArrayList(gestionClases.listarClases());
        tablaClases.setItems(listaClases);
    }

    private void recargarComboClases() {
        cmbDiaClase.getItems().setAll("Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo");
        cmbAsignaturaClase.getItems().clear();
        cmbAsignaturaClase.getItems().add("Seleccione una asignatura");
        for (Asignatura a : gestionAsignaturas.listarTodos()) {
            cmbAsignaturaClase.getItems().add(a.getNombre());
        }
        cmbDocenteClase.getItems().clear();
        cmbDocenteClase.getItems().add("Seleccione un docente");
        for (Docente d : gestionDocentes.listarTodos()) {
            cmbDocenteClase.getItems().add(d.getCodigo() + " - " + d.getNombreCompleto());
        }
        cmbGrupoClase.getItems().clear();
        cmbGrupoClase.getItems().add("Seleccione un grupo");
        for (Grupo g : gestionGrupos.listarTodos()) {
            cmbGrupoClase.getItems().add(g.getNombre());
        }
        cmbAulaClase.getItems().clear();
        cmbAulaClase.getItems().add("Seleccione un aula");
        for (Aula a : gestionAulas.listarTodos()) {
            cmbAulaClase.getItems().add(a.getNombre());
        }
        cmbPeriodoClase.getItems().clear();
        cmbPeriodoClase.getItems().add("Seleccione un periodo");
        for (PeriodoAcademico p : gestionPeriodos.listarTodos()) {
            cmbPeriodoClase.getItems().add(p.getNombre());
        }
    }

    private void cargarEnFormulario(Clase c) {
        claseEditando = c;
        recargarComboClases();
        cmbDiaClase.setValue(c.getDiaSemana());
        txtInicioClase.setText(c.getHoraInicio());
        txtFinClase.setText(c.getHoraFin());
        cmbAsignaturaClase.setValue(c.getAsignatura() != null ? c.getAsignatura().getNombre() : null);
        cmbDocenteClase.setValue(c.getDocente() != null ? c.getDocente().getCodigo() + " - " + c.getDocente().getNombreCompleto() : null);
        cmbGrupoClase.setValue(c.getGrupo() != null ? c.getGrupo().getNombre() : null);
        cmbAulaClase.setValue(c.getAula() != null ? c.getAula().getNombre() : null);
        cmbPeriodoClase.setValue(c.getPeriodo() != null ? c.getPeriodo().getNombre() : null);
        panelFormularioClase.setVisible(true);
        panelFormularioClase.setManaged(true);
    }

    private void eliminar(Clase c) {
        int respuesta = Dialogos.M3("Confirmar eliminación", "¿Está seguro de eliminar esta clase?");
        if (respuesta != 0) return;
        try {
            gestionClases.eliminarClase(c);
            cargarClases();
        } catch (IllegalArgumentException e) {
            Dialogos.M1(e.getMessage());
        }
    }

    @FXML private void handleNuevaClase() { claseEditando = null; recargarComboClases(); limpiarFormularioClase(); panelFormularioClase.setVisible(true); panelFormularioClase.setManaged(true); }
    @FXML private void handleGuardarClase() {
        String dia = cmbDiaClase.getValue(), inicio = txtInicioClase.getText().trim(), fin = txtFinClase.getText().trim();
        String asignaturaNombre = cmbAsignaturaClase.getValue(), docenteSel = cmbDocenteClase.getValue();
        String grupoNombre = cmbGrupoClase.getValue(), aulaNombre = cmbAulaClase.getValue(), periodoNombre = cmbPeriodoClase.getValue();

        if (dia == null || inicio.isBlank() || fin.isBlank() || asignaturaNombre == null || asignaturaNombre.startsWith("Seleccione")
                || docenteSel == null || docenteSel.startsWith("Seleccione") || grupoNombre == null || grupoNombre.startsWith("Seleccione")
                || aulaNombre == null || aulaNombre.startsWith("Seleccione") || periodoNombre == null || periodoNombre.startsWith("Seleccione")) {
            Dialogos.M1("Todos los campos son obligatorios."); return;
        }
        if (!inicio.matches("([01]\\d|2[0-3]):[0-5]\\d") || !fin.matches("([01]\\d|2[0-3]):[0-5]\\d")) {
            Dialogos.M1("Formato de hora inválido. Use HH:mm (ej. 08:00, 14:30)."); return;
        }

        Asignatura asig = gestionAsignaturas.buscarAsignatura(asignaturaNombre);
        Docente doc = gestionDocentes.buscarPorCodigo(docenteSel.split(" - ")[0]);
        Grupo grupo = gestionGrupos.buscarPorNombre(grupoNombre);
        Aula aula = gestionAulas.buscarAula(aulaNombre);
        PeriodoAcademico periodo = gestionPeriodos.buscarPorNombre(periodoNombre);
        if (asig == null || doc == null || grupo == null || aula == null || periodo == null) {
            Dialogos.M1("Datos inválidos."); return;
        }

        try {
            if (claseEditando == null) gestionClases.crearClase(dia, inicio, fin, asig, doc, grupo, aula, periodo);
            else gestionClases.actualizarClase(claseEditando, dia, inicio, fin, asig, doc, grupo, aula, periodo);
            cargarClases();
            panelFormularioClase.setVisible(false);
            panelFormularioClase.setManaged(false);
        } catch (IllegalArgumentException e) { Dialogos.M1(e.getMessage()); }
    }
    @FXML private void handleCancelarClase() { panelFormularioClase.setVisible(false); panelFormularioClase.setManaged(false); }

    @FXML private void handleVerHorario() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/visor_Horario.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(btnVerHorario.getScene().getWindow());

            Scene scene = new Scene(root);
            stage.setScene(scene);

            Stage owner = (Stage) btnVerHorario.getScene().getWindow();
            double width = owner.getWidth() * 0.8;
            double height = owner.getHeight() * 0.8;
            stage.setWidth(width);
            stage.setHeight(height);
            stage.setX(owner.getX() + (owner.getWidth() - width) / 2);
            stage.setY(owner.getY() + (owner.getHeight() - height) / 2);

            stage.showAndWait();
        } catch (IOException e) {
            Dialogos.M1("No se pudo abrir el visor de horario.");
        }
    }

    private void limpiarFormularioClase() {
        cmbDiaClase.getSelectionModel().selectFirst();
        txtInicioClase.clear(); txtFinClase.clear();
        cmbAsignaturaClase.getSelectionModel().selectFirst();
        cmbDocenteClase.getSelectionModel().selectFirst();
        cmbGrupoClase.getSelectionModel().selectFirst();
        cmbAulaClase.getSelectionModel().selectFirst();
        cmbPeriodoClase.getSelectionModel().selectFirst();
    }

    public TabPane getTabPane() {
        return tabPaneHorarios;
    }
}