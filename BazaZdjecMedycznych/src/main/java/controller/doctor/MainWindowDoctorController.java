/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.doctor;

import controller.Window;
import controller.admin.MainWindowAdminController;
import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.Common;
import model.ResourceBundleMaster;
import model.db.DBConnector;
import model.db.DBUsersManager;
import model.enums.UserType;
import model.tableentries.UserEntry;

/**
 * FXML Controller class
 *
 * @author PeerZet
 */
public class MainWindowDoctorController extends Window {

    /**
     * Initializes the controller class.
     */
    @FXML
    Button buttonLogout;
    @FXML
    Label labelInfo;
    @FXML
    TableColumn tableColumnPatient;
    @FXML
    TableColumn tableColumnShowPictures;
    @FXML
    TableColumn tableColumnShowDiagnosis;
    @FXML
    TableView tableView;
    @FXML
    Label labelLoggedAs;
    private DBUsersManager usersMaster = new DBUsersManager();
    ObservableList data = FXCollections.observableArrayList();
    boolean flag = true;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        usersMaster.setUserType(UserType.PATIENT);
        labelLoggedAs.setText(labelLoggedAs.getText() + " " + Common.COMMON.getLoggedUser());
        linkTableColumns();
        fillUsersTable();
        initButtons();
    }

    private void fillUsersTable() {
        data.removeAll(data);
        linkTableColumns();
        HashMap<String, String> names = usersMaster.readNames();
        System.out.println(names.size());
        Set<String> usernameSet = names.keySet();
        for (String username : usernameSet) { //from 1 because we use id in table from 1
            String name = names.get(username);
            data.add(new UserEntry(username, name));

        }
        if (flag) {
            tableView.setItems(data);
            tableView.setEditable(true);
            flag = false;
        }
    }

    private void linkTableColumns() {
        tableColumnPatient.setCellValueFactory(new PropertyValueFactory<UserEntry, String>("name"));
        tableColumnShowPictures.setCellValueFactory(new PropertyValueFactory<UserEntry, String>("username"));
        tableColumnShowDiagnosis.setCellValueFactory(new PropertyValueFactory<UserEntry, String>("username"));
        tableColumnShowPictures.setCellFactory(new Callback<TableColumn<UserEntry, String>, TableCell<UserEntry, String>>() {

            @Override
            public TableCell<UserEntry, String> call(TableColumn<UserEntry, String> param) {
                TableCell<UserEntry, String> cell = new TableCell<UserEntry, String>() {

                    @Override
                    protected void updateItem(String item, boolean empty) {
                        final Button button = new Button(ResourceBundleMaster.TRANSLATOR.getTranslation("showPictures"));
                        {
                            if (!empty) {
                                if (item != null) {
                                    super.updateItem(item, empty);
                                    button.setMinWidth(160);
                                    button.setVisible(true);
                                    setGraphic(button);//kurwa mać 1h w dupie bo tego nie było
                                    button.setOnAction(event -> {
                                        Common.COMMON.setUsernameOfPictures(item);
                                        showWindow("doctor/ShowPatientPicturesWindow.fxml");
                                    });
                                }
                            }
                        }

                    }

                };
                cell.setAlignment(Pos.CENTER);
                return cell;
            }

        });
        tableColumnShowDiagnosis.setCellFactory(new Callback<TableColumn<UserEntry, String>, TableCell<UserEntry, String>>() {

            @Override
            public TableCell<UserEntry, String> call(TableColumn<UserEntry, String> param) {
                TableCell<UserEntry, String> cell = new TableCell<UserEntry, String>() {

                    @Override
                    protected void updateItem(String item, boolean empty) {
                        final Button button = new Button(ResourceBundleMaster.TRANSLATOR.getTranslation("showDiagnosis"));
                        {
                            if (!empty) {
                                if (item != null) {
                                    super.updateItem(item, empty);
                                    button.setMinWidth(160);
                                    button.setVisible(true);
                                    setGraphic(button);//kurwa mać 1h w dupie bo tego nie było
                                    button.setOnAction(event -> {
                                        Common.COMMON.setUsernameOfPictures(item);
                                        showWindow("doctor/ShowPatientDiagnosisWindow.fxml");
                                    });
                                }
                            }
                        }

                    }

                };
                cell.setAlignment(Pos.CENTER);
                return cell;
            }

        });
    }

    private void initButtons() {
        this.buttonLogout.setOnAction(event -> {
            logoutAndClose();
        });
    }

    public void logoutAndClose() {
        try {
            DBConnector.master.logout();
            showWindow("LoginWindow.fxml");
            Stage stage = (Stage) this.labelInfo.getScene().getWindow();
            stage.close();
        } catch (SQLException ex) {
            Logger.getLogger(MainWindowAdminController.class.getName()).log(Level.SEVERE, null, ex);
            labelInfo.setText(ResourceBundleMaster.TRANSLATOR.getTranslation("connectionError"));
        }
    }
}
