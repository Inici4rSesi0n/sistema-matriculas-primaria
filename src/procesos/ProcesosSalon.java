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

public class ProcesosSalon {
    public static Salon leerSalon(FRSalones vista, Grado grado, Docente tutor) {
        String aula = vista.txtSalon.getText().trim();
        int cupoMax = Integer.parseInt(vista.txtAlumnosMAX.getText().trim());
        return new Salon(aula, grado, tutor, cupoMax);
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
    public static String validarCampos(FRSalones vista, ListaSalones lista) {
        String aula = vista.txtSalon.getText().trim();
        if (aula.isEmpty()) return "El nombre del salón no puede estar vacío.";
        if (vista.cbxGrado.getSelectedItem() == null) return "Debe seleccionar un grado.";
        String gradoSeleccionado = (String) vista.cbxGrado.getSelectedItem();
        if (lista.buscarPorGrado(gradoSeleccionado).stream()
                .anyMatch(s -> s.getAula().equalsIgnoreCase(aula))) {
            return "Ya existe el salón '" + aula + "' en " + gradoSeleccionado + ".";
        }
        if (vista.txtAlumnosMAX.getText().trim().isEmpty())
            return "El cupo máximo no puede estar vacío.";

        try {
            Integer.parseInt(vista.txtAlumnosMAX.getText().trim());
        } catch (NumberFormatException e) {
            return "El cupo máximo debe ser un número válido.";
        }
        return null;}
}