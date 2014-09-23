/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.admin;

import controller.Window;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.ResourceBundleMaster;
import model.db.DBManagerCommon;
import model.exception.RegexException;
import model.regex.RegexPatternChecker;
import model.tableentries.BodyPartEntry;

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
    Button buttonAddBodyPart;
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
    TableColumn tableColumnCheckbox;
    @FXML
    Label labelInfo;
    RegexPatternChecker checker = new RegexPatternChecker();
    private DBManagerCommon managerCommon = new DBManagerCommon();
    ObservableList data = FXCollections.observableArrayList();
    boolean flag = true;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        rb = ResourceBundleMaster.TRANSLATOR.getResourceBundle();
        initButtons();
        linkTableColumns();//after it we will can paste UserEntry to the table
        try {
            fillBodyPartsTable();
        } catch (SQLException ex) {
            Logger.getLogger(AddBodyPartController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void fillBodyPartsTable() throws SQLException {
        data.removeAll(data);
        linkTableColumns();
        for (String bodyPart : managerCommon.getBodyParts()) {
            data.add(new BodyPartEntry(bodyPart, false));
        }
        if (flag) {
            tableViewBodyPart.setItems(data);
            tableViewBodyPart.setEditable(true);
            flag = false;
        }
    }

    private void linkTableColumns() {
        tableColumnBodyPart.setCellValueFactory(new PropertyValueFactory<BodyPartEntry, String>("bodyPart"));
        tableColumnCheckbox.setCellValueFactory(new PropertyValueFactory<BodyPartEntry, Boolean>("selected"));
        tableColumnCheckbox.setCellFactory(new Callback<TableColumn<BodyPartEntry, Boolean>, TableCell<BodyPartEntry, Boolean>>() {

            @Override
            public TableCell<BodyPartEntry, Boolean> call(TableColumn<BodyPartEntry, Boolean> param) {
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
        buttonAddBodyPart.setOnAction((event) -> {
            try {
                String bodyPart = this.textFieldBodyPart.getText();
                checker.verifySingleWord(bodyPart);
                managerCommon.addBodyPart(bodyPart);
                data.add(new BodyPartEntry(bodyPart, false));
                this.labelInfo.setText(ResourceBundleMaster.TRANSLATOR.getTranslation("bodyPartAdded"));
                
            } catch (RegexException ex) {
                Logger.getLogger(AddBodyPartController.class.getName()).log(Level.SEVERE, null, ex);
                this.labelInfo.setText(ResourceBundleMaster.TRANSLATOR.getTranslation("illegalSingleWord"));
            } catch (SQLException ex) {
                Logger.getLogger(AddBodyPartController.class.getName()).log(Level.SEVERE, null, ex);
                this.labelInfo.setText(ResourceBundleMaster.TRANSLATOR.getTranslation("internalError"));
            }
        }
        );
        buttonMarkAll.setOnAction(event -> {
            if (!data.isEmpty()) {
                for (Object body : data) {
                    BodyPartEntry bodyEntry = (BodyPartEntry) body;
                    bodyEntry.selected.setValue(Boolean.TRUE);
                }
            }
        });
        buttonUnmarkAll.setOnAction(event -> {
            if (!data.isEmpty()) {
                for (Object body : data) {
                    BodyPartEntry bodyEntry = (BodyPartEntry) body;
                    bodyEntry.selected.setValue(Boolean.FALSE);
                }
            }
        });
        buttonDeleteBodyPart.setOnAction(event -> {
            if (!data.isEmpty()) {
                List<String> bodyPartsToDelete = new ArrayList<String>();
                for (Object bodyPart : data) {
                    BodyPartEntry bodyPar = (BodyPartEntry) bodyPart;
                    if (bodyPar.getSelected()) {
                        bodyPartsToDelete.add(bodyPar.getBodyPart());//if they have checkbox = true we will delete them
                    }

                }
                try {
                    if (!bodyPartsToDelete.isEmpty()) {
                        managerCommon.deleteBodyParts(bodyPartsToDelete);
                        fillBodyPartsTable();
                        this.labelInfo.setText(ResourceBundleMaster.TRANSLATOR.getTranslation("bodyPartDeleted"));
                    }

                } catch (SQLException ex) {
                    Logger.getLogger(ManageUsersController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        buttonRefresh.setOnAction(event -> {
            try {
                fillBodyPartsTable();
            } catch (SQLException ex) {
                Logger.getLogger(AddBodyPartController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    protected void closeWindow() {
        Stage stage = (Stage) this.buttonDeleteBodyPart.getScene().getWindow();
        stage.close();//tu jest nadal problem bo nie wiem skat tego stage wziac
    }

    public void setWindowTitle(String title) {
        Stage stage = (Stage) this.buttonDeleteBodyPart.getScene().getWindow();
        stage.setTitle(title);
    }

}
