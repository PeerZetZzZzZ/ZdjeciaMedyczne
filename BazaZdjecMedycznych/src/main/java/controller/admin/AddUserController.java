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
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import model.db.DBUsersManager;
import model.enums.UserType;

/**
 * FXML Controller class
 *
 * @author peer
 */
public class AddUserController extends Window {

    @FXML
    private ComboBox accountTypeCombobox;

    @FXML
    private Button createUserButton;
    
    @FXML
    private TextField usernameTextField;
    
    @FXML
    private TextField passwordTextField;
    private DBUsersManager usersMaster = new DBUsersManager();

    private String username;
    private String password;
    private UserType usertype;

    public AddUserController() {
        super();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        fillAccountType();
        // TODO
    }

    private void fillAccountType() {

        accountTypeCombobox.getItems().addAll(UserType.values());
    }

    private void initButtons() {
        createUserButton.setOnAction(event -> {
            addNewUser();
        });
    }

    private void addNewUser() {
        usersMaster.createUserInApplication(null, null, UserType.ADMIN);
    }

    private void readUser() {
        if(usernameTextField.getText()!=null){
            
        }
    }
}


