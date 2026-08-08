package presentacion.controlador;

import presentacion.dialogos.Dialogos;
import infraestructura.configuracion.SpringContext;
import aplicacion.casosdeuso.GestionAsignaturas;
import aplicacion.casosdeuso.GestionDocentes;
import aplicacion.casosdeuso.GestionGrados;
import aplicacion.casosdeuso.GestionGrupos;
import aplicacion.casosdeuso.GestionUsuarios;
import dominio.modelo.Asignatura;
import dominio.modelo.CoordinadorAcademico;
import dominio.modelo.Docente;
import dominio.modelo.Grado;
import dominio.modelo.Grupo;
import dominio.modelo.Usuario;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

/**
 *
 * @author inici4rsesi0n
 */
public class tab_AsignacionesController implements Initializable {

    @FXML private TabPane tabPaneAsignaciones;
    @FXML private Tab tabAsignacionDocente, tabAsignacionCoordinacion;

    @FXML private TableView<AsignacionItem> tablaAsignaturasDocente;
    @FXML private TableColumn<AsignacionItem, String> colCodigoDocenteAsignatura, colNombreDocenteAsignatura, colAsignaturaDocente;
    @FXML private TableColumn<AsignacionItem, Void> colAccionesAsignaturaDocente;
    @FXML private ComboBox<String> cmbDocenteAsignatura, cmbAsignaturaDocente;
    @FXML private Button btnAgregarAsignaturaDocente;

    @FXML private TableView<TutorItem> tablaTutores;
    @FXML private TableColumn<TutorItem, String> colGrupoTutor, colDocenteTutor;
    @FXML private TableColumn<TutorItem, Void> colAccionesTutor;
    @FXML private ComboBox<String> cmbDocenteTutor, cmbGrupoTutor;
    @FXML private Button btnAsignarTutor;

    @FXML private TableView<CoordinacionItem> tablaCoordinaciones;
    @FXML private TableColumn<CoordinacionItem, String> colCoordinadorNombre, colGradoCoordinado;
    @FXML private TableColumn<CoordinacionItem, Void> colAccionesCoordinacion;
    @FXML private ComboBox<String> cmbCoordinador, cmbGradoCoordinacion;
    @FXML private Button btnAsignarCoordinacion;

    private GestionDocentes gestionDocentes;
    private GestionAsignaturas gestionAsignaturas;
    private GestionGrupos gestionGrupos;
    private GestionUsuarios gestionUsuarios;
    private GestionGrados gestionGrados;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        gestionDocentes = SpringContext.getBean(GestionDocentes.class);
        gestionAsignaturas = SpringContext.getBean(GestionAsignaturas.class);
        gestionGrupos = SpringContext.getBean(GestionGrupos.class);
        gestionUsuarios = SpringContext.getBean(GestionUsuarios.class);
        gestionGrados = SpringContext.getBean(GestionGrados.class);

        configurarCombos();
        configurarTablaAsignacionesDocente();
        configurarTablaTutores();
        configurarTablaCoordinaciones();
        cargarDatos();

        tabPaneAsignaciones.getSelectionModel().selectedItemProperty().addListener((obs, oldTab, newTab) -> {
            if (newTab != null) {
                recargarCombosAsignaciones();
                cargarDatos();
            }
        });
    }

    private void configurarCombos() {
        recargarCombosAsignaciones();
    }

    private void recargarCombosAsignaciones() {
        cmbDocenteAsignatura.getItems().clear();
        cmbDocenteAsignatura.getItems().add("Seleccione un docente");
        for (Docente d : gestionDocentes.listarTodos()) {
            cmbDocenteAsignatura.getItems().add(d.getCodigo() + " - " + d.getNombreCompleto());
        }
        cmbDocenteAsignatura.getSelectionModel().selectFirst();

        cmbAsignaturaDocente.getItems().clear();
        cmbAsignaturaDocente.getItems().add("Seleccione una asignatura");
        for (Asignatura a : gestionAsignaturas.listarTodos()) {
            cmbAsignaturaDocente.getItems().add(a.getNombre());
        }
        cmbAsignaturaDocente.getSelectionModel().selectFirst();

        cmbDocenteTutor.getItems().clear();
        cmbDocenteTutor.getItems().add("Seleccione un docente");
        for (Docente d : gestionDocentes.listarTodos()) {
            cmbDocenteTutor.getItems().add(d.getCodigo() + " - " + d.getNombreCompleto());
        }
        cmbDocenteTutor.getSelectionModel().selectFirst();

        cmbGrupoTutor.getItems().clear();
        cmbGrupoTutor.getItems().add("Seleccione un grupo");
        for (Grupo g : gestionGrupos.listarTodos()) {
            cmbGrupoTutor.getItems().add(g.getNombre());
        }
        cmbGrupoTutor.getSelectionModel().selectFirst();

        cmbCoordinador.getItems().clear();
        cmbCoordinador.getItems().add("Seleccione un coordinador");
        List<Usuario> usuarios = gestionUsuarios.listarTodos();
        for (Usuario u : usuarios) {
            if (u instanceof CoordinadorAcademico) {
                cmbCoordinador.getItems().add(u.getCodigo() + " - " + u.getNombre() + " " + u.getApellido());
            }
        }
        cmbCoordinador.getSelectionModel().selectFirst();

        cmbGradoCoordinacion.getItems().clear();
        cmbGradoCoordinacion.getItems().add("Seleccione un grado");
        for (Grado g : gestionGrados.listarTodos()) {
            cmbGradoCoordinacion.getItems().add(g.getNombre());
        }
        cmbGradoCoordinacion.getSelectionModel().selectFirst();
    }

    private void cargarDatos() {
        cargarAsignacionesDocente();
        cargarTutores();
        cargarCoordinaciones();
    }

    private void configurarTablaAsignacionesDocente() {
        colCodigoDocenteAsignatura.setCellValueFactory(new PropertyValueFactory<>("codigoDocente"));
        colNombreDocenteAsignatura.setCellValueFactory(new PropertyValueFactory<>("nombreDocente"));
        colAsignaturaDocente.setCellValueFactory(new PropertyValueFactory<>("nombreAsignatura"));
        configurarAccionesAsignaturaDocente();
    }

    private void configurarAccionesAsignaturaDocente() {
        colAccionesAsignaturaDocente.setCellFactory(param -> new TableCell<>() {
            private final Button btnQuitar = new Button("Quitar");
            {
                btnQuitar.getStyleClass().add("boton-tabla-eliminar");
                btnQuitar.setOnAction(e -> {
                    AsignacionItem item = getTableView().getItems().get(getIndex());
                    Docente docente = gestionDocentes.buscarPorCodigo(item.getCodigoDocente());
                    if (docente != null) {
                        gestionDocentes.removerAsignatura(docente, item.getAsignatura());
                    }
                    cargarAsignacionesDocente();
                });
            }
            @Override protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : new HBox(10, btnQuitar));
            }
        });
    }

    private void cargarAsignacionesDocente() {
        List<AsignacionItem> items = new ArrayList<>();
        for (Docente d : gestionDocentes.listarTodos()) {
            for (Asignatura a : d.getAsignaturas()) {
                items.add(new AsignacionItem(d.getCodigo(), d.getNombreCompleto(), a.getNombre(), a));
            }
        }
        tablaAsignaturasDocente.setItems(FXCollections.observableArrayList(items));
    }

    private void configurarTablaTutores() {
        colGrupoTutor.setCellValueFactory(new PropertyValueFactory<>("nombreGrupo"));
        colDocenteTutor.setCellValueFactory(new PropertyValueFactory<>("nombreTutor"));
        configurarAccionesTutor();
    }

    private void configurarAccionesTutor() {
        colAccionesTutor.setCellFactory(param -> new TableCell<>() {
            private final Button btnQuitar = new Button("Quitar");
            {
                btnQuitar.getStyleClass().add("boton-tabla-eliminar");
                btnQuitar.setOnAction(e -> {
                    TutorItem item = getTableView().getItems().get(getIndex());
                    Grupo grupo = item.getGrupo();
                    gestionGrupos.removerTutor(grupo);
                    cargarTutores();
                });
            }
            @Override protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : new HBox(10, btnQuitar));
            }
        });
    }

    private void cargarTutores() {
        List<TutorItem> items = new ArrayList<>();
        for (Grupo g : gestionGrupos.listarTodos()) {
            if (g.getTutor() != null) {
                items.add(new TutorItem(g));
            }
        }
        tablaTutores.setItems(FXCollections.observableArrayList(items));
        tablaTutores.refresh();
    }

    private void configurarTablaCoordinaciones() {
        colCoordinadorNombre.setCellValueFactory(new PropertyValueFactory<>("nombreCoordinador"));
        colGradoCoordinado.setCellValueFactory(new PropertyValueFactory<>("nombreGrado"));
        configurarAccionesCoordinacion();
    }

    private void configurarAccionesCoordinacion() {
        colAccionesCoordinacion.setCellFactory(param -> new TableCell<>() {
            private final Button btnQuitar = new Button("Quitar");
            {
                btnQuitar.getStyleClass().add("boton-tabla-eliminar");
                btnQuitar.setOnAction(e -> {
                    CoordinacionItem item = getTableView().getItems().get(getIndex());
                    CoordinadorAcademico coord = item.getCoordinador();
                    Grado grado = item.getGrado();
                    coord.removerGrado(grado);
                    gestionUsuarios.actualizarUsuario(coord);
                    cargarCoordinaciones();
                });
            }
            @Override protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : new HBox(10, btnQuitar));
            }
        });
    }

    private void cargarCoordinaciones() {
        List<CoordinacionItem> items = new ArrayList<>();
        List<Usuario> usuarios = gestionUsuarios.listarTodos();
        for (Usuario u : usuarios) {
            if (u instanceof CoordinadorAcademico coord) {
                for (Grado g : coord.getGradosSupervisados()) {
                    items.add(new CoordinacionItem(coord, g));
                }
            }
        }
        tablaCoordinaciones.setItems(FXCollections.observableArrayList(items));
        tablaCoordinaciones.refresh();
    }

    @FXML private void handleAgregarAsignaturaDocente() {
        String docenteSel = cmbDocenteAsignatura.getValue();
        String asigSel = cmbAsignaturaDocente.getValue();
        if (docenteSel == null || docenteSel.startsWith("Seleccione")
                || asigSel == null || asigSel.startsWith("Seleccione")) {
            Dialogos.M1("Seleccione un docente y una asignatura.");
            return;
        }
        String cod = docenteSel.split(" - ")[0];
        Docente docente = gestionDocentes.buscarPorCodigo(cod);
        if (docente == null) {
            Dialogos.M1("Docente no encontrado.");
            return;
        }
        Asignatura a = gestionAsignaturas.buscarAsignatura(asigSel);
        if (a == null) {
            Dialogos.M1("Asignatura no encontrada.");
            return;
        }
        try {
            gestionDocentes.agregarAsignatura(docente, a);
            cargarAsignacionesDocente();
        } catch (IllegalArgumentException e) {
            Dialogos.M1(e.getMessage());
        }
    }

    @FXML private void handleAsignarTutor() {
        String docenteSel = cmbDocenteTutor.getValue();
        String grupoSel = cmbGrupoTutor.getValue();
        if (docenteSel == null || docenteSel.startsWith("Seleccione")
                || grupoSel == null || grupoSel.startsWith("Seleccione")) {
            Dialogos.M1("Seleccione un docente y un grupo.");
            return;
        }
        String cod = docenteSel.split(" - ")[0];
        Docente docente = gestionDocentes.buscarPorCodigo(cod);
        if (docente == null) {
            Dialogos.M1("Docente no encontrado.");
            return;
        }
        Grupo grupo = gestionGrupos.buscarPorNombre(grupoSel);
        if (grupo == null) {
            Dialogos.M1("Grupo no encontrado.");
            return;
        }
        try {
            gestionGrupos.asignarTutor(grupo, docente);
            cargarTutores();
        } catch (IllegalArgumentException e) {
            Dialogos.M1(e.getMessage());
        }
    }

    @FXML private void handleAsignarCoordinacion() {
        String coordSel = cmbCoordinador.getValue();
        String gradoSel = cmbGradoCoordinacion.getValue();
        if (coordSel == null || coordSel.startsWith("Seleccione")
                || gradoSel == null || gradoSel.startsWith("Seleccione")) {
            Dialogos.M1("Seleccione un coordinador y un grado.");
            return;
        }
        String cod = coordSel.split(" - ")[0];
        Usuario u = gestionUsuarios.buscarPorCodigo(cod);
        if (!(u instanceof CoordinadorAcademico coord)) {
            Dialogos.M1("El usuario seleccionado no es un coordinador.");
            return;
        }
        Grado g = gestionGrados.buscarPorNombre(gradoSel);
        if (g == null) {
            Dialogos.M1("Grado no encontrado.");
            return;
        }
        try {
            coord.agregarGrado(g);
            gestionUsuarios.actualizarUsuario(coord);
            cargarCoordinaciones();
        } catch (IllegalArgumentException e) {
            Dialogos.M1(e.getMessage());
        }
    }

    public TabPane getTabPane() {
        return tabPaneAsignaciones;
    }

    public static class AsignacionItem {
        private final String codigoDocente;
        private final String nombreDocente;
        private final String nombreAsignatura;
        private final Asignatura asignatura;

        public AsignacionItem(String codigoDocente, String nombreDocente, String nombreAsignatura, Asignatura asignatura) {
            this.codigoDocente = codigoDocente;
            this.nombreDocente = nombreDocente;
            this.nombreAsignatura = nombreAsignatura;
            this.asignatura = asignatura;
        }

        public String getCodigoDocente() { return codigoDocente; }
        public String getNombreDocente() { return nombreDocente; }
        public String getNombreAsignatura() { return nombreAsignatura; }
        public Asignatura getAsignatura() { return asignatura; }
    }

    public static class TutorItem {
        private final Grupo grupo;

        public TutorItem(Grupo grupo) {
            this.grupo = grupo;
        }

        public Grupo getGrupo() { return grupo; }
        public String getNombreGrupo() { return grupo.getNombre(); }
        public String getNombreTutor() {
            return grupo.getTutor() != null ? grupo.getTutor().getNombreCompleto() : "Sin tutor";
        }
    }

    public static class CoordinacionItem {
        private final CoordinadorAcademico coordinador;
        private final Grado grado;

        public CoordinacionItem(CoordinadorAcademico coordinador, Grado grado) {
            this.coordinador = coordinador;
            this.grado = grado;
        }

        public CoordinadorAcademico getCoordinador() { return coordinador; }
        public Grado getGrado() { return grado; }
        public String getNombreCoordinador() { return coordinador.getNombreCompleto(); }
        public String getNombreGrado() { return grado.getNombre(); }
    }
}