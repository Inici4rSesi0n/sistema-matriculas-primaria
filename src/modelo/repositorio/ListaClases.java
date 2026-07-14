package modelo.repositorio;
import modelo.Clase;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
/**
 *
 * @author inici4rsesi0n
 */
public class ListaClases implements Serializable{
    private static final long serialVersionUID = 1L;
    private ArrayList<Clase> lista;
    
    public ListaClases(){lista=new ArrayList<>();}
    public void agregar(Clase ev){lista.add(ev);}
    public Clase obtener(int pos){return lista.get(pos);}
    public ArrayList<Clase> buscarPorSalonYGrado(String aula, String grado){
        ArrayList<Clase> resultado = new ArrayList<>();
        for(Clase ev:lista){
            if(ev.getSalon().getAula().equalsIgnoreCase(aula)
                    && ev.getSalon().getGrado().getNom().equalsIgnoreCase(grado)){
                resultado.add(ev);
            }
        }
        resultado.sort(Comparator.comparing(Clase::getDiaSemana).thenComparing(Clase::getHoraInicio));
        return resultado;
    }
    public boolean existeDocenteEnHorario(String codigoDocente, String dia, String horaInicio, String horaFin) {
        for (int i = 0; i < lista.size(); i++) {
            Clase c = lista.get(i);
            if (c.getDocente().getCod().equals(codigoDocente) &&
                c.getDiaSemana().equals(dia) &&
                c.getHoraInicio().equals(horaInicio) &&
                c.getHoraFin().equals(horaFin)) {
                return true;
            }
        }return false;}
    public int buscarClase(String grado, String aula, String dia, String horaInicio, String horaFin) {
        for (int i = 0; i < lista.size(); i++) {
            Clase c = lista.get(i);
            if (c.getSalon().getGrado().getNom().equals(grado) &&
                c.getSalon().getAula().equals(aula) &&
                c.getDiaSemana().equals(dia) &&
                c.getHoraInicio().equals(horaInicio) &&
                c.getHoraFin().equals(horaFin)) {
                return i;}
        }return -1;}
    public void actualizar(int pos, Clase nuevo){lista.set(pos, nuevo);}
    public void eliminar(int pos){lista.remove(pos);}

    public ArrayList<Clase> getLista() {return lista;}
    public void setLista(ArrayList<Clase> lista) {this.lista = lista;}
}
