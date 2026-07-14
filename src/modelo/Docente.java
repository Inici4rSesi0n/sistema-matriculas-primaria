package modelo;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author inici4rsesi0n
 */
public class Docente extends Usuario{
    private String especialidad;
    private List<Asignatura> asignaturas;
    private Salon tutoria;
    
    public Docente(){
        super();
        this.asignaturas = new ArrayList<>();
    }
    public Docente(String especialidad, List<Asignatura> asignaturas, 
            Salon tutoria, String cod, String pwd, String dni, 
            String nom, String ape, int age) {
        super(cod, pwd, dni, nom, ape, age);
        this.especialidad = especialidad;
        this.asignaturas = (asignaturas!=null)?asignaturas:new ArrayList<>();
        this.tutoria = tutoria;
    }

    public String getEspecialidad() {return especialidad;}
    public void setEspecialidad(String especialidad) {this.especialidad = especialidad;}
    public List<Asignatura> getAsignaturas() {return asignaturas;}
    public void setAsignaturas(List<Asignatura> asignaturas) {this.asignaturas = asignaturas;}
    public Salon getTutoria() {return tutoria;}
    public void setTutoria(Salon tutoria) {this.tutoria = tutoria;}
    public String getNombreCompleto(){
        return getNom() + " " + getApe();
    }

    @Override
    public String toString() {
        return super.toString() + ", Especialidad=" +especialidad+
                ", Tutoría=" + (tutoria !=null?tutoria.getAula():"Sin tutoría") +
                ", Asignaturas=" +(asignaturas !=null?asignaturas.size():0);
    }
    
}
