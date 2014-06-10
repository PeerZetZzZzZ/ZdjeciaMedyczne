/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.enums.UserType;

/**
 * Class is responsible for deliver methods for managing users
 *
 * @author peer
 */
public class DBUsersManager {

    private Statement statement = DBConnector.master.getStatement();
    private HashMap<Integer, String> usernameMap = new HashMap<Integer, String>();
    private HashMap<Integer, String> passwordMap = new HashMap<Integer, String>();
    private HashMap<Integer, String> accountTypeMap = new HashMap<Integer, String>();
    private ResultSet usersDbSet;
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

    public void createUserInApplication() {
        // statement.execute("INSERT INTO ZdjeciaMedyczne.UsersDB VALUES(" + String.valueOf(userInsertIndex) + "," + username + "," + password + "," + "PATIENT" + ")");

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
            while(usersDbSet.next()) {
                accountTypeMap.put(usersDbSet.getInt("id"), usersDbSet.getString("account_type"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBUsersManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return accountTypeMap;
    }
}
