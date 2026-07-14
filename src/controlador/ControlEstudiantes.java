package controlador;
import almacenamiento.Persistencia;
import modelo.Estudiante;
import modelo.Grado;
import modelo.Salon;
import modelo.repositorio.CatalogoGrados;
import modelo.repositorio.GestorDirector;
import modelo.repositorio.ListaDocentes;
import modelo.repositorio.ListaEstudiantes;
import modelo.repositorio.ListaSalones;
import procesos.Mensajes;
import procesos.ProcesosEstudiantes;
import vista.FREstudiante;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
/**
 *
 * @author inici4rsesi0n
 */
public class ControlEstudiantes implements ActionListener {
    private FREstudiante vista;
    private ListaEstudiantes listaEstudiantes;
    private ListaSalones listaSalones;
    private CatalogoGrados catalogoGrados;
    private ListaDocentes listaDocentes;
    private GestorDirector gestorDirector;
    private int posicion = -1;

    public ControlEstudiantes(FREstudiante vista) {
        this.vista = vista;
        this.listaEstudiantes = Persistencia.cargarListaEstudiantes();
        this.listaSalones = Persistencia.cargarListaSalones();
        this.catalogoGrados = Persistencia.cargarCatalogoGrados();
        this.listaDocentes = Persistencia.cargarListaDocentes();
        this.gestorDirector = Persistencia.cargarGestorDirector();
        ProcesosEstudiantes.llenarComboGrados(vista, catalogoGrados);
        if (catalogoGrados.getGrados().size() > 0) {
            String primerGrado = catalogoGrados.getGrados().get(0).getNom();
            vista.cbxGrado.setSelectedItem(primerGrado);
            ProcesosEstudiantes.actualizarComboSalones(vista, listaSalones, primerGrado);
        }
        vista.btnRegistrar.addActionListener(this);
        vista.btnEliminar.addActionListener(this);
        vista.btnConsultar.addActionListener(this);
        vista.btnRefrescar.addActionListener(this);
        vista.btnLimpiar.addActionListener(this);
        vista.cbxGrado.addActionListener(e -> {
            String gradoSeleccionado = (String) vista.cbxGrado.getSelectedItem();
            ProcesosEstudiantes.actualizarComboSalones(vista, listaSalones, gradoSeleccionado);
        });
        vista.jTable1.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int fila = vista.jTable1.getSelectedRow();
                    if (fila >= 0) {
                        posicion = fila;
                        Estudiante est = listaEstudiantes.obtener(fila);
                        vista.txtCodigo.setText(est.getCod());
                        vista.txtDNI.setText(est.getDni());
                        vista.txtNombre.setText(est.getNom());
                        vista.txtApellido.setText(est.getApe());
                        vista.txtEdad.setText(String.valueOf(est.getAge()));
                        vista.txtContraseña.setText("");
                        if (est.getSalon() != null) {
                            vista.cbxGrado.setSelectedItem(est.getSalon().getGrado().getNom());
                            vista.cbxSalon.setSelectedItem(est.getSalon().getAula());
                        } else {
                            if (est.getGrado() != null) {
                                vista.cbxGrado.setSelectedItem(est.getGrado().getNom());
                            } else {
                                vista.cbxGrado.setSelectedIndex(0);
                            }
                            vista.cbxSalon.setSelectedItem("Sin Salón");
                        }
                    }
                }
            }
        });
        ProcesosEstudiantes.mostrarDatos(vista.jTable1, listaEstudiantes);
    }
    private Grado obtenerGradoSeleccionado() {
        String nombreGrado = (String) vista.cbxGrado.getSelectedItem();
        if (nombreGrado == null) {
            return null;
        }
        for (int i = 0; i < catalogoGrados.getGrados().size(); i++) {
            Grado g = catalogoGrados.getGrados().get(i);
            if (g.getNom().equals(nombreGrado)) {
                return g;
            }
        }
        return null;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.btnRegistrar) {
            String error = ProcesosEstudiantes.validarCampos(vista, listaEstudiantes, listaDocentes, gestorDirector);
            if (error != null) {
                Mensajes.M1(error);
                return;
            }
            Salon salon = ProcesosEstudiantes.obtenerSalonSeleccionado(vista, listaSalones);
            Grado grado = obtenerGradoSeleccionado();
            if (grado == null) {
                Mensajes.M1("Debe seleccionar un grado.");
                return;
            }
            Estudiante nuevo = ProcesosEstudiantes.leerEstudiante(vista, salon, grado);
            listaEstudiantes.agregar(nuevo);
            Persistencia.guardar(listaEstudiantes, "estudiantes.bin");
            Mensajes.M1("Estudiante registrado correctamente.");
            ProcesosEstudiantes.mostrarDatos(vista.jTable1, listaEstudiantes);
            ProcesosEstudiantes.limpiarCampos(vista);
        } else if (e.getSource() == vista.btnEliminar) {
            if (posicion < 0) {
                Mensajes.M1("Seleccione un estudiante de la tabla.");
                return;
            }
            int confirm = Mensajes.M3("Eliminar", "¿Desea eliminar al estudiante?");
            if (confirm == JOptionPane.OK_OPTION) {
                listaEstudiantes.eliminar(posicion);
                Persistencia.guardar(listaEstudiantes, "estudiantes.bin");
                Mensajes.M1("Estudiante eliminado.");
                ProcesosEstudiantes.mostrarDatos(vista.jTable1, listaEstudiantes);
                ProcesosEstudiantes.limpiarCampos(vista);
                posicion = -1;
            }
        } else if (e.getSource() == vista.btnConsultar) {
            String codigo = Mensajes.M2("Ingrese el código del estudiante:");
            if (codigo == null || codigo.trim().isEmpty()) {
                return;
            }
            int pos = listaEstudiantes.buscarPorCODIGO(codigo.trim());
            if (pos == -1) {
                Mensajes.M1("Estudiante no encontrado.");
            } else {
                vista.jTable1.setRowSelectionInterval(pos, pos);
                vista.jTable1.scrollRectToVisible(vista.jTable1.getCellRect(pos, 0, true));
            }
        } else if (e.getSource() == vista.btnRefrescar) {
            ProcesosEstudiantes.mostrarDatos(vista.jTable1, listaEstudiantes);
            ProcesosEstudiantes.limpiarCampos(vista);
        } else if (e.getSource() == vista.btnLimpiar) {
            ProcesosEstudiantes.limpiarCampos(vista);
            posicion = -1;
        }
    }
}
