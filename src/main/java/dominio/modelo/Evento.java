package dominio.modelo;
import java.io.Serializable;
/**
 *
 * @author inici4rsesi0n
 */
public abstract class Evento implements Serializable {
    private static final long serialVersionUID = 1L;
    private FranjaHoraria franja;
    private PeriodoAcademico periodo;
    protected Evento() {}
    public Evento(FranjaHoraria franja, PeriodoAcademico periodo) {
        if (franja == null) {
            throw new IllegalArgumentException("La franja horaria no puede ser nula");
        }
        if (periodo == null) {
            throw new IllegalArgumentException("El periodo académico no puede ser nulo");
        }
        this.franja = franja;
        this.periodo = periodo;
    }
    public abstract String getDescripcion();
    public String getDiaSemana() { return franja.getDiaSemana(); }
    public String getHoraInicio() { return franja.getHoraInicio(); }
    public String getHoraFin() { return franja.getHoraFin(); }
    public FranjaHoraria getFranja() { return franja; }
    public void setFranja(FranjaHoraria franja) { this.franja = franja; }
    public PeriodoAcademico getPeriodo() { return periodo; }
    public void setPeriodo(PeriodoAcademico periodo) { this.periodo = periodo; }
    @Override
    public String toString() {
        String desc = getDescripcion();
        return (desc != null ? desc : "Sin descripción") + " (" + franja + ")";
    }
}