package modelo.tabla;
import modelo.Asignatura;
import modelo.repositorio.ListaAsignaturas;
import javax.swing.table.AbstractTableModel;
/**
 *
 * @author inici4rsesi0n
 */
public class AsignaturaTable extends AbstractTableModel{
    private final ListaAsignaturas lista;
    private final String[] columnas = {"N°","Nombre"};
    
    public AsignaturaTable(ListaAsignaturas lista){this.lista = lista;}

    @Override
    public int getRowCount() {return lista.getLista().size();}
    @Override
    public int getColumnCount() {return columnas.length;}
    @Override
    public String getColumnName(int column) {return columnas[column];}
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Asignatura a = lista.obtener(rowIndex);
        return switch (columnIndex){
            case 0 -> rowIndex+1;
            case 1 -> a.getNom();
                default -> null;
        };
    }
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {return false;}
}
