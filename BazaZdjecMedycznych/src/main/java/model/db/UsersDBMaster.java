/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package model.db;

import java.sql.Statement;

/**
 *
 * @author peer
 */
public class UsersDBMaster {
    private Statement statement = DBConnector.master.getStatement();

}
