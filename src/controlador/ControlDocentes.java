package controlador;
import almacenamiento.Persistencia;
import modelo.Docente;
import modelo.repositorio.GestorDirector;
import modelo.repositorio.ListaAsignaturas;
import modelo.repositorio.ListaDocentes;
import modelo.repositorio.ListaEstudiantes;
import procesos.Mensajes;
import procesos.ProcesosDocente;
import vista.FRDocentes;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
/**
 *
 * @author inici4rsesi0n
 */
public class ControlDocentes implements ActionListener {
    private FRDocentes vista;
    private ListaDocentes listaDocentes;
    private ListaAsignaturas listaAsignaturas;
    private ListaEstudiantes listaEstudiantes;
    private GestorDirector gestorDirector;
    private int posicion = -1;

    public ControlDocentes(FRDocentes vista) {
        this.vista = vista;
        this.listaDocentes = Persistencia.cargarListaDocentes();
        this.listaAsignaturas = Persistencia.cargarListaAsignaturas();
        this.listaEstudiantes = Persistencia.cargarListaEstudiantes();
        this.gestorDirector = Persistencia.cargarGestorDirector();
        ProcesosDocente.llenarComboEspecialidad(vista, listaAsignaturas);
        vista.btnRegistrar.addActionListener(this);
        vista.btnEliminar.addActionListener(this);
        vista.btnConsultar.addActionListener(this);
        vista.btnRefrescar.addActionListener(this);
        vista.btnLimpiar.addActionListener(this);
        vista.tblRegistroDocentes.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int fila = vista.tblRegistroDocentes.getSelectedRow();
                    if (fila >= 0) {
                        posicion = fila;
                        Docente d = listaDocentes.obtener(fila);
                        vista.txtCodigo.setText(d.getCod());
                        vista.txtDNI.setText(d.getDni());
                        vista.txtNombre.setText(d.getNom());
                        vista.txtApellido.setText(d.getApe());
                        vista.txtEdad.setText(String.valueOf(d.getAge()));
                        vista.cbxEspecialidad.setSelectedItem(d.getEspecialidad());
                        vista.txtContraseña.setText("");
                    }
                }
            }
        });
        ProcesosDocente.mostrarDatos(vista.tblRegistroDocentes, listaDocentes);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.btnRegistrar) {
            String error = ProcesosDocente.validarCampos(vista, listaDocentes, listaEstudiantes, gestorDirector);
            if (error != null) {
                Mensajes.M1(error);
                return;
            }
            Docente nuevo = ProcesosDocente.leerDocente(vista);
            listaDocentes.agregar(nuevo);
            Persistencia.guardar(listaDocentes, "docentes.bin");
            Mensajes.M1("Docente registrado correctamente.");
            ProcesosDocente.mostrarDatos(vista.tblRegistroDocentes, listaDocentes);
            ProcesosDocente.limpiarCampos(vista);
        } else if (e.getSource() == vista.btnEliminar) {
            if (posicion < 0) {
                Mensajes.M1("Seleccione un docente de la tabla.");
                return;
            }
            int confirm = Mensajes.M3("Eliminar", "¿Desea eliminar al docente?");
            if (confirm == JOptionPane.OK_OPTION) {
                listaDocentes.eliminar(posicion);
                Persistencia.guardar(listaDocentes, "docentes.bin");
                Mensajes.M1("Docente eliminado.");
                ProcesosDocente.mostrarDatos(vista.tblRegistroDocentes, listaDocentes);
                ProcesosDocente.limpiarCampos(vista);
                posicion = -1;
            }
        } else if (e.getSource() == vista.btnConsultar) {
            String codigo = Mensajes.M2("Ingrese el código del docente:");
            if (codigo == null || codigo.trim().isEmpty()) {
                return;
            }
            int pos = listaDocentes.buscarPorCODIGO(codigo.trim());
            if (pos == -1) {
                Mensajes.M1("Docente no encontrado.");
            } else {
                vista.tblRegistroDocentes.setRowSelectionInterval(pos, pos);
                vista.tblRegistroDocentes.scrollRectToVisible(vista.tblRegistroDocentes.getCellRect(pos, 0, true));
            }
        } else if (e.getSource() == vista.btnRefrescar) {
            ProcesosDocente.mostrarDatos(vista.tblRegistroDocentes, listaDocentes);
            ProcesosDocente.limpiarCampos(vista);
        } else if (e.getSource() == vista.btnLimpiar) {
            ProcesosDocente.limpiarCampos(vista);
            posicion = -1;
        }
    }
}
