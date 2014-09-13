package model.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.dbutils.QueryRunner;

/**
 *
 * @author PeerZet
 */
public class DBManager {

    private Connection connection = DBConnector.master.getConnection();
    private Statement statement = DBConnector.master.getStatement();
    private QueryRunner queryRunner = DBConnector.master.getQueryRunner();//for easy quries
    
    public List<String> getDoctors() throws SQLException{
        ResultSet doctors = statement.executeQuery("SELECT name,surname FROM MedicalPictures.Doctor");
        List<String> doctorsList = new ArrayList<>();
        while(doctors.next()){
            String name = doctors.getString("name");
            String surname = doctors.getString("surname");
            String doctorSummary = name + " "+surname;
            doctorsList.add(doctorSummary);
        }
        return doctorsList;
    }
    public List<String> getTechnicians() throws SQLException{
        ResultSet technicians = statement.executeQuery("SELECT name,surname FROM MedicalPictures.Technician");
        List<String> techniciansList = new ArrayList<>();
        while(technicians.next()){
            String name = technicians.getString("name");
            String surname = technicians.getString("surname");
            String technicianSummary = name + " "+surname;
            techniciansList.add(technicianSummary);
        }
        return techniciansList;
    }
}
