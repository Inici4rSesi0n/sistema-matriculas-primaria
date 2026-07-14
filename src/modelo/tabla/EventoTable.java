package modelo.tabla;
import modelo.Evento;
import modelo.Recreo;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.swing.table.AbstractTableModel;
/**
 *
 * @author inici4rsesi0n
 */
public class EventoTable extends AbstractTableModel{
    private static final String[] DIAS={"Lunes","Martes","Miércoles","Jueves",
    "Viernes"};
    private  List<String> franjasHorarias = new ArrayList<>();
    private  Map<String, Map<String, String>> datos = new LinkedHashMap<>();
    
    public EventoTable(ArrayList<Evento> eventos){
        franjasHorarias.clear();
        datos.clear();
        for (Evento ev:eventos) {
            String franja = ev.getHoraInicio() + " - " + ev.getHoraFin();
            if (!datos.containsKey(franja)) {
                datos.put(franja, new LinkedHashMap<>());
                franjasHorarias.add(franja);
            }
    }
    franjasHorarias.sort(Comparator.comparing(f->f.split(" - ")[0]));
    for (Evento ev:eventos) {
            String franja = ev.getHoraInicio() + " - " + ev.getHoraFin();
            String descripcion = ev.getDescripcion();
            if(ev instanceof Recreo){
                for(String dia:DIAS){
                    datos.get(franja).put(dia, descripcion);
                }
            } else{
                String dia = ev.getDiaSemana();
                datos.get(franja).put(dia, descripcion);
            }
        }
    }
    
    @Override
    public int getRowCount() {return franjasHorarias.size();}
    @Override
    public int getColumnCount() {return DIAS.length+1;}
    @Override
    public String getColumnName(int column) {
        if (column==0) return "Hora";
        return DIAS[column -1];
    }
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        String franja = franjasHorarias.get(rowIndex);
        if(columnIndex==0){return franja;}
        String dia = DIAS[columnIndex-1];
        Map<String, String> fila=datos.get(franja);
        return fila.getOrDefault(dia, "");
    }
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {return false;}
}
