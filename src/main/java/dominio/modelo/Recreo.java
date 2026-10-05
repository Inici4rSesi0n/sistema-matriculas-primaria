package dominio.modelo;
import java.util.Objects;

/**
 *
 * @author inici4rsesi0n
 */
public class Recreo extends Evento {
    private static final long serialVersionUID = 1L;
    private String descripcion;
    private PeriodoAcademico periodo;
    protected Recreo() {
        super();
        this.descripcion = "Recreo";
    }
    public Recreo(FranjaHoraria franja, String descripcion, PeriodoAcademico periodo) {
        super(franja);
        if (periodo == null) throw new IllegalArgumentException("El periodo académico no puede ser nulo");
        this.descripcion = (descripcion != null && !descripcion.isBlank()) ? descripcion : "Recreo";
        this.periodo = periodo;
    }
    @Override
    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public PeriodoAcademico getPeriodo() { return periodo; }
    public void setPeriodo(PeriodoAcademico periodo) { this.periodo = periodo; }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Recreo)) return false;
        Recreo recreo = (Recreo) o;
        return Objects.equals(getFranja(), recreo.getFranja()) &&
               Objects.equals(periodo, recreo.periodo) &&
               Objects.equals(descripcion, recreo.descripcion);
    }
    @Override
    public int hashCode() {
        return Objects.hash(getFranja(), periodo, descripcion);
    }
}
