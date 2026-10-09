package presentacion.controlador;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
/**
 *
 * @author inici4rsesi0n
 */
@Component
@Scope("prototype")
public class tab_CatalogosController implements Initializable {
    @FXML private TabPane tabPaneCatalogos;
    @FXML private Tab tabAsignaturas, tabPeriodos, tabAulas, tabGrupos, tabRecreos, tabGrados, tabTurnos;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
    public TabPane getTabPane() {
        return tabPaneCatalogos;
    }
}
