/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package model.db;

import java.sql.SQLException;
import java.sql.Statement;
import model.enums.UserType;

/**
 * Class is responsible for deliver methods for managing users
 * @author peer
 */
public class DBUsersManager {
    private Statement statement = DBConnector.master.getStatement(); 

    public boolean createUserInDatabase(String username, String password, UserType usertype) throws SQLException{
        switch(usertype){
            case PATIENT:
                statement.execute("CREATE USER '"+username+"'@'localhost' IDENTIFIED BY '"+password+"'");
                statement.execute("GRANT SELECT ON ZdjeciaMedyczne.Pacjent TO" +username);
                statement.execute("GRANT SELECT ON ZdjeciaMedyczne.CzescCiala TO" +username);
                statement.execute("GRANT SELECT ON ZdjeciaMedyczne.Lekarz TO" +username);
                statement.execute("INSERT INTO")
            case ADMIN:
                
                
        }
    }



}
