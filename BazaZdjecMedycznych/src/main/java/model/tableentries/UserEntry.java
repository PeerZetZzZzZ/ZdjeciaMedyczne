/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.tableentries;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author peer
 */
public class UserEntry {

    public SimpleIntegerProperty id ;
    public SimpleStringProperty username ;
    public SimpleStringProperty password ;
    public SimpleStringProperty accountType;

    public UserEntry(Integer id,String username,String password,String accountType){
        this.id= new SimpleIntegerProperty(id);
        this.username= new SimpleStringProperty(username);
        this.password= new SimpleStringProperty(password);
        this.accountType= new SimpleStringProperty(accountType);
    }
    public Integer getUserId() {
        return id.get();
    }
    public String getUsername() {
        return username.get();
    }
    public String getPassword() {
        return password.get();
    }
    public String getAccountType() {
        return accountType.get();
    }
}
