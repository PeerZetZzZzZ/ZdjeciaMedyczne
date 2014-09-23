/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.enums.UserType;
import model.exception.RegexException;
import model.regex.RegexPatternChecker;

/**
 * Class is responsible for deliver methods for managing users
 *
 * @author peer
 */
public class DBUsersManager extends DBManager {

    private HashMap<String, String> usernameMap = new HashMap<String, String>();
    private HashMap<String, String> passwordMap = new HashMap<String, String>();
    private HashMap<String, String> accountTypeMap = new HashMap<String, String>();
    private HashMap<String, String> namesMap = new HashMap<String, String>();
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
    private UserType user = null;//for this user account we will get all data

    public DBUsersManager() {
        updateResultSet();
    }

    public void setUserType(UserType user) {
        this.user = user;
    }

    public int checkLinesAmountInTable(String tableName) {
        try {
            return statement.executeQuery(tableName).getStatement().getMaxRows();
        } catch (SQLException ex) {
            Logger.getLogger(DBUsersManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public void createUser(String name, String surname, String gender, String age, String username, String password, String specialization, String usertype) throws SQLException, RegexException {
        patternChecker.verifyUser(username);
        patternChecker.verifyPassword(password);
        patternChecker.verifySingleWord(name);
        patternChecker.verifySingleWord(surname);
        patternChecker.verifySingleNumber(age);
        if (usertype.equals("DOCTOR")) {
            patternChecker.verifySingleWord(specialization);
        }
        patternChecker.verifySingleWord(gender);
        createLoginUser(username, password, usertype);
        createPerson(username, name, surname, age, gender, specialization, usertype);

    }

    /**
     * Creates user in Users tables which is used only for logging to
     * application and verify if user can log in .
     *
     * @param username
     * @param password
     * @param usertype
     * @throws RegexException when not valid input was deliverd
     * @throws SQLException1
     */
    private void createLoginUser(String username, String password, String type) throws RegexException, SQLException {
        queryRunner.update(connection, "INSERT INTO MedicalPictures.UsersDB VALUES(?,MD5(?),?)", username, password, type);

    }

    private void createPerson(String username, String name, String surname, String age, String gender, String specialization, String usertype) throws RegexException, SQLException {
        switch (usertype) {
            case "ADMIN":
                queryRunner.update(connection, "INSERT INTO MedicalPictures.Admin VALUES(?,?,?,?,?)", username,
                        name, surname, gender, age);
                break;
            case "TECHNICIAN":
                queryRunner.update(connection, "INSERT INTO MedicalPictures.Technician VALUES(?,?,?,?,?)", username,
                        name, surname, gender, age);
                break;
            case "PATIENT":
                queryRunner.update(connection, "INSERT INTO MedicalPictures.Patient VALUES(?,?,?,?,?)", username,
                        name, surname, gender, age);
                break;
            case "DOCTOR":
                queryRunner.update(connection, "INSERT INTO MedicalPictures.Doctor VALUES(?,?,?,?,?,?)", username,
                        name, surname, gender, age, specialization);
                break;
        }

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
    public HashMap<String, String> readUsernames() {
        if (!usernameMap.isEmpty()) {
            usernameMap.clear();
        }
        try {
            updateResultSet();
            usersDbSet.beforeFirst();
            while (usersDbSet.next()) {
                usernameMap.put(usersDbSet.getString("username"), usersDbSet.getString("username"));
            }
            usersDbSet.close();
        } catch (SQLException ex) {
            Logger.getLogger(DBUsersManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return usernameMap;
    }

    public void updateResultSet() {
        try {
            usersDbSet = readAllUsersFromUsersDB();
        } catch (SQLException ex) {
            Logger.getLogger(DBUsersManager.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private ResultSet readAllUsersFromUsersDB() throws SQLException {
        ResultSet users;
        if (user != null) {
            switch (user) {
                case PATIENT:
                    users = statement.executeQuery("SELECT u.username as username, u.password as password"
                            + ", u.account_type as account_type,p.name as name, p.surname as surname"
                            + " FROM MedicalPictures.UsersDB u JOIN MedicalPictures.Patient p ON u.username=p.username "
                            + "WHERE u.account_type='" + user.toString() + "'");
                    break;
                default:
                    users = statement.executeQuery("SELECT * FROM MedicalPictures.UsersDB WHERE account_type='"
                            + user.toString() + "'");
                    break;

            }
        } else {
            users = statement.executeQuery("SELECT * FROM MedicalPictures.UsersDB");
        }
        return users;
    }

    public HashMap<String, String> readPasswords() {
        if (!passwordMap.isEmpty()) {
            passwordMap.clear();
        }
        try {
            updateResultSet();
            usersDbSet.beforeFirst();
            while (usersDbSet.next()) {
                passwordMap.put(usersDbSet.getString("username"), usersDbSet.getString("password"));
            }
            usersDbSet.close();
        } catch (SQLException ex) {
            Logger.getLogger(DBUsersManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return passwordMap;
    }

    public HashMap<String, String> readAccountTypes() {
        if (!usernameMap.isEmpty()) {
            accountTypeMap.clear();
        }
        try {
            updateResultSet();
            usersDbSet.beforeFirst();
            while (usersDbSet.next()) {
                accountTypeMap.put(usersDbSet.getString("username"), usersDbSet.getString("account_type"));
            }
            usersDbSet.close();
        } catch (SQLException ex) {
            Logger.getLogger(DBUsersManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return accountTypeMap;
    }

    public HashMap<String, String> readNames() {
        if (!namesMap.isEmpty()) {
            namesMap.clear();
        }
        try {
            updateResultSet();
            usersDbSet.beforeFirst();
            while (usersDbSet.next()) {
                if (user == UserType.PATIENT) {
                    namesMap.put(usersDbSet.getString("username"), usersDbSet.getString("name") + " " + usersDbSet.getString("surname"));
                }
            }
            usersDbSet.close();
        } catch (SQLException ex) {
            Logger.getLogger(DBUsersManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return namesMap;
    }

    /**
     * This Method returns the id for inserting new row for the given table.
     *
     * @param table
     * @return
     * @throws SQLException
     */
    private int getInsertId(String table) throws SQLException {
        ResultSet lastId = statement.executeQuery("SELECT COUNT(*) AS AMOUNT FROM MedicalPictures." + table);
        lastId.first();
        int amountOfRows = lastId.getInt("AMOUNT");
        lastId.close();
        return amountOfRows + 1;
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
        set.close();
        return userValues;
    }

    public void editUser(String username, String name, String surname, String age, String gender, String specialization, String usertype) throws SQLException, RegexException {
        HashMap<String, String> existingUser = getUserValues(username);
        String accountType = existingUser.get("account_type");
        switch (accountType) {
            case "ADMIN":
                queryRunner.update(connection, "DELETE FROM MedicalPictures.Admin WHERE username=?", username);
                break;
            case "TECHNICIAN":
                queryRunner.update(connection, "DELETE FROM MedicalPictures.Technician WHERE username=?", username);
                break;
            case "PATIENT":
                queryRunner.update(connection, "DELETE FROM MedicalPictures.Patient WHERE username=?", username);
                break;
            case "DOCTOR":
                queryRunner.update(connection, "DELETE FROM MedicalPictures.Doctor WHERE username=?", username);
                break;

        }
        queryRunner.update(connection, "UPDATE MedicalPictures.UsersDB SET account_type=? WHERE username=?", usertype, username);
        patternChecker.verifyUser(username);
        patternChecker.verifySingleWord(name);
        patternChecker.verifySingleWord(surname);
        patternChecker.verifySingleNumber(age);
        if (usertype.equals("DOCTOR")) {
            patternChecker.verifySingleWord(specialization);
        }
        patternChecker.verifySingleWord(gender);
        createPerson(username, name, surname, age, gender, specialization, usertype);
    }

    public UserType readSingleUserType(String username, String password) throws SQLException {
        ResultSet user = statement.executeQuery("SELECT account_type FROM MedicalPictures.UsersDB WHERE username='" + username + "' AND "
                + "password=MD5('" + password + "')");
        while (user.next()) {
            String type = user.getString("account_type");
            switch (type) {
                case "ADMIN":
                    return UserType.ADMIN;
                case "PATIENT":
                    return UserType.PATIENT;
                case "TECHNICIAN":
                    return UserType.TECHNICIAN;
                case "DOCTOR":
                    return UserType.DOCTOR;
            }
        }
        return null;
    }

    public UserType readSingleUserType(String username) throws SQLException {
        ResultSet user = statement.executeQuery("SELECT account_type FROM MedicalPictures.UsersDB WHERE username='" + username + "'");
        while (user.next()) {
            String type = user.getString("account_type");
            switch (type) {
                case "ADMIN":
                    return UserType.ADMIN;
                case "PATIENT":
                    return UserType.PATIENT;
                case "TECHNICIAN":
                    return UserType.TECHNICIAN;
                case "DOCTOR":
                    return UserType.DOCTOR;
            }
        }
        return null;
    }
}
