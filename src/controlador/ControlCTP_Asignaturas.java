package controlador;
import almacenamiento.Persistencia;
import modelo.Estudiante;
import modelo.repositorio.ListaClases;
import procesos.ProcesosCTP_Asignaturas;
import vista.FCTP_Asignaturas;
/**
 *
 * @author inici4rsesi0n
 */
public class ControlCTP_Asignaturas {
    private FCTP_Asignaturas vista;
    private Estudiante estudiante;
    private ListaClases listaClases;

    public ControlCTP_Asignaturas(FCTP_Asignaturas vista, Estudiante estudiante) {
        this.vista = vista;
        this.estudiante = estudiante;
        this.listaClases = Persistencia.cargarListaEventosClase();
        ProcesosCTP_Asignaturas.cargarAsignaturas(vista, estudiante, listaClases);
    }
}
