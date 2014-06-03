/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package model.db;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author peer
 */
public class DBConnector {
    
       /**
     * The object of the DatabaseMaster, which is responsible for connections to
     * the database.
     */
    public static DBConnector master = new DBConnector();
    private Statement statement;
    private DatabaseMetaData data;
    private Connection con;
    private DatabaseMetaData dbmd;

    /**
     * The name of the driver for DB connection. We use mysql driver.
     */
    private String driverName="com.mysql.jdbc.Driver"; 
    /**
     * The url for db connection.
     */
    private String dbUrl="jdbc:mysql://";
    /**
     * The name of the server where DB exists.
     */
    private String serverName="localhost";
    /**
     * The port for DB connection
     */
    private String port="3306";
    /**
     * The database name we connect.
     */
    private String databaseName="MedicalPictures";
    
    private String restrictedUsername="restrictedUser";
    private String restrictedPassword="restrictedReadOnly";
   
    private String patientUsername="patientsAccount";
    private String patientPassword="patientsPassword";
    
    private String technicianUsername="technicianAccount";
    private String technicianPassword="technicianPassword";
    
    private String doctorUsername="doctorAccount";
    private String doctorPassword="doctorPassword";
    
    private String adminUsername="adminAccount";
    private String adminPassword="adminPassword";
    private DBConnector() {
    }
    /**
     * Method allows connecting to the mysql server, for the given account.
     * @return True in case of successful login and false in other case.
     */
    public boolean createDatabaseRestrictedConnection(){
           return connectToTheDB(driverName,dbUrl,serverName,port,databaseName,restrictedUsername,restrictedPassword);
    }
    public boolean createDatabasePatientConnection(){
           return connectToTheDB(driverName,dbUrl,serverName,port,databaseName,patientUsername,patientPassword);   
    }
    public boolean createDatabaseDoctorConnection(){
           return connectToTheDB(driverName,dbUrl,serverName,port,databaseName,doctorUsername,doctorPassword);   
    }
    public boolean createDatabaseAdminConnection(){
           return connectToTheDB(driverName,dbUrl,serverName,port,databaseName,adminUsername,adminPassword);   
    }
      public boolean createDatabaseTechnicianConnection(){
           return connectToTheDB(driverName,dbUrl,serverName,port,databaseName,technicianUsername,technicianPassword);   
    }
    /**
     * Method connects to the database.
     *
     * @param className The classname of driver.
     * @param url Url of the database.
     * @param serverName Name of the server.
     * @param port Port of the connection.
     * @param database The name of the database.
     * @param username The name of the username.
     * @param password The password for the username.
     * @return True - if connected, false - otherwise.
     */
    public boolean connectToTheDB(String className, String url, String serverName, String port, String database, String username, String password) {
        try {
            Class.forName(className);
            con = DriverManager.getConnection(url + serverName + ":" + port + "/" + database, username, password);
            data = con.getMetaData();
            statement = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            dbmd = con.getMetaData();
        
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(DBConnector.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }
    public Statement getStatement(){
        return statement;
    }
    public boolean disconnectFromServer(){
        try {
            con.close();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(DBConnector.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
}
