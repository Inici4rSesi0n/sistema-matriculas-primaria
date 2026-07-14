package modelo.tabla;
import modelo.Docente;
import modelo.repositorio.ListaDocentes;
import javax.swing.table.AbstractTableModel;
/**
 *
 * @author inici4rsesi0n
 */
public class GestionAsignaturaTable extends AbstractTableModel{
    private final ListaDocentes lista;
    private final String[] columnas = {"Nombre Completo", "Código", "Especialidad", "N° Asignaturas"};
    
    public GestionAsignaturaTable(ListaDocentes lista) {this.lista = lista;}
    @Override
    public int getRowCount() {return lista.getLista().size();}
    @Override
    public int getColumnCount() {return columnas.length;}
    @Override
    public String getColumnName(int column) {return columnas[column];}
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Docente d = lista.obtener(rowIndex);
        return switch (columnIndex) {
            case 0 -> d.getNombreCompleto();
            case 1 -> d.getCod();
            case 2 -> d.getEspecialidad();
            case 3 -> (d.getAsignaturas() != null) ? d.getAsignaturas().size() : 0;
            default -> null;
        };
    }
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {return false;}
}
