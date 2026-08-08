package presentacion.controlador;

import presentacion.dialogos.Dialogos;
import presentacion.estadousuario.SesionUsuario;
import infraestructura.configuracion.ProveedorInfraestructura;
import infraestructura.seguridad.UtilLimpieza;
import dominio.modelo.Usuario;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 *
 * @author inici4rsesi0n
 */
public class FxLoginController implements Initializable {
    @FXML private TextField txtCodigo;
    @FXML private PasswordField txtPassword;
    @FXML private TextField txtPasswordRevealed;
    @FXML private Label btnTogglePassword;
    @FXML private Button btnLogin;
    @FXML private Button btnRegresar;
    @FXML private CheckBox chkMantenerSesion;
    @FXML private ImageView imgLogo;
    private String modo;
    private Usuario usuarioAutenticado;
    private boolean regresando = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        sincronizarPasswordFields();
        btnTogglePassword.setOnMouseClicked(this::togglePasswordVisibility);
        txtPasswordRevealed.textProperty().addListener((obs, oldVal, newVal) -> {
            if (txtPasswordRevealed.isVisible()) {
                txtPassword.setText(newVal);
            }
        });
        txtPassword.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!txtPasswordRevealed.isVisible()) {
                txtPasswordRevealed.setText(newVal);
            }
        });
    }

    public void limpiarCampos() {
        txtCodigo.clear();
        txtPassword.clear();
        txtPasswordRevealed.clear();
        chkMantenerSesion.setSelected(false);
        usuarioAutenticado = null;
        regresando = false;
    }

    public void setModo(String modo) {
        this.modo = modo;
    }

    public boolean isLoginExitoso() {
        return usuarioAutenticado != null;
    }

    public Usuario getUsuarioAutenticado() {
        return usuarioAutenticado;
    }

    public boolean isRegresando() {
        return regresando;
    }

    @FXML
    private void handleLogin() {
        String codigo = txtCodigo.getText();
        char[] contraseña = obtenerContrasena();

        if (codigo == null || codigo.isBlank() || contraseña == null || contraseña.length == 0) {
            Dialogos.M1("Debe ingresar un código y una contraseña.");
            return;
        }
        try {
            Usuario usuario = ProveedorInfraestructura.getAutenticarUsuario().ejecutar(codigo, contraseña);
            if (usuario != null) {
                this.usuarioAutenticado = usuario;
                if (chkMantenerSesion.isSelected()) {
                    SesionUsuario.iniciarSesion(usuario);
                }
                Stage stage = (Stage) btnLogin.getScene().getWindow();
                stage.close();
            } else {
                Dialogos.M1("Código o contraseña incorrectos.");
            }
        } finally {
            UtilLimpieza.limpiarContraseña(contraseña);
        }
    }

    @FXML
    private void handleRegresar() {
        regresando = true;
        Stage stage = (Stage) btnLogin.getScene().getWindow();
        stage.close();
    }

    private char[] obtenerContrasena() {
        if (txtPasswordRevealed.isVisible()) {
            String pwd = txtPasswordRevealed.getText();
            return (pwd != null) ? pwd.toCharArray() : null;
        } else {
            String pwd = txtPassword.getText();
            return (pwd != null) ? pwd.toCharArray() : null;
        }
    }

    private void togglePasswordVisibility(MouseEvent event) {
        boolean revelado = txtPasswordRevealed.isVisible();
        txtPasswordRevealed.setVisible(!revelado);
        txtPassword.setVisible(revelado);

        if (!revelado) {
            txtPasswordRevealed.setText(txtPassword.getText());
        } else {
            txtPassword.setText(txtPasswordRevealed.getText());
        }
        btnTogglePassword.setText(revelado ? "👁" : "🙈");
    }

    private void sincronizarPasswordFields() {
        txtPasswordRevealed.setVisible(false);
        txtPassword.setVisible(true);
    }
}