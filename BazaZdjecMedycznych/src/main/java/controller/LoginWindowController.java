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
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.Common;
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
            String resultOfLoging = loginProvider.connectToDatabase(usernameTextField.getText(), passwordTextField.getText());
            if (resultOfLoging.equals("Successful")) {
                Stage loginWindow = (Stage) this.loginWindowBorderPane.getScene().getWindow();
                Common.COMMON.setLoggedUser(this.usernameTextField.getText());
                loginWindow.hide();
                showWindow("patient/MainWindowPatient.fxml");
//                showWindow("admin/MainWindowAdmin.fxml");
//                showWindow("doctor/MainWindowDoctor.fxml");
            }
            clearTextFields();

            errorLabel.setText(resultOfLoging);
        } catch (SQLException ex) {
            Logger.getLogger(LoginWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void clearTextFields() {
        usernameTextField.setText("");
        passwordTextField.setText("");
    }
}
