package controlador;
import almacenamiento.Persistencia;
import modelo.Director;
import modelo.Usuario;
import modelo.repositorio.GestorDirector;
import modelo.repositorio.ListaDocentes;
import modelo.repositorio.ListaEstudiantes;
import procesos.Mensajes;
import procesos.ProcesosLogin;
import procesos.ProcesosPrincipal;
import vista.FLogin;
import vista.FPortalCTP;
import vista.FCTPClass;
import vista.FMenuDirector;
import vista.FPrincipal;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import modelo.Estudiante;
/**
 *
 * @author inici4rsesi0n
 */
public class ControlLogin implements ActionListener {
    private FLogin vista;
    private FPrincipal principal;
    private GestorDirector gestorDirector;
    private ListaDocentes listaDocentes;
    private ListaEstudiantes listaEstudiantes;

    public ControlLogin(FLogin vista, FPrincipal principal) {
        this.vista = vista;
        this.principal = principal;
        this.gestorDirector = Persistencia.cargarGestorDirector();
        this.listaDocentes = Persistencia.cargarListaDocentes();
        this.listaEstudiantes = Persistencia.cargarListaEstudiantes();
        vista.cbxOpciones.addItem("Portal CTP");
        vista.cbxOpciones.addItem("CTP+ Class");
        vista.btnIniciarSesion.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.btnIniciarSesion) {
            String error = ProcesosLogin.validarCampos(vista);
            if (error != null) {
                Mensajes.M1(error);
                return;
            }
            Usuario usuario = ProcesosLogin.autenticarUsuario(
                    vista, gestorDirector, listaDocentes, listaEstudiantes
            );
            if (usuario == null) {
                Mensajes.M1("Código o contraseña incorrectos.");
                return;
            }
            String opcion = (String) vista.cbxOpciones.getSelectedItem();
            if (opcion.equals("Portal CTP")) {
                if (usuario instanceof Director) {
                    FMenuDirector menu = new FMenuDirector();
                    ControlMenuDirector controlMenu = new ControlMenuDirector(menu, principal);
                    vista.dispose();
                    ProcesosPrincipal.mostrarInternalFrame(principal, menu);
                } else if (usuario instanceof Estudiante) {
                    FPortalCTP portal = new FPortalCTP();
                    ControlPortalCTP controlPortal = new ControlPortalCTP(portal, (Estudiante) usuario, principal);
                    vista.dispose();
                    ProcesosPrincipal.mostrarInternalFrame(principal, portal);
                } else {
                    Mensajes.M1("No tienes permisos para acceder a este portal.");
                }
            } else if (opcion.equals("CTP+ Class")) {
                FCTPClass ctpc = new FCTPClass();
                ControlCTPClass control = new ControlCTPClass(ctpc, usuario, principal);
                vista.dispose();
                ProcesosPrincipal.mostrarInternalFrame(principal, ctpc);
            }
        }
    }
}
