package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

public class MainWindowTechnician implements Initializable {
    
    @FXML
    private Label label;
    
    @FXML
    private void handleButtonAction(ActionEvent event) {
<<<<<<< HEAD:BazaZdjecMedycznych/src/main/java/controller/MainWindowTechnician.java
        System.out.println("You clicked me fucker !");
=======
        System.out.println("You clicked me fat motherfucker2!");
>>>>>>> master:BazaZdjecMedycznych/src/main/java/controller/MainWindowTechnician.java
        label.setText("Hello World!");
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
}
