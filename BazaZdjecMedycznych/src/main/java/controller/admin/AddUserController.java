/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.admin;

import controller.Window;
import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.ResourceBundleMaster;
import model.db.DBUsersManager;
import model.enums.GenderType;
import model.enums.UserType;
import model.exception.RegexException;

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
    @FXML
    private TextField ageTextField;
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField surnameTextField;
    @FXML
    private TextField specializationTextField;
    @FXML
    private Label specializationLabel;
    @FXML
    private Label infoLabel;
    @FXML
    private ComboBox genderCombobox;
    @FXML
    private Button closeButton;
    private DBUsersManager usersMaster = new DBUsersManager();

    private String username;
    private String password;
    private UserType usertype;
    boolean userEdition = false;//if it's true, we should not insert user but edit him ;)

    public AddUserController() {
        super();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        fillAccountType();
        initButtons();
        this.specializationLabel.setVisible(false);
        this.specializationTextField.setVisible(false);

        nameTextField.setText("Marek");
        surnameTextField.setText("Kulomb");
        ageTextField.setText("15");
        usernameTextField.setText("mar4");
        passwordTextField.setText("aaa2");

    }

    private void fillAccountType() {

        accountTypeCombobox.getItems().addAll(UserType.values());
        genderCombobox.getItems().addAll(GenderType.values());
        accountTypeCombobox.valueProperty().addListener(event -> {
            showSpecilization();
        });
    }

    private void initButtons() {
        createUserButton.setOnAction(event -> {
            addNewUser();
        });
        closeButton.setOnAction(event -> {
            closeWindow();
        });
    }

    /**
     * Checks if it should show specilization editText and label, only if
     * combobox has value doctor or technician
     */
    private void showSpecilization() {
        if ((accountTypeCombobox.getValue().toString().equals(UserType.DOCTOR.toString()))) {
            specializationTextField.setVisible(true);
            specializationLabel.setVisible(true);
        } else {
            specializationLabel.setVisible(false);
            specializationTextField.setVisible(false);
        }
    }

    private void addNewUser() {
        try {
            if (genderCombobox.getValue() != null && accountTypeCombobox.getValue() != null) {
                if (!userEdition) {
                    usersMaster.createUser(nameTextField.getText(), surnameTextField.getText(), genderCombobox.getValue().toString(),
                            ageTextField.getText(), usernameTextField.getText(), passwordTextField.getText(), specializationTextField.getText(),
                            accountTypeCombobox.getValue().toString());
                    infoLabel.setText("User " + usernameTextField.getText() + " added!");
                } else {
                    usersMaster.editUser(usernameTextField.getText(), nameTextField.getText(), surnameTextField.getText(), ageTextField.getText(), genderCombobox.getValue().toString(),
                            specializationTextField.getText(),
                            accountTypeCombobox.getValue().toString());
                    infoLabel.setText("User " + usernameTextField.getText() + " edited!");
                }
            } else {
                infoLabel.setText(ResourceBundleMaster.TRANSLATOR.getTranslation("provideValues"));
            }
        } catch (SQLException ex) {
            this.infoLabel.setText(ex.getMessage());
            Logger.getLogger(AddUserController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RegexException ex) {
            this.infoLabel.setText(ex.getMessage());
            Logger.getLogger(AddUserController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void readUser() {
        if (usernameTextField.getText() != null) {

        }
    }

    public void fillValuesWithUser(String username) {
        try {
            userEdition = true;
            HashMap<String, String> user = usersMaster.getUserValues(username);
            this.nameTextField.setText(user.get("name"));
            this.surnameTextField.setText(user.get("surname"));
            this.genderCombobox.setValue(user.get("sex"));
            this.ageTextField.setText(user.get("age"));
            this.usernameTextField.setText(user.get("username"));
            this.usernameTextField.setEditable(false);
            this.passwordTextField.setText(user.get("password"));
            String accountType = user.get("account_type");
            this.accountTypeCombobox.setValue(accountType);
            if (accountType.equals("DOCTOR")) {
                this.specializationTextField.setText(user.get("specialization"));
                this.specializationTextField.setVisible(true);
            }
            this.createUserButton.setText(ResourceBundleMaster.TRANSLATOR.getTranslation("saveUser"));
        } catch (SQLException ex) {
            Logger.getLogger(AddUserController.class.getName()).log(Level.SEVERE, null, ex);
            closeWindow();
        }
    }

    protected void closeWindow() {
        Stage stage = (Stage) this.closeButton.getScene().getWindow();
        stage.close();//tu jest nadal problem bo nie wiem skat tego stage wziac
    }

    public void setWindowTitle(String title) {
        Stage stage = (Stage) this.closeButton.getScene().getWindow();
        stage.setTitle(title);
    }

    public Stage getStage() {
        return (Stage) this.closeButton.getScene().getWindow();
    }
}
