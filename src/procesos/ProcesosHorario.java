package procesos;
import modelo.*;
import modelo.repositorio.*;
import modelo.tabla.EventoTable;
import vista.FRHorarios;
import javax.swing.DefaultComboBoxModel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/**
 *
 * @author inici4rsesi0n
 */
public class ProcesosHorario {
    private static final List<String> HORAS = Arrays.asList(
            "08:00 - 09:00", "09:00 - 10:15", "10:45 - 11:45",
            "11:45 - 12:30", "12:30 - 13:00"
    );
    private static final List<String> DIAS_SEMANA = Arrays.asList(
            "Lunes", "Martes", "Miércoles", "Jueves", "Viernes"
    );
    public static void llenarCombos(FRHorarios vista, CatalogoGrados catalogoGrados,
            ListaSalones listaSalones, ListaAsignaturas listaAsignaturas,ListaDocentes listaDocentes) {
        DefaultComboBoxModel<String> modeloGrados = new DefaultComboBoxModel<>();
        for (int i = 0; i < catalogoGrados.getGrados().size(); i++) {
            modeloGrados.addElement(catalogoGrados.getGrados().get(i).getNom());
        }
        vista.cbxGrado.setModel(modeloGrados);
        vista.cbxSalon.setModel(new DefaultComboBoxModel<>());
        DefaultComboBoxModel<String> modeloAsignaturas = new DefaultComboBoxModel<>();
        for (int i = 0; i < listaAsignaturas.getLista().size(); i++) {
            modeloAsignaturas.addElement(listaAsignaturas.obtener(i).getNom());
        }
        vista.cbxAsignatura.setModel(modeloAsignaturas);
        DefaultComboBoxModel<String> modeloDocentes = new DefaultComboBoxModel<>();
        for (int i = 0; i < listaDocentes.getLista().size(); i++) {
            Docente d = listaDocentes.obtener(i);
            modeloDocentes.addElement(d.getNombreCompleto() + " (" + d.getCod() + ")");
        }
        vista.cbxDocente.setModel(modeloDocentes);
        DefaultComboBoxModel<String> modeloDias = new DefaultComboBoxModel<>();
        for (String dia : DIAS_SEMANA) {
            modeloDias.addElement(dia);
        }
        vista.cbxDia.setModel(modeloDias);
        DefaultComboBoxModel<String> modeloHoras = new DefaultComboBoxModel<>();
        for (String hora : HORAS) {
            modeloHoras.addElement(hora);
        }
        vista.cbxHora.setModel(modeloHoras);
    }
    public static void actualizarComboSalones(FRHorarios vista, ListaSalones listaSalones, String gradoSeleccionado) {
        DefaultComboBoxModel<String> modeloSalones = new DefaultComboBoxModel<>();
        if (gradoSeleccionado != null) {
            ArrayList<Salon> salonesDelGrado = listaSalones.buscarPorGrado(gradoSeleccionado);
            for (Salon s : salonesDelGrado) {
                modeloSalones.addElement(s.getAula());
            }
        }
        vista.cbxSalon.setModel(modeloSalones);
    }
    public static void actualizarComboAsignaturasPorDocente(FRHorarios vista, ListaDocentes listaDocentes, String docenteSeleccionado) {
        DefaultComboBoxModel<String> modeloAsignaturas = new DefaultComboBoxModel<>();
        if (docenteSeleccionado != null) {
            String codigoDocente = docenteSeleccionado.substring(
                    docenteSeleccionado.lastIndexOf("(") + 1,
                    docenteSeleccionado.lastIndexOf(")"));
            int pos = listaDocentes.buscarPorCODIGO(codigoDocente);
            if (pos != -1) {
                Docente d = listaDocentes.obtener(pos);
                if (d.getAsignaturas() != null) {
                    for (Asignatura a : d.getAsignaturas()) {
                        modeloAsignaturas.addElement(a.getNom());
                    }
                }
            }
        }
        vista.cbxAsignatura.setModel(modeloAsignaturas);
    }
    public static Clase crearClase(String dia, String horaInicio, String horaFin,
                                   Asignatura asignatura, Docente docente, Salon salon) {
        return new Clase(dia, horaInicio, horaFin, asignatura, docente, salon);
    }
    public static Clase leerClase(FRHorarios vista, ListaAsignaturas listaAsignaturas,
            ListaDocentes listaDocentes, ListaSalones listaSalones, String gradoSeleccionado) {
        String nombreAsignatura = (String) vista.cbxAsignatura.getSelectedItem();
        Asignatura asignatura = listaAsignaturas.obtener(
                listaAsignaturas.buscarPorNombre(nombreAsignatura));
        String tutorSeleccionado = (String) vista.cbxDocente.getSelectedItem();
        String codigoDocente = tutorSeleccionado.substring(
                tutorSeleccionado.lastIndexOf("(") + 1,
                tutorSeleccionado.lastIndexOf(")"));
        Docente docente = listaDocentes.obtener(
                listaDocentes.buscarPorCODIGO(codigoDocente));
        String dia = (String) vista.cbxDia.getSelectedItem();
        String franja = (String) vista.cbxHora.getSelectedItem();
        String[] partes = franja.split(" - ");
        String horaInicio = partes[0];
        String horaFin = partes[1];
        String nombreSalon = (String) vista.cbxSalon.getSelectedItem();
        ArrayList<Salon> salonesDelGrado = listaSalones.buscarPorGrado(gradoSeleccionado);
        Salon salon = null;
        for (Salon s : salonesDelGrado) {
            if (s.getAula().equals(nombreSalon)) {
                salon = s;
                break;
            }
        }
        return crearClase(dia, horaInicio, horaFin, asignatura, docente, salon);
    }
    public static EventoTable construirModeloTabla(ListaClases listaClases, String grado,
            String salon, CatalogoRecreo catalogoRecreo) {
        ArrayList<Clase> clases = listaClases.buscarPorSalonYGrado(salon, grado);
        ArrayList<Evento> eventos = new ArrayList<>(clases);
        Recreo recreo = catalogoRecreo.getRecreo();
        eventos.add(recreo);
        return new EventoTable(eventos);
    }
    public static String validarEventoDatos(String dia, String horaInicio, String horaFin,
                                            String salon, String gradoSeleccionado,
                                            String codigoDocente, ListaClases listaClases) {
        ArrayList<Clase> clases = listaClases.buscarPorSalonYGrado(salon, gradoSeleccionado);
        for (Clase c : clases) {
            if (c.getDiaSemana().equals(dia) &&
                c.getHoraInicio().equals(horaInicio) &&
                c.getHoraFin().equals(horaFin)) {
                return "Ya existe un evento en ese día y horario para este salón.";
            }
        }
        if (codigoDocente != null && !codigoDocente.isEmpty()) {
            if (listaClases.existeDocenteEnHorario(codigoDocente, dia, horaInicio, horaFin)) {
                return "El docente ya tiene una clase asignada en ese día y horario.";
            }
        }
        return null;
    }
    public static String validarEvento(FRHorarios vista, ListaClases listaClases, 
            String gradoSeleccionado, ListaDocentes listaDocentes) {
        String dia = (String) vista.cbxDia.getSelectedItem();
        String franja = (String) vista.cbxHora.getSelectedItem();
        String[] partes = franja.split(" - ");
        String horaInicio = partes[0];
        String horaFin = partes[1];
        String salon = (String) vista.cbxSalon.getSelectedItem();
        String docenteSeleccionado = (String) vista.cbxDocente.getSelectedItem();
        String codigoDocente = null;
        if (docenteSeleccionado != null) {
            codigoDocente = docenteSeleccionado.substring(
                    docenteSeleccionado.lastIndexOf("(") + 1,
                    docenteSeleccionado.lastIndexOf(")"));
        }
        return validarEventoDatos(dia, horaInicio, horaFin, salon, gradoSeleccionado, codigoDocente, listaClases);
    }
    public static String obtenerInfoContextual(String gradoSeleccionado, String salonSeleccionado,
            ListaSalones listaSalones, CatalogoGrados catalogoGrados, ArrayList<Clase> clases) {
        StringBuilder info = new StringBuilder();
        info.append("Grado: ").append(gradoSeleccionado).append("\n");
        info.append("Salón: ").append(salonSeleccionado).append("\n");

        ArrayList<Salon> salonesDelGrado = listaSalones.buscarPorGrado(gradoSeleccionado);
        for (Salon s : salonesDelGrado) {
            if (s.getAula().equals(salonSeleccionado)) {
                if (s.getTutor() != null) {
                    info.append("Tutor: ").append(s.getTutor().getNombreCompleto()).append("\n");
                } else {
                    info.append("Tutor: Sin tutor asignado\n");
                }
                break;
            }
        }
        info.append("Turno: Mañana\n");
        return info.toString();
    }
    public static void eliminarClase(ListaClases listaClases, String grado, String salon, 
            String dia, String horaInicio, String horaFin) {
        int pos = listaClases.buscarClase(grado, salon, dia, horaInicio, horaFin);
        if (pos != -1) {
            listaClases.eliminar(pos);
        }
    }
}