/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author peer
 */
public class Window implements Initializable {
    private ResourceBundle resourceBundle;
    private String resourceBundleName="messages_EN";

    public Window(){
           resourceBundle = ResourceBundle.getBundle(resourceBundleName);
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
    
    protected void showWindow(String windowName) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/"+windowName),resourceBundle);

        Scene scene = new Scene(root);
        scene.getStylesheets().add("/styles/Styles.css");
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }
    
    
}
