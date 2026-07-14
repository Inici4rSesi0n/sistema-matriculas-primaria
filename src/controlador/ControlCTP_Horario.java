package controlador;
import almacenamiento.Persistencia;
import modelo.Estudiante;
import modelo.repositorio.CatalogoRecreo;
import modelo.repositorio.ListaClases;
import procesos.ProcesosCTP_Horario;
import vista.FCTP_Horario;
/**
 *
 * @author inici4rsesi0n
 */
public class ControlCTP_Horario {
    private FCTP_Horario vista;
    private Estudiante estudiante;
    private ListaClases listaClases;
    private CatalogoRecreo catalogoRecreo;

    public ControlCTP_Horario(FCTP_Horario vista, Estudiante estudiante) {
        this.vista = vista;
        this.estudiante = estudiante;
        this.listaClases = Persistencia.cargarListaEventosClase();
        this.catalogoRecreo = new CatalogoRecreo();
        ProcesosCTP_Horario.cargarHorario(vista, estudiante, listaClases, catalogoRecreo);
    }
}
