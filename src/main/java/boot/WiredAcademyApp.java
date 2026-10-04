package boot;

import infraestructura.persistencia.ManejadorPersistencia;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 *
 * @author inici4rsesi0n
 */
@SpringBootApplication(scanBasePackages = {"infraestructura", "aplicacion", "presentacion", "boot"})
public class WiredAcademyApp extends Application {

    private static ConfigurableApplicationContext contextoSpring;

    @Override
    public void init() {
        contextoSpring = SpringApplication.run(WiredAcademyApp.class);
    }

    @Override
    public void start(Stage stage) throws Exception {
        contextoSpring.getBean(BootstrapRunner.class).inicializar(stage);

        Parent root = FXMLLoader.load(getClass().getResource("/fxml/main.fxml"));
        Scene scene = new Scene(root, 1200, 800);
        stage.setTitle("WiredAcademy · Ecosistema Educativo Digital");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() {
        contextoSpring.close();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
