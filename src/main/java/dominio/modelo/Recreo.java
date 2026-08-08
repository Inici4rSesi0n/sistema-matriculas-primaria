package dominio.modelo;
import java.util.Objects;
/**
 *
 * @author inici4rsesi0n
 */
public class Recreo extends Evento {
    private static final long serialVersionUID = 1L;
    private String descripcion;
    protected Recreo() {
        super();
        this.descripcion = "Recreo";
    }
    public Recreo(FranjaHoraria franja, String descripcion, PeriodoAcademico periodo) {
        super(franja, periodo);
        this.descripcion = (descripcion != null && !descripcion.isBlank()) ? descripcion : "Recreo";
    }
    @Override
    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Recreo)) return false;
        Recreo recreo = (Recreo) o;
        return Objects.equals(getFranja(), recreo.getFranja()) &&
               Objects.equals(getPeriodo(), recreo.getPeriodo()) &&
               Objects.equals(descripcion, recreo.descripcion);
    }
    @Override
    public int hashCode() {
        return Objects.hash(getFranja(), getPeriodo(), descripcion);
    }
}