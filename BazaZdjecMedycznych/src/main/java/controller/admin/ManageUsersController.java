/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.admin;

import controller.Window;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import model.ResourceBundleMaster;
import model.StageMaster;
import model.db.DBUsersManager;
import model.tableentries.UserEntry;

/**
 * FXML Controller class
 *
 * @author peer
 */
public class ManageUsersController extends Window {

    /**
     * Initializes the controller class.
     */
    @FXML
    private TableView usersTableView;
    @FXML
    private TableColumn idTableColumn;
    @FXML
    private TableColumn usernameTableColumn;
    @FXML
    private TableColumn passwordTableColumn;
    @FXML
    private TableColumn accountTypeTableColumn;
    @FXML
    private Button addUserButton;
    @FXML
    private BorderPane manageUsersBorderPane;
    private DBUsersManager usersMaster = new DBUsersManager();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        rb=ResourceBundleMaster.TRANSLATOR.getResourceBundle();
        initButtons();
        linkTableColumns();//after it we will can paste UserEntry to the table
        fillUsersTable();
    }

    private void fillUsersTable() {
        usersMaster.updateResultSet();
        ObservableList data = FXCollections.observableArrayList();
        HashMap<Integer, String> usernames = usersMaster.readUsernames();
        HashMap<Integer, String> passwords = usersMaster.readPasswords();
        HashMap<Integer, String> accountTypes = usersMaster.readAccountTypes();
        for (int i = 1; i < usernames.size() + 1; i++) { //from 1 because we use id in table from 1
            String username = usernames.get(i);
            String password = passwords.get(i);
            String accountType = accountTypes.get(i);
            data.add(new UserEntry(i, username, password, accountType));
        }
        usersTableView.setItems(data);
    }

    private void linkTableColumns() {
        idTableColumn.setCellValueFactory(new PropertyValueFactory<UserEntry, Integer>("id"));
        usernameTableColumn.setCellValueFactory(new PropertyValueFactory<UserEntry, String>("username"));
        passwordTableColumn.setCellValueFactory(new PropertyValueFactory<UserEntry, String>("password"));
        accountTypeTableColumn.setCellValueFactory(new PropertyValueFactory<UserEntry, String>("accountType"));
    }

    private void initButtons() {
        addUserButton.setOnAction((event) -> {
            Parent manageUsersRoot = StageMaster.master.getRoot("admin/AddUser.fxml");
            if (manageUsersRoot != null) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                Parent root = StageMaster.master.getRoot("admin/MainWindowAdmin.fxml");
//                try {
//                    Pane p = fxmlLoader.load((getClass().getResource("/fxml/admin/MainWindowAdmin.fxml"),ResourceBundleMaster.translator.getResourceBundle()instanceof));
//                    
//                } catch (IOException ex) {
//                    Logger.getLogger(ManageUsersController.class.getName()).log(Level.SEVERE, null, ex);
//                }
//                MainWindowAdminController mainWindowAdminController = fxmlLoader.<MainWindowAdminController>getController();
//                mainWindowAdminController.getBorderPane().setCenter(manageUsersRoot);
                showWindow("admin/AddUser.fxml");
                

            }
        }
        );
    }
}
