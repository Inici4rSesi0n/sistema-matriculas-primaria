package controlador;
import modelo.Estudiante;
import modelo.Usuario;
import procesos.ProcesosCTPClass;
import procesos.ProcesosPrincipal;
import vista.FCTPClass;
import vista.FCTP_Asignaturas;
import vista.FCTP_Horario;
import vista.FLogin;
import vista.FPrincipal;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
/**
 *
 * @author inici4rsesi0n
 */
public class ControlCTPClass implements ActionListener {

    private FCTPClass vista;
    private Usuario usuario;
    private FPrincipal principal;

    public ControlCTPClass(FCTPClass vista, Usuario usuario, FPrincipal principal) {
        this.vista = vista;
        this.usuario = usuario;
        this.principal = principal;
        ProcesosCTPClass.cargarDatosUsuario(vista, usuario);
        vista.btnAsignaturas.addActionListener(this);
        vista.btnHorario.addActionListener(this);
        vista.btnCerrarSesion.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.btnAsignaturas) {
            if (usuario instanceof Estudiante) {
                FCTP_Asignaturas asignaturas = new FCTP_Asignaturas();
                ControlCTP_Asignaturas ctrlAsig = new ControlCTP_Asignaturas(asignaturas, (Estudiante) usuario);
                ProcesosCTPClass.mostrarInternalFrame(vista.dspMostrador, asignaturas);
            }
        } else if (e.getSource() == vista.btnHorario) {
            if (usuario instanceof Estudiante) {
                FCTP_Horario horario = new FCTP_Horario();
                ControlCTP_Horario ctrlHorario = new ControlCTP_Horario(horario, (Estudiante) usuario);
                ProcesosCTPClass.mostrarInternalFrame(vista.dspMostrador, horario);
            }
        } else if (e.getSource() == vista.btnCerrarSesion) {
            vista.dispose();
            FLogin login = new FLogin();
            ControlLogin controlLogin = new ControlLogin(login, principal);
            ProcesosPrincipal.mostrarInternalFrame(principal, login);
        }
    }
}
