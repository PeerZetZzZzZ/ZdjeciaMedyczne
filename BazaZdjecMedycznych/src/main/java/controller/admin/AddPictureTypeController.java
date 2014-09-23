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
import model.tableentries.PictureTypeEntry;

/**
 * FXML Controller class
 *
 * @author peer
 */
public class AddPictureTypeController extends Window {

    /**
     * Initializes the controller class.
     */
    @FXML
    Button buttonAddPictureType;
    @FXML
    TextField textFieldPictureType;
    @FXML
    Button buttonDeletePictureType;
    @FXML
    Button buttonMarkAll;
    @FXML
    Button buttonUnmarkAll;
    @FXML
    Button buttonRefresh;
    @FXML
    TableColumn tableColumnPictureType;
    @FXML
    TableView tableViewPictureType;
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
        for (String pictureType : managerCommon.getPictureTypes()) {
            data.add(new PictureTypeEntry(pictureType, false));
        }
        if (flag) {
            tableViewPictureType.setItems(data);
            tableViewPictureType.setEditable(true);
            flag = false;
        }
    }

    private void linkTableColumns() {
        tableColumnPictureType.setCellValueFactory(new PropertyValueFactory<PictureTypeEntry, String>("pictureType"));
        tableColumnCheckbox.setCellValueFactory(new PropertyValueFactory<PictureTypeEntry, Boolean>("selected"));
        tableColumnCheckbox.setCellFactory(new Callback<TableColumn<PictureTypeEntry, Boolean>, TableCell<PictureTypeEntry, Boolean>>() {

            @Override
            public TableCell<PictureTypeEntry, Boolean> call(TableColumn<PictureTypeEntry, Boolean> param) {
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
        buttonAddPictureType.setOnAction((event) -> {
            try {
                String pictureType = this.textFieldPictureType.getText();
                checker.verifySingleWord(pictureType);
                managerCommon.addPictureType(pictureType);
                data.add(new PictureTypeEntry(pictureType, false));
                this.labelInfo.setText(ResourceBundleMaster.TRANSLATOR.getTranslation("pictureTypeAdded"));

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
                for (Object picture : data) {
                    PictureTypeEntry picturePartEntry = (PictureTypeEntry) picture;
                    picturePartEntry.selected.setValue(Boolean.TRUE);
                }
            }
        });
        buttonUnmarkAll.setOnAction(event -> {
            if (!data.isEmpty()) {
                for (Object picture : data) {
                    PictureTypeEntry pictureEntry = (PictureTypeEntry) picture;
                    pictureEntry.selected.setValue(Boolean.FALSE);
                }
            }
        });
        buttonDeletePictureType.setOnAction(event -> {
            if (!data.isEmpty()) {
                List<String> pictureTypesToDelete = new ArrayList<String>();
                for (Object pictureType : data) {
                    PictureTypeEntry pictureTyp = (PictureTypeEntry) pictureType;
                    if (pictureTyp.getSelected()) {
                        pictureTypesToDelete.add(pictureTyp.getPictureType());//if they have checkbox = true we will delete them
                    }

                }
                try {
                    if (!pictureTypesToDelete.isEmpty()) {
                        managerCommon.deletePictureType(pictureTypesToDelete);
                        fillBodyPartsTable();
                        this.labelInfo.setText(ResourceBundleMaster.TRANSLATOR.getTranslation("pictureTypeDeleted"));
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
        Stage stage = (Stage) this.buttonDeletePictureType.getScene().getWindow();
        stage.close();//tu jest nadal problem bo nie wiem skat tego stage wziac
    }

    public void setWindowTitle(String title) {
        Stage stage = (Stage) this.buttonDeletePictureType.getScene().getWindow();
        stage.setTitle(title);
    }

}
