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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.Common;
import model.ResourceBundleMaster;
import model.db.DBManagerCommon;
import model.db.DBPicturesManager;
import model.db.DBUsersManager;
import model.enums.UserType;
import model.exception.FileTooBigException;
import model.exception.PictureDataException;
import model.file.FileDeliver;
import model.tableentries.PictureEntry;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * FXML Controller class
 *
 * @author PeerZet
 */
public class AddPictureController extends Window {

    @FXML
    private TableColumn tableColumnPictureName;
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
    private TableColumn tableColumnSaveCheckbox;
    @FXML
    private TableView tableViewPictures;
    @FXML
    private Button buttonAddPicture;
    @FXML
    private Button buttonSaveAll;
    @FXML
    private Button buttonMarkAll;
    @FXML
    private Button buttonUnmarkAll;
    @FXML
    private Label labelInfo;
    private DBPicturesManager pictureManager = new DBPicturesManager();
    private DBUsersManager managerUsers = new DBUsersManager();
    private DBManagerCommon commonManager = new DBManagerCommon();
    ObservableList data = FXCollections.observableArrayList();

    private final FileChooser fileChooser = new FileChooser();
    private Stage fileChooserStage = new Stage();
    private List<PictureEntry> dataTemporary = new ArrayList<>();//here will be all pictures added from system but not in db
    private ObservableList<String> doctorsList;
    private ObservableList<String> bodyPartList;
    private ObservableList<String> pictureTypeList;
    String selectedDoctorUsername;
    HashMap<String, String> filesData; //data of pictures which will be saved to DB, it's picture name: data

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            initButtons();
            linkTableColumns();//after it we will can paste UserEntry to the table
            doctorsList = commonManager.getDoctors();//we read all the doctors
            bodyPartList = commonManager.getBodyParts();//we read all the doctors
            pictureTypeList = commonManager.getPictureTypes();//we read all the doctors
            fillPicturesTable();
            checkIfAdmin();
            fileChooserStage.setTitle(ResourceBundleMaster.TRANSLATOR.getTranslation("selectPictures"));
        } catch (SQLException ex) {
            Logger.getLogger(AddPictureController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void initButtons() {
        buttonAddPicture.setOnAction(event -> {
            try {
                List<File> listOfFiles = fileChooser.showOpenMultipleDialog(fileChooserStage);
                getFiles(listOfFiles);
            } catch (SQLException ex) {
                Logger.getLogger(AddPictureController.class.getName()).log(Level.SEVERE, null, ex);

            }
        });
        buttonMarkAll.setOnAction(event -> {
            if (!data.isEmpty()) {
                for (Object user : data) {
                    PictureEntry pictureEntry = (PictureEntry) user;
                    pictureEntry.selected.setValue(Boolean.TRUE);
                }
            }
        });
        buttonUnmarkAll.setOnAction(event -> {
            if (!data.isEmpty()) {
                for (Object user : data) {
                    PictureEntry pictureEntry = (PictureEntry) user;
                    pictureEntry.selected.setValue(Boolean.FALSE);
                }
            }
        });
        buttonSaveAll.setOnAction(event -> {
            try {
                pictureManager.updatePictures(data, filesData);
                labelInfo.setText(ResourceBundleMaster.TRANSLATOR.getTranslation("successfullyAddedPictures"));
            } catch (SQLException ex) {
                Logger.getLogger(AddPictureController.class.getName()).log(Level.SEVERE, null, ex);
                labelInfo.setText(ResourceBundleMaster.TRANSLATOR.getTranslation("pictureUpdateError"));
            } catch (PictureDataException ex) {
                Logger.getLogger(AddPictureController.class.getName()).log(Level.SEVERE, null, ex);
                labelInfo.setText(ex.getMessage());
            }
        });
    }

    private void fillPicturesTable() throws SQLException {
        data.clear();
        linkTableColumns();
        dataTemporary.stream().forEach((picture) -> {
            data.add(picture);
        });
        HashMap<String, String> ids = pictureManager.readIds();
        HashMap<String, String> picture_names = pictureManager.readPicture_names();
        HashMap<String, String> capture_times = pictureManager.readCapture_datetimes();
        HashMap<String, String> usernames = pictureManager.readUsernames();
        HashMap<String, String> technician_usernames = pictureManager.readTechnician_usernames();
        HashMap<String, String> doctor_names = pictureManager.readDoctor_names();
        HashMap<String, String> body_parts = pictureManager.readBody_parts();
        HashMap<String, String> picture_types = pictureManager.readPicture_types();
        Set<String> idsOfRow = ids.keySet();
        for (String id : idsOfRow) { //from 1 because we use id in table from 1
            String picture_name = picture_names.get(id);
            String capture_time = capture_times.get(id);
            String username = usernames.get(id);
            String technician_username = technician_usernames.get(id);
            String doctor_username = doctor_names.get(id);
            String body_part = body_parts.get(id);
            String picture_type = picture_types.get(id);
            String description = "";
            String technicianUsername = Common.COMMON.getLoggedUser();
            String doctorUsername = "";
            data.add(new PictureEntry(id, picture_name, capture_time, username, technician_username, doctor_username, body_part, picture_type, description, technicianUsername, doctorUsername, true, false));
        }   //description above is empty value "" because we dont need to read it now and use it in table
        tableViewPictures.setItems(data);
        tableViewPictures.setEditable(true);
    }

    private void linkTableColumns() {
        tableColumnCaptureDatetime.setCellValueFactory(new PropertyValueFactory<PictureEntry, String>("capture_datetime"));
        tableColumnPictureName.setCellValueFactory(new PropertyValueFactory<PictureEntry, String>("picture_name"));
        tableColumnUsername.setCellValueFactory(new PropertyValueFactory<PictureEntry, String>("username"));
        tableColumnTechnicianUsername.setCellValueFactory(new PropertyValueFactory<PictureEntry, String>("technician_username"));
        tableColumnDoctorUsername.setCellValueFactory(new PropertyValueFactory<PictureEntry, String>("doctor_name"));
        tableColumnDoctorUsername.setCellFactory(new Callback<TableColumn<PictureEntry, String>, TableCell<PictureEntry, String>>() {

            @Override
            public TableCell<PictureEntry, String> call(TableColumn<PictureEntry, String> param) {
                ComboBoxTableCell cell = new ComboBoxTableCell(doctorsList) {

                    @Override
                    public void updateItem(Object item, boolean empty) {
                        super.updateItem(item, empty); //To change body of generated methods, choose Tools | Templates.
                    }

                };
                return cell;
            }

        });
        tableColumnBodyPart.setCellValueFactory(new PropertyValueFactory<PictureEntry, String>("body_part"));
        tableColumnBodyPart.setCellFactory(new Callback<TableColumn<PictureEntry, String>, TableCell<PictureEntry, String>>() {

            @Override
            public TableCell<PictureEntry, String> call(TableColumn<PictureEntry, String> param) {
                ComboBoxTableCell cell = new ComboBoxTableCell(bodyPartList) {

                    @Override
                    public void updateItem(Object item, boolean empty) {
                        super.updateItem(item, empty); //To change body of generated methods, choose Tools | Templates.
                    }

                };
                cell.setVisible(true);
                return cell;
            }

        });
        tableColumnPictureType.setCellValueFactory(new PropertyValueFactory<PictureEntry, String>("picture_type"));
        tableColumnPictureType.setCellFactory(new Callback<TableColumn<PictureEntry, String>, TableCell<PictureEntry, String>>() {

            @Override
            public TableCell<PictureEntry, String> call(TableColumn<PictureEntry, String> param) {
                ComboBoxTableCell cell = new ComboBoxTableCell(pictureTypeList) {

                    @Override
                    public void updateItem(Object item, boolean empty) {
                        super.updateItem(item, empty); //To change body of generated methods, choose Tools | Templates.
                    }

                };
                cell.setVisible(true);
                return cell;
            }

        });
        tableColumnSaveCheckbox.setCellValueFactory(new PropertyValueFactory<PictureEntry, Boolean>("selected"));
        tableColumnSaveCheckbox.setCellFactory(new Callback<TableColumn<PictureEntry, Boolean>, TableCell<PictureEntry, Boolean>>() {

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

    private void getFiles(List<File> listOfFiles) throws SQLException {
        try {
            FileDeliver deliver = new FileDeliver(listOfFiles);
            addFilesToTable(deliver.getFilesData());
        } catch (FileTooBigException ex) {
            Logger.getLogger(AddPictureController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AddPictureController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void addFilesToTable(HashMap<String, String> filesData) throws SQLException {
        this.filesData = filesData;
        Set<String> keys = filesData.keySet();
        dataTemporary.clear();
        DateTimeFormatter ftm = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");//mysql format
        String capture_datetime = ftm.print(DateTime.now());
        String technician_name = commonManager.getTechnician(Common.COMMON.getLoggedUser());
        for (String pictureName : keys) {
            String id = "";//the new values must be edited before inserting to db
            String username = Common.COMMON.getUsernameOfPictures();
            String doctor_name = "NEW";
            String body_part = "NEW";
            String picture_type = "NEW";
            String description = "";
            String technician_username = Common.COMMON.getLoggedUser();
            String doctor_username = "NEW";
            boolean selected = true;//by default we want add all these to db
            boolean justAdded = true;//it means that we just added them, without flag we would not insert to DB again
            PictureEntry picture = new PictureEntry(id, pictureName, capture_datetime, username, technician_name,
                    doctor_name, body_part, picture_type, description, technician_username, doctor_username, selected, justAdded);
            dataTemporary.add(picture);
        }
        fillPicturesTable();
    }

    private void checkIfAdmin() throws SQLException {
        if (managerUsers.readSingleUserType(Common.COMMON.getLoggedUser()).equals(UserType.ADMIN)) {
            this.buttonAddPicture.setVisible(false);
        }
    }
}
