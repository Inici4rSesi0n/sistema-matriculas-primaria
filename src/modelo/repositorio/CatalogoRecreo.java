package modelo.repositorio;
import modelo.Recreo;
import java.io.*;
/**
 *
 * @author inici4rsesi0n
 */
public class CatalogoRecreo{
    private static final String ARCHIVO_RECREO = "recreo.bin";
    private Recreo recreo;
    
    public CatalogoRecreo(){cargarCatalogo();}
    private void cargarCatalogo() {
        File archivo = new File(ARCHIVO_RECREO);
        if (archivo.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
                recreo = (Recreo) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                crearRecreoPorDefecto();
            }
        } else {crearRecreoPorDefecto();}
    }
    private void crearRecreoPorDefecto() {
        recreo = new Recreo("","10:15","10:45","Recreo");
        guardar();
    }
    public void guardar() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARCHIVO_RECREO))) {
            oos.writeObject(recreo);
        } catch (IOException e) {
            throw new RuntimeException("No se pudo guardar la configuración del recreo.", e);
        }
    }
    public Recreo getRecreo() {return recreo;}
    public void setRecreo(Recreo recreo) {
        this.recreo = recreo;
        guardar();
    }
}
