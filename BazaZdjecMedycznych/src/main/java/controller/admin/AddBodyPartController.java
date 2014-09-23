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
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.Common;
import model.ResourceBundleMaster;
import model.db.DBManagerCommon;
import model.db.DBUsersManager;
import model.enums.UserType;
import model.tableentries.UserEntry;

/**
 * FXML Controller class
 *
 * @author peer
 */
public class AddBodyPartController extends Window {

    /**
     * Initializes the controller class.
     */
    @FXML
    Button addUserButton;
    @FXML
    TextField textFieldBodyPart;
    @FXML
    Button buttonDeleteBodyPart;
    @FXML
    Button buttonMarkAll;
    @FXML
    Button buttonUnmarkAll;
    @FXML
    Button buttonRefresh;
    @FXML
    TableColumn tableColumnBodyPart;
    @FXML
    TableView tableViewBodyPart;
    @FXML
    TableColumn checkboxTableColumn;

    private DBManagerCommon managerCommon = new DBManagerCommon();
    ObservableList data = FXCollections.observableArrayList();
    boolean flag = true;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        rb = ResourceBundleMaster.TRANSLATOR.getResourceBundle();
        initButtons();
        linkTableColumns();//after it we will can paste UserEntry to the table
        fillUsersTable();
    }

    private void fillUsersTable() throws SQLException {
        data.removeAll(data);
        linkTableColumns();
        data = managerCommon.getBodyParts();

        if (flag) {
            tableViewBodyPart.setItems(data);
            tableViewBodyPart.setEditable(true);
            flag = false;
        }
    }

    private void linkTableColumns() {
        tableColumnBodyPart.setCellValueFactory(new PropertyValueFactory<UserEntry, String>("username"));
  
        tableColumnCheckbox.setCellValueFactory(new PropertyValueFactory<UserEntry, Boolean>("selected"));
        tableColumnCheckbox.setCellFactory(new Callback<TableColumn<UserEntry, Boolean>, TableCell<UserEntry, Boolean>>() {

            @Override
            public TableCell<UserEntry, Boolean> call(TableColumn<UserEntry, Boolean> param) {
                CheckBoxTableCell cell = new CheckBoxTableCell() {

                    @Override
                    public void updateItem(Object item, boolean empty) {
                        if (!empty) {
                            super.updateItem(item, empty); //To change body of generated methods, choose Tools | Templates.
                        }
                    }

                };
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
                        managerCommon.deleteUsers(usersToDelete);
                        fillUsersTable();
                    }

                } catch (SQLException ex) {
                    Logger.getLogger(ManageUsersController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        buttonRefresh.setOnAction(event -> {
            fillUsersTable();
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
