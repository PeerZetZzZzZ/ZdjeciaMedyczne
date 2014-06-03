package controller.technician;

import controller.Window;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.ResourceBundleMaster;

public class MainWindowTechnicianController extends Window {
  
    @FXML
    private Button picturesCatalogButton;
    @FXML
    private BorderPane borderPaneMiddle;
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
               // showWindow("technician/PicturesCatalog.fxml");
               try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/technician/PicturesCatalog.fxml"),ResourceBundleMaster.translator.getResourceBundle());
            Scene scene = new Scene(root);
            scene.getStylesheets().add("/styles/Styles.css");
//            borderPaneMiddle=
           
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            System.err.println("showWindow: Error while showing the window");
            Logger.getLogger(Window.class.getName()).log(Level.SEVERE, null, ex);
        }
        });
    }
        
    
}
