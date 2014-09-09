package model;

/**
 * This class is used to handle common information for whole application runtime
 * @author PeerZet
 */
public class Common {

    public static Common COMMON = new Common();
    private Common(){
        
    }
    private String loggedUser ="";
    public void setLoggedUser(String user){
        this.loggedUser=user;
    }
    public String getLoggedUser(){
        return loggedUser;
    }
}
