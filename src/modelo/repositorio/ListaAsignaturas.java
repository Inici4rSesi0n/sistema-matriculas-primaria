package modelo.repositorio;
import java.io.Serializable;
import java.util.ArrayList;
import modelo.Asignatura;
/**
 *
 * @author inici4rsesi0n
 */
public class ListaAsignaturas implements Serializable{
    private static final long serialVersionUID = 1L;
    private ArrayList<Asignatura> lista;
    
    public ListaAsignaturas(){
        lista = new ArrayList<>();
    }
    public void agregar(Asignatura a){lista.add(a);}
    public Asignatura obtener(int pos){return lista.get(pos);}
    public int buscarPorNombre(String nombre){
        for (int i = 0; i < lista.size(); i++) {
            if(lista.get(i).getNom().equalsIgnoreCase(nombre)){
                return i;
            }
        }
        return -1;
    }
    public void actualizar(int pos, Asignatura nueva){lista.set(pos, nueva);}
    public void eliminar(int pos){lista.remove(pos);}
    
    public ArrayList<Asignatura> getLista() {return lista;}
    public void setLista(ArrayList<Asignatura> lista) {this.lista = lista;}
}
