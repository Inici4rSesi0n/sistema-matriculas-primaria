package modelo;
/**
 *
 * @author inici4rsesi0n
 */
public class Estudiante extends Usuario{
    private Salon salon;
    private Grado grado;
    
    public Estudiante(){super();}
    public Estudiante(Salon salon, Grado grado, String cod, String pwd, String dni, String nom, String ape, int age) {
        super(cod, pwd, dni, nom, ape, age);
        this.salon = salon;
        this.grado = grado;
    }
    
    public Salon getSalon() {return salon;}
    public void setSalon(Salon salon) {this.salon = salon;}
    public String getNombreCompleto() {return getNom() + " " + getApe();}
    public Grado getGrado() {return (salon != null && salon.getGrado() != null) ? salon.getGrado() : grado;}
    public void setGrado(Grado grado) {this.grado = grado;}
    @Override
    public String toString() {
        return super.toString() + ", Salon=" + (salon!=null?salon.getAula(): "Sin asignar");
    }
}
