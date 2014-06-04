/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.db;

import java.sql.SQLException;
import java.sql.Statement;
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

    /**
     * This value will be the id of the inserted user to UsersDB and other
     * Patient,Doctor,Admin,Technician tables
     */
    private int userInsertIndex = 1;

    public DBUsersManager() {

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

}
