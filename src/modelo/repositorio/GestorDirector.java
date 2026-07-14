package modelo.repositorio;
import modelo.Director;
import java.util.ArrayList;
import java.io.*;
/**
 *
 * @author inici4rsesi0n
 */
public class GestorDirector{
    private static final String ARCHIVO_DIRECTOR = "director.bin";
    private Director director;
    
    public GestorDirector(){cargarDirector();}
    private void cargarDirector(){
        File archivo = new File(ARCHIVO_DIRECTOR);
        if(archivo.exists()){
            try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))){
                director = (Director) ois.readObject();
            } catch(IOException | ClassNotFoundException e){
                crearDirectorPorDefecto();
            }
        } else{
            crearDirectorPorDefecto();
        }
    }
    private void crearDirectorPorDefecto(){
        director = new Director(Director.COD_POR_DEFECTO, Director.HASH_PWD_POR_DEFECTO,
        "79187555","Director","General",40); guardar();
    }
    public void guardar(){
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARCHIVO_DIRECTOR))){
            oos.writeObject(director);
        } catch(IOException e){
            throw new RuntimeException("No se pudo guardar el director.", e);
        }
    }
    public Director getDirector() {return director;}
}
