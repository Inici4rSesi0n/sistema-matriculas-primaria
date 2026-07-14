package modelo.repositorio;
import modelo.Estudiante;
import java.io.Serializable;
import java.util.ArrayList;
/**
 *
 * @author inici4rsesi0n
 */
public class ListaEstudiantes implements Serializable{
    private static final long serialVersionUID = 1L;
    private ArrayList<Estudiante> lista;
    
    public ListaEstudiantes(){lista = new ArrayList<>();}
    public void agregar(Estudiante e){lista.add(e);}
    public Estudiante obtener(int pos){return lista.get(pos);}
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
    public ArrayList<Estudiante> buscarPorGrado(String grado) {
    ArrayList<Estudiante> resultado = new ArrayList<>();
        for (Estudiante e : lista) {
            if (e.getSalon() != null && e.getSalon().getGrado().getNom().equalsIgnoreCase(grado)){
                resultado.add(e);
            }
        }
        return resultado;
    }
    public void actualizar(int pos, Estudiante nuevo){lista.set(pos, nuevo);}
    public void eliminar(int pos){lista.remove(pos);}

    public ArrayList<Estudiante> getLista() {return lista;}
    public void setLista(ArrayList<Estudiante> lista) {this.lista = lista;}
}
