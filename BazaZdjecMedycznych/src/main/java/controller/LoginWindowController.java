package controller;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.Common;
import model.ResourceBundleMaster;
import model.login.LoginProvider;

/**
 * FXML Controller class
 *
 * @author peer
 */
public class LoginWindowController extends Window {

    @FXML
    private Button loginButton;
    @FXML
    private TextField usernameTextField;
    @FXML
    private TextField passwordTextField;
    @FXML
    private Label errorLabel;
    @FXML
    private BorderPane loginWindowBorderPane;
    /**
     * Initializes the controller class.
     */
    private LoginProvider loginProvider;

    public LoginWindowController() {

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loginProvider = new LoginProvider();
        loginButton.setOnAction((event) -> {
            loginToDatabase();
        });

    }

    private void loginToDatabase() {
        try {
            boolean flag = true;
            String resultOfLoging = loginProvider.connectToDatabase(usernameTextField.getText(), passwordTextField.getText());
            if (resultOfLoging.equals("Successful root")) {
                errorLabel.setText(ResourceBundleMaster.TRANSLATOR.getTranslation("databaseSchemaCreated"));
                flag = false;
            } else if (resultOfLoging.contains("Successful")) {
                Stage loginWindow = (Stage) this.loginWindowBorderPane.getScene().getWindow();
                Common.COMMON.setLoggedUser(this.usernameTextField.getText());
                String userTyp = resultOfLoging.substring(11, resultOfLoging.length());
                showSpecifiedWindow(userTyp);
                closeWindow();
            }
            clearTextFields();
            if (flag) {
                errorLabel.setText(ResourceBundleMaster.TRANSLATOR.getTranslation("unsuccessfulLoginMessage"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(LoginWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void clearTextFields() {
        usernameTextField.setText("");
        passwordTextField.setText("");
    }

    private void closeWindow() {
        Stage stage = (Stage) usernameTextField.getScene().getWindow();
        stage.close();
    }

    private void showSpecifiedWindow(String userTyp) {
        switch (userTyp) {
            case "DOCTOR":
                showWindow("doctor/MainWindowDoctor.fxml");
                break;
            case "PATIENT":
                showWindow("patient/MainWindowPatient.fxml");
                break;
            case "TECHNICIAN":
                showWindow("technician/MainWindowTechnician.fxml");
                break;
            case "ADMIN":
                showWindow("admin/MainWindowAdmin.fxml");
                break;
        }
    }
}
