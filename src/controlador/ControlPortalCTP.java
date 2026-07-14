package controlador;
import almacenamiento.Persistencia;
import modelo.Estudiante;
import modelo.Salon;
import modelo.repositorio.CatalogoRecreo;
import modelo.repositorio.ListaClases;
import modelo.repositorio.ListaEstudiantes;
import modelo.repositorio.ListaSalones;
import procesos.Mensajes;
import procesos.ProcesosPortalCTP;
import procesos.ProcesosPrincipal;
import vista.FPortalCTP;
import vista.FLogin;
import vista.FPrincipal;
import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
/**
 *
 * @author inici4rsesi0n
 */
public class ControlPortalCTP implements ActionListener {
    private FPortalCTP vista;
    private Estudiante estudiante;
    private FPrincipal principal;
    private ListaSalones listaSalones;
    private ListaEstudiantes listaEstudiantes;
    private ListaClases listaClases;
    private CatalogoRecreo catalogoRecreo;
    private String gradoEstudiante;

    public ControlPortalCTP(FPortalCTP vista, Estudiante estudiante, FPrincipal principal) {
        this.vista = vista;
        this.estudiante = estudiante;
        this.principal = principal;
        this.listaSalones = Persistencia.cargarListaSalones();
        this.listaEstudiantes = Persistencia.cargarListaEstudiantes();
        this.listaClases = Persistencia.cargarListaEventosClase();
        this.catalogoRecreo = new CatalogoRecreo();
        this.gradoEstudiante = (estudiante.getGrado() != null) ? estudiante.getGrado().getNom() : null;
        ProcesosPortalCTP.cargarDatosEstudiante(vista, estudiante);
        if (gradoEstudiante != null) {
            ProcesosPortalCTP.actualizarComboSalones(vista, listaSalones, listaEstudiantes, gradoEstudiante);
        }
        if (estudiante.getSalon() != null) {
            vista.cbxSalon.setSelectedItem(estudiante.getSalon().getAula());
            vista.btnRegistrarMatricula.setEnabled(false);
            refrescarInfoSalon();
        } else if (vista.cbxSalon.getItemCount() > 0) {
            refrescarInfoSalon();
        }
        vista.btnRegistrarMatricula.addActionListener(this);
        vista.btnGuardarMatricula.addActionListener(this);
        vista.btnEliminarMatricula.addActionListener(this);
        vista.btnCerrarSesion.addActionListener(this);
        vista.cbxSalon.addActionListener(e -> refrescarInfoSalon());
    }
    private void refrescarInfoSalon() {
        String aulaSeleccionada = (String) vista.cbxSalon.getSelectedItem();
        if (aulaSeleccionada == null || gradoEstudiante == null) {
            return;
        }
        Salon salonSeleccionado = null;
        for (int i = 0; i < listaSalones.getLista().size(); i++) {
            Salon s = listaSalones.obtener(i);
            if (s.getAula().equals(aulaSeleccionada) && s.getGrado().getNom().equals(gradoEstudiante)) {
                salonSeleccionado = s;
                break;
            }
        }
        if (salonSeleccionado == null) {
            return;
        }
        ProcesosPortalCTP.cargarTablaAlumnos(vista.tblListaEstudiantes, listaEstudiantes, salonSeleccionado);
        ProcesosPortalCTP.cargarTablaHorario(vista.tblHorario, listaClases, gradoEstudiante, aulaSeleccionada, catalogoRecreo);
        ProcesosPortalCTP.mostrarInfoSalon(vista, salonSeleccionado, listaEstudiantes);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.btnRegistrarMatricula) {
            if (estudiante.getSalon() != null) {
                Mensajes.M1("Ya estás matriculado en "
                        + estudiante.getSalon().getGrado().getNom() + " "
                        + estudiante.getSalon().getAula()
                        + ". Usa 'Eliminar matrícula' primero si deseas cambiarte.");
                return;
            }
            String aulaSeleccionada = (String) vista.cbxSalon.getSelectedItem();
            if (aulaSeleccionada == null || gradoEstudiante == null) {
                Mensajes.M1("No hay salones disponibles.");
                return;
            }
            Salon salonElegido = null;
            for (int i = 0; i < listaSalones.getLista().size(); i++) {
                Salon s = listaSalones.obtener(i);
                if (s.getAula().equals(aulaSeleccionada) && s.getGrado().getNom().equals(gradoEstudiante)) {
                    salonElegido = s;
                    break;
                }
            }
            if (salonElegido == null) {
                Mensajes.M1("Salón no encontrado.");
                return;
            }
            if (!ProcesosPortalCTP.validarCupo(salonElegido, listaEstudiantes)) {
                Mensajes.M1("El salón " + aulaSeleccionada + " ha alcanzado su cupo máximo.");
                return;
            }
            estudiante.setSalon(salonElegido);
            int pos = listaEstudiantes.buscarPorCODIGO(estudiante.getCod());
            if (pos != -1) {
                listaEstudiantes.actualizar(pos, estudiante);
            }
            refrescarInfoSalon();
            vista.btnRegistrarMatricula.setEnabled(false);
            Mensajes.M1("Pre-matrícula registrada en " + gradoEstudiante + " " + aulaSeleccionada + ".\nPresione 'Guardar' para confirmar los cambios.");
        } else if (e.getSource() == vista.btnGuardarMatricula) {
            int pos = listaEstudiantes.buscarPorCODIGO(estudiante.getCod());
            if (pos != -1) {
                listaEstudiantes.actualizar(pos, estudiante);
                Persistencia.guardar(listaEstudiantes, "estudiantes.bin");
                Mensajes.M1("Matrícula guardada correctamente.");
                refrescarInfoSalon();
            } else {
                Mensajes.M1("Error: no se encontró al estudiante en la lista.");
            }
        } else if (e.getSource() == vista.btnEliminarMatricula) {
            int confirm = Mensajes.M3("Eliminar matrícula", "¿Desea anular su matrícula en este salón?");
            if (confirm == JOptionPane.OK_OPTION) {
                estudiante.setSalon(null);
                int pos = listaEstudiantes.buscarPorCODIGO(estudiante.getCod());
                if (pos != -1) {
                    listaEstudiantes.actualizar(pos, estudiante);
                    Persistencia.guardar(listaEstudiantes, "estudiantes.bin");
                    Mensajes.M1("Matrícula eliminada.");
                    refrescarInfoSalon();
                    vista.btnRegistrarMatricula.setEnabled(true);
                }
            }
        } else if (e.getSource() == vista.btnCerrarSesion) {
            vista.dispose();
            FLogin login = new FLogin();
            ControlLogin controlLogin = new ControlLogin(login, principal);
            ProcesosPrincipal.mostrarInternalFrame(principal, login);
        }
    }
}
