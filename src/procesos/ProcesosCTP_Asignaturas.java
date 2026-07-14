package procesos;
import modelo.Clase;
import modelo.Estudiante;
import modelo.repositorio.ListaClases;
import vista.FCTP_Asignaturas;
import java.util.ArrayList;

public class ProcesosCTP_Asignaturas {
    public static void cargarAsignaturas(FCTP_Asignaturas vista, Estudiante estudiante,
            ListaClases listaClases) {
        if (estudiante.getSalon() == null) {
            return;
        }
        String grado = estudiante.getSalon().getGrado().getNom();
        String salon = estudiante.getSalon().getAula();
        ArrayList<Clase> clases = listaClases.buscarPorSalonYGrado(salon, grado);
        for (Clase c : clases) {
            String nomAsig = c.getAsignatura().getNom();
            String nomDoc = c.getDocente().getNombreCompleto();
            if (nomAsig.equalsIgnoreCase("Comunicación")) {
                vista.txtComunicacion.setText(nomAsig);
                vista.txtDocenteComu.setText(nomDoc);
            } else if (nomAsig.equalsIgnoreCase("Matemática")) {
                vista.txtMatematica.setText(nomAsig);
                vista.txtDocenteMate.setText(nomDoc);
            } else if (nomAsig.equalsIgnoreCase("Inglés")) {
                vista.txtIngles.setText(nomAsig);
                vista.txtDocenteIngles.setText(nomDoc);
            } else if (nomAsig.equalsIgnoreCase("Arte")) {
                vista.txtArte.setText(nomAsig);
                vista.txtDocenteArte.setText(nomDoc);
            } else if (nomAsig.equalsIgnoreCase("Ciencia y Tecnología")) {
                vista.txtCienciaYTecnologia.setText(nomAsig);
                vista.txtDocenteCyTecno.setText(nomDoc);
            } else if (nomAsig.equalsIgnoreCase("Personal Social")) {
                vista.txtPersonalSocial.setText(nomAsig);
                vista.txtDocentePSocial.setText(nomDoc);
            } else if (nomAsig.equalsIgnoreCase("Educación Física")) {
                vista.txtEducacionFisica.setText(nomAsig);
                vista.txtDocenteEFisica.setText(nomDoc);
            } else if (nomAsig.equalsIgnoreCase("Educación Religiosa")) {
                vista.txtEducacionReligiosa.setText(nomAsig);
                vista.txtDocenteEReligiosa.setText(nomDoc);
            } else if (nomAsig.equalsIgnoreCase("Tutoría y Orientación Educativa")) {
                vista.txtTutoria.setText(nomAsig);
                vista.txtDocenteTutor.setText(nomDoc);
            }
        }
    }
}
