package controlador;
import procesos.ProcesosMenuDirector;
import procesos.ProcesosPrincipal;
import vista.FMenuDirector;
import vista.FLogin;
import vista.FPrincipal;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
/**
 *
 * @author inici4rsesi0n
 */
public class ControlMenuDirector implements ActionListener {
    private FMenuDirector vista;
    private FPrincipal principal;

    public ControlMenuDirector(FMenuDirector vista, FPrincipal principal) {
        this.vista = vista;
        this.principal = principal;
        vista.btnMenuRegistros.addActionListener(e -> {
            ProcesosMenuDirector.abrirMenuRegistros(vista);
        });
        vista.btnCerrarSesion.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.btnCerrarSesion) {
            vista.dispose();
            FLogin login = new FLogin();
            ControlLogin controlLogin = new ControlLogin(login, principal);
            ProcesosPrincipal.mostrarInternalFrame(principal, login);
        }
    }
}
