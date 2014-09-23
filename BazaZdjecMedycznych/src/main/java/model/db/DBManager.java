package model.db;

import java.sql.Connection;
import java.sql.Statement;
import org.apache.commons.dbutils.QueryRunner;

/**
 *
 * @author PeerZet
 */
public class DBManager {

    protected Connection connection = DBConnector.master.getConnection();
    protected Statement statement = DBConnector.master.getStatement();
    protected QueryRunner queryRunner = DBConnector.master.getQueryRunner();//for easy quries
}
