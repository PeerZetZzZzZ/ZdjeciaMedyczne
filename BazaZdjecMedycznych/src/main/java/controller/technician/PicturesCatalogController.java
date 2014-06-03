/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package controller.technician;

import controller.Window;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;

/**
 * FXML Controller class
 *
 * @author peer
 */
public class PicturesCatalogController extends Window {
    
    @FXML
    private BorderPane borderPanePictureCatalog;
    public PicturesCatalogController(){
        super();
    }
    public BorderPane sharePane(){
        return borderPanePictureCatalog;
    }
}
