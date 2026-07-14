package modelo.tabla;
import modelo.Estudiante;
import modelo.repositorio.ListaEstudiantes;
import javax.swing.table.AbstractTableModel;
/**
 *
 * @author inici4rsesi0n
 */
public class EstudianteTable extends AbstractTableModel{
    private final ListaEstudiantes lista;
    private final String[] columnas ={"N°","Código", "DNI", "Nombre", "Apellido", "Edad",
    "Grado", "Salón"};
    public EstudianteTable(ListaEstudiantes lista){this.lista=lista;}

    @Override
    public int getRowCount() {return lista.getLista().size();}
    @Override
    public int getColumnCount() {return columnas.length;}
    @Override
    public String getColumnName(int column) {return columnas[column];}
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Estudiante e = lista.obtener(rowIndex);
        return switch (columnIndex){
            case 0 -> rowIndex+1;
            case 1 -> e.getCod();
            case 2 -> e.getDni();
            case 3 -> e.getNom();
            case 4 -> e.getApe();
            case 5 -> e.getAge();
            case 6 -> (e.getGrado() != null) ? e.getGrado().getNom() : "Sin grado";
            case 7 -> (e.getSalon()!=null)?e.getSalon().getAula():"Sin Salón";
            default -> null;
        };
    }
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {return false;}
}
