package controlador;
import almacenamiento.Persistencia;
import modelo.Docente;
import modelo.Grado;
import modelo.Salon;
import modelo.repositorio.CatalogoGrados;
import modelo.repositorio.ListaDocentes;
import modelo.repositorio.ListaSalones;
import procesos.Mensajes;
import procesos.ProcesosSalon;
import vista.FRSalones;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
/**
 *
 * @author inici4rsesi0n
 */
public class ControlSalones implements ActionListener {
    private FRSalones vista;
    private ListaSalones listaSalones;
    private ListaDocentes listaDocentes;
    private CatalogoGrados catalogoGrados;
    private int posicion = -1;

    public ControlSalones(FRSalones vista) {
        this.vista = vista;
        this.listaSalones = Persistencia.cargarListaSalones();
        this.listaDocentes = Persistencia.cargarListaDocentes();
        this.catalogoGrados = Persistencia.cargarCatalogoGrados();
        ProcesosSalon.llenarComboGrados(vista, catalogoGrados);
        ProcesosSalon.llenarComboTutores(vista, listaDocentes);
        vista.btnRegistrar.addActionListener(this);
        vista.btnEliminar.addActionListener(this);
        vista.btnRefrescar.addActionListener(this);
        vista.btnLimpiar.addActionListener(this);
        vista.tblRegistroSalones.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int fila = vista.tblRegistroSalones.getSelectedRow();
                    if (fila >= 0) {
                        posicion = fila;
                        Salon s = listaSalones.obtener(fila);
                        vista.txtSalon.setText(s.getAula());
                        vista.txtAlumnosMAX.setText(String.valueOf(s.getCupoMax()));
                        vista.cbxGrado.setSelectedItem(s.getGrado().getNom());
                        if (s.getTutor() != null) {
                            vista.cbxTutor.setSelectedItem(s.getTutor().getNombreCompleto() + " (" + s.getTutor().getCod() + ")");
                        } else {
                            vista.cbxTutor.setSelectedIndex(0);
                        }
                    }
                }
            }
        });ProcesosSalon.mostrarDatos(vista.tblRegistroSalones, listaSalones);}
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.btnRegistrar) {
            String error = ProcesosSalon.validarCampos(vista, listaSalones);
            if (error != null) {
                Mensajes.M1(error);
                return;
            }
            Grado grado = obtenerGradoSeleccionado();
            Docente tutor = obtenerTutorSeleccionado();
            Salon nuevo = ProcesosSalon.leerSalon(vista, grado, tutor);
            listaSalones.agregar(nuevo);
            if (tutor != null) {
                tutor.setTutoria(nuevo);
            }
            Persistencia.guardar(listaSalones, "salones.bin");
            Persistencia.guardar(listaDocentes, "docentes.bin"); // Guardar docentes actualizados
            Mensajes.M1("Salón registrado correctamente.");
            ProcesosSalon.mostrarDatos(vista.tblRegistroSalones, listaSalones);
            ProcesosSalon.limpiarCampos(vista);
        }
        else if (e.getSource() == vista.btnEliminar) {
            if (posicion < 0) {
                Mensajes.M1("Seleccione un salón de la tabla.");
                return;
            }
            int confirm = Mensajes.M3("Eliminar", "¿Desea eliminar el salón?");
            if (confirm == JOptionPane.OK_OPTION) {
                Salon salonAEliminar = listaSalones.obtener(posicion);
                Docente tutorDelSalon = salonAEliminar.getTutor();
                if (tutorDelSalon != null) {
                    tutorDelSalon.setTutoria(null);
                }
                listaSalones.eliminar(posicion);
                Persistencia.guardar(listaSalones, "salones.bin");
                Persistencia.guardar(listaDocentes, "docentes.bin"); // Guardar docentes actualizados
                Mensajes.M1("Salón eliminado.");
                ProcesosSalon.mostrarDatos(vista.tblRegistroSalones, listaSalones);
                ProcesosSalon.limpiarCampos(vista);
                posicion = -1;
            }
        }
        else if (e.getSource() == vista.btnRefrescar) {
            ProcesosSalon.mostrarDatos(vista.tblRegistroSalones, listaSalones);
            ProcesosSalon.limpiarCampos(vista);
        }
        else if (e.getSource() == vista.btnLimpiar) {
            ProcesosSalon.limpiarCampos(vista);
            posicion = -1;
        }
    }
    private Grado obtenerGradoSeleccionado() {
        String nombreGrado = (String) vista.cbxGrado.getSelectedItem();
        if (nombreGrado == null) return null;
        for (int i = 0; i < catalogoGrados.getGrados().size(); i++) {
            Grado g = catalogoGrados.getGrados().get(i);
            if (g.getNom().equals(nombreGrado)) {
                return g;
            }
        }return null;}
    private Docente obtenerTutorSeleccionado() {
        String tutorSeleccionado = (String) vista.cbxTutor.getSelectedItem();
        if (tutorSeleccionado == null || tutorSeleccionado.equals("Sin tutor")) {
            return null;
        }
        String codigo = tutorSeleccionado.substring(
                tutorSeleccionado.lastIndexOf("(") + 1,
                tutorSeleccionado.lastIndexOf(")")
        );
        int pos = listaDocentes.buscarPorCODIGO(codigo);
        return (pos != -1) ? listaDocentes.obtener(pos) : null;
    }
}