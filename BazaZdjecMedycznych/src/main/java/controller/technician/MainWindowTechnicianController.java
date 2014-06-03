package controller.technician;

import controller.Window;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class MainWindowTechnicianController extends Window {
  
    @FXML
    private Button picturesCatalogButton;

    public MainWindowTechnicianController(){
        super();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        initButtons();

    }

    private void initButtons() {
        picturesCatalogButton.setOnAction((event) -> {
                showWindow("technician/PicturesCatalog.fxml");
        });
    }
        
    
}
