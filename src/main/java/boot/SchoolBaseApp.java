package boot;
import infraestructura.configuracion.ProveedorInfraestructura;
import infraestructura.persistencia.ManejadorPersistencia;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
/**
 *
 * @author inici4rsesi0n
 */
public class SchoolBaseApp extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Object repoAdmin = ManejadorPersistencia.cargar("administradores.bin");

        if (repoAdmin == null) {
            Bootstrap.inicializar(stage);
            ProveedorInfraestructura.inicializar();
            repoAdmin = ManejadorPersistencia.cargar("administradores.bin");
            if (repoAdmin == null) {
                throw new RuntimeException("No se pudo inicializar el sistema.");
            }
        }
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/main.fxml"));
        Scene scene = new Scene(root, 1200, 800);
        stage.setTitle("SchoolBase - Sistema de Gestión Educativa");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}