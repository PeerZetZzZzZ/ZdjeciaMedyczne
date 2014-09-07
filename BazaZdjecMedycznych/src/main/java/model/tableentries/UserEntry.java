/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.tableentries;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author peer
 */
public class UserEntry {

    public SimpleStringProperty username;
    public SimpleStringProperty password;
    public SimpleStringProperty accountType;
    public BooleanProperty selected;

    public UserEntry(String username, String password, String accountType, Boolean selected) {
        this.username = new SimpleStringProperty(username);
        this.password = new SimpleStringProperty(password);
        this.accountType = new SimpleStringProperty(accountType);
        this.selected = new SimpleBooleanProperty(selected);
    }

    public Boolean getSelected() {
        return selected.get();
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
    public BooleanProperty selectedProperty(){ //neccessary if we want to have this checkbox value in Table
        return selected;
    }
}
