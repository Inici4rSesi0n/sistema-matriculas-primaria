package procesos;
import controlador.*;
import vista.*;
import javax.swing.JInternalFrame;

public class ProcesosMenuRegistros {
    public static JInternalFrame abrirDocentes() {
        FRDocentes frm = new FRDocentes();
        new ControlDocentes(frm);
        frm.setTitle("Registro de Docentes");
        return frm;
    }
    public static JInternalFrame abrirEstudiantes() {
        FREstudiante frm = new FREstudiante();
        new ControlEstudiantes(frm);
        frm.setTitle("Registro de Estudiantes");
        return frm;
    }
    public static JInternalFrame abrirSalones() {
        FRSalones frm = new FRSalones();
        new ControlSalones(frm);
        frm.setTitle("Registro de Salones");
        return frm;
    }
    public static JInternalFrame abrirAsignaturas() {
        FRAsignaturas frm = new FRAsignaturas();
        new ControlAsignaturas(frm);
        frm.setTitle("Registro de Asignaturas");
        return frm;
    }
    public static JInternalFrame abrirHorarios() {
        FRHorarios frm = new FRHorarios();
        new ControlHorarios(frm);
        frm.setTitle("Registro de Horarios");
        return frm;
    }
    public static JInternalFrame abrirGestionAsignaturas() {
        FRGestionAsignaturas frm = new FRGestionAsignaturas();
        new ControlGestionAsignaturas(frm);
        frm.setTitle("Gestión de Asignaturas por Docente");
        return frm;
    }
}
