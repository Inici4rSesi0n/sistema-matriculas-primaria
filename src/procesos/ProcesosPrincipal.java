package procesos;
import vista.FPrincipal;
import javax.swing.JInternalFrame;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import java.beans.PropertyVetoException;

public class ProcesosPrincipal {
    public static void mostrarInternalFrame(FPrincipal vista, JInternalFrame frame) {
        for (JInternalFrame f : vista.dspPrincipal.getAllFrames()) {
            if (f.getClass().equals(frame.getClass())) {
                if (!f.isVisible()) {
                    f.setVisible(true);
                }
                try {
                    f.setSelected(true);
                } catch (PropertyVetoException e) {
                }
                return;
            }
        }
        frame.addInternalFrameListener(new InternalFrameAdapter() {
            @Override
            public void internalFrameIconified(InternalFrameEvent e) {
                vista.dspPrincipal.revalidate();
                vista.dspPrincipal.repaint();
            }
            @Override
            public void internalFrameDeiconified(InternalFrameEvent e) {
                vista.dspPrincipal.revalidate();
                vista.dspPrincipal.repaint();
            }
        });
        vista.dspPrincipal.add(frame);
        frame.setVisible(true);
        try {
            frame.setMaximum(true);
        } catch (PropertyVetoException e) {
            frame.setSize(vista.dspPrincipal.getWidth(), vista.dspPrincipal.getHeight());
        }
        try {
            frame.setSelected(true);
        } catch (PropertyVetoException e) {
        }
        frame.toFront();
        vista.dspPrincipal.revalidate();
        vista.dspPrincipal.repaint();
    }
}
