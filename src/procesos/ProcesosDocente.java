package procesos;
import modelo.Asignatura;
import modelo.Director;
import modelo.Docente;
import modelo.repositorio.GestorDirector;
import modelo.repositorio.ListaAsignaturas;
import modelo.repositorio.ListaDocentes;
import modelo.repositorio.ListaEstudiantes;
import modelo.tabla.DocenteTable;
import vista.FRDocentes;
import javax.swing.JTable;
import java.util.ArrayList;

public class ProcesosDocente {
    public static Docente leerDocente(FRDocentes vista) {
        String cod = vista.txtCodigo.getText().trim();
        char[] pwd = vista.txtContraseña.getPassword();
        String hashPwd = Utilidades.generarHash(pwd);
        Utilidades.limpiarContraseña(pwd);
        String dni = vista.txtDNI.getText().trim();
        String nom = vista.txtNombre.getText().trim();
        String ape = vista.txtApellido.getText().trim();
        int age = Integer.parseInt(vista.txtEdad.getText().trim());
        String especialidad = vista.cbxEspecialidad.getSelectedItem().toString();
        return new Docente(especialidad, new ArrayList<>(), null, cod, hashPwd, dni, nom, ape, age);
    }
    public static void limpiarCampos(FRDocentes vista) {
        vista.txtCodigo.setText("");
        vista.txtDNI.setText("");
        vista.txtNombre.setText("");
        vista.txtApellido.setText("");
        vista.txtEdad.setText("");
        vista.txtContraseña.setText("");
        vista.cbxEspecialidad.setSelectedIndex(0);
        vista.tblRegistroDocentes.clearSelection();
        vista.txtCodigo.requestFocus();
    }
    public static void mostrarDatos(JTable tabla, ListaDocentes lista) {
        DocenteTable modelo = new DocenteTable(lista);
        tabla.setModel(modelo);
    }
    public static String validarCampos(FRDocentes vista, ListaDocentes lista,
            ListaEstudiantes listaEstudiantes, GestorDirector gestorDirector) {
        String cod = vista.txtCodigo.getText().trim();
        if (cod.isEmpty()) {return "El código no puede estar vacío.";}
        if (lista.buscarPorCODIGO(cod) != -1) {return "El código ya existe.";}
        if (listaEstudiantes.buscarPorCODIGO(cod) != -1) {return "El código ya existe.";}
        Director director = gestorDirector.getDirector();
        if (director.getCod().equals(cod)) {return "El código ya existe.";}
        if (vista.txtDNI.getText().trim().isEmpty()) {return "El DNI no puede estar vacío.";}
        if (vista.txtNombre.getText().trim().isEmpty()) {return "El nombre no puede estar vacío.";}
        if (vista.txtApellido.getText().trim().isEmpty()) {return "El apellido no puede estar vacío.";}
        if (vista.txtEdad.getText().trim().isEmpty()) {return "La edad no puede estar vacía.";}
        if (vista.txtContraseña.getPassword().length == 0) {return "La contraseña no puede estar vacía.";}
        if (vista.cbxEspecialidad.getSelectedItem() == null) {return "Debe seleccionar una especialidad.";}
        try {
            Integer.parseInt(vista.txtEdad.getText().trim());
        } catch (NumberFormatException e) {
            return "La edad debe ser un número válido.";
        }
        return null;
    }
    public static void llenarComboEspecialidad(FRDocentes vista, ListaAsignaturas listaAsignaturas) {
        for (int i = 0; i < listaAsignaturas.getLista().size(); i++) {
            Asignatura a = listaAsignaturas.obtener(i);
            vista.cbxEspecialidad.addItem(a.getNom());
        }
    }
}
