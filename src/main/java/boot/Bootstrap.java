package boot;

import presentacion.controlador.BootstrapController;
import infraestructura.configuracion.ProveedorInfraestructura;
import aplicacion.casosdeuso.BootstrapUseCase;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author inici4rsesi0n
 */
public class Bootstrap {
    public static void inicializar(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    Bootstrap.class.getResource("/fxml/ConfiguracionInicial.fxml"));
            Parent root = loader.load();
            BootstrapController controller = loader.getController();
            Stage stage = new Stage();
            stage.setTitle("Configuración Inicial del Sistema");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(primaryStage);
            stage.showAndWait();

            BootstrapUseCase bootstrapUseCase = ProveedorInfraestructura.getBootstrapUseCase();
            bootstrapUseCase.inicializarSistema(controller);
        } catch (IOException e) {
            throw new RuntimeException("No se pudo cargar la ventana de configuración inicial.", e);
        }
    }
}