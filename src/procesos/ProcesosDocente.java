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
/**
 *
 * @author inici4rsesi0n
 */
public class ProcesosDocente {

    public static Docente crearDocente(String especialidad, String cod, String hashPwd, String dni, String nom, String ape, int age) {
        return new Docente(especialidad, new ArrayList<>(), null, cod, hashPwd, dni, nom, ape, age);
    }
    public static Docente leerDocente(FRDocentes vista) {
        String cod = vista.txtCodigo.getText().trim();
        char[] pwd = vista.txtContraseña.getPassword();
        String hashPwd = Seguridad.generarHash(pwd);
        Seguridad.limpiarContraseña(pwd);
        String dni = vista.txtDNI.getText().trim();
        String nom = vista.txtNombre.getText().trim();
        String ape = vista.txtApellido.getText().trim();
        int age = Integer.parseInt(vista.txtEdad.getText().trim());
        String especialidad = vista.cbxEspecialidad.getSelectedItem().toString();
        return crearDocente(especialidad, cod, hashPwd, dni, nom, ape, age);
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
    public static String validarDatosDocente(String cod, String dni, String nom, String ape, String edadTexto,
                                             char[] password, String especialidad,
                                             ListaDocentes lista, ListaEstudiantes listaEstudiantes, GestorDirector gestorDirector) {
        if (cod == null || cod.trim().isEmpty()) return "El código no puede estar vacío.";
        if (lista.buscarPorCODIGO(cod) != -1) return "El código ya existe.";
        if (listaEstudiantes.buscarPorCODIGO(cod) != -1) return "El código ya existe.";
        Director director = gestorDirector.getDirector();
        if (director.getCod().equals(cod)) return "El código ya existe.";
        if (dni == null || dni.trim().isEmpty()) return "El DNI no puede estar vacío.";
        if (nom == null || nom.trim().isEmpty()) return "El nombre no puede estar vacío.";
        if (ape == null || ape.trim().isEmpty()) return "El apellido no puede estar vacío.";
        if (edadTexto == null || edadTexto.trim().isEmpty()) return "La edad no puede estar vacía.";
        if (password == null || password.length == 0) return "La contraseña no puede estar vacía.";
        if (especialidad == null) return "Debe seleccionar una especialidad.";
        try {
            Integer.parseInt(edadTexto.trim());
        } catch (NumberFormatException e) {
            return "La edad debe ser un número válido.";
        }
        return null;
    }
    public static String validarCampos(FRDocentes vista, ListaDocentes lista,
            ListaEstudiantes listaEstudiantes, GestorDirector gestorDirector) {
        String cod = vista.txtCodigo.getText().trim();
        String dni = vista.txtDNI.getText().trim();
        String nom = vista.txtNombre.getText().trim();
        String ape = vista.txtApellido.getText().trim();
        String edadTexto = vista.txtEdad.getText().trim();
        char[] password = vista.txtContraseña.getPassword();
        String especialidad = (String) vista.cbxEspecialidad.getSelectedItem();
        return validarDatosDocente(cod, dni, nom, ape, edadTexto, password, especialidad, lista, listaEstudiantes, gestorDirector);
    }
    public static void llenarComboEspecialidad(FRDocentes vista, ListaAsignaturas listaAsignaturas) {
        for (int i = 0; i < listaAsignaturas.getLista().size(); i++) {
            Asignatura a = listaAsignaturas.obtener(i);
            vista.cbxEspecialidad.addItem(a.getNom());
        }
    }
}