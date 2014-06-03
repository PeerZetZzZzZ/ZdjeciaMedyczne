/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.login;

import model.ResourceBundleMaster;
import model.db.DBConnector;
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
    public String connectToDatabase(String username, String password) {
        boolean result = connector.createDatabaseAdminConnection();//first we 
        if (result) {
            if (username != null && password != null) {
                if (failsCounter < 5) {
                    //boolean result = connector.createDatabaseConnection(username, password);       
                    dateTimeNow = DateTime.now().plusSeconds(30);
                    if (!result) {
                        failsCounter++;
                        return ResourceBundleMaster.translator.getTranslation("unsuccessfulLoginMessage");
                    }
                    return "Successful";
                } else {

                    DateTime punishmentTime = DateTime.now();
                    Seconds secondsBetween = Seconds.secondsBetween(punishmentTime, dateTimeNow);
                    if (secondsBetween.getSeconds() > 0) {
                        failsCounter = 0;
                    }
                    //Return information about unsuccessful login and time punishment
                    return ResourceBundleMaster.translator.getTranslation("waitPunishmentMessage")
                            + String.valueOf(secondsBetween.getSeconds()) + ResourceBundleMaster.translator.getTranslation("seconds");

                }
            }
            return ResourceBundleMaster.translator.getTranslation("emptyUsernameOrPasswordMessage");
        }
        return ResourceBundleMaster.translator.getTranslation("unsuccessfulLoginMessage");

    }

    public boolean connectToApplication(String username, String password) {

    }

}
