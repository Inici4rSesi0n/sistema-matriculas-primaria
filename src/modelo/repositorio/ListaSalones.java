package modelo.repositorio;
import modelo.Salon;
import java.io.Serializable;
import java.util.ArrayList;
/**
 *
 * @author inici4rsesi0n
 */
public class ListaSalones implements Serializable{
    private static final long serialVersionUID = 1L;
    private ArrayList<Salon> lista;
    
    public ListaSalones(){lista=new ArrayList<>();}
    public void agregar(Salon s){lista.add(s);}
    public Salon obtener(int pos){return lista.get(pos);}
    public ArrayList<Salon> buscarPorGrado(String grado){
        ArrayList<Salon> resultado = new ArrayList<>();
        for(Salon s:lista){
            if(s.getGrado().getNom().equalsIgnoreCase(grado)){
                resultado.add(s);
            }
        }
        return resultado;
    }
    public void actualizar(int pos, Salon nuevo){lista.set(pos, nuevo);}
    public void eliminar(int pos){lista.remove(pos);}

    public ArrayList<Salon> getLista() {return lista;}
    public void setLista(ArrayList<Salon> lista) {this.lista = lista;}
}
