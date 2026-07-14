package procesos;
import almacenamiento.Persistencia;
import modelo.Asignatura;
import modelo.Docente;
import modelo.repositorio.ListaAsignaturas;
import modelo.repositorio.ListaDocentes;
import vista.FRGestionAsignaturas;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import java.util.ArrayList;
import java.util.List;

public class ProcesosGestionAsignaturas {
    public static void cargarDocentesEnCombo(FRGestionAsignaturas vista, ListaDocentes listaDocentes) {
        for (int i = 0; i < listaDocentes.getLista().size(); i++) {
            Docente d = listaDocentes.obtener(i);
            vista.cbxDocente.addItem(d.getNombreCompleto() + " (" + d.getCod() + ")");
        }
    }
    public static void cargarDatosDocente(Docente docente, ListaAsignaturas todas,
            JList<String> lstAsignadas, JList<String> lstDisponibles) {
        DefaultListModel<String> modeloAsignadas = new DefaultListModel<>();
        if (docente.getAsignaturas() != null) {
            for (Asignatura a : docente.getAsignaturas()) {
                modeloAsignadas.addElement(a.getNom());
            }
        }
        lstAsignadas.setModel(modeloAsignadas);
        DefaultListModel<String> modeloDisponibles = new DefaultListModel<>();
        for (int i = 0; i < todas.getLista().size(); i++) {
            Asignatura a = todas.obtener(i);
            boolean yaAsignada = false;
            if (docente.getAsignaturas() != null) {
                for (Asignatura asignada : docente.getAsignaturas()) {
                    if (asignada.getNom().equalsIgnoreCase(a.getNom())) {
                        yaAsignada = true;
                        break;
                    }
                }
            }
            if (!yaAsignada) {
                modeloDisponibles.addElement(a.getNom());
            }
        }lstDisponibles.setModel(modeloDisponibles);
    }
    public static void moverAsignatura(JList<String> origen, JList<String> destino) {
        String seleccionado = origen.getSelectedValue();
        if (seleccionado == null) return;
        DefaultListModel<String> modeloOrigen = (DefaultListModel<String>) origen.getModel();
        DefaultListModel<String> modeloDestino = (DefaultListModel<String>) destino.getModel();
        modeloOrigen.removeElement(seleccionado);
        modeloDestino.addElement(seleccionado);
    }
    public static void guardarAsignaciones(JList<String> lstAsignadas, Docente docente, 
            ListaAsignaturas listaAsignaturas, ListaDocentes listaDocentes){
        List<Asignatura> nuevas = new ArrayList<>();
        for (int i = 0; i < lstAsignadas.getModel().getSize(); i++) {
            String nombre = lstAsignadas.getModel().getElementAt(i);
            Asignatura a = buscarAsignaturaPorNombre(nombre, listaAsignaturas);
            if (a != null) {
                nuevas.add(a);
            }
        }
        docente.setAsignaturas(nuevas);
        Persistencia.guardar(listaDocentes, "docentes.bin");
    }
    private static Asignatura buscarAsignaturaPorNombre(String nombre, ListaAsignaturas lista) {
        int pos = lista.buscarPorNombre(nombre);
        return (pos != -1) ? lista.obtener(pos) : null;
    }
}