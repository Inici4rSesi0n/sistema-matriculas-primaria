package procesos;
import modelo.Director;
import modelo.Estudiante;
import modelo.Grado;
import modelo.Salon;
import modelo.repositorio.CatalogoGrados;
import modelo.repositorio.GestorDirector;
import modelo.repositorio.ListaDocentes;
import modelo.repositorio.ListaEstudiantes;
import modelo.repositorio.ListaSalones;
import modelo.tabla.EstudianteTable;
import vista.FREstudiante;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JTable;
import java.util.ArrayList;

public class ProcesosEstudiantes {
    public static Estudiante leerEstudiante(FREstudiante vista, Salon salon, Grado grado) {
        String cod = vista.txtCodigo.getText().trim();
        char[] pwd = vista.txtContraseña.getPassword();
        String hashPwd = Utilidades.generarHash(pwd);
        Utilidades.limpiarContraseña(pwd);
        String dni = vista.txtDNI.getText().trim();
        String nom = vista.txtNombre.getText().trim();
        String ape = vista.txtApellido.getText().trim();
        int age = Integer.parseInt(vista.txtEdad.getText().trim());
        return new Estudiante(salon, grado, cod, hashPwd, dni, nom, ape, age);
    }
    public static void limpiarCampos(FREstudiante vista) {
        vista.txtCodigo.setText("");
        vista.txtDNI.setText("");
        vista.txtNombre.setText("");
        vista.txtApellido.setText("");
        vista.txtEdad.setText("");
        vista.txtContraseña.setText("");
        vista.cbxGrado.setSelectedIndex(0);
        vista.cbxSalon.removeAllItems();
        vista.jTable1.clearSelection();
        vista.txtCodigo.requestFocus();
    }
    public static void mostrarDatos(JTable tabla, ListaEstudiantes lista) {
        EstudianteTable modelo = new EstudianteTable(lista);
        tabla.setModel(modelo);
    }
    public static void llenarComboGrados(FREstudiante vista, CatalogoGrados catalogo) {
        DefaultComboBoxModel<String> modeloGrados = new DefaultComboBoxModel<>();
        for (int i = 0; i < catalogo.getGrados().size(); i++) {
            modeloGrados.addElement(catalogo.getGrados().get(i).getNom());
        }
        vista.cbxGrado.setModel(modeloGrados);
    }
    public static void actualizarComboSalones(FREstudiante vista, ListaSalones listaSalones, String gradoSeleccionado) {
        DefaultComboBoxModel<String> modeloSalones = new DefaultComboBoxModel<>();
        modeloSalones.addElement("Sin Salón");
        if (gradoSeleccionado != null) {
            ArrayList<Salon> salonesDelGrado = listaSalones.buscarPorGrado(gradoSeleccionado);
            for (Salon s : salonesDelGrado) {
                modeloSalones.addElement(s.getAula());
            }
        }
        vista.cbxSalon.setModel(modeloSalones);
    }
    public static ListaEstudiantes filtrarPorGrado(ListaEstudiantes lista, String grado) {
        ListaEstudiantes filtrada = new ListaEstudiantes();
        for (int i = 0; i < lista.getLista().size(); i++) {
            Estudiante e = lista.obtener(i);
            if (e.getSalon() != null && e.getSalon().getGrado().getNom().equals(grado)) {
                filtrada.agregar(e);
            }
        }
        return filtrada;
    }
    public static String validarCampos(FREstudiante vista, ListaEstudiantes lista,
            ListaDocentes listaDocentes, GestorDirector gestorDirector) {
        String cod = vista.txtCodigo.getText().trim();
        if (cod.isEmpty()) {return "El código no puede estar vacío.";}
        if (lista.buscarPorCODIGO(cod) != -1) {return "El código ya existe.";}
        if (listaDocentes.buscarPorCODIGO(cod) != -1) {return "El código ya existe.";}
        Director director = gestorDirector.getDirector();
        if (director.getCod().equals(cod)) {return "El código ya existe.";}
        if (vista.txtDNI.getText().trim().isEmpty()) {return "El DNI no puede estar vacío.";}
        if (vista.txtNombre.getText().trim().isEmpty()) {return "El nombre no puede estar vacío.";}
        if (vista.txtApellido.getText().trim().isEmpty()) {return "El apellido no puede estar vacío.";}
        if (vista.txtEdad.getText().trim().isEmpty()) {return "La edad no puede estar vacía.";}
        if (vista.txtContraseña.getPassword().length == 0) {return "La contraseña no puede estar vacía.";}
        if (vista.cbxGrado.getSelectedItem() == null) {return "Debe seleccionar un grado.";}
        if (vista.cbxSalon.getSelectedItem() == null) {return "Debe seleccionar un salón.";}
        try {
            Integer.parseInt(vista.txtEdad.getText().trim());
        } catch (NumberFormatException e) {
            return "La edad debe ser un número válido.";
        }
        return null;
    }
    public static Salon obtenerSalonSeleccionado(FREstudiante vista, ListaSalones listaSalones) {
        String gradoSeleccionado = (String) vista.cbxGrado.getSelectedItem();
        String aulaSeleccionada = (String) vista.cbxSalon.getSelectedItem();
        if (gradoSeleccionado == null || aulaSeleccionada == null || aulaSeleccionada.equals("Sin Salón")) {
            return null;
        }
        ArrayList<Salon> salonesDelGrado = listaSalones.buscarPorGrado(gradoSeleccionado);
        for (Salon s : salonesDelGrado) {
            if (s.getAula().equals(aulaSeleccionada)) {
                return s;
            }
        }
        return null;
    }
}
