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
import model.exception.RegexException;
import org.apache.commons.dbutils.QueryRunner;

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
    private QueryRunner run = new QueryRunner();//used to run easy queries from apache dbutils

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

    private String patientUsername = "pAccount";
    private String patientPassword = "patientsPassword";

    private String technicianUsername = "tAccount";
    private String technicianPassword = "technicianPassword";

    private String doctorUsername = "dAccount";
    private String doctorPassword = "doctorPassword";

    private String adminUsername = "aAccount";
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

    public boolean createDatabaseRootConnection(String password) {
        return connectToTheDB(driverName, dbUrl, serverName, port, databaseName, "root", password);
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

    public Connection getConnection() {
        return con;
    }

    public QueryRunner getQueryRunner() {
        return run;
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
                    statement.execute("GRANT SELECT ON MedicalPictures.Patient TO '" + username + "'@'localhost'");
                    statement.execute("GRANT SELECT ON MedicalPictures.BodyPart TO '" + username + "'@'localhost'");
                    statement.execute("GRANT SELECT ON MedicalPictures.Doctor TO '" + username + "'@'localhost'");
                    statement.execute("GRANT SELECT ON MedicalPictures.Diagnosis TO '" + username + "'@'localhost'");
                    statement.execute("GRANT SELECT ON MedicalPictures.Picture TO '" + username + "'@'localhost'");
                    statement.execute("GRANT SELECT ON MedicalPictures.PictureType TO '" + username + "'@'localhost'");
                    statement.execute("GRANT SELECT ON MedicalPictures.UsersDB TO '" + username + "'@'localhost'");
                    statement.execute("GRANT SELECT ON MedicalPictures.Technician TO '" + username + "'@'localhost'");
                    break;
                case ADMIN:
                    statement.execute("CREATE USER '" + username + "'@'localhost' IDENTIFIED BY '" + password + "'");
                    statement.execute("GRANT SELECT,UPDATE,INSERT,DELETE,ALTER,INDEX,UPDATE,REFERENCES,EXECUTE ON MedicalPictures.* TO '" + username + "'@'localhost'");
                    break;
                case DOCTOR:
                    statement.execute("CREATE USER '" + username + "'@'localhost' IDENTIFIED BY '" + password + "'");
                    statement.execute("GRANT SELECT ON MedicalPictures.Patient TO '" + username + "'@'localhost'");
                    statement.execute("GRANT SELECT ON MedicalPictures.BodyPart TO '" + username + "'@'localhost'");
                    statement.execute("GRANT SELECT ON MedicalPictures.Doctor TO '" + username + "'@'localhost'");
                    statement.execute("GRANT SELECT,UPDATE,DELETE,ALTER,INSERT  ON MedicalPictures.Diagnosis TO '" + username + "'@'localhost'");
                    statement.execute("GRANT SELECT ON MedicalPictures.PictureType TO '" + username + "'@'localhost'");
                    statement.execute("GRANT SELECT,UPDATE,ALTER  ON MedicalPictures.Picture TO '" + username + "'@'localhost'");
                    statement.execute("GRANT SELECT ON MedicalPictures.UsersDB TO '" + username + "'@'localhost'");
                    statement.execute("GRANT SELECT ON MedicalPictures.Technician TO '" + username + "'@'localhost'");

                    break;
                case TECHNICIAN:
                    statement.execute("CREATE USER '" + username + "'@'localhost' IDENTIFIED BY '" + password + "'");
                    statement.execute("GRANT SELECT ON MedicalPictures.Patient TO '" + username + "'@'localhost'");
                    statement.execute("GRANT SELECT ON MedicalPictures.BodyPart TO '" + username + "'@'localhost'");
                    statement.execute("GRANT SELECT ON MedicalPictures.Doctor TO '" + username + "'@'localhost'");
                    statement.execute("GRANT SELECT ON MedicalPictures.Technician TO '" + username + "'@'localhost'");
                    statement.execute("GRANT SELECT ON MedicalPictures.PictureType TO '" + username + "'@'localhost'");
                    statement.execute("GRANT SELECT,UPDATE,DELETE,ALTER,INSERT ON MedicalPictures.Picture TO '" + username + "'@'localhost'");
                    statement.execute("GRANT FILE ON *.* TO '" + username + "'@'localhost'");
                    statement.execute("GRANT SELECT ON MedicalPictures.UsersDB TO '" + username + "'@'localhost'");
                    break;
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBConnector.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Method creates the basic database schema, indcluding tables, users and
     * their permissions. Users- users which will be used for database server
     * connection.
     *
     * @throws SQLException When there is a problem with executing some
     * statement
     */
    public void createDatabaseSchema() throws SQLException {
        statement.execute("CREATE TABLE IF NOT EXISTS MedicalPictures.UsersDB(username varchar(100) primary key,password varchar(100),account_type varchar(15))");

        statement.execute("CREATE TABLE IF NOT EXISTS MedicalPictures.Patient(username varchar(100), name varchar(100), surname varchar(100),sex varchar(6), age integer, foreign key (username) references UsersDB(username) ON DELETE CASCADE ON UPDATE CASCADE)");
        statement.execute("CREATE TABLE IF NOT EXISTS MedicalPictures.Admin(username varchar(100), name varchar(100), surname varchar(100),sex varchar(6), age integer, foreign key (username) references UsersDB(username) ON DELETE CASCADE ON UPDATE CASCADE)");
        statement.execute("CREATE TABLE IF NOT EXISTS MedicalPictures.Doctor(username varchar(100), name varchar(100), surname varchar(100),sex varchar(6), age integer,specialization varchar(50), foreign key (username) references UsersDB(username) ON DELETE CASCADE ON UPDATE CASCADE)");
        statement.execute("CREATE TABLE IF NOT EXISTS MedicalPictures.Technician(username varchar(100), name varchar(100), surname varchar(100),sex varchar(6), age integer, foreign key (username) references UsersDB(username) ON DELETE CASCADE ON UPDATE CASCADE)");

        statement.execute("CREATE TABLE IF NOT EXISTS MedicalPictures.BodyPart(body_part varchar(100) primary key)");
        statement.execute("CREATE TABLE IF NOT EXISTS MedicalPictures.PictureType(picture_type varchar(100) primary key)");

        statement.execute("CREATE TABLE IF NOT EXISTS MedicalPictures.Diagnosis(id varchar(36) primary key, username varchar(100), doctor_username varchar(100), description varchar(500), foreign key (username) references MedicalPictures.Patient(username) ON DELETE CASCADE ON UPDATE CASCADE)");
        statement.execute("CREATE TABLE IF NOT EXISTS MedicalPictures.Picture(id varchar(36) primary key,picture_name varchar(100), capture_datetime datetime, picture_data LONGBLOB, username varchar(100), technician_username varchar(100),"
                + "         doctor_username varchar(100), body_part varchar(100), picture_type varchar(100),picture_description varchar(200), foreign key (username) references MedicalPictures.Patient(username) ON DELETE CASCADE ON UPDATE CASCADE)");
        createUserInDatabaseWithTheGivenPermissions(adminUsername, adminPassword, UserType.ADMIN);
        createUserInDatabaseWithTheGivenPermissions(doctorUsername, doctorPassword, UserType.DOCTOR);
        createUserInDatabaseWithTheGivenPermissions(technicianUsername, technicianPassword, UserType.TECHNICIAN);
        createUserInDatabaseWithTheGivenPermissions(patientUsername, patientPassword, UserType.PATIENT);
    }

    public void logout() throws SQLException {
        this.con.close();
    }

    public UserType createSpecifiedDatabaseConnection(String username, String password) throws SQLException, RegexException {
        return connectToApplication(username, password);
    }

    public UserType connectToApplication(String username, String password) throws SQLException, RegexException {
        DBUsersManager managerUsers = new DBUsersManager();
        UserType user = managerUsers.readSingleUserType(username, password);
        disconnectFromServer();
        if (user != null) {
            switch (user) {
                case ADMIN:
                    boolean result = createDatabaseAdminConnection();
                    if (result) {
                        return user;
                    }
                    break;
                case DOCTOR:
                    if (createDatabaseDoctorConnection()) {
                        return user;
                    }
                case TECHNICIAN:
                    if (createDatabaseTechnicianConnection()) {
                        return user;
                    }
                case PATIENT:
                    if (createDatabasePatientConnection()) {
                        return user;
                    }
            }
        } else {
            return null;
        }
        return null;
    }
}
