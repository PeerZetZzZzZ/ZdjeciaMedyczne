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
import model.enums.UserType;

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
    private String driverName = "com.mysql.jdbc.Driver";
    /**
     * The url for db connection.
     */
    private String dbUrl = "jdbc:mysql://";
    /**
     * The name of the server where DB exists.
     */
    private String serverName = "localhost";
    /**
     * The port for DB connection
     */
    private String port = "3306";
    /**
     * The database name we connect.
     */
    private String databaseName = "MedicalPictures";

    private String restrictedUsername = "restrictedUser";
    private String restrictedPassword = "restrictedReadOnly";

    private String patientUsername = "patientsAccount";
    private String patientPassword = "patientsPassword";

    private String technicianUsername = "technicianAccount";
    private String technicianPassword = "technicianPassword";

    private String doctorUsername = "doctorAccount";
    private String doctorPassword = "doctorPassword";

    private String adminUsername = "adminAccount";
    private String adminPassword = "adminPassword";

    private DBConnector() {
    }

    /**
     * Method allows connecting to the mysql server, for the given account.
     *
     * @return True in case of successful login and false in other case.
     */
    public boolean createDatabaseRestrictedConnection() {
        return connectToTheDB(driverName, dbUrl, serverName, port, databaseName, restrictedUsername, restrictedPassword);
    }

    public boolean createDatabasePatientConnection() {
        return connectToTheDB(driverName, dbUrl, serverName, port, databaseName, patientUsername, patientPassword);
    }

    public boolean createDatabaseDoctorConnection() {
        return connectToTheDB(driverName, dbUrl, serverName, port, databaseName, doctorUsername, doctorPassword);
    }

    public boolean createDatabaseAdminConnection() {
        return connectToTheDB(driverName, dbUrl, serverName, port, databaseName, adminUsername, adminPassword);
    }

    public boolean createDatabaseTechnicianConnection() {
        return connectToTheDB(driverName, dbUrl, serverName, port, databaseName, technicianUsername, technicianPassword);
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

    public Statement getStatement() {
        return statement;
    }

    public boolean disconnectFromServer() {
        try {
            con.close();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(DBConnector.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public void createUserInDatabaseWithTheGivenPermissions(String username, String password, UserType usertype) {
        try {
            switch (usertype) {
                case PATIENT:
                    statement.execute("CREATE USER '" + username + "'@'localhost' IDENTIFIED BY '" + password + "'");
                    statement.execute("GRANT SELECT ON MedicalPictures.Patient TO '" + username + "@'localhost'");
                    statement.execute("GRANT SELECT ON MedicalPictures.BodyParts TO '" + username + "@'localhost'");
                    statement.execute("GRANT SELECT ON MedicalPictures.Doctor TO '" + username + "@'localhost'");
                    statement.execute("GRANT SELECT ON MedicalPictures.Diagnosis TO '" + username + "@'localhost'");
                    statement.execute("GRANT SELECT ON MedicalPictures.Picture TO '" + username + "@'localhost'");
                    statement.execute("GRANT SELECT ON MedicalPictures.PictureType TO '" + username + "@'localhost'");
                    break;
                case ADMIN:
                    statement.execute("CREATE USER '" + username + "'@'localhost' IDENTIFIED BY '" + password + "'");
                    statement.execute("GRANT SELECT,UPDATE,INSERT,DELETE,ALTER,INDEX,UPDATE,REFERENCES,EXECUTE ON MedicalPictures.* TO '" + username + "'@'localhost'");
                    break;
                case DOCTOR:
                    statement.execute("CREATE USER '" + username + "'@'localhost' IDENTIFIED BY '" + password + "'");
                    statement.execute("GRANT SELECT ON MedicalPictures.Patient TO '" + username + "@'localhost'");
                    statement.execute("GRANT SELECT ON MedicalPictures.BodyParts TO '" + username + "@'localhost'");
                    statement.execute("GRANT SELECT ON MedicalPictures.Doctor TO '" + username + "@'localhost'");
                    statement.execute("GRANT SELECT,UPDATE,DELETE,ALTER  ON MedicalPictures.Diagnosis TO '" + username + "@'localhost'");
                    statement.execute("GRANT SELECT ON MedicalPictures.PictureType TO '" + username + "@'localhost'");
                    statement.execute("GRANT GRANT SELECT,UPDATE,ALTER  ON MedicalPictures.Picture TO '" + username + "@'localhost'");
                    break;
                case TECHNICIAN:
                    statement.execute("CREATE USER '" + username + "'@'localhost' IDENTIFIED BY '" + password + "'");
                    statement.execute("GRANT SELECT ON MedicalPictures.Patient TO '" + username + "@'localhost'");
                    statement.execute("GRANT SELECT ON MedicalPictures.BodyParts TO '" + username + "@'localhost'");
                    statement.execute("GRANT SELECT ON MedicalPictures.Doctor TO '" + username + "@'localhost'");
                    statement.execute("GRANT SELECT ON MedicalPictures.Technician TO '" + username + "@'localhost'");
                    statement.execute("GRANT SELECT ON MedicalPictures.PictureType TO '" + username + "@'localhost'");
                    statement.execute("GRANT SELECT,UPDATE,DELETE,ALTER ON MedicalPictures.Picture TO '" + username + "@'localhost'");
                    statement.execute("GRANT SELECT,UPDATE,DELETE,ALTER ON MedicalPictures.PictureCapture TO '" + username + "@'localhost'");
                    break;
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBConnector.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *  Method creates the basic database schema, indcluding tables, users and their permissions. Users- users which will be used for database server connection.
     * @throws SQLException When there is a problem with executing some statement
     */
    public void createDatabaseSchema() throws SQLException {
        statement.execute("CREATE TABLE IF NOT EXISTS MedicalPictures.UsersDB(id integer primary key, username varchar(100),password varchar(100),account_type varchar(15))");
        statement.execute("CREATE TABLE IF NOT EXISTS MedicalPictures.Patient(id integer, name varchar(100), surname varchar(100),sex varchar(6),address varchar(150), age integer, foreign key (id) references UsersDB(id))");
        statement.execute("CREATE TABLE IF NOT EXISTS MedicalPictures.Admin(id integer, name varchar(100), surname varchar(100),sex varchar(6),address varchar(150), age integer, foreign key (id) references UsersDB(id))");
        statement.execute("CREATE TABLE IF NOT EXISTS MedicalPictures.Doctor(id integer, name varchar(100), surname varchar(100),sex varchar(6),address varchar(150), age integer,specialization varchar(50), foreign key (id) references UsersDB(id))");
        statement.execute("CREATE TABLE IF NOT EXISTS MedicalPictures.Technician(id integer, name varchar(100), surname varchar(100),sex varchar(6),address varchar(150), age integer,specialization varchar(50), foreign key (id) references UsersDB(id))");
        statement.execute("CREATE TABLE IF NOT EXISTS MedicalPictures.BodyPart(id integer primary key, body_part varchar(255))");
        statement.execute("CREATE TABLE IF NOT EXISTS MedicalPictures.PictureType(id integer primary key, picture_type varchar(255))");
        statement.execute("CREATE TABLE IF NOT EXISTS MedicalPictures.Diagnosis(id integer primary key, patient_id integer, doctor_id integer, description varchar(500), foreign key (patient_id) references MedicalPictures.Patient(id), foreign key (doctor_id) references MedicalPictures(id))");
        statement.execute("CREATE TABLE IF NOT EXISTS MedicalPictures.PictureCapture(id integer primary key, capture_datetime datetime, picture_id integer, technician_id integer, foreign key (picture_id) references MedicalPictures.Picture(id), foreign key (technician_id) references MedicalPictures.Technician(id))");
        statement.execute("CREATE USER 'restrictedUser'@'localhost' IDENTIFIED by 'restrictedReadOnly'");
        statement.execute("GRANT SELECT ON MedicalPictures.UsersDB TO 'restrictedUser'@'localhost'");
        createUserInDatabaseWithTheGivenPermissions(adminUsername, adminPassword, UserType.ADMIN);
        createUserInDatabaseWithTheGivenPermissions(adminUsername, adminPassword, UserType.DOCTOR);
        createUserInDatabaseWithTheGivenPermissions(adminUsername, adminPassword, UserType.TECHNICIAN);
        createUserInDatabaseWithTheGivenPermissions(adminUsername, adminPassword, UserType.PATIENT);
    }
}
