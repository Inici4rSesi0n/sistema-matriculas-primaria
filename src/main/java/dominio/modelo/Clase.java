package dominio.modelo;
import java.util.Objects;
/**
 *
 * @author inici4rsesi0n
 */
public class Clase extends Evento {
    private static final long serialVersionUID = 1L;
    private Asignatura asignatura;
    private Docente docente;
    private Grupo grupo;
    private Aula aula;
    protected Clase() {
        super();
    }
    public Clase(FranjaHoraria franja, PeriodoAcademico periodo,
                 Asignatura asignatura, Docente docente,
                 Grupo grupo, Aula aula) {
        super(franja, periodo);
        if (asignatura == null) throw new IllegalArgumentException("La asignatura no puede ser nula");
        if (docente == null) throw new IllegalArgumentException("El docente no puede ser nulo");
        if (grupo == null) throw new IllegalArgumentException("El grupo no puede ser nulo");
        if (aula == null) throw new IllegalArgumentException("El aula no puede ser nula");
        this.asignatura = asignatura;
        this.docente = docente;
        this.grupo = grupo;
        this.aula = aula;
    }
    public Asignatura getAsignatura() { return asignatura; }
    public void setAsignatura(Asignatura asignatura) { this.asignatura = asignatura; }
    public Docente getDocente() { return docente; }
    public void setDocente(Docente docente) { this.docente = docente; }
    public Grupo getGrupo() { return grupo; }
    public void setGrupo(Grupo grupo) { this.grupo = grupo; }
    public Aula getAula() { return aula; }
    public void setAula(Aula aula) { this.aula = aula; }
    @Override
    public String getDescripcion() {
        String nomAsig = (asignatura != null) ? asignatura.getNombre() : "Sin asignatura";
        String nomDoc = (docente != null) ? docente.getNombre() + " " + docente.getApellido() : "Sin docente";
        return nomAsig + " (" + nomDoc + ")";
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Clase)) return false;
        Clase clase = (Clase) o;
        return Objects.equals(getFranja(), clase.getFranja()) &&
               Objects.equals(getPeriodo(), clase.getPeriodo()) &&
               Objects.equals(asignatura, clase.asignatura) &&
               Objects.equals(docente, clase.docente) &&
               Objects.equals(grupo, clase.grupo) &&
               Objects.equals(aula, clase.aula);
    }
    @Override
    public int hashCode() {
        return Objects.hash(getFranja(), getPeriodo(), asignatura, docente, grupo, aula);
    }
}