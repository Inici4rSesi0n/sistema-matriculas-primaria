package modelo.tabla;
import modelo.Salon;
import modelo.repositorio.ListaSalones;
import javax.swing.table.AbstractTableModel;
/**
 *
 * @author inici4rsesi0n
 */
public class SalonTable extends AbstractTableModel{
    private final ListaSalones lista;
    private final String[] columnas={"N°", "Salón", "Grado", "Tutor", "N° Alumnos Máx"};
    
    public SalonTable(ListaSalones lista){this.lista = lista;}
    @Override
    public int getRowCount() {return lista.getLista().size();}
    @Override
    public int getColumnCount() {return columnas.length;}
    @Override
    
    public Object getValueAt(int rowIndex, int columnIndex) {
        Salon s = lista.obtener(rowIndex);
        return switch(columnIndex){
            case 0 -> rowIndex+1;
            case 1 -> s.getAula();
            case 2 -> (s.getGrado()!=null)?s.getGrado().getNom():"Sin grado";
            case 3 -> (s.getTutor()!=null)?s.getTutor().getNombreCompleto():"Sin tutor";
            case 4 -> s.getCupoMax();
                default -> null;
        };
    }
    @Override
    public String getColumnName(int column) {return columnas[column];}
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {return false;}
    
    
}
