package model.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import model.Common;
import model.ResourceBundleMaster;
import model.exception.PictureDataException;
import model.exception.RegexException;
import model.regex.RegexPatternChecker;
import model.tableentries.PictureEntry;

/**
 *
 * @author PeerZet
 */
public class DBPicturesManager extends DBManager {

    private HashMap<String, String> idsMap = new HashMap<String, String>();
    private HashMap<String, String> picture_namesMap = new HashMap<String, String>();
    private HashMap<String, String> capture_datetimedMap = new HashMap<String, String>();
    private HashMap<String, String> usernameMap = new HashMap<String, String>();
    private HashMap<String, String> technician_usernameMap = new HashMap<String, String>();
    private HashMap<String, String> doctor_usernameMap = new HashMap<String, String>();
    private HashMap<String, String> doctor_nameMap = new HashMap<String, String>();
    private HashMap<String, String> body_partMap = new HashMap<String, String>();
    private HashMap<String, String> picture_typeMap = new HashMap<String, String>();
    private ResultSet picturesDbSet;
    /**
     * Used for checking if the input values are correct, before inserting them
     * to DB.
     */
    private final RegexPatternChecker patternChecker = new RegexPatternChecker();
    /**
     * This value will be the id of the inserted user to UsersDB and other
     * Patient,Doctor,Admin,Technician tables
     */
    private int userInsertIndex = 1;
    private DBManagerCommon commonManager = new DBManagerCommon();

    public DBPicturesManager() {
    }

    public int checkLinesAmountInTable(String tableName) {
        try {
            return statement.executeQuery(tableName).getStatement().getMaxRows();
        } catch (SQLException ex) {
            Logger.getLogger(DBUsersManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public void createPicture(String captureDatetime, byte[] pictureData, String username, String technician_username, String doctor_username,
            String body_part, String picture_type) throws SQLException, RegexException {
        patternChecker.verifyUser(username);
        patternChecker.verifyUser(technician_username);
        patternChecker.verifyUser(doctor_username);
        queryRunner.update(connection, "INSERT INTO MedicalPictures.Picture VALUES(UUID(),?,?,?,?,?,?,?)", captureDatetime, pictureData, username,
                technician_username, doctor_username, body_part, picture_type);
    }

    public void deleteUsers(List<String> usernames) throws SQLException {
        for (String username : usernames) {
            queryRunner.update(connection, "DELETE FROM MedicalPictures.UsersDB WHERE username=?", username);
        }
    }

    /**
     * Method returns the Hashmap of users ID and their usernames
     *
     * @return
     */
    public HashMap<String, String> readIds() {
        try {
            if (!idsMap.isEmpty()) {
                idsMap.clear();
            }
            updateResultSet();
            picturesDbSet.beforeFirst();
            while (picturesDbSet.next()) {
                idsMap.put(picturesDbSet.getString("id"), picturesDbSet.getString("id"));
            }
            picturesDbSet.close();
        } catch (SQLException ex) {
            Logger.getLogger(DBPicturesManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return idsMap;
    }

    public HashMap<String, String> readPicture_names() {
        if (!picture_namesMap.isEmpty()) {
            picture_namesMap.clear();
        }
        try {
            updateResultSet();
            picturesDbSet.beforeFirst();
            while (picturesDbSet.next()) {
                String name = picturesDbSet.getString("picture_name");
                picture_namesMap.put(picturesDbSet.getString("id"), name);
            }
            picturesDbSet.close();
        } catch (SQLException ex) {
            Logger.getLogger(DBUsersManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return picture_namesMap;
    }

    public HashMap<String, String> readUsernames() {
        if (!usernameMap.isEmpty()) {
            usernameMap.clear();
        }
        try {
            updateResultSet();
            picturesDbSet.beforeFirst();
            while (picturesDbSet.next()) {
                usernameMap.put(picturesDbSet.getString("id"), picturesDbSet.getString("username"));
            }
            picturesDbSet.close();
        } catch (SQLException ex) {
            Logger.getLogger(DBUsersManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return usernameMap;
    }

    public HashMap<String, String> readCapture_datetimes() {
        try {
            if (!capture_datetimedMap.isEmpty()) {
                capture_datetimedMap.clear();
            }
            updateResultSet();
            picturesDbSet.beforeFirst();
            while (picturesDbSet.next()) {
                capture_datetimedMap.put(picturesDbSet.getString("id"), picturesDbSet.getString("capture_datetime"));
            }
            picturesDbSet.close();
        } catch (SQLException ex) {
            Logger.getLogger(DBPicturesManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return capture_datetimedMap;
    }

    public HashMap<String, String> readTechnician_usernames() {
        try {
            if (!technician_usernameMap.isEmpty()) {
                technician_usernameMap.clear();
            }
            updateResultSet();
            picturesDbSet.beforeFirst();
            while (picturesDbSet.next()) {
                technician_usernameMap.put(picturesDbSet.getString("id"), picturesDbSet.getString("technician_username"));
            }
            picturesDbSet.close();
        } catch (SQLException ex) {
            Logger.getLogger(DBPicturesManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return technician_usernameMap;
    }

    public HashMap<String, String> readDoctor_usernames() {
        try {
            if (!doctor_usernameMap.isEmpty()) {
                doctor_usernameMap.clear();
            }
            updateResultSet();
            picturesDbSet.beforeFirst();
            while (picturesDbSet.next()) {
                doctor_usernameMap.put(picturesDbSet.getString("id"), picturesDbSet.getString("doctor_username"));
            }
            picturesDbSet.close();
        } catch (SQLException ex) {
            Logger.getLogger(DBPicturesManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return doctor_usernameMap;
    }

    public HashMap<String, String> readBody_parts() {
        try {
            if (!body_partMap.isEmpty()) {
                body_partMap.clear();
            }
            updateResultSet();
            picturesDbSet.beforeFirst();
            while (picturesDbSet.next()) {
                body_partMap.put(picturesDbSet.getString("id"), picturesDbSet.getString("body_part"));
            }
            picturesDbSet.close();
        } catch (SQLException ex) {
            Logger.getLogger(DBPicturesManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return body_partMap;
    }

    public HashMap<String, String> readDoctor_names() throws SQLException {
        if (!doctor_nameMap.isEmpty()) {
            doctor_nameMap.clear();
        }
        updateResultSet();
        picturesDbSet.beforeFirst();
        HashMap<String, String> idAndDoctorUsernames = new HashMap<>();
        while (picturesDbSet.next()) {
            idAndDoctorUsernames.put(picturesDbSet.getString("id"), picturesDbSet.getString("doctor_username"));
        }
        Set<String> keySet = idAndDoctorUsernames.keySet();
        for (String key : keySet) {
            String doctor_username = idAndDoctorUsernames.get(key);//we get id of the picture
            ResultSet doctor = statement.executeQuery("SELECT DISTINCT(name), surname FROM MedicalPictures.Doctor JOIN MedicalPictures.Picture ON \n"
                    + "doctor_username ='" + doctor_username + "'");
            if (doctor.next()) {
                String name = doctor.getString("name");
                String surname = doctor.getString("surname");
                String doctor_name = doctor_username + " " + name + " " + surname;
                doctor_nameMap.put(key, doctor_name);
                doctor.close();
            }
        }
        picturesDbSet.close();
        return doctor_nameMap;
    }

    public HashMap<String, String> readPicture_types() {
        try {
            if (!picture_typeMap.isEmpty()) {
                picture_typeMap.clear();
            }
            updateResultSet();
            picturesDbSet.beforeFirst();
            while (picturesDbSet.next()) {
                picture_typeMap.put(picturesDbSet.getString("id"), picturesDbSet.getString("picture_type"));
            }
            picturesDbSet.close();
        } catch (SQLException ex) {
            Logger.getLogger(DBPicturesManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return picture_typeMap;
    }

    public void updateResultSet() {
        try {
            String username = Common.COMMON.getUsernameOfPictures();
            picturesDbSet = readAllPicturesFromUsersDB(username);
        } catch (SQLException ex) {
            Logger.getLogger(DBUsersManager.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private ResultSet readAllPicturesFromUsersDB(String username) throws SQLException {
        ResultSet pictures = statement.executeQuery("SELECT id,picture_name,capture_datetime,"
                + "username,technician_username,doctor_username,body_part,picture_type,picture_description FROM MedicalPictures.Picture WHERE username='" + username + "'");
        return pictures;
    }

    public void updatePictures(ObservableList data, HashMap<String, String> fileData) throws SQLException, PictureDataException {
        verifyPictureData(data);
        deletePictures(data);
        savePictures(data, fileData);

    }

    private void savePictures(ObservableList data, HashMap<String, String> fileData) throws SQLException {
        for (Object pictureObject : data) {
            PictureEntry picture = (PictureEntry) pictureObject;
            if (picture.justAdded) {//we dont want to add pictures which already are in DB
                if (picture.getSelected()) {//we want to add only pictures that are selected
                    String pictureName = picture.getPicture_name();
                    String capture_datetime = picture.getCapture_datetime();
                    String doctor_username = commonManager.getDoctorUsername(picture.getDoctor_name());
                    String pictureData = fileData.get(pictureName);
                    String username = picture.getUsername();
                    String technician_username = picture.getTechnician_username();
                    String body_part = picture.getBody_part();
                    String picture_type = picture.getPicture_type();
                    String description = picture.getDescription();
                    queryRunner.update(connection, "INSERT INTO MedicalPictures.Picture VALUES(UUID(),?,?,LOAD_FILE(?),?,?,?,?,?,?)",
                            pictureName,
                            capture_datetime,
                            pictureData,
                            username,
                            technician_username,
                            doctor_username,
                            body_part,
                            picture_type,
                            description);
                }
            } else {
                String id = picture.getId();
                if (checkIfPictureExist(id)) {//if it exists we must update it
                    String doctor_username = commonManager.getDoctorUsername(picture.getDoctor_name());
                    String body_part = picture.getBody_part();
                    String picture_type = picture.getPicture_type();
                    queryRunner.update(connection, "UPDATE MedicalPictures.Picture SET doctor_username=?, body_part=?,picture_type=? "
                            + "WHERE id=?",
                            doctor_username,
                            body_part,
                            picture_type,
                            id);
                }
            }
        }
    }

    private boolean checkIfPictureExist(String pictureId) throws SQLException {
        ResultSet id = statement.executeQuery("SELECT id FROM MedicalPictures.Picture WHERE id='" + pictureId + "'");
        if (id.first()) {
            return true;
        }
        return false;
    }

    private void deletePictures(ObservableList data) throws SQLException {
        for (Object pictureObject : data) {
            PictureEntry picture = (PictureEntry) pictureObject;
            if (!picture.getSelected()) {//we dont want to add pictures which already are in DB
                if (!picture.justAdded) {//if just added we will make with them nothing, cause they are not in DB
                    String pictureId = picture.getId();
                    queryRunner.update(connection, "DELETE FROM MedicalPictures.Picture WHERE id=?", pictureId);
                }
            }
        }
    }

    private void verifyPictureData(ObservableList data) throws PictureDataException {
        for (Object object : data) {
            PictureEntry picture = (PictureEntry) object;
            if (picture.getSelected()) {
                String doctor_name = picture.getDoctor_name();
                String body_part = picture.getBody_part();
                String picture_type = picture.getPicture_type();
                if (doctor_name.equals("NEW") || body_part.equals("NEW") || picture_type.equals("NEW")) {
                    throw new PictureDataException(ResourceBundleMaster.TRANSLATOR.getTranslation("pictureDataIncorrect"));
                }
            }
        }
    }

}
