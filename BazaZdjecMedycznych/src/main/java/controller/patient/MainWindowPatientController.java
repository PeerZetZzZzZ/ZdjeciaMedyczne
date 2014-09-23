/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.patient;

import controller.Window;
import controller.admin.MainWindowAdminController;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Common;
import model.ResourceBundleMaster;
import model.db.DBConnector;
import model.db.DBPatientManager;
import model.file.FileDeliver;

/**
 * FXML Controller class
 *
 * @author PeerZet
 */
public class MainWindowPatientController extends Window {

    /**
     * Initializes the controller class.
     */
    @FXML
    Label labelName;
    @FXML
    Label labelSurname;
    @FXML
    Label labelAge;
    @FXML
    Label labelDoctorName;
    @FXML
    Label labelPictureDate;
    @FXML
    Label labelInfo;
    @FXML
    Label labelBodyPart;
    @FXML
    Label labelTechnicianName;
    @FXML
    Button buttonClose;
    @FXML
    ImageView imageViewPicture;
    @FXML
    Slider sliderPicture;
    @FXML
    Slider sliderDiagnosis;
    @FXML
    TextArea textAreaPictureDescription;
    @FXML
    Button buttonSavePicture;
    @FXML
    TextArea textAreaDiagnosis;
    @FXML
    Label labelLoggedAs;
    Stage savePictureStage = new Stage();
    FileChooser fileChooser = new FileChooser();
    HashMap<String, byte[]> pictureList = new HashMap<>();
    HashMap<String, HashMap<String, String>> picturesDescriptionMap = new HashMap<>();
    DBPatientManager patientManager = new DBPatientManager();
    String username = Common.COMMON.getLoggedUser();
    int pictureIndex = 0;
    FileDeliver fileDeliver = new FileDeliver();
    String pictureName = "";
    HashMap<Integer, HashMap<String, String>> diagnosisMap = new HashMap<>();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initButtons();
        try {
            fillUserInfo();
            readAllPictures();
            readAllPicturesDescription();
            readAllDiagnosis();
            initSliders();
            labelLoggedAs.setText(labelLoggedAs.getText() + " " + Common.COMMON.getLoggedUser());

        } catch (SQLException | IOException ex) {
            this.labelInfo.setText(ResourceBundleMaster.TRANSLATOR.getTranslation("internalError"));
            Logger.getLogger(MainWindowPatientController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void initButtons() {
        buttonClose.setOnAction(event -> {
            logoutAndClose();
        });
        buttonSavePicture.setOnAction(event -> {
            try {
                savePicture();
            } catch (IOException ex) {
                Logger.getLogger(MainWindowPatientController.class.getName()).log(Level.SEVERE, null, ex);
            }

        });
    }

    private void fillUserInfo() throws SQLException {
        HashMap<String, String> userInfo = patientManager.getPatientInfo(username);
        this.labelName.setText(userInfo.get("name"));
        this.labelSurname.setText(userInfo.get("surname"));
        this.labelAge.setText(userInfo.get("age"));
    }

    private void showPicture() throws IOException {
        if (!pictureName.isEmpty()) {
            Image image = new Image(new ByteArrayInputStream(pictureList.get(pictureName)));
            this.imageViewPicture.setImage(image);
        }

    }

    private void showPictureDescription(int index) throws SQLException {
        int i = 0;
        HashMap<String, String> singlePicture = null;
        for (HashMap<String, String> pic : picturesDescriptionMap.values()) {
            if (i == index) {
                singlePicture = pic;
                break;
            }
            i++;
        }
        String doctor_name = singlePicture.get("doctor");
        String techician_name = singlePicture.get("technician");
        String capture_datetime = singlePicture.get("capture_datetime");
        String picture_description = singlePicture.get("picture_description");
        String body_part = singlePicture.get("body_part");
        pictureName = singlePicture.get("picture_name");
        this.labelDoctorName.setText(doctor_name);
        this.labelTechnicianName.setText(techician_name);
        this.labelPictureDate.setText(capture_datetime);
        this.textAreaPictureDescription.setText(picture_description);
        this.labelBodyPart.setText(body_part);
    }

    private void readAllPictures() throws SQLException {
        pictureList = patientManager.getPicturesData(username);
    }

    private void readAllPicturesDescription() throws SQLException {
        picturesDescriptionMap = patientManager.getPicturesDescription(username);
    }

    private void initSliders() throws IOException, SQLException {
        if (pictureList.size() > 0) {
            sliderPicture.setMax(pictureList.size() - 1);
        }
        if (!diagnosisMap.isEmpty()) {
            sliderDiagnosis.setMax(diagnosisMap.size() - 1);
        }
        sliderPicture.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                    Number old_val, Number new_val) {
                int index = new_val.intValue();
                try {
                    showPictureDescription(index);
                    showPicture();
                    pictureIndex = index;
                } catch (IOException ex) {
                    Logger.getLogger(MainWindowPatientController.class.getName()).log(Level.SEVERE, null, ex);
                    labelInfo.setText(ResourceBundleMaster.TRANSLATOR.getTranslation("internalError"));
                } catch (SQLException ex) {
                    Logger.getLogger(MainWindowPatientController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        if (pictureList.size() > 0) {
            showPictureDescription(0);
            showPicture();
        }
        if (!diagnosisMap.isEmpty()) {
            showDiagnosis(0);
        }
        sliderDiagnosis.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                    Number old_val, Number new_val) {
                int index = new_val.intValue();
                showDiagnosis(index);
            }

        });
    }

    private void savePicture() throws IOException {
        int i = 0;
        HashMap<String, String> fileMap = null;
        for (HashMap<String, String> image : picturesDescriptionMap.values()) {
            if (i == pictureIndex) {
                fileMap = image;
                break;
            }
            i++;
        }
        byte[] pictureData = pictureList.get(pictureName);
        if (fileMap != null) {
            fileChooser.setInitialFileName(fileMap.get("picture_name"));
            File path = fileChooser.showSaveDialog(savePictureStage);
            if (!path.isDirectory()) {
                FileDeliver deliver = new FileDeliver();
                if (deliver.verifyFileExtension(path.getAbsolutePath())) {
                    deliver.saveFile(path.getAbsolutePath(), pictureData);
                }
            }
        }
    }

    private void readAllDiagnosis() throws SQLException {
        diagnosisMap = patientManager.getAllDiagnosis(Common.COMMON.getLoggedUser());
    }

    private void showDiagnosis(int index) {
        if (!diagnosisMap.isEmpty()) {
            HashMap<String, String> diagnosis = diagnosisMap.get(index);
            String doctor = diagnosis.get("doctor");
            String description = diagnosis.get("description");
            this.textAreaDiagnosis.setText(ResourceBundleMaster.TRANSLATOR.getTranslation("assessmentBy") + " " + doctor + ": " + description);
        }
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
