package modelo;
/**
 *
 * @author inici4rsesi0n
 */
public class Clase extends Evento{
    private Asignatura asignatura;
    private Docente docente;
    private Salon salon;
    
    public Clase(){super();}
    public Clase(String diaSemana, String horaInicio, String horaFin, 
            Asignatura asignatura, Docente docente, Salon salon) {
        super(diaSemana, horaInicio, horaFin);
        this.asignatura = asignatura;
        this.docente = docente;
        this.salon = salon;
    }

    public Asignatura getAsignatura() {return asignatura;}
    public void setAsignatura(Asignatura asignatura) {this.asignatura = asignatura;}
    public Docente getDocente() {return docente;}
    public void setDocente(Docente docente) {this.docente = docente;}
    public Salon getSalon() {return salon;}
    public void setSalon(Salon salon) {this.salon = salon;}

    @Override
    public String getDescripcion() {
        String nomAsig = (asignatura != null) ? asignatura.getNom() : "Sin asignatura";
        String nomDoc = (docente != null) ? docente.getNombreCompleto() : "Sin docente";
        return nomAsig + " (" + nomDoc + ")";
    }
}
