package controlador;
import procesos.ProcesosPrincipal;
import vista.FLogin;
import vista.FPrincipal;
/**
 *
 * @author inici4rsesi0n
 */
public class ControlPrincipal {
    private FPrincipal vista;

    public ControlPrincipal(FPrincipal vista) {
        this.vista = vista;
        FLogin login = new FLogin();
        new ControlLogin(login, vista);
        ProcesosPrincipal.mostrarInternalFrame(vista, login);
    }
}
