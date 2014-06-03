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
 * @author peer
 */
public class DBUsersManager {
    private Statement statement = DBConnector.master.getStatement(); 

    /**
     * This value will be the id of the inserted user to UsersDB and other Patient,Doctor,Admin,Technician tables
     */
    private int userInsertIndex =1;
    
    public DBUsersManager(){
        
    }
    
    public boolean createUserInDatabase(String username, String password, UserType usertype) throws SQLException{
        switch(usertype){
            case PATIENT:
                statement.execute("CREATE USER '"+username+"'@'localhost' IDENTIFIED BY '"+password+"'");
                statement.execute("GRANT SELECT ON MedicalPictures.Patient TO" +username);
                statement.execute("GRANT SELECT ON MedicalPictures.BodyParts TO" +username);
                statement.execute("GRANT SELECT ON MedicalPictures.Doctor TO" +username);
                statement.execute("INSERT INTO ZdjeciaMedyczne.UsersDB VALUES("+String.valueOf(userInsertIndex)+","+username+","+password+","+"PATIENT"+")");
                return true;
            //case ADMIN:
                
                
        }
        return false;
    }
    public void createDatabaseSchema() throws SQLException{
        statement.execute("CREATE DATABASE IF NOT EXISTS MedicalPictures");
        statement.execute("CREATE TABLE IF NOT EXISTS UsersDB(id integer primary key, username varchar(100),password varchar(100),account_type varchar(15))");
        statement.execute("CREATE TABLE IF NOT EXISTS MedicalPictures.Patient(id integer, name varchar(100), surname varchar(100),sex varchar(6),address varchar(150), age integer, foreign key (id) references UsersDB(id))"); 
    }
    public int checkLinesAmountInTable(String tableName){
        try {
            return statement.executeQuery(tableName).getStatement().getMaxRows();
        } catch (SQLException ex) {
            Logger.getLogger(DBUsersManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }
    

}
