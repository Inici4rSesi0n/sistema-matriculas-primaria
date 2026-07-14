package modelo.repositorio;
import modelo.Docente;
import java.io.Serializable;
import java.util.ArrayList;
/**
 *
 * @author inici4rsesi0n
 */
public class ListaDocentes implements Serializable{
    private static final long serialVersionUID = 1L;
    private ArrayList<Docente> lista;
    
    public ListaDocentes(){lista = new ArrayList<>();}
    public void agregar(Docente d){lista.add(d);}
    public Docente obtener(int pos){return lista.get(pos);}
    public int buscarPorDNI(String dni){
        for (int i = 0; i < lista.size(); i++) {
            if(lista.get(i).getDni().equalsIgnoreCase(dni)){
                return i;
            }
        }
        return -1;
    }
    public int buscarPorCODIGO(String cod){
        for (int i = 0; i < lista.size(); i++) {
            if(lista.get(i).getCod().equalsIgnoreCase(cod)){
                return i;
            }
        }
        return -1;
    }
    public void actualizar(int pos, Docente nuevo){lista.set(pos, nuevo);}
    public void eliminar(int pos){lista.remove(pos);}

    public ArrayList<Docente> getLista() {return lista;}
    public void setLista(ArrayList<Docente> lista) {this.lista = lista;}
}
