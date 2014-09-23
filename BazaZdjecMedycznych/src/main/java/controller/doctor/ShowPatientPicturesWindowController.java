/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.doctor;

import controller.patient.MainWindowPatientController;
import java.io.ByteArrayInputStream;
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
import javafx.stage.Stage;
import model.Common;
import model.ResourceBundleMaster;
import model.db.DBPatientManager;
import model.exception.RegexException;
import model.regex.RegexPatternChecker;

/**
 * FXML Controller class
 *
 */
public class ShowPatientPicturesWindowController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    Slider slider;
    @FXML
    Label labelInfo;
    List<byte[]> pictureList = new ArrayList<>();
    DBPatientManager patientManager = new DBPatientManager();
    @FXML
    ImageView imageViewPicture;
    @FXML
    TextArea textAreaPictureDescription;
    @FXML
    Button buttonClose;
    @FXML
    Button buttonSave;
    @FXML
    Label labelImageName;
    HashMap<String, HashMap<String, String>> picturesDescriptionMap = new HashMap<>();
    int pictureIndex = 0;
    RegexPatternChecker checker = new RegexPatternChecker();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            readAllPictures();
            readAllPicturesDescription();
            initSliders();
            initButtons();
        } catch (SQLException ex) {
            Logger.getLogger(ShowPatientPicturesWindowController.class.getName()).log(Level.SEVERE, null, ex);
            labelInfo.setText(ResourceBundleMaster.TRANSLATOR.getTranslation("internalError"));
        } catch (IOException ex) {
            Logger.getLogger(ShowPatientPicturesWindowController.class.getName()).log(Level.SEVERE, null, ex);
            labelInfo.setText(ResourceBundleMaster.TRANSLATOR.getTranslation("internalError"));
        }
    }

    private void readAllPictures() throws SQLException {
        pictureList = patientManager.getPicturesData(Common.COMMON.getUsernameOfPictures());
    }

    private void initSliders() throws IOException, SQLException {
        if (!pictureList.isEmpty()) {
            showPicture(0);
            showPictureDescription(0);
        }
        slider.setMax(pictureList.size() - 1);
        slider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                    Number old_val, Number new_val) {
                int index = new_val.intValue();
                try {
                    showPicture(index);
                    showPictureDescription(index);
                    pictureIndex = index;
                } catch (IOException ex) {
                    Logger.getLogger(MainWindowPatientController.class.getName()).log(Level.SEVERE, null, ex);
                    labelInfo.setText(ResourceBundleMaster.TRANSLATOR.getTranslation("internalError"));
                } catch (SQLException ex) {
                    Logger.getLogger(ShowPatientPicturesWindowController.class.getName()).log(Level.SEVERE, null, ex);
                    labelInfo.setText(ResourceBundleMaster.TRANSLATOR.getTranslation("internalError"));
                }
            }
        });
    }

    private void showPicture(int index) throws IOException {
        Image image = new Image(new ByteArrayInputStream(pictureList.get(index)));
        this.imageViewPicture.setImage(image);
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
        String picture_description = singlePicture.get("picture_description");
        String name = singlePicture.get("picture_name");
        this.textAreaPictureDescription.setText(picture_description);
        this.labelImageName.setText(name);
    }

    private void readAllPicturesDescription() throws SQLException {
        picturesDescriptionMap = patientManager.getPicturesDescription(Common.COMMON.getUsernameOfPictures());
    }

    private void initButtons() {
        this.buttonClose.setOnAction(event -> {
            Stage stage = (Stage) this.buttonClose.getScene().getWindow();
            stage.close();
        });
        this.buttonSave.setOnAction(event -> {
            int i = 0;
            HashMap<String, String> singlePicture = null;
            for (HashMap<String, String> pic : picturesDescriptionMap.values()) {
                if (i == pictureIndex) {
                    singlePicture = pic;
                    break;
                }
                i++;
            }
            try {
                String description = textAreaPictureDescription.getText();
                try {
                    checker.verifySingleDescription(description);
                    patientManager.updatePictureDescription(description, singlePicture.get("id"));
                } catch (RegexException ex) {
                    Logger.getLogger(ShowPatientPicturesWindowController.class.getName()).log(Level.SEVERE, null, ex);
                    labelInfo.setText(ex.getMessage());
                }
            } catch (SQLException ex) {
                Logger.getLogger(ShowPatientPicturesWindowController.class.getName()).log(Level.SEVERE, null, ex);
                labelInfo.setText(ResourceBundleMaster.TRANSLATOR.getTranslation("internalError"));
            }
        });
    }
}
