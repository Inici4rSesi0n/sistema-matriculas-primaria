package procesos;
import modelo.Docente;
import modelo.Grado;
import modelo.Salon;
import modelo.repositorio.CatalogoGrados;
import modelo.repositorio.ListaDocentes;
import modelo.repositorio.ListaSalones;
import modelo.tabla.SalonTable;
import vista.FRSalones;
import javax.swing.JTable;
/**
 *
 * @author inici4rsesi0n
 */
public class ProcesosSalon {
    public static String validarDatosSalon(String aula, String gradoSeleccionado, String cupoMaxTexto, ListaSalones lista) {
        String aulaTrim = aula != null ? aula.trim() : "";
        if (aulaTrim.isEmpty()) return "El nombre del salón no puede estar vacío.";
        if (gradoSeleccionado == null) return "Debe seleccionar un grado.";
        if (lista.buscarPorGrado(gradoSeleccionado).stream()
                .anyMatch(s -> s.getAula().equalsIgnoreCase(aulaTrim))) {
            return "Ya existe el salón '" + aulaTrim + "' en " + gradoSeleccionado + ".";
        }
        if (cupoMaxTexto == null || cupoMaxTexto.trim().isEmpty())
            return "El cupo máximo no puede estar vacío.";
        try {
            Integer.parseInt(cupoMaxTexto.trim());
        } catch (NumberFormatException e) {
            return "El cupo máximo debe ser un número válido.";
        }
        return null;
    }
    public static String validarCampos(FRSalones vista, ListaSalones lista) {
        String aula = vista.txtSalon.getText().trim();
        String grado = (String) vista.cbxGrado.getSelectedItem();
        String cupo = vista.txtAlumnosMAX.getText().trim();
        return validarDatosSalon(aula, grado, cupo, lista);
    }
    public static Salon crearSalon(String aula, int cupoMax, Grado grado, Docente tutor) {
        return new Salon(aula, grado, tutor, cupoMax);
    }
    public static Salon leerSalon(FRSalones vista, Grado grado, Docente tutor) {
        String aula = vista.txtSalon.getText().trim();
        int cupoMax = Integer.parseInt(vista.txtAlumnosMAX.getText().trim());
        return crearSalon(aula, cupoMax, grado, tutor);
    }
    public static void limpiarCampos(FRSalones vista) {
        vista.txtSalon.setText("");
        vista.txtAlumnosMAX.setText("");
        vista.cbxGrado.setSelectedIndex(0);
        vista.cbxTutor.setSelectedIndex(0);
        vista.tblRegistroSalones.clearSelection();
        vista.txtSalon.requestFocus();
    }
    public static void mostrarDatos(JTable tabla, ListaSalones lista) {
        SalonTable modelo = new SalonTable(lista);
        tabla.setModel(modelo);
    }
    public static void llenarComboGrados(FRSalones vista, CatalogoGrados catalogo) {
        for (int i = 0; i < catalogo.getGrados().size(); i++) {
            vista.cbxGrado.addItem(catalogo.getGrados().get(i).getNom());
        }
    }
    public static void llenarComboTutores(FRSalones vista, ListaDocentes listaDocentes) {
        vista.cbxTutor.addItem("Sin tutor");
        for (int i = 0; i < listaDocentes.getLista().size(); i++) {
            Docente d = listaDocentes.obtener(i);
            vista.cbxTutor.addItem(d.getNombreCompleto() + " (" + d.getCod() + ")");
        }
    }
}