package controller.technician;

import controller.Window;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import model.StageMaster;

public class MainWindowTechnicianController extends Window {

    @FXML
    private Button picturesCatalogButton;
    @FXML
    private BorderPane borderPaneMainWindowTechnician;
    PicturesCatalogController picturesController = new PicturesCatalogController();

    public MainWindowTechnicianController() {
        super();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        initButtons();

    }

    private void initButtons() {
        picturesCatalogButton.setOnAction((event) -> {
            fillMiddle();
        });
    }

    private void fillMiddle() {
        Parent picturesCatalogRoot = StageMaster.master.getRoot("technician/PicturesCatalog.fxml");
        if (picturesCatalogRoot != null) {
            this.borderPaneMainWindowTechnician.setCenter(picturesCatalogRoot);
        }
        //picturesCatalogStage.show();
    }

}
