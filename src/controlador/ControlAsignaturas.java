package controlador;
import modelo.repositorio.ListaAsignaturas;
import procesos.Mensajes;
import procesos.ProcesosAsignatura;
import vista.FRAsignaturas;
import almacenamiento.Persistencia;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.JOptionPane;

/**
 *
 * @author inici4rsesi0n
 */
public class ControlAsignaturas implements ActionListener {

    private FRAsignaturas vista;
    private ListaAsignaturas lista;
    private int posicion = -1;

    public ControlAsignaturas(FRAsignaturas vista) {
        this.vista = vista;
        lista = Persistencia.cargarListaAsignaturas();
        vista.btnRegistrar.addActionListener(this);
        vista.btnEliminar.addActionListener(this);
        vista.btnRefrescar.addActionListener(this);
        vista.btnLimpiar.addActionListener(this);
        vista.tblRegistroAsignaturas.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int fila = vista.tblRegistroAsignaturas.getSelectedRow();
                    if (fila >= 0) {
                        posicion = fila;
                        vista.txtAsignatura.setText(lista.obtener(fila).getNom());
                    }
                }
            }
        });
        ProcesosAsignatura.mostrarDatos(vista.tblRegistroAsignaturas, lista);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.btnRegistrar) {
            String error = ProcesosAsignatura.validarCampos(vista, lista);
            if (error != null) {
                Mensajes.M1(error);
                return;
            }
            lista.agregar(ProcesosAsignatura.leerAsignatura(vista));
            Persistencia.guardar(lista, "asignaturas.bin");
            Mensajes.M1("Asignatura registrada.");
            ProcesosAsignatura.mostrarDatos(vista.tblRegistroAsignaturas, lista);
            ProcesosAsignatura.limpiarCampos(vista);
        } else if (e.getSource() == vista.btnEliminar) {
            if (posicion < 0) {
                Mensajes.M1("Seleccione una asignatura de la tabla.");
                return;
            }
            int confirm = Mensajes.M3("Eliminar", "¿Desea eliminar la asignatura?");
            if (confirm == JOptionPane.OK_OPTION) {
                lista.eliminar(posicion);
                Persistencia.guardar(lista, "asignaturas.bin");
                Mensajes.M1("Asignatura eliminada.");
                ProcesosAsignatura.mostrarDatos(vista.tblRegistroAsignaturas, lista);
                ProcesosAsignatura.limpiarCampos(vista);
                posicion = -1;
            }
        } else if (e.getSource() == vista.btnRefrescar) {
            ProcesosAsignatura.mostrarDatos(vista.tblRegistroAsignaturas, lista);
            ProcesosAsignatura.limpiarCampos(vista);
        } else if (e.getSource() == vista.btnLimpiar) {
            ProcesosAsignatura.limpiarCampos(vista);
            posicion = -1;
        }
    }

}
