package model.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Common;
import model.exception.RegexException;
import model.regex.RegexPatternChecker;
import org.apache.commons.dbutils.QueryRunner;

/**
 *
 * @author PeerZet
 */
public class DBPicturesManager {

    private Connection connection = DBConnector.master.getConnection();
    private Statement statement = DBConnector.master.getStatement();
    private QueryRunner queryRunner = DBConnector.master.getQueryRunner();//for easy quries
    private HashMap<String, String> idsMap = new HashMap<String, String>();
    private HashMap<String, String> capture_datetimedMap = new HashMap<String, String>();
    private HashMap<String, String> usernameMap = new HashMap<String, String>();
    private HashMap<String, String> technician_usernameMap = new HashMap<String, String>();
    private HashMap<String, String> doctor_usernameMap = new HashMap<String, String>();
    private HashMap<String, String> body_partMap = new HashMap<String, String>();
    private HashMap<String, String> picture_typeMap = new HashMap<String, String>();
    private ResultSet usersDbSet;
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

    public DBPicturesManager() {
        updateResultSet();
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
            usersDbSet.beforeFirst();
            while (usersDbSet.next()) {
                idsMap.put(usersDbSet.getString("id"), usersDbSet.getString("id"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBPicturesManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return idsMap;
    }

    public HashMap<String, String> readUsernames() {
        if (!usernameMap.isEmpty()) {
            usernameMap.clear();
        }
        try {
            usersDbSet.beforeFirst();
            while (usersDbSet.next()) {
                usernameMap.put(usersDbSet.getString("id"), usersDbSet.getString("username"));
            }
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
            usersDbSet.beforeFirst();
            while (usersDbSet.next()) {
                capture_datetimedMap.put(usersDbSet.getString("id"), usersDbSet.getString("capture_datetime"));
            }
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
            usersDbSet.beforeFirst();
            while (usersDbSet.next()) {
                technician_usernameMap.put(usersDbSet.getString("id"), usersDbSet.getString("technician_username"));
            }
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
            usersDbSet.beforeFirst();
            while (usersDbSet.next()) {
                doctor_usernameMap.put(usersDbSet.getString("id"), usersDbSet.getString("doctor_username"));
            }
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
            usersDbSet.beforeFirst();
            while (usersDbSet.next()) {
                body_partMap.put(usersDbSet.getString("id"), usersDbSet.getString("body_part"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBPicturesManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return body_partMap;
    }

    public HashMap<String, String> readPicture_types() {
        try {
            if (!picture_typeMap.isEmpty()) {
                picture_typeMap.clear();
            }
            usersDbSet.beforeFirst();
            while (usersDbSet.next()) {
                picture_typeMap.put(usersDbSet.getString("id"), usersDbSet.getString("picture_type"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBPicturesManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return picture_typeMap;
    }

    public void updateResultSet() {
        try {
            String username = Common.COMMON.getUsernameOfPictures();
            usersDbSet = readAllPicturesFromUsersDB(username);
        } catch (SQLException ex) {
            Logger.getLogger(DBUsersManager.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private ResultSet readAllPicturesFromUsersDB(String username) throws SQLException {
        ResultSet pictures = statement.executeQuery("SELECT * FROM MedicalPictures.Picture WHERE username='"+username+"'");
        return pictures;
    }


    public HashMap<String, String> getUserValues(String username) throws SQLException {
        ResultSet set = statement.executeQuery("SELECT * FROM MedicalPictures.UsersDB WHERE username='" + username + "'");
        HashMap<String, String> userValues = new HashMap<String, String>();
        if (set.first()) {
            userValues.put("username", set.getString("username"));
            userValues.put("password", set.getString("password"));
            String accountType = set.getString("account_type");
            userValues.put("account_type", accountType);
            switch (accountType) {
                case "ADMIN":
                    ResultSet adminValues = statement.executeQuery("SELECT * FROM MedicalPictures.Admin WHERE username='" + username + "'");
                    if (adminValues.first()) {
                        userValues.put("name", adminValues.getString("name"));
                        userValues.put("surname", adminValues.getString("surname"));
                        userValues.put("sex", adminValues.getString("sex"));
                        userValues.put("age", adminValues.getString("age"));
                    }
                    break;
                case "TECHNICIAN":
                    ResultSet technicianValues = statement.executeQuery("SELECT * FROM MedicalPictures.Technician WHERE username='" + username + "'");
                    if (technicianValues.first()) {
                        userValues.put("name", technicianValues.getString("name"));
                        userValues.put("surname", technicianValues.getString("surname"));
                        userValues.put("sex", technicianValues.getString("sex"));
                        userValues.put("age", technicianValues.getString("age"));
                    }
                    break;
                case "DOCTOR":
                    ResultSet doctorValues = statement.executeQuery("SELECT * FROM MedicalPictures.Doctor WHERE username='" + username + "'");
                    if (doctorValues.first()) {
                        userValues.put("name", doctorValues.getString("name"));
                        userValues.put("surname", doctorValues.getString("surname"));
                        userValues.put("sex", doctorValues.getString("sex"));
                        userValues.put("age", doctorValues.getString("age"));
                        userValues.put("specialization", doctorValues.getString("specialization"));

                    }
                    break;
                case "PATIENT":
                    ResultSet patientValues = statement.executeQuery("SELECT * FROM MedicalPictures.Patient WHERE username='" + username + "'");
                    if (patientValues.first()) {
                        userValues.put("name", patientValues.getString("name"));
                        userValues.put("surname", patientValues.getString("surname"));
                        userValues.put("sex", patientValues.getString("sex"));
                        userValues.put("age", patientValues.getString("age"));

                    }
                    break;
            }
        }
        return userValues;
    }
}
