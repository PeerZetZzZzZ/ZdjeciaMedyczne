/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.doctor;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
public class AddDiagnosisWindowController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    Label labelInfo;
    @FXML
    Button buttonClose;
    @FXML
    Button buttonSave;
    @FXML
    TextArea textAreaDiagnosis;
    DBPatientManager patientManager = new DBPatientManager();
    RegexPatternChecker matcher = new RegexPatternChecker();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initButtons();
    }

    private void initButtons() {
        buttonClose.setOnAction(event -> {
            Stage stage = (Stage) this.buttonClose.getScene().getWindow();
            stage.close();
        });
        buttonSave.setOnAction(event -> {
            try {
                labelInfo.setText("");
                matcher.verifySingleDiagnosis(this.textAreaDiagnosis.getText());
                patientManager.insertDiagnosis(this.textAreaDiagnosis.getText(), Common.COMMON.getUsernameOfPictures(),
                        Common.COMMON.getLoggedUser());
                labelInfo.setText(ResourceBundleMaster.TRANSLATOR.getTranslation("diagnosisSavedSuccessfully"));
                this.textAreaDiagnosis.clear();
            } catch (RegexException | SQLException ex) {
                Logger.getLogger(AddDiagnosisWindowController.class.getName()).log(Level.SEVERE, null, ex);
                labelInfo.setText(ResourceBundleMaster.TRANSLATOR.getTranslation("internalError"));
            }
        });
    }

}
