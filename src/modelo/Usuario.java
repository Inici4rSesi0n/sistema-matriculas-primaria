package modelo;
import java.io.Serializable;
import java.util.Arrays;
import procesos.Utilidades;
/**
 *
 * @author inici4rsesi0n
 */
public abstract class Usuario implements Serializable{
    private static final long serialVersionUID = 1L;
    private String cod;
    private String pwd;
    private String dni;
    private String nom;
    private String ape;
    private int age;

    public Usuario(){}
    public Usuario(String cod, String pwd, String dni, String nom, String ape, int age) {
        this.cod = cod;
        this.pwd = pwd;
        this.dni = dni;
        this.nom = nom;
        this.ape = ape;
        this.age = age;
    }
    public boolean autenticarUsuario(String codigo, char[] contraseñaIngresada){
        if (contraseñaIngresada == null) return false;
        String hashIngresado = Utilidades.generarHash(contraseñaIngresada);
        boolean resultado = this.cod.equals(codigo) && this.pwd.equals(hashIngresado);
        limpiarContraseña(contraseñaIngresada);
        return resultado;
    }
    public static void limpiarContraseña(char[] pwd){
        if(pwd!=null){
            Arrays.fill(pwd, '\0');
        }
    }
    
    public String getCod() {return cod;}
    public void setCod(String cod) {this.cod = cod;}
    public String getHashPwd() {return pwd;}
    public void setHashPwd(String pwd) {this.pwd = pwd;}
    public String getDni() {return dni;}
    public void setDni(String dni) {this.dni = dni;}
    public String getNom() {return nom;}
    public void setNom(String nom) {this.nom = nom;}
    public String getApe() {return ape;}
    public void setApe(String ape) {this.ape = ape;}
    public int getAge() {return age;}
    public void setAge(int age) {this.age = age;}

    @Override
    public String toString() {
        return "Usuario [cod=" +cod+ ", dni=" +dni+ ", nom=" +nom+ ", ape=" +ape+ ", age=" +age+ "]";
    }
    
}
