package modelo.repositorio;
import modelo.Turno;
import java.io.*;
import java.util.ArrayList;
/**
 *
 * @author inici4rsesi0n
 */
public class CatalogoTurnos implements Serializable{
    private static final long serialVersionUID = 1L;
    private static final String ARCHIVO_TURNOS = "turnos.bin";
    private ArrayList<Turno> turnos;
    
    public CatalogoTurnos(){cargarCatalogo();}
    private void cargarCatalogo(){
        File archivo = new File(ARCHIVO_TURNOS);
        if(archivo.exists()){
            try(ObjectInputStream ois=new ObjectInputStream(new FileInputStream(archivo))){
                turnos = (ArrayList<Turno>) ois.readObject();
            } catch(IOException | ClassNotFoundException e){
                crearTurnosPorDefecto();
            } 
        } else{crearTurnosPorDefecto();}
    }
    private void crearTurnosPorDefecto(){
        turnos = new ArrayList<>();
        turnos.add(new Turno("Mañana"));
        turnos.add(new Turno("Tarde"));
        guardar();
    }
    private void guardar(){
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARCHIVO_TURNOS))){
            oos.writeObject(turnos);
        } catch(IOException e){
            throw new RuntimeException("No se pudo guardar los turnos");
        }
    }
    public ArrayList<Turno> getTurnos() {return turnos;}
}
