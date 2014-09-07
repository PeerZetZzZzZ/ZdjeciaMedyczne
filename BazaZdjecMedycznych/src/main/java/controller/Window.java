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
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
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
    protected Stage stage;
    private Window controller;

    /**
     * Method is responisble for showing independent window.
     *
     * @param windowName Name of the window to show, exactly the path to the
     * window, without /fxml/ part
     */
    protected Object showWindow(String windowName) {
        try {
            URL location = getClass().getResource("/fxml/" + windowName);
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(location);
            loader.setResources(ResourceBundleMaster.TRANSLATOR.getResourceBundle());
            loader.setBuilderFactory(new JavaFXBuilderFactory());
            Parent root = loader.load(location.openStream());
            Scene scene = new Scene(root);
            scene.getStylesheets().add("/styles/Styles.css");
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
            return loader.getController(); 
        } catch (IOException ex) {
            System.err.println("showWindow: Error while showing the window");
            Logger.getLogger(Window.class.getName()).log(Level.SEVERE, null, ex);
        }
        return controller;
    }

    protected void closeWindow() {
        stage.close();//tu jest nadal problem bo nie wiem skat tego stage wziac
    }

    public void setWindowTitle(String title) {
        stage.setTitle(title);
    }
}
