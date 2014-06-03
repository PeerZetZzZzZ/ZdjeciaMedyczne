/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.ResourceBundleMaster;

/**
 *
 * @author peer
 */
public class Window implements Initializable {

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    
    }
    
    /**
     * Method is responisble for showing independent window.
     * @param windowName Name of the window to show, exactly the path to the window, without /fxml/ part
     */
    protected void showWindow(String windowName){
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/"+windowName),ResourceBundleMaster.translator.getResourceBundle());
            Scene scene = new Scene(root);
            scene.getStylesheets().add("/styles/Styles.css");
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            System.err.println("showWindow: Error while showing the window");
            Logger.getLogger(Window.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    
}
