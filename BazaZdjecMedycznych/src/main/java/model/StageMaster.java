/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.IOException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author peer
 */
public class StageMaster {
    public static StageMaster master = new StageMaster();
    ResourceBundle resourceBundle = ResourceBundleMaster.TRANSLATOR.getResourceBundle();
    
    private StageMaster(){
        
    }
    public Stage createWindow(String windowName) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/" + windowName), resourceBundle);
            Scene scene = new Scene(root);
            scene.getStylesheets().add("/styles/Styles.css");
            Stage stage = new Stage();
            stage.setTitle("JavaFX and Maven");
            stage.setScene(scene);
            
            return stage;
        } catch (IOException ex) {
            Logger.getLogger(StageMaster.class.getName()).log(Level.SEVERE, null, ex);
            return new Stage();
        }
    }
    public Parent getRoot(String windowName){
        try {
            return FXMLLoader.load(getClass().getResource("/fxml/" + windowName), resourceBundle);
        } catch (IOException ex) {
            Logger.getLogger(StageMaster.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        
    }
}
