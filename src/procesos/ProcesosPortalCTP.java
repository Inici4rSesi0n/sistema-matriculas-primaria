package procesos;
import modelo.*;
import modelo.repositorio.*;
import modelo.tabla.EventoTable;
import vista.FPortalCTP;
import javax.swing.DefaultComboBoxModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTable;
import java.util.ArrayList;

public class ProcesosPortalCTP {
    public static void cargarDatosEstudiante(FPortalCTP vista, Estudiante est) {
        vista.txtAlumno.setText(est.getNombreCompleto());
        vista.txtCodigo.setText(est.getCod());
        vista.txtGrado.setText(est.getGrado() != null ? est.getGrado().getNom() : "Sin grado");
    }
    public static void actualizarComboSalones(FPortalCTP vista, ListaSalones listaSalones,
            ListaEstudiantes listaEstudiantes, String grado) {
        DefaultComboBoxModel<String> modelo = new DefaultComboBoxModel<>();
        if (grado != null) {
            ArrayList<Salon> salones = listaSalones.buscarPorGrado(grado);
            for (Salon s : salones) {
                int matriculados = contarAlumnosEnSalon(s, listaEstudiantes);
                if (matriculados < s.getCupoMax()) {
                    modelo.addElement(s.getAula());
                }
            }
        }vista.cbxSalon.setModel(modelo);}
    public static void cargarTablaAlumnos(JTable tabla, ListaEstudiantes listaEstudiantes, Salon salon) {
        String[] titulos = {"N°", "Alumnos"};
        DefaultTableModel modelo = new DefaultTableModel(null, titulos);
        int contador = 0;
        if (salon != null) {
            for (int i = 0; i < listaEstudiantes.getLista().size(); i++) {
                Estudiante e = listaEstudiantes.obtener(i);
                if (e.getSalon() != null && e.getSalon().equals(salon)) {
                    contador++;
                    modelo.addRow(new Object[]{contador, e.getNombreCompleto()});
                }
            }
        }tabla.setModel(modelo);}
    public static void cargarTablaHorario(JTable tabla, ListaClases listaClases, String grado,
            String salon, CatalogoRecreo catalogoRecreo) {
        EventoTable modelo = ProcesosHorario.construirModeloTabla(listaClases, grado, salon, catalogoRecreo);
        tabla.setModel(modelo);
    }
    public static void mostrarInfoSalon(FPortalCTP vista, Salon salon, ListaEstudiantes listaEstudiantes) {
        if (salon == null) {
            vista.txtTutorAsignado.setText("");
            vista.txtMaxAlumnos.setText("");
            vista.txtCanAlumnos.setText("");
            return;
        }
        vista.txtTutorAsignado.setText(
            salon.getTutor() != null ? salon.getTutor().getNombreCompleto() : "Sin tutor"
        );
        vista.txtMaxAlumnos.setText(String.valueOf(salon.getCupoMax()));
        int matriculados = contarAlumnosEnSalon(salon, listaEstudiantes);
        vista.txtCanAlumnos.setText(String.valueOf(matriculados));
    }
    public static boolean validarCupo(Salon salon, ListaEstudiantes listaEstudiantes) {
        int matriculados = contarAlumnosEnSalon(salon, listaEstudiantes);
        return matriculados < salon.getCupoMax();
    }
    private static int contarAlumnosEnSalon(Salon salon, ListaEstudiantes listaEstudiantes) {
        int contador = 0;
        for (int i = 0; i < listaEstudiantes.getLista().size(); i++) {
            Estudiante e = listaEstudiantes.obtener(i);
            if (e.getSalon() != null && e.getSalon().equals(salon)) {
                contador++;
            }
        }return contador;}
}