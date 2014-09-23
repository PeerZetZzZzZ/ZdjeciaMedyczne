package model.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.commons.dbutils.QueryRunner;

/**
 *
 * @author PeerZet
 */
public class DBManagerCommon extends DBManager {

    /**
     * It returns username , name+ + surname
     *
     * @return
     * @throws SQLException
     */
    public ObservableList<String> getDoctors() throws SQLException {
        ResultSet doctors = statement.executeQuery("SELECT username,name,surname FROM MedicalPictures.Doctor");
        ObservableList<String> doctorsList = FXCollections.observableArrayList();
        while (doctors.next()) {
            String username = doctors.getString("username");
            String name = doctors.getString("name");
            String surname = doctors.getString("surname");
            String doctorSummary = username + " " + name + " " + surname;
            doctorsList.add(doctorSummary);
        }
        return doctorsList;
    }

    public String getTechnician(String username) throws SQLException {
        ResultSet technicians = statement.executeQuery("SELECT name,surname FROM MedicalPictures.Technician WHERE username='" + username + "'");
        String technicianSummary = "";
        while (technicians.next()) {
            String name = technicians.getString("name");
            String surname = technicians.getString("surname");
            technicianSummary = name + " " + surname;
        }
        return technicianSummary;
    }

    public ObservableList<String> getBodyParts() throws SQLException {
        ResultSet bodyParts = statement.executeQuery("SELECT * FROM MedicalPictures.BodyPart");
        ObservableList<String> bodyPartsList = FXCollections.observableArrayList();
        while (bodyParts.next()) {
            bodyPartsList.add(bodyParts.getString("body_part"));
        }
        return bodyPartsList;
    }

    public ObservableList<String> getPictureTypes() throws SQLException {
        ResultSet pictureTypes = statement.executeQuery("SELECT * FROM MedicalPictures.PictureType");
        ObservableList<String> pictureTypeList = FXCollections.observableArrayList();
        while (pictureTypes.next()) {
            pictureTypeList.add(pictureTypes.getString("picture_type"));
        }
        return pictureTypeList;
    }

    public String getDoctorUsername(String nameAndSurname) {
        int index = nameAndSurname.indexOf(" ");
        String username = nameAndSurname.substring(0, index);
        return username;
    }
}
