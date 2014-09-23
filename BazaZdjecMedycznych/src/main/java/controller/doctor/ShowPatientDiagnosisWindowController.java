/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.doctor;

import controller.Window;
import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import model.Common;
import model.ResourceBundleMaster;
import model.db.DBPatientManager;
import model.exception.RegexException;
import model.regex.RegexPatternChecker;

/**
 * FXML Controller class
 *
 * @author PeerZet
 */
public class ShowPatientDiagnosisWindowController extends Window {

    /**
     * Initializes the controller class.
     */
    @FXML
    Button buttonClose;
    @FXML
    Button buttonAdd;
    @FXML
    Button buttonDelete;
    @FXML
    Button buttonSaveDiagnosis;
    @FXML
    TextArea textAreaDiagnosis;
    @FXML
    Label labelInfo;
    @FXML
    Slider slider;
    @FXML
    Button buttonRefresh;
    DBPatientManager patientManager = new DBPatientManager();
    RegexPatternChecker matcher = new RegexPatternChecker();

    HashMap<Integer, HashMap<String, String>> diagnosisMap = new HashMap<>();
    int imageIndex = 0;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initButtons();
        try {
            readAllDiagnosis();
            initSliders();
        } catch (SQLException ex) {
            Logger.getLogger(ShowPatientDiagnosisWindowController.class.getName()).log(Level.SEVERE, null, ex);
            labelInfo.setText(ResourceBundleMaster.TRANSLATOR.getTranslation("internalError"));

        }
    }

    private void initButtons() {
        buttonClose.setOnAction(event -> {
            Stage stage = (Stage) this.buttonAdd.getScene().getWindow();
            stage.close();
        });
        buttonRefresh.setOnAction(event -> {
            try {
                readAllDiagnosis();
                initSliders();
            } catch (SQLException ex) {
                labelInfo.setText(ResourceBundleMaster.TRANSLATOR.getTranslation("internalError"));
            }
        });
        buttonDelete.setOnAction(event -> {
            try {
                deleteDiagnosis(imageIndex);
            } catch (SQLException ex) {
                Logger.getLogger(ShowPatientDiagnosisWindowController.class.getName()).log(Level.SEVERE, null, ex);
                labelInfo.setText(ResourceBundleMaster.TRANSLATOR.getTranslation("internalError"));
            }
        });
        buttonAdd.setOnAction(event -> {
            Common.COMMON.setUsernameOfPictures(Common.COMMON.getUsernameOfPictures());
            showWindow("doctor/AddDiagnosisWindow.fxml");
        });
        buttonSaveDiagnosis.setOnAction(event -> {
            try {
                updateDiagnosis(imageIndex);
            } catch (SQLException | RegexException ex) {
                Logger.getLogger(ShowPatientDiagnosisWindowController.class.getName()).log(Level.SEVERE, null, ex);
                labelInfo.setText(ResourceBundleMaster.TRANSLATOR.getTranslation("internalError"));
            }
        });
    }

    private void showDiagnosis(int index) {
        if (!diagnosisMap.isEmpty()) {
            HashMap<String, String> diagnosis = diagnosisMap.get(index);
            String description = diagnosis.get("description");
            this.textAreaDiagnosis.setText(description);
        }
    }

    private void deleteDiagnosis(int index) throws SQLException {
        if (!diagnosisMap.isEmpty()) {
            HashMap<String, String> diagnosis = diagnosisMap.get(index);
            String id = diagnosis.get("id");
            patientManager.removeDiagnosis(id);
            this.labelInfo.setText(ResourceBundleMaster.TRANSLATOR.getTranslation("diagnosisDeleted"));
        } else {
            this.labelInfo.setText(ResourceBundleMaster.TRANSLATOR.getTranslation("noDiagnosisToDelete"));
        }
        readAllDiagnosis();
        initSliders();

    }

    private void readAllDiagnosis() throws SQLException {
        diagnosisMap = patientManager.getAllDiagnosis(Common.COMMON.getUsernameOfPictures(),Common.COMMON.getLoggedUser());
    }

    private void initSliders() {
        if (!diagnosisMap.isEmpty()) {
            slider.setMax(diagnosisMap.size() - 1);
            HashMap<String, String> first = diagnosisMap.get(0);
            textAreaDiagnosis.setText(first.get("description"));
        } else {
            textAreaDiagnosis.setText(ResourceBundleMaster.TRANSLATOR.getTranslation("noDiagnosis"));
        }
        slider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                    Number old_val, Number new_val) {
                int index = new_val.intValue();
                showDiagnosis(index);
                imageIndex = index;

            }
        });
    }

    private void updateDiagnosis(int imageIndex) throws SQLException, RegexException {
        if (!diagnosisMap.isEmpty()) {
            HashMap<String, String> diagnosis = diagnosisMap.get(imageIndex);
            String id = diagnosis.get("id");
            String description = textAreaDiagnosis.getText();
            matcher.verifySingleDiagnosis(description);
            patientManager.updateDiagnosis(id, description);
            labelInfo.setText(ResourceBundleMaster.TRANSLATOR.getTranslation("diagnosisUpdated"));
        } else {
            labelInfo.setText(ResourceBundleMaster.TRANSLATOR.getTranslation("noDiagnosisToUpdate"));
        }
    }

}
