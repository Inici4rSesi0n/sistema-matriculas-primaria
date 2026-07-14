package principal;
import controlador.ControlPrincipal;
import vista.FPrincipal;
import javax.swing.SwingUtilities;
/**
 *
 * @author inici4rsesi0n
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FPrincipal principal = new FPrincipal();
            ControlPrincipal control = new ControlPrincipal(principal);
            principal.setTitle("CTP - Sistema de Matrículas");
            principal.setVisible(true);
        });
    }
}
