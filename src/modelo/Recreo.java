package modelo;
import java.io.Serializable;
/**
 *
 * @author inici4rsesi0n
 */
public class Recreo extends Evento{
    private String descripcion;
    
    public Recreo(){
        super();
        this.descripcion = "Recreo";
    }
    public Recreo(String diaSemana, String horaInicio, String horaFin, String descripcion) {
        super(diaSemana, horaInicio, horaFin);
        this.descripcion = descripcion;
    }
    
    @Override
    public String getDescripcion() {return descripcion;}
    public void setDescripcion(String descripcion) {this.descripcion = descripcion;}
    @Override
    public String toString() {
        return descripcion + " (" + getDiaSemana() + " " + getHoraInicio() + "-" + getHoraFin() + ")";
    }
}
