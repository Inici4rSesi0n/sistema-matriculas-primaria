package almacenamiento;
import modelo.repositorio.CatalogoGrados;
import modelo.repositorio.ListaSalones;
import modelo.repositorio.ListaAsignaturas;
import modelo.repositorio.GestorDirector;
import modelo.repositorio.ListaClases;
import modelo.repositorio.ListaEstudiantes;
import modelo.repositorio.ListaDocentes;
import java.io.*;
/**
 *
 * @author inici4rsesi0n
 */
public class Persistencia {
    public static GestorDirector cargarGestorDirector(){return new GestorDirector();}
    public static CatalogoGrados cargarCatalogoGrados(){return new CatalogoGrados();}
    public static ListaSalones cargarListaSalones(){
        ListaSalones lista = cargar("salones.bin");
        if(lista==null){
            lista = new ListaSalones();
        }
        return lista;
    }
    public static ListaAsignaturas cargarListaAsignaturas(){
        ListaAsignaturas lista = cargar("asignaturas.bin");
        if(lista==null){
            lista = new ListaAsignaturas();
        }
        return lista;
    }
    public static ListaDocentes cargarListaDocentes(){
        ListaDocentes lista = cargar("docentes.bin");
        if(lista==null){
            lista = new ListaDocentes();
        }
        return lista;
    }
    public static ListaEstudiantes cargarListaEstudiantes(){
        ListaEstudiantes lista = cargar("estudiantes.bin");
        if(lista==null){
            lista = new ListaEstudiantes();
        }
        return lista;
    }
    public static ListaClases cargarListaEventosClase(){
        ListaClases lista = cargar("eventos.bin");
        if(lista==null){
            lista = new ListaClases();
        }
        return lista;
    }
    public static void guardar(Object objeto, String archivo){
        try(ObjectOutputStream oos=new ObjectOutputStream(new FileOutputStream(archivo))){
            oos.writeObject(objeto);
        } catch(IOException e){
            throw new RuntimeException("No se pudo guardar el archivo " + archivo, e);
        }
    }
    @SuppressWarnings("unchecked")
    public static <T> T cargar(String archivo){
        File file = new File(archivo);
        if(!file.exists()){return null;}
        try(ObjectInputStream ois= new ObjectInputStream(new FileInputStream(archivo))){
            return (T) ois.readObject();
        } catch(IOException | ClassNotFoundException e){
            throw new RuntimeException("No se pudo cargar el archivo: " + archivo, e);
        }
    }
}
