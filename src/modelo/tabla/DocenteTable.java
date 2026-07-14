package modelo.tabla;
import modelo.Docente;
import modelo.repositorio.ListaDocentes;
import javax.swing.table.AbstractTableModel;
/**
 *
 * @author inici4rsesi0n
 */
public class DocenteTable extends AbstractTableModel{
    private final ListaDocentes lista;
    private final String[] columnas ={"N°","Código", "DNI", "Nombre", "Apellido", "Edad",
    "Especialidad", "Tutoría", "N° Asignaturas"};
    public DocenteTable(ListaDocentes lista){this.lista=lista;}

    @Override
    public int getRowCount() {return lista.getLista().size();}
    @Override
    public int getColumnCount() {return columnas.length;}

    @Override
    public String getColumnName(int column) {return columnas[column];}
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Docente d = lista.obtener(rowIndex);
        return switch (columnIndex){
            case 0 -> rowIndex + 1;
            case 1 -> d.getCod();
            case 2 -> d.getDni();
            case 3 -> d.getNom();
            case 4 -> d.getApe();
            case 5 -> d.getAge();
            case 6 -> d.getEspecialidad();
            case 7 -> (d.getTutoria()!=null && d.getTutoria().getGrado()!=null)?
                d.getTutoria().getGrado().getNom()+ " " +d.getTutoria().getAula():"Sin tutoría";
            case 8 -> (d.getAsignaturas()!=null)?d.getAsignaturas().size():0;
            default -> null;
        };
    }
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {return false;}
    
}
