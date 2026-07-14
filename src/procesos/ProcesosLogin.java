package procesos;
import modelo.Director;
import modelo.Docente;
import modelo.Estudiante;
import modelo.Usuario;
import modelo.repositorio.GestorDirector;
import modelo.repositorio.ListaDocentes;
import modelo.repositorio.ListaEstudiantes;
import vista.FLogin;

public class ProcesosLogin {
    public static String validarCampos(FLogin vista) {
        if (vista.txtCodigo.getText().trim().isEmpty()) return "Ingrese su código.";
        if (vista.txtContraseña.getPassword().length == 0) return "Ingrese su contraseña.";
        return null;
    }
    public static Usuario autenticarUsuario(FLogin vista, GestorDirector gestorDirector,
            ListaDocentes listaDocentes, ListaEstudiantes listaEstudiantes) {
        String codigo = vista.txtCodigo.getText().trim();
        char[] password = vista.txtContraseña.getPassword();
        Director director = gestorDirector.getDirector();
        if (director.getCod().equals(codigo) && director.autenticarUsuario(codigo, password)) {
            return director;
        }
        int posDoc = listaDocentes.buscarPorCODIGO(codigo);
        if (posDoc != -1) {
            Docente docente = listaDocentes.obtener(posDoc);
            if (docente.autenticarUsuario(codigo, password)) return docente;
        }
        int posEst = listaEstudiantes.buscarPorCODIGO(codigo);
        if (posEst != -1) {
            Estudiante estudiante = listaEstudiantes.obtener(posEst);
            if (estudiante.autenticarUsuario(codigo, password)) return estudiante;
        }
        Utilidades.limpiarContraseña(password);
        return null;
    }
}