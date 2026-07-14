package procesos;
import modelo.Docente;
import modelo.Estudiante;
import modelo.Usuario;
import vista.FCTPClass;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;

public class ProcesosCTPClass {
    public static void cargarDatosUsuario(FCTPClass vista, Usuario usuario) {
        vista.txtNombreCompleto.setText(usuario.getNom() + " " + usuario.getApe());
        vista.txtCodigo.setText(usuario.getCod());
        if (usuario instanceof Estudiante) {
            Estudiante est = (Estudiante) usuario;
            vista.txtGrado.setText(est.getGrado() != null ? est.getGrado().getNom() : "Sin grado");
            vista.txtSalon.setText(est.getSalon() != null ? est.getSalon().getAula() : "Sin salón");
        } else if (usuario instanceof Docente) {
            Docente doc = (Docente) usuario;
            if (doc.getTutoria() != null) {
                vista.txtGrado.setText(doc.getTutoria().getGrado().getNom());
                vista.txtSalon.setText(doc.getTutoria().getAula());
            } else {
                vista.txtGrado.setText("");
                vista.txtSalon.setText("");
            }
        }
    }
    public static void mostrarInternalFrame(JDesktopPane desktop, JInternalFrame frame) {
        for (JInternalFrame f : desktop.getAllFrames()) {
            if (f.getClass().equals(frame.getClass())) {
                try {
                    f.setSelected(true);
                } catch (java.beans.PropertyVetoException e) {
                }
                return;
            }
        }
        desktop.add(frame);
        frame.setVisible(true);
        try {
            frame.setMaximum(true);
        } catch (java.beans.PropertyVetoException e) {
            frame.setSize(desktop.getWidth() / 2, desktop.getHeight() / 2);
            frame.setLocation(20, 20);
        }
    }
}
