/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.technician;

import controller.Window;
import java.io.File;
import java.io.IOException;
import java.net.URL;
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
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.Common;
import model.ResourceBundleMaster;
import model.db.DBPicturesManager;
import model.exception.FileTooBigException;
import model.file.FileDeliver;
import model.tableentries.PictureEntry;
import model.tableentries.UserEntry;
import org.joda.time.DateTime;

/**
 * FXML Controller class
 *
 * @author PeerZet
 */
public class AddPictureController extends Window {

    @FXML
    private TableColumn tableColumnId;
    @FXML
    private TableColumn tableColumnCaptureDatetime;
    @FXML
    private TableColumn tableColumnUsername;
    @FXML
    private TableColumn tableColumnTechnicianUsername;
    @FXML
    private TableColumn tableColumnDoctorUsername;
    @FXML
    private TableColumn tableColumnBodyPart;
    @FXML
    private TableColumn tableColumnPictureType;
    @FXML
    private TableColumn tableColumnEditButton;
    @FXML
    private TableView tableViewPictures;
    @FXML
    private Button buttonAddPicture;
    private DBPicturesManager pictureMaster = new DBPicturesManager();
    ObservableList data = FXCollections.observableArrayList();

    private final FileChooser fileChooser = new FileChooser();
    private Stage fileChooserStage = new Stage();
    private List<PictureEntry> dataTemporary = new ArrayList<>();//here will be all pictures added from system but not in db

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initButtons();
        linkTableColumns();//after it we will can paste UserEntry to the table
        fillPicturesTable();
        fileChooserStage.setTitle(ResourceBundleMaster.TRANSLATOR.getTranslation("selectPictures"));
    }

    private void initButtons() {
        buttonAddPicture.setOnAction(event -> {
            List<File> listOfFiles = fileChooser.showOpenMultipleDialog(fileChooserStage);
            getFiles(listOfFiles);
        });
    }

    private void fillPicturesTable() {
        pictureMaster.updateResultSet();//reading all users
        data.clear();
        dataTemporary.stream().forEach((picture) -> {
            data.add(picture);
        });
        HashMap<String, String> ids = pictureMaster.readIds();
        HashMap<String, String> capture_times = pictureMaster.readCapture_datetimes();
        HashMap<String, String> usernames = pictureMaster.readUsernames();
        HashMap<String, String> technician_usernames = pictureMaster.readTechnician_usernames();
        HashMap<String, String> doctor_usernames = pictureMaster.readDoctor_usernames();
        HashMap<String, String> body_parts = pictureMaster.readBody_parts();
        HashMap<String, String> picture_types = pictureMaster.readPicture_types();
        Set<String> idsOfRow = ids.keySet();
        for (String id : idsOfRow) { //from 1 because we use id in table from 1
            String capture_time = capture_times.get(id);
            String username = usernames.get(id);
            String technician_username = technician_usernames.get(id);
            String doctor_username = doctor_usernames.get(id);
            String body_part = body_parts.get(id);
            String picture_type = picture_types.get(id);
            data.add(new PictureEntry(id, capture_time, username, technician_username, doctor_username, body_part, picture_type));
        }
        tableViewPictures.setItems(data);
        tableViewPictures.setEditable(true);
    }

    private void linkTableColumns() {
        tableColumnId.setCellValueFactory(new PropertyValueFactory<PictureEntry, String>("id"));
        tableColumnCaptureDatetime.setCellValueFactory(new PropertyValueFactory<PictureEntry, String>("capture_datetime"));
        tableColumnUsername.setCellValueFactory(new PropertyValueFactory<PictureEntry, String>("username"));
        tableColumnTechnicianUsername.setCellValueFactory(new PropertyValueFactory<PictureEntry, String>("technician_username"));
        tableColumnDoctorUsername.setCellValueFactory(new PropertyValueFactory<PictureEntry, String>("doctor_username"));
        tableColumnBodyPart.setCellValueFactory(new PropertyValueFactory<PictureEntry, String>("body_part"));
        tableColumnPictureType.setCellValueFactory(new PropertyValueFactory<PictureEntry, String>("picture_type"));
        tableColumnEditButton.setCellValueFactory(new PropertyValueFactory<PictureEntry, Boolean>("selected"));
        tableColumnEditButton.setCellFactory(new Callback<TableColumn<PictureEntry, Boolean>, TableCell<PictureEntry, Boolean>>() {

            @Override
            public TableCell<PictureEntry, Boolean> call(TableColumn<PictureEntry, Boolean> param) {
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

    private void getFiles(List<File> listOfFiles) {
        try {
            FileDeliver deliver = new FileDeliver(listOfFiles);
            addFilesToTable(deliver.getFilesData());
        } catch (FileTooBigException ex) {
            Logger.getLogger(AddPictureController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AddPictureController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void addFilesToTable(HashMap<String, byte[]> filesData) {
        Set<String> keys = filesData.keySet();
        dataTemporary.clear();
        for (String fileName : keys) {
            PictureEntry picture = new PictureEntry(fileName, DateTime.now().toString(), Common.COMMON.getUsernameOfPictures(), "doctor", "tech",
                    "body part", "picture type");
            dataTemporary.add(picture);
        }
        fillPicturesTable();
    }
}
