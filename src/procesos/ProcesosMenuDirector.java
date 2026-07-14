package procesos;
import controlador.ControlMenuRegistros;
import vista.*;
import javax.swing.SwingUtilities;

public class ProcesosMenuDirector {

    public static void abrirMenuRegistros(FMenuDirector ventanaActual) {
        FPrincipal principal = (FPrincipal) SwingUtilities.getAncestorOfClass(FPrincipal.class, ventanaActual);
        FMenuRegistros menuRegistros = new FMenuRegistros();
        ControlMenuRegistros control = new ControlMenuRegistros(menuRegistros, principal);
        menuRegistros.setTitle("Menú de Registros");
        ProcesosPrincipal.mostrarInternalFrame(principal, menuRegistros);
        ventanaActual.setVisible(false);
    }
}
