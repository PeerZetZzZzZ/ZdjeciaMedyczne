/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.admin;

import controller.Window;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.StageMaster;

/**
 * FXML Controller class
 *
 * @author peer
 */
public class MainWindowAdminController extends Window {

    @FXML
    private Button manageUsersButton;
    @FXML
    private BorderPane mainWindowAdminBorderPane;

    /**
     * Initializes the controller class.
     */
    public MainWindowAdminController() {
        super();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        super.initialize(url, rb);
        this.stage = (Stage) this.manageUsersButton.getScene().getWindow();
        initButtons();

    }

    private void initButtons() {
        manageUsersButton.setOnAction((event) -> {
            showManageUserWindow();
        });
    }

    private void showManageUserWindow() {
        Parent manageUsersRoot = StageMaster.master.getRoot("admin/ManageUsers.fxml");
        if (manageUsersRoot != null) {
            this.mainWindowAdminBorderPane.setCenter(manageUsersRoot);
        }
    }

    public BorderPane getBorderPane() {
        return mainWindowAdminBorderPane;
    }

}
