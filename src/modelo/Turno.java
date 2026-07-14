package modelo;
import java.io.Serializable;
/**
 *
 * @author inici4rsesi0n
 */
public class Turno implements Serializable{
    private static final long serialVersionUID = 1L;
    private String turno;
    
    public Turno(){}
    public Turno(String turno) {
        this.turno = turno;
    }

    public String getTurno() {return turno;}
    public void setTurno(String turno) {this.turno = turno;}

    @Override
    public String toString() {
        return "Horario [turno=" +turno+ "]";
    }
    
    
    
}
