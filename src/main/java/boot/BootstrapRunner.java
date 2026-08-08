package boot;
import aplicacion.casosdeuso.BootstrapUseCase;
import infraestructura.persistencia.ManejadorPersistencia;
import infraestructura.persistencia.Recargable;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import presentacion.controlador.BootstrapController;
import org.springframework.stereotype.Component;
import java.util.List;
/**
 *
 * @author inici4rsesi0n
 */
@Component
public class BootstrapRunner {

    private final List<Recargable> repositoriosRecargables;
    private final BootstrapUseCase bootstrapUseCase;

    public BootstrapRunner(List<Recargable> repositoriosRecargables, BootstrapUseCase bootstrapUseCase) {
        this.repositoriosRecargables = repositoriosRecargables;
        this.bootstrapUseCase = bootstrapUseCase;
    }

    public void inicializar(Stage stage) {
        Object repoAdmin = ManejadorPersistencia.cargar("administradores.bin");

        if (repoAdmin == null) {
            try {
                FXMLLoader loader = new FXMLLoader(
                        getClass().getResource("/fxml/ConfiguracionInicial.fxml"));
                Parent root = loader.load();
                BootstrapController controller = loader.getController();

                Stage configStage = new Stage();
                configStage.setTitle("Configuración Inicial del Sistema");
                configStage.setScene(new Scene(root));
                configStage.initModality(Modality.APPLICATION_MODAL);
                configStage.initOwner(stage);
                configStage.showAndWait();

                bootstrapUseCase.inicializarSistema(controller);

                for (Recargable repositorio : repositoriosRecargables) {
                    repositorio.recargar();
                }
                repoAdmin = ManejadorPersistencia.cargar("administradores.bin");
                if (repoAdmin == null) {
                    throw new RuntimeException("No se pudo inicializar el sistema.");
                }
            } catch (Exception e) {
                throw new RuntimeException("No se pudo cargar la ventana de configuración inicial.", e);
            }
        }
    }
}