/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.exception.RegexException;
import model.regex.RegexPatternChecker;
import org.apache.commons.dbutils.QueryRunner;

/**
 * Class is responsible for deliver methods for managing users
 *
 * @author peer
 */
public class DBUsersManager {

    private Connection connection = DBConnector.master.getConnection();
    private Statement statement = DBConnector.master.getStatement();
    private QueryRunner queryRunner = DBConnector.master.getQueryRunner();//for easy quries
    private HashMap<Integer, String> usernameMap = new HashMap<Integer, String>();
    private HashMap<Integer, String> passwordMap = new HashMap<Integer, String>();
    private HashMap<Integer, String> accountTypeMap = new HashMap<Integer, String>();
    private ResultSet usersDbSet;
    /**
     * Used for checking if the input values are correct, before inserting them
     * to DB.
     */
    private final RegexPatternChecker patternChecker = new RegexPatternChecker() ;
    /**
     * This value will be the id of the inserted user to UsersDB and other
     * Patient,Doctor,Admin,Technician tables
     */
    private int userInsertIndex = 1;

    public DBUsersManager() {
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

    public void createUser(String name, String surname,String gender, String age, String username, String password, String specialization, String usertype) throws SQLException, RegexException {
       createLoginUser(username,password,usertype);
       createPerson(name,surname,age,gender,specialization,usertype);

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
        patternChecker.verifyUser(username);
        patternChecker.verifyPassword(password);
        int amount = getInsertId("UsersDB");
        queryRunner.update(connection, "INSERT INTO MedicalPictures.UsersDB VALUES(?,?,?,?)",amount,username,password,type);

    }

    private void createPerson(String name, String surname, String age, String gender, String specialization,String usertype) throws RegexException, SQLException {
        patternChecker.verifySingleWord(name);
        patternChecker.verifySingleWord(surname);
        patternChecker.verifySingleNumber(age);
        if(usertype.equals("DOCTOR")){
            patternChecker.verifySingleWord(specialization);
        }
        patternChecker.verifySingleWord(gender);
        switch(usertype){
            case "ADMIN":   
                queryRunner.update(connection,"INSERT INTO MedicalPictures.Admin VALUES(?,?,?,?,?)",getInsertId("Admin"),
                        name,surname,gender,age);
                break;
            case "TECHNICIAN":
                queryRunner.update(connection,"INSERT INTO MedicalPictures.Technician VALUES(?,?,?,?,?,?)",getInsertId("Technician"),
                        name,surname,gender,age);
                break;
            case "PATIENT":
                queryRunner.update(connection,"INSERT INTO MedicalPictures.Patient VALUES(?,?,?,?,?)",getInsertId("Patient"),
                        name,surname,gender,age);
                break;
            case "DOCTOR":
                queryRunner.update(connection,"INSERT INTO MedicalPictures.Doctor VALUES(?,?,?,?,?,?)",getInsertId("Doctor"),
                        name,surname,gender,age,specialization);
                break;
        }

    }

    /**
     * Method returns the Hashmap of users ID and their usernames
     *
     * @return
     */
    public HashMap<Integer, String> readUsernames() {
        if (!usernameMap.isEmpty()) {
            usernameMap.clear();
        }
        try {
            usersDbSet.beforeFirst();
            while (usersDbSet.next()) {
                usernameMap.put(usersDbSet.getInt("id"), usersDbSet.getString("username"));
            }
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
        ResultSet users = statement.executeQuery("SELECT * FROM MedicalPictures.UsersDB");
        return users;
    }

    public HashMap<Integer, String> readPasswords() {
        if (!passwordMap.isEmpty()) {
            passwordMap.clear();
        }
        try {
            usersDbSet.beforeFirst();
            while (usersDbSet.next()) {
                passwordMap.put(usersDbSet.getInt("id"), usersDbSet.getString("password"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBUsersManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return passwordMap;
    }

    public HashMap<Integer, String> readAccountTypes() {
        if (!usernameMap.isEmpty()) {
            accountTypeMap.clear();
        }
        try {
            usersDbSet.beforeFirst();
            while (usersDbSet.next()) {
                accountTypeMap.put(usersDbSet.getInt("id"), usersDbSet.getString("account_type"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBUsersManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return accountTypeMap;
    }
    
    /**
     * This Method returns the id for inserting new row for the given table.
     * @param table
     * @return
     * @throws SQLException 
     */
    private int getInsertId(String table) throws SQLException{
        ResultSet lastId = statement.executeQuery("SELECT COUNT(*) AS AMOUNT FROM MedicalPictures."+table);
        lastId.first();
        int amountOfRows = lastId.getInt("AMOUNT");
        return amountOfRows +1;
    }
}
