package controlador;
import almacenamiento.Persistencia;
import modelo.*;
import modelo.repositorio.*;
import modelo.tabla.EventoTable;
import procesos.Mensajes;
import procesos.ProcesosHorario;
import vista.FRHorarios;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
/**
 *
 * @author inici4rsesi0n
 */
public class ControlHorarios implements ActionListener {
    private FRHorarios vista;
    private ListaClases listaClases;
    private ListaSalones listaSalones;
    private ListaAsignaturas listaAsignaturas;
    private ListaDocentes listaDocentes;
    private CatalogoGrados catalogoGrados;
    private CatalogoRecreo catalogoRecreo;
    private String gradoSeleccionado;
    private String salonSeleccionado;

    public ControlHorarios(FRHorarios vista) {
        this.vista = vista;
        this.listaClases = Persistencia.cargarListaEventosClase();
        this.listaSalones = Persistencia.cargarListaSalones();
        this.listaAsignaturas = Persistencia.cargarListaAsignaturas();
        this.listaDocentes = Persistencia.cargarListaDocentes();
        this.catalogoGrados = Persistencia.cargarCatalogoGrados();
        this.catalogoRecreo = new CatalogoRecreo();
        ProcesosHorario.llenarCombos(vista, catalogoGrados, listaSalones, listaAsignaturas, listaDocentes);
        if (catalogoGrados.getGrados().size() > 0) {
            gradoSeleccionado = catalogoGrados.getGrados().get(0).getNom();
            vista.cbxGrado.setSelectedItem(gradoSeleccionado);
            ProcesosHorario.actualizarComboSalones(vista, listaSalones, gradoSeleccionado);
            if (vista.cbxSalon.getItemCount() > 0) {
                salonSeleccionado = (String) vista.cbxSalon.getItemAt(0);
                vista.cbxSalon.setSelectedItem(salonSeleccionado);
                refrescarTabla();
            }
        }
        vista.btnRegistrar.addActionListener(this);
        vista.btnEliminar.addActionListener(this);
        vista.btnGuardar.addActionListener(this);
        vista.btnRefrescar.addActionListener(this);
        vista.cbxGrado.addActionListener(e -> {
            gradoSeleccionado = (String) vista.cbxGrado.getSelectedItem();
            if (gradoSeleccionado != null) {
                ProcesosHorario.actualizarComboSalones(vista, listaSalones, gradoSeleccionado);
                if (vista.cbxSalon.getItemCount() > 0) {
                    salonSeleccionado = (String) vista.cbxSalon.getItemAt(0);
                    vista.cbxSalon.setSelectedItem(salonSeleccionado);
                    refrescarTabla();
                }
            }
        });
        vista.cbxSalon.addActionListener(e -> {
            salonSeleccionado = (String) vista.cbxSalon.getSelectedItem();
            if (salonSeleccionado != null) {
                refrescarTabla();
            }
        });
        vista.cbxDocente.addActionListener(e -> {
            String docenteSeleccionado = (String) vista.cbxDocente.getSelectedItem();
            ProcesosHorario.actualizarComboAsignaturasPorDocente(vista, listaDocentes, docenteSeleccionado);
        });
    }

    private void refrescarTabla() {
        if (gradoSeleccionado != null && salonSeleccionado != null) {
            ArrayList<Clase> clasesFiltradas = listaClases.buscarPorSalonYGrado(salonSeleccionado, gradoSeleccionado);
            EventoTable modelo = ProcesosHorario.construirModeloTabla(
                    listaClases, gradoSeleccionado, salonSeleccionado, catalogoRecreo);
            vista.tblRegistroHorario.setModel(modelo);
            String info = ProcesosHorario.obtenerInfoContextual(
                    gradoSeleccionado, salonSeleccionado, listaSalones, catalogoGrados, clasesFiltradas);
            vista.txaAreaInfo.setText(info);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.btnRegistrar) {
            if (gradoSeleccionado == null || salonSeleccionado == null) {
                Mensajes.M1("Seleccione un grado y un salón.");
                return;
            }
            String error = ProcesosHorario.validarEvento(vista, listaClases, gradoSeleccionado, listaDocentes);
            if (error != null) {
                Mensajes.M1(error);
                return;
            }
            Clase nuevaClase = ProcesosHorario.leerClase(
                    vista, listaAsignaturas, listaDocentes, listaSalones, gradoSeleccionado);
            listaClases.agregar(nuevaClase);
            Persistencia.guardar(listaClases, "eventos.bin");
            Mensajes.M1("Evento registrado correctamente.");
            refrescarTabla();
        }
        else if (e.getSource() == vista.btnEliminar) {
            int fila = vista.tblRegistroHorario.getSelectedRow();
            int columna = vista.tblRegistroHorario.getSelectedColumn();
            if (fila == -1 || columna < 1) {
                Mensajes.M1("Seleccione una celda con un curso.");
                return;
            }
            String franja = (String) vista.tblRegistroHorario.getValueAt(fila, 0);
            String dia = vista.tblRegistroHorario.getColumnName(columna);
            String valorCelda = (String) vista.tblRegistroHorario.getValueAt(fila, columna);

            if (valorCelda == null || valorCelda.equals("Recreo") || valorCelda.trim().isEmpty()) {
                Mensajes.M1("No se puede eliminar esta celda.");
                return;
            }
            String[] partes = franja.split(" - ");
            String horaInicio = partes[0];
            String horaFin = partes[1];
            int confirm = Mensajes.M3("Eliminar", "¿Desea eliminar el evento " + valorCelda
                    + " del " + dia + " " + franja + "?");
            if (confirm == JOptionPane.OK_OPTION) {
                ProcesosHorario.eliminarClase(listaClases, gradoSeleccionado, salonSeleccionado,
                        dia, horaInicio, horaFin);
                Persistencia.guardar(listaClases, "eventos.bin");
                refrescarTabla();
                Mensajes.M1("Evento eliminado correctamente.");
            }
        }
        else if (e.getSource() == vista.btnGuardar) {
            Persistencia.guardar(listaClases, "eventos.bin");
            Mensajes.M1("Cambios guardados correctamente.");
        }
        else if (e.getSource() == vista.btnRefrescar) {
            refrescarTabla();
        }
    }
}