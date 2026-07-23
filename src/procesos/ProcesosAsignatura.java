package procesos;
import modelo.Asignatura;
import modelo.repositorio.ListaAsignaturas;
import modelo.tabla.AsignaturaTable;
import vista.FRAsignaturas;
import javax.swing.JTable;
/**
 *
 * @author inici4rsesi0n
 */
public class ProcesosAsignatura {
    public static Asignatura crearAsignatura(String nombre) {
        return new Asignatura(nombre);
    }
    public static Asignatura leerAsignatura(FRAsignaturas vista){
        String nombre = vista.txtAsignatura.getText().trim();
        return crearAsignatura(nombre);
    }
    public static void limpiarCampos(FRAsignaturas vista){
        vista.txtAsignatura.setText("");
        vista.tblRegistroAsignaturas.clearSelection();
        vista.txtAsignatura.requestFocus();
    }
    public static void mostrarDatos(JTable tabla, ListaAsignaturas lista){
        AsignaturaTable modelo = new AsignaturaTable(lista);
        tabla.setModel(modelo);
    }
    public static void seleccionarFila(JTable tabla, int fila){
        if(fila >= 0 && fila < tabla.getRowCount()){
            tabla.setRowSelectionInterval(fila, fila);
            tabla.scrollRectToVisible(tabla.getCellRect(fila, 0, true));
        }
    }
    public static String validarDatosAsignatura(String nombre, ListaAsignaturas lista) {
        String nombreTrim = nombre != null ? nombre.trim() : "";
        if (nombreTrim.isEmpty()) {
            return "El nombre de la asignatura no puede estar vacío.";
        }
        if (lista.buscarPorNombre(nombreTrim) != -1) {
            return "La asignatura '" + nombreTrim + "' ya existe.";
        }
        return null;
    }
    public static String validarCampos(FRAsignaturas vista, ListaAsignaturas lista){
        String nombre = vista.txtAsignatura.getText().trim();
        return validarDatosAsignatura(nombre, lista);
    }
}