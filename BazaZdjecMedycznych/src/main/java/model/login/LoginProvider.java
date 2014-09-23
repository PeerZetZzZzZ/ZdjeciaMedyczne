/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.login;

import java.sql.SQLException;
import model.ResourceBundleMaster;
import model.db.DBConnector;
import model.db.DBUsersManager;
import model.enums.UserType;
import model.exception.RegexException;
import org.joda.time.DateTime;
import org.joda.time.Seconds;

/**
 *
 * @author peer
 */
public class LoginProvider {

    private DBConnector connector = DBConnector.master;
    /**
     * Used for counting not successful logins.
     */
    private int failsCounter = 0;
    /**
     * Used for storge the time of not seccessful login
     */
    private DateTime dateTimeNow;

    public LoginProvider() {
    }

    /**
     * Method allows for loging to the database. User has 5 attemtps, later must
     * wait for 30 seconds, for next login
     *
     * @param username Username for connection.
     * @param password Password for the given username/
     * @return "successful" in case of success and punishment time left in case
     * of not success
     */
    public String connectToDatabase(String username, String password) throws SQLException, RegexException {
        boolean result = connector.createDatabaseRestrictedConnection();//first we create restricted connection
        UserType user = null;
        if (result) {
            if (!username.equals("") && !password.equals("")) {
                user = connector.createSpecifiedDatabaseConnection(username, password);
                dateTimeNow = DateTime.now().plusSeconds(30);
                if (!result || user == null) {
                    if (username.equals("root")) {
                        if (connector.createDatabaseRootConnection(password)) {
                            connector.createDatabaseSchema();
                            return "Successful root";
                        }
                    }
                    return ResourceBundleMaster.TRANSLATOR.getTranslation("unsuccessfulLoginMessage");
                }
                return "Successful " + user.toString();
            } else {
                return ResourceBundleMaster.TRANSLATOR.getTranslation("emptyUsernameOrPasswordMessage");
            }
        } else {
            return ResourceBundleMaster.TRANSLATOR.getTranslation("unsuccessfulLoginMessage");
        }

    }

}
