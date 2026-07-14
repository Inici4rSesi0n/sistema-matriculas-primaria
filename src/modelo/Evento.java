package modelo;
import java.io.Serializable;
/**
 *
 * @author inici4rsesi0n
 */
public abstract class Evento implements Serializable{
    private static final long serialVersionUID = 1L;
    private String diaSemana;
    private String horaInicio;
    private String horaFin;
    
    public Evento(){}

    public Evento(String diaSemana, String horaInicio, String horaFin) {
        this.diaSemana = diaSemana;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
    }
    public abstract String getDescripcion();

    public String getDiaSemana() {return diaSemana;}
    public void setDiaSemana(String diaSemana) {this.diaSemana = diaSemana;}
    public String getHoraInicio() {return horaInicio;}
    public void setHoraInicio(String horaInicio) {this.horaInicio = horaInicio;}
    public String getHoraFin() {return horaFin;}
    public void setHoraFin(String horaFin) {this.horaFin = horaFin;}

    @Override
    public String toString() {
        return getDescripcion() + " (" + diaSemana + " " + horaInicio + "-" + horaFin + ")";
    }
}
