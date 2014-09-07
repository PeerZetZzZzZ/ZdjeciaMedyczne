/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.admin;

import com.mysql.jdbc.RowData;
import controller.Window;
import java.awt.Checkbox;
import java.net.URL;
import java.sql.Array;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;
import model.ResourceBundleMaster;
import model.StageMaster;
import model.db.DBUsersManager;
import model.enums.UserType;
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
    private DBUsersManager usersMaster = new DBUsersManager();
    int i = 0;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        rb = ResourceBundleMaster.TRANSLATOR.getResourceBundle();
        initButtons();
        linkTableColumns();//after it we will can paste UserEntry to the table
        fillUsersTable();
    }

    private void fillUsersTable() {
        usersMaster.updateResultSet();
        ObservableList data = FXCollections.observableArrayList();
        HashMap<String, String> usernames = usersMaster.readUsernames();
        HashMap<String, String> passwords = usersMaster.readPasswords();
        HashMap<String, String> accountTypes = usersMaster.readAccountTypes();
        Set<String> usernameSet = usernames.keySet();
        for (String user : usernameSet) { //from 1 because we use id in table from 1
            String username = usernames.get(user);
            String password = passwords.get(user);
            String accountType = accountTypes.get(user);
            data.add(new UserEntry(username, password, accountType, new Boolean(true)));
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
                    final Button button = new Button(ResourceBundleMaster.TRANSLATOR.getTranslation("information"));

                    {
                        button.setMinWidth(160);
                        button.setVisible(true);
                        setGraphic(button);//kurwa mać 1h w dupie bo tego nie było
                        button.setOnAction(event -> {
                            System.out.println(param);
                            System.out.println(param.getCellData(getIndex()));
                        });
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
                CheckBoxTableCell cell = new CheckBoxTableCell(){

                    @Override
                    public void updateItem(Object item, boolean empty) {
                        super.updateItem(item, empty); //To change body of generated methods, choose Tools | Templates.
                        System.out.println("kon rafal ");
                    }
                    
                };
//                cell.setAlignment(Pos.CENTER);
                return cell;
            }
        });
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

    public class CheckBoxCell extends CheckBoxTableCell<UserType, Boolean> {

    }

}
