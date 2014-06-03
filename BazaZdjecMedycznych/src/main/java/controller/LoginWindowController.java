package controller;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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
        String resultOfLoging=loginProvider.connectToDatabase(usernameTextField.getText(),passwordTextField.getText());
        if(resultOfLoging.equals("Successful"))
            
        errorLabel.setText(loginProvider.connectToDatabase(usernameTextField.getText(),passwordTextField.getText()));
    }

}

