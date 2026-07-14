package procesos;
import modelo.Estudiante;
import modelo.repositorio.CatalogoRecreo;
import modelo.repositorio.ListaClases;
import modelo.tabla.EventoTable;
import vista.FCTP_Horario;

public class ProcesosCTP_Horario {
    public static void cargarHorario(FCTP_Horario vista, Estudiante estudiante,
            ListaClases listaClases, CatalogoRecreo catalogoRecreo) {
        if (estudiante.getSalon() == null) {
            return;
        }
        String grado = estudiante.getSalon().getGrado().getNom();
        String salon = estudiante.getSalon().getAula();
        EventoTable modelo = ProcesosHorario.construirModeloTabla(listaClases, grado, salon, catalogoRecreo);
        vista.tblHorarioAlumno.setModel(modelo);
    }
}
