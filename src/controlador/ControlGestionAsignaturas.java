package controlador;
import almacenamiento.Persistencia;
import modelo.Docente;
import modelo.repositorio.ListaAsignaturas;
import modelo.repositorio.ListaDocentes;
import modelo.tabla.GestionAsignaturaTable;
import procesos.Mensajes;
import procesos.ProcesosGestionAsignaturas;
import vista.FRGestionAsignaturas;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
/**
 *
 * @author inici4rsesi0n
 */
public class ControlGestionAsignaturas implements ActionListener {
    private FRGestionAsignaturas vista;
    private ListaDocentes listaDocentes;
    private ListaAsignaturas listaAsignaturas;
    private Docente docenteSeleccionado;
    private GestionAsignaturaTable modeloTabla;

    public ControlGestionAsignaturas(FRGestionAsignaturas vista) {
        this.vista = vista;
        this.listaDocentes = Persistencia.cargarListaDocentes();
        this.listaAsignaturas = Persistencia.cargarListaAsignaturas();
        ProcesosGestionAsignaturas.cargarDocentesEnCombo(vista, listaDocentes);
        if (listaDocentes.getLista().size() > 0) {
            vista.cbxDocente.setSelectedIndex(0);
            docenteSeleccionado = listaDocentes.obtener(0);
            ProcesosGestionAsignaturas.cargarDatosDocente(
                    docenteSeleccionado,
                    listaAsignaturas,
                    vista.lstAsignadas,
                    vista.lstDisponibles
            );
        }
        modeloTabla = new GestionAsignaturaTable(listaDocentes);
        vista.tblRegistroGestionAsignaturas.setModel(modeloTabla);
        vista.cbxDocente.addActionListener(this);
        vista.btnRegistrar.addActionListener(this);
        vista.btnEliminar.addActionListener(this);
        vista.btnGuardar.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.cbxDocente) {
            String seleccion = (String) vista.cbxDocente.getSelectedItem();
            if (seleccion != null) {
                String codigo = seleccion.substring(
                        seleccion.lastIndexOf("(") + 1,
                        seleccion.lastIndexOf(")")
                );
                int pos = listaDocentes.buscarPorCODIGO(codigo);
                if (pos != -1) {
                    docenteSeleccionado = listaDocentes.obtener(pos);
                    ProcesosGestionAsignaturas.cargarDatosDocente(
                            docenteSeleccionado,
                            listaAsignaturas,
                            vista.lstAsignadas,
                            vista.lstDisponibles
                    );
                }
            }
        } else if (e.getSource() == vista.btnRegistrar) {
            ProcesosGestionAsignaturas.moverAsignatura(vista.lstDisponibles, vista.lstAsignadas);
        } else if (e.getSource() == vista.btnEliminar) {
            ProcesosGestionAsignaturas.moverAsignatura(vista.lstAsignadas, vista.lstDisponibles);
        } else if (e.getSource() == vista.btnGuardar) {
            if (docenteSeleccionado == null) {
                Mensajes.M1("Seleccione un docente.");
                return;
            }
            ProcesosGestionAsignaturas.guardarAsignaciones(
                    vista.lstAsignadas,
                    docenteSeleccionado,
                    listaAsignaturas,
                    listaDocentes
            );
            modeloTabla.fireTableDataChanged();
            Mensajes.M1("Asignaciones guardadas correctamente.");
        }
    }
}