package procesos;
import modelo.Director;
import modelo.Docente;
import modelo.Estudiante;
import modelo.Usuario;
import modelo.repositorio.GestorDirector;
import modelo.repositorio.ListaDocentes;
import modelo.repositorio.ListaEstudiantes;
import vista.FLogin;
/**
 *
 * @author inici4rsesi0n
 */
public class ProcesosLogin {

    public static String validarCredenciales(String codigo, char[] password) {
        if (codigo == null || codigo.trim().isEmpty()) return "Ingrese su código.";
        if (password == null || password.length == 0) return "Ingrese su contraseña.";
        return null;
    }

    public static Usuario autenticar(String codigo, char[] password,GestorDirector gestorDirector
            ,ListaDocentes listaDocentes,ListaEstudiantes listaEstudiantes) {
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
        return null;
    }
    public static String validarCampos(FLogin vista) {
        return validarCredenciales(
            vista.txtCodigo.getText().trim(),
            vista.txtContraseña.getPassword()
        );
    }
    public static Usuario autenticarUsuario(FLogin vista, GestorDirector gestorDirector,
            ListaDocentes listaDocentes, ListaEstudiantes listaEstudiantes) {
        String codigo = vista.txtCodigo.getText().trim();
        char[] password = vista.txtContraseña.getPassword();
        Usuario usuario = autenticar(codigo, password, gestorDirector, listaDocentes, listaEstudiantes);
        Seguridad.limpiarContraseña(password);
        return usuario;
    }
}