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
/**
 *
 * @author inici4rsesi0n
 */
public class ProcesosGestionAsignaturas {
    public static void cargarDocentesEnCombo(FRGestionAsignaturas vista, ListaDocentes listaDocentes) {
        for (int i = 0; i < listaDocentes.getLista().size(); i++) {
            Docente d = listaDocentes.obtener(i);
            vista.cbxDocente.addItem(d.getNombreCompleto() + " (" + d.getCod() + ")");
        }
    }
    public static List<String> filtrarNombresDisponibles(Docente docente, ListaAsignaturas todas) {
        List<String> disponibles = new ArrayList<>();
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
                disponibles.add(a.getNom());
            }
        }
        return disponibles;
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
        for (String nombre : filtrarNombresDisponibles(docente, todas)) {
            modeloDisponibles.addElement(nombre);
        }
        lstDisponibles.setModel(modeloDisponibles);
    }
    public static void moverAsignatura(JList<String> origen, JList<String> destino) {
        String seleccionado = origen.getSelectedValue();
        if (seleccionado == null) return;
        DefaultListModel<String> modeloOrigen = (DefaultListModel<String>) origen.getModel();
        DefaultListModel<String> modeloDestino = (DefaultListModel<String>) destino.getModel();
        modeloOrigen.removeElement(seleccionado);
        modeloDestino.addElement(seleccionado);
    }
    public static List<Asignatura> convertiNombresAAsignaturas(List<String> nombres, ListaAsignaturas listaAsignaturas) {
        List<Asignatura> resultado = new ArrayList<>();
        for (String nombre : nombres) {
            int pos = listaAsignaturas.buscarPorNombre(nombre);
            if (pos != -1) {
                resultado.add(listaAsignaturas.obtener(pos));
            }
        }
        return resultado;
    }
    public static void guardarAsignaciones(JList<String> lstAsignadas, Docente docente, 
            ListaAsignaturas listaAsignaturas, ListaDocentes listaDocentes){
        List<String> nombres = new ArrayList<>();
        for (int i = 0; i < lstAsignadas.getModel().getSize(); i++) {
            nombres.add(lstAsignadas.getModel().getElementAt(i));
        }
        List<Asignatura> nuevas = convertiNombresAAsignaturas(nombres, listaAsignaturas);
        docente.setAsignaturas(nuevas);
        Persistencia.guardar(listaDocentes, "docentes.bin");
    }
}