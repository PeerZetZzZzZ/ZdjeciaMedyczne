/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.admin;

import controller.Window;
import controller.technician.AddPictureController;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.Common;
import model.ResourceBundleMaster;
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
    private TableColumn usernameTableColumn;
    @FXML
    private TableColumn passwordTableColumn;
    @FXML
    private TableColumn accountTypeTableColumn;
    @FXML
    private Button addUserButton;
    @FXML
    private BorderPane manageUsersBorderPane;
    @FXML
    private TableColumn checkboxTableColumn;
    @FXML
    private TableColumn buttonTableColumn;
    @FXML
    private TableColumn tableColumnManagePictures;
    @FXML
    private Button markAllButton;
    @FXML
    private Button deleteUserButton;
    @FXML
    private Button unmarkAllButton;
 
    private DBUsersManager usersMaster = new DBUsersManager();
    ObservableList data = FXCollections.observableArrayList();

    @Override

    public void initialize(URL url, ResourceBundle rb) {
        rb = ResourceBundleMaster.TRANSLATOR.getResourceBundle();

        initButtons();
        linkTableColumns();//after it we will can paste UserEntry to the table
        fillUsersTable();
    }

    private void fillUsersTable() {
        usersMaster.updateResultSet();//reading all users
        data.clear();
        HashMap<String, String> usernames = usersMaster.readUsernames();
        HashMap<String, String> passwords = usersMaster.readPasswords();
        HashMap<String, String> accountTypes = usersMaster.readAccountTypes();
        Set<String> usernameSet = usernames.keySet();
        for (String user : usernameSet) { //from 1 because we use id in table from 1
            String username = usernames.get(user);
            String password = passwords.get(user);
            String accountType = accountTypes.get(user);
            data.add(new UserEntry(username, password, accountType, false));
        }
        usersTableView.setItems(data);
        usersTableView.setEditable(true);
    }

    private void linkTableColumns() {
        usernameTableColumn.setCellValueFactory(new PropertyValueFactory<UserEntry, String>("username"));
        passwordTableColumn.setCellValueFactory(new PropertyValueFactory<UserEntry, String>("password"));
        accountTypeTableColumn.setCellValueFactory(new PropertyValueFactory<UserEntry, String>("accountType"));
        buttonTableColumn.setCellValueFactory(new PropertyValueFactory<UserEntry, String>("username"));
        buttonTableColumn.setCellFactory(new Callback<TableColumn<UserEntry, String>, TableCell<UserEntry, String>>() {

            @Override
            public TableCell<UserEntry, String> call(TableColumn<UserEntry, String> param) {
                TableCell<UserEntry, String> cell = new TableCell<UserEntry, String>() {

                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        final Button button = new Button(ResourceBundleMaster.TRANSLATOR.getTranslation("edit"));

                        {
                            if (!empty) {
                                button.setMinWidth(160);
                                button.setVisible(true);
                                setGraphic(button);//kurwa mać 1h w dupie bo tego nie było
                                button.setOnAction(event -> {
                                    AddUserController controller = (AddUserController) showWindow("admin/AddUser.fxml");
                                    controller.fillValuesWithUser(item);
                                    controller.getStage().setOnCloseRequest(close ->{
                                        fillUsersTable();
                                    });

                                });
                            }
                        }

                    }

                };
                cell.setAlignment(Pos.CENTER);
                return cell;
            }

        });
        tableColumnManagePictures.setCellValueFactory(new PropertyValueFactory<UserEntry, String>("username"));
        tableColumnManagePictures.setCellFactory(new Callback<TableColumn<UserEntry, String>, TableCell<UserEntry, String>>() {

            @Override
            public TableCell<UserEntry, String> call(TableColumn<UserEntry, String> param) {
                TableCell<UserEntry, String> cell = new TableCell<UserEntry, String>() {

                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        final Button button = new Button(ResourceBundleMaster.TRANSLATOR.getTranslation("managePictures"));

                        {
                            if (!empty) {
                                button.setMinWidth(160);
                                button.setVisible(true);
                                setGraphic(button);//kurwa mać 1h w dupie bo tego nie było
                                button.setOnAction(event -> {
                                    Common.COMMON.setUsernameOfPictures(item);
                                    AddPictureController controller = (AddPictureController) showWindow("technician/AddPicture.fxml");
                                });
                            }
                        }

                    }

                };
                cell.setAlignment(Pos.CENTER);
                return cell;
            }

        });
        checkboxTableColumn.setCellValueFactory(new PropertyValueFactory<UserEntry, Boolean>("selected"));
        checkboxTableColumn.setCellFactory(new Callback<TableColumn<UserEntry, Boolean>, TableCell<UserEntry, Boolean>>() {

            @Override
            public TableCell<UserEntry, Boolean> call(TableColumn<UserEntry, Boolean> param) {
                CheckBoxTableCell cell = new CheckBoxTableCell() {

                    @Override
                    public void updateItem(Object item, boolean empty) {
                        if (!empty) {
                            super.updateItem(item, empty); //To change body of generated methods, choose Tools | Templates.
                        }
//                        System.out.println(empty);
                    }

                };
//                cell.setAlignment(Pos.CENTER);
                return cell;
            }
        });
    }

    private void initButtons() {
        addUserButton.setOnAction((event) -> {
            showWindow("admin/AddUser.fxml");
        }
        );
        markAllButton.setOnAction(event -> {
            if (!data.isEmpty()) {
                for (Object user : data) {
                    UserEntry userEntry = (UserEntry) user;
                    userEntry.selected.setValue(Boolean.TRUE);
                }
            }
        });
        unmarkAllButton.setOnAction(event -> {
            if (!data.isEmpty()) {
                for (Object user : data) {
                    UserEntry userEntry = (UserEntry) user;
                    userEntry.selected.setValue(Boolean.FALSE);
                }
            }
        });
        deleteUserButton.setOnAction(event -> {
            if (!data.isEmpty()) {
                List<String> usersToDelete = new ArrayList<String>();
                for (Object user : data) {
                    UserEntry userEntry = (UserEntry) user;
                    if (userEntry.getSelected()) {
                        usersToDelete.add(userEntry.getUsername());//if they have checkbox = true we will delete them
                    }

                }
                try {
                    if (!usersToDelete.isEmpty()) {
                        usersMaster.deleteUsers(usersToDelete);
                        fillUsersTable();
                    }

                } catch (SQLException ex) {
                    Logger.getLogger(ManageUsersController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    protected void closeWindow() {
        Stage stage = (Stage) this.deleteUserButton.getScene().getWindow();
        stage.close();//tu jest nadal problem bo nie wiem skat tego stage wziac
    }

    public void setWindowTitle(String title) {
        Stage stage = (Stage) this.deleteUserButton.getScene().getWindow();
        stage.setTitle(title);
    }

}
