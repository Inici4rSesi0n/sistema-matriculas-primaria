package presentacion.controlador;

import presentacion.dialogos.Dialogos;
import infraestructura.configuracion.SpringContext;
import dominio.modelo.Usuario;
import aplicacion.casosdeuso.GestionUsuarios;
import dominio.puerto.externo.HashProvider;
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
public class s_GestionUsuariosController implements Initializable {

    @FXML private TableView<Usuario> tablaUsuarios;
    @FXML private TableColumn<Usuario, String> colCodigo;
    @FXML private TableColumn<Usuario, String> colNombre;
    @FXML private TableColumn<Usuario, String> colApellido;
    @FXML private TableColumn<Usuario, String> colDni;
    @FXML private TableColumn<Usuario, String> colRol;
    @FXML private TableColumn<Usuario, Void> colAcciones;

    @FXML private VBox panelFormulario;
    @FXML private ComboBox<Usuario.Rol> cmbRol;
    @FXML private TextField txtCodigo;
    @FXML private TextField txtNombre;
    @FXML private TextField txtApellido;
    @FXML private TextField txtDni;
    @FXML private TextField txtEdad;
    @FXML private TextField txtContrasena;
    @FXML private Button btnNuevoUsuario;
    @FXML private Button btnGuardar;
    @FXML private Button btnCancelar;

    private ObservableList<Usuario> listaUsuarios;
    private Usuario usuarioEditando;
    private GestionUsuarios gestionUsuarios;
    private HashProvider hashProvider;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        gestionUsuarios = SpringContext.getBean(GestionUsuarios.class);
        hashProvider = SpringContext.getBean(HashProvider.class);
        configurarTabla();
        configurarColumnaAcciones();
        configurarComboRoles();
        cargarUsuarios();
    }

    private void configurarTabla() {
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colApellido.setCellValueFactory(new PropertyValueFactory<>("apellido"));
        colDni.setCellValueFactory(new PropertyValueFactory<>("dni"));
        colRol.setCellValueFactory(new PropertyValueFactory<>("rol"));
    }

    private void configurarColumnaAcciones() {
        colAcciones.setCellFactory(param -> new TableCell<>() {
            private final Button btnEditar = new Button("Editar");
            private final Button btnEliminar = new Button("Eliminar");
            private final HBox hbox = new HBox(10, btnEditar, btnEliminar);

            {
                btnEditar.getStyleClass().add("boton-tabla-editar");
                btnEliminar.getStyleClass().add("boton-tabla-eliminar");

                btnEditar.setOnAction(event -> {
                    Usuario usuario = getTableView().getItems().get(getIndex());
                    cargarUsuarioEnFormulario(usuario);
                });

                btnEliminar.setOnAction(event -> {
                    Usuario usuario = getTableView().getItems().get(getIndex());
                    eliminarUsuario(usuario);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(hbox);
                }
            }
        });
    }

    private void configurarComboRoles() {
        cmbRol.getItems().addAll(Usuario.Rol.values());
        cmbRol.setConverter(new StringConverter<>() {
            @Override
            public String toString(Usuario.Rol rol) {
                return rol != null ? rol.name() : "";
            }

            @Override
            public Usuario.Rol fromString(String string) {
                return Usuario.Rol.valueOf(string);
            }
        });
        cmbRol.getSelectionModel().selectFirst();
    }

    private void cargarUsuarios() {
        listaUsuarios = FXCollections.observableArrayList(gestionUsuarios.listarTodos());
        tablaUsuarios.setItems(listaUsuarios);
    }

    @FXML
    private void handleNuevoUsuario() {
        usuarioEditando = null;
        limpiarFormulario();
        txtCodigo.setText(generarCodigoUnico());
        txtCodigo.setEditable(true);
        panelFormulario.setManaged(true);
        panelFormulario.setVisible(true);
    }

    @FXML
    private void handleCancelar() {
        limpiarFormulario();
        panelFormulario.setManaged(false);
        panelFormulario.setVisible(false);
    }

    @FXML
    private void handleGuardar() {
        Usuario.Rol rol = cmbRol.getValue();
        String codigo = txtCodigo.getText().trim();
        String nombre = txtNombre.getText().trim();
        String apellido = txtApellido.getText().trim();
        String dni = txtDni.getText().trim();
        String edadTexto = txtEdad.getText().trim();
        String contrasenaTexto = txtContrasena.getText();

        if (rol == null || codigo.isBlank() || nombre.isBlank()
                || apellido.isBlank() || dni.isBlank() || edadTexto.isBlank()) {
            Dialogos.M1("Todos los campos obligatorios deben estar completos.");
            return;
        }

        int edad;
        try {
            edad = Integer.parseInt(edadTexto);
        } catch (NumberFormatException e) {
            Dialogos.M1("La edad debe ser un número válido.");
            return;
        }

        if (usuarioEditando == null && contrasenaTexto.isBlank()) {
            Dialogos.M1("Debe asignar una contraseña al nuevo usuario.");
            return;
        }

        try {
            if (usuarioEditando == null) {
                char[] contrasena = contrasenaTexto.toCharArray();
                String hash = hashProvider.generarHash(contrasena);
                gestionUsuarios.agregarUsuario(codigo, hash, dni, nombre, apellido, edad, rol, null);
            } else {
                if (!codigo.equals(usuarioEditando.getCodigo())) {
                    Dialogos.M1("No se puede modificar el código de un usuario existente.");
                    return;
                }
                if (!dni.equals(usuarioEditando.getDni()) && gestionUsuarios.existeDni(dni, usuarioEditando)) {
                    Dialogos.M1("Ya existe otro usuario con ese DNI.");
                    return;
                }

                usuarioEditando.setNombre(nombre);
                usuarioEditando.setApellido(apellido);
                usuarioEditando.setDni(dni);
                usuarioEditando.setEdad(edad);
                if (!contrasenaTexto.isBlank()) {
                    char[] nuevaContrasena = contrasenaTexto.toCharArray();
                    usuarioEditando.setHashContrasena(hashProvider.generarHash(nuevaContrasena));
                }
                gestionUsuarios.actualizarUsuario(usuarioEditando);
            }

            cargarUsuarios();
            limpiarFormulario();
            panelFormulario.setManaged(false);
            panelFormulario.setVisible(false);
        } catch (IllegalArgumentException e) {
            Dialogos.M1(e.getMessage());
        }
    }

    private void cargarUsuarioEnFormulario(Usuario usuario) {
        usuarioEditando = usuario;
        panelFormulario.setManaged(true);
        panelFormulario.setVisible(true);
        cmbRol.setValue(usuario.getRol());
        txtCodigo.setText(usuario.getCodigo());
        txtCodigo.setEditable(false);
        txtNombre.setText(usuario.getNombre());
        txtApellido.setText(usuario.getApellido());
        txtDni.setText(usuario.getDni());
        txtEdad.setText(String.valueOf(usuario.getEdad()));
        txtContrasena.clear();
    }

    private void eliminarUsuario(Usuario usuario) {
        int respuesta = Dialogos.M3("Confirmar eliminación",
                "¿Está seguro de eliminar al usuario " + usuario.getCodigo() + "?");
        if (respuesta == 0) {
            try {
                gestionUsuarios.eliminarUsuario(usuario);
                cargarUsuarios();
            } catch (IllegalArgumentException e) {
                Dialogos.M1(e.getMessage());
            }
        }
    }

    private void limpiarFormulario() {
        usuarioEditando = null;
        cmbRol.getSelectionModel().selectFirst();
        txtCodigo.clear();
        txtCodigo.setEditable(true);
        txtNombre.clear();
        txtApellido.clear();
        txtDni.clear();
        txtEdad.clear();
        txtContrasena.clear();
    }

    private String generarCodigoUnico() {
        String codigo;
        do {
            int numero = (int) (Math.random() * 90_000_000) + 10_000_000;
            codigo = "U" + numero;
        } while (gestionUsuarios.buscarPorCodigo(codigo) != null);
        return codigo;
    }
}