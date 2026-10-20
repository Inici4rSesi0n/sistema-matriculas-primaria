package presentacion.controlador;

import aplicacion.casosdeuso.portaldeingreso.administraciondelsistema.academico.GestionGrupos;
import aplicacion.casosdeuso.portaldeingreso.administraciondelsistema.usuarios.GestionDocentes;
import dominio.modelo.Docente;
import dominio.modelo.Grupo;
import dominio.puerto.externo.LoggerPort;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import presentacion.dialogos.Dialogos;
import presentacion.eventos.SistemaEventBus;
import presentacion.eventos.TipoEvento;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 *
 * @author inici4rsesi0n
 */
@Component
@Scope("prototype")
public class AsignacionTutorController {

    private final GestionGrupos gestionGrupos;
    private final GestionDocentes gestionDocentes;
    private final LoggerPort logger;

    @FXML private ComboBox<String> cmbDocente;
    @FXML private ComboBox<String> cmbGrupo;

    public AsignacionTutorController(GestionGrupos gestionGrupos, GestionDocentes gestionDocentes, LoggerPort logger) {
        this.gestionGrupos = gestionGrupos;
        this.gestionDocentes = gestionDocentes;
        this.logger = logger;
    }

    @FXML
    public void initialize() {
        cmbDocente.getItems().clear();
        for (Docente d : gestionDocentes.listarTodos()) {
            cmbDocente.getItems().add(d.getCodigo() + " - " + d.getNombreCompleto());
        }
        cmbDocente.getSelectionModel().selectFirst();

        cmbGrupo.getItems().clear();
        for (Grupo g : gestionGrupos.listarTodos()) {
            cmbGrupo.getItems().add(g.getNombre() + " (" + g.getGrado().getNombre() + ")");
        }
        cmbGrupo.getSelectionModel().selectFirst();
    }

    @FXML
    private void handleGuardar() {
        String docenteSel = cmbDocente.getValue();
        String grupoSel = cmbGrupo.getValue();

        if (docenteSel == null || docenteSel.startsWith("Seleccione") || grupoSel == null || grupoSel.startsWith("Seleccione")) {
            Dialogos.info("Debe seleccionar un docente y un grupo."); return;
        }

        try {
            String codDocente = docenteSel.split(" - ")[0];
            Docente docente = gestionDocentes.buscarPorCodigo(codDocente);
            
            String nombreGrupo = grupoSel.substring(0, grupoSel.lastIndexOf(" (")).trim();
            Grupo grupo = gestionGrupos.buscarPorNombre(nombreGrupo);

            if (docente == null || grupo == null) {
                Dialogos.info("Docente o grupo no encontrado."); return;
            }

            gestionGrupos.asignarTutor(grupo, docente);
            SistemaEventBus.notificar(TipoEvento.GRUPOS, TipoEvento.DOCENTES);
            
            Stage stage = (Stage) cmbDocente.getScene().getWindow();
            stage.close();
            
        } catch (Exception e) {
            logger.error("Error al asignar tutor: " + e.getMessage());
            Dialogos.info(e.getMessage());
        }
    }

    @FXML
    private void handleCancelar() {
        Stage stage = (Stage) cmbDocente.getScene().getWindow();
        stage.close();
    }
}
