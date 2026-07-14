package modelo.repositorio;
import modelo.Grado;
import java.io.*;
import java.util.ArrayList;
/**
 *
 * @author inici4rsesi0n
 */
public class CatalogoGrados implements Serializable{
    private static final long serialVersionUID = 1L;
    private static final String ARCHIVO_GRADOS = "grados.bin";
    private ArrayList<Grado> grados;
    
    public CatalogoGrados(){cargarCatalogo();}
    private void cargarCatalogo(){
        File archivo = new File(ARCHIVO_GRADOS);
        if(archivo.exists()){
            try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))){
                grados = (ArrayList<Grado>) ois.readObject();
            } catch(IOException | ClassNotFoundException e){
                crearGradosPorDefecto();
            }
        } else{
            crearGradosPorDefecto();
        }
    }
    private void crearGradosPorDefecto(){
        grados = new ArrayList<>();
        String[] nombres={"1er Grado", "2do Grado", "3er Grado", "4to Grado",
            "5to Grado", "6to Grado"};
        for (int i = 0; i < nombres.length; i++) {
            grados.add(new Grado(nombres[i], "Primaria"));
        }
        guardar();
    }
    private void guardar(){
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARCHIVO_GRADOS))){
            oos.writeObject(grados);
        } catch(IOException e){
            throw new RuntimeException("No se pudo guardar los grados", e);
        }
    }
    public ArrayList<Grado> getGrados() {return grados;}
}
