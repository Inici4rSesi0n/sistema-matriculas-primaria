package modelo;
import java.io.Serializable;
/**
 *
 * @author inici4rsesi0n
 */
public class Salon implements Serializable{
    private static final long serialVersionUID = 1L;
    private String aula;
    private Grado grado;
    private Docente tutor;
    private int cupoMax;
    
    public Salon(){}
    public Salon(String aula, Grado grado, Docente tutor, int cupoMax) {
        this.aula = aula;
        this.grado = grado;
        this.tutor = tutor;
        this.cupoMax = cupoMax;
    }

    public String getAula() {return aula;}
    public void setAula(String aula) {this.aula = aula;}
    public Grado getGrado() {return grado;}
    public void setGrado(Grado grado) {this.grado = grado;}
    public Docente getTutor() {return tutor;}
    public void setTutor(Docente tutor) {this.tutor = tutor;}
    public int getCupoMax() {return cupoMax;}
    public void setCupoMax(int cupoMax) {this.cupoMax = cupoMax;}

    @Override
    public String toString() {
        return "Salon [aula=" +aula+ ", grado=" +(grado!=null?grado.getNom():"Sin grado") +
                ", tutor=" + (tutor!=null?tutor.getNombreCompleto():"Sin tutor")+
                ", cupoMaximo=" +cupoMax+ "]";
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Salon other = (Salon) obj;
        return aula != null && grado != null
                && aula.equalsIgnoreCase(other.aula)
                && grado.equals(other.grado);
    }
    @Override
    public int hashCode() {
        return (aula + grado.getNom()).toLowerCase().hashCode();
    }
}
