package controlador;
import procesos.ProcesosMenuRegistros;
import procesos.ProcesosPrincipal;
import vista.FMenuRegistros;
import vista.FMenuDirector;
import vista.FPrincipal;
import javax.swing.JInternalFrame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyVetoException;
/**
 *
 * @author inici4rsesi0n
 */
public class ControlMenuRegistros implements ActionListener {

    private FMenuRegistros vista;
    private FPrincipal principal;

    public ControlMenuRegistros(FMenuRegistros vista, FPrincipal principal) {
        this.vista = vista;
        this.principal = principal;
        vista.mniEstudiantes.addActionListener(this);
        vista.mniDocentes.addActionListener(this);
        vista.mniSalones.addActionListener(this);
        vista.mniAsignaturas.addActionListener(this);
        vista.mniHorarios.addActionListener(this);
        vista.mniGestionAsignaturas.addActionListener(this);
        vista.btnRegresar.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.mniDocentes) {
            ProcesosPrincipal.mostrarInternalFrame(principal, ProcesosMenuRegistros.abrirDocentes());
        } else if (e.getSource() == vista.mniEstudiantes) {
            ProcesosPrincipal.mostrarInternalFrame(principal, ProcesosMenuRegistros.abrirEstudiantes());
        } else if (e.getSource() == vista.mniSalones) {
            ProcesosPrincipal.mostrarInternalFrame(principal, ProcesosMenuRegistros.abrirSalones());
        } else if (e.getSource() == vista.mniAsignaturas) {
            ProcesosPrincipal.mostrarInternalFrame(principal, ProcesosMenuRegistros.abrirAsignaturas());
        } else if (e.getSource() == vista.mniHorarios) {
            ProcesosPrincipal.mostrarInternalFrame(principal, ProcesosMenuRegistros.abrirHorarios());
        } else if (e.getSource() == vista.mniGestionAsignaturas) {
            ProcesosPrincipal.mostrarInternalFrame(principal, ProcesosMenuRegistros.abrirGestionAsignaturas());
        } else if (e.getSource() == vista.btnRegresar) {
            vista.dispose();
            FMenuDirector menuDirector = new FMenuDirector();
            ControlMenuDirector controlMenu = new ControlMenuDirector(menuDirector, principal);
            ProcesosPrincipal.mostrarInternalFrame(principal, menuDirector);
        }
    }
}
