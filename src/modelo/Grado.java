package modelo;
import java.io.Serializable;
/**
 *
 * @author inici4rsesi0n
 */
public class Grado implements Serializable{
    private static final long serialVersionUID = 1L;
    private String nom;
    private String nvl;
    
    public Grado(){
    }
    public Grado(String nom, String nvl) {
        this.nom = nom;
        this.nvl = nvl;
    }

    public String getNom() {return nom;}
    public void setNom(String nom) {this.nom = nom;}
    public String getNvl() {return nvl;}
    public void setNvl(String nvl) {this.nvl = nvl;}

    @Override
    public String toString() {
        return "Grado [nombre=" +nom+ ", nivel=" +nvl+ "]";
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Grado other = (Grado) obj;
        return nom != null && nom.equalsIgnoreCase(other.nom);
    }
    @Override
    public int hashCode() {
        return nom != null ? nom.toLowerCase().hashCode() : 0;
    }
}
    
