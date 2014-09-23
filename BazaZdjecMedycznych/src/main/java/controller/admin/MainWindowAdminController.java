/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.admin;

import controller.Window;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.Common;
import model.ResourceBundleMaster;
import model.StageMaster;
import model.db.DBConnector;

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
    @FXML
    private Label loggedAsLabel;
    @FXML
    private Label infoLabel;
    @FXML
    private Button logoutButton;
    @FXML
    private Button managePicturesButton;
    @FXML
    private Button buttonManageBodyParts;
    @FXML
    private Button buttonManagePictureTypes;

    /**
     * Initializes the controller class.
     */
    public MainWindowAdminController() {
        super();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        super.initialize(url, rb);
        initButtons();
        loggedAsLabel.setText(loggedAsLabel.getText() + " " + Common.COMMON.getLoggedUser());

    }

    private void initButtons() {
        manageUsersButton.setOnAction((event) -> {
            showManageUserWindow();
        });

        logoutButton.setOnAction((event) -> {
            logoutAndClose();
        });
        managePicturesButton.setOnAction(event -> {
            showManagePicturesWindow();
        });
        buttonManageBodyParts.setOnAction(event -> {
            showManageBodyPartsWindow();
        });
        buttonManagePictureTypes.setOnAction(event -> {
            showManagePictureTypesWindow();
        });

    }

    private void showManageUserWindow() {
        Parent manageUsersRoot = StageMaster.master.getRoot("admin/ManageUsers.fxml");
        if (manageUsersRoot != null) {
            this.mainWindowAdminBorderPane.setCenter(manageUsersRoot);
        }
    }

    private void showManagePicturesWindow() {
        Parent manageUsersRoot = StageMaster.master.getRoot("admin/ManagePictures.fxml");
        if (manageUsersRoot != null) {
            this.mainWindowAdminBorderPane.setCenter(manageUsersRoot);
        }
    }

    private void showManageBodyPartsWindow() {
        Parent manageUsersRoot = StageMaster.master.getRoot("admin/AddBodyPart.fxml");
        if (manageUsersRoot != null) {
            this.mainWindowAdminBorderPane.setCenter(manageUsersRoot);
        }
    }
    private void showManagePictureTypesWindow() {
        Parent manageUsersRoot = StageMaster.master.getRoot("admin/AddPictureType.fxml");
        if (manageUsersRoot != null) {
            this.mainWindowAdminBorderPane.setCenter(manageUsersRoot);
        }
    }

    public BorderPane getBorderPane() {
        return mainWindowAdminBorderPane;
    }

    protected void closeWindow() {
        Stage stage = (Stage) this.manageUsersButton.getScene().getWindow();
        stage.close();//tu jest nadal problem bo nie wiem skat tego stage wziac
    }

    public void setWindowTitle(String title) {
        Stage stage = (Stage) this.manageUsersButton.getScene().getWindow();
        stage.setTitle(title);
    }

    public void logoutAndClose() {
        try {
            DBConnector.master.logout();
            showWindow("LoginWindow.fxml");
            Stage stage = (Stage) this.loggedAsLabel.getScene().getWindow();
            stage.close();
        } catch (SQLException ex) {
            Logger.getLogger(MainWindowAdminController.class.getName()).log(Level.SEVERE, null, ex);
            infoLabel.setText(ResourceBundleMaster.TRANSLATOR.getTranslation("connectionError"));
        }
    }

}
