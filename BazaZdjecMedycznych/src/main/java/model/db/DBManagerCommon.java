package model.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

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

    public void addBodyPart(String bodyPart) throws SQLException {
        queryRunner.update(connection, "INSERT INTO MedicalPictures.BodyPart VALUES(?)", bodyPart);
    }

    public void deleteBodyParts(List<String> bodyParts) throws SQLException {
        for (String bodyPart : bodyParts) {
            queryRunner.update(connection, "DELETE FROM MedicalPictures.BodyPart WHERE  body_part=?", bodyPart);
        }
    }

    public ObservableList<String> getPictureTypes() throws SQLException {
        ResultSet pictureTypes = statement.executeQuery("SELECT * FROM MedicalPictures.PictureType");
        ObservableList<String> pictureTypeList = FXCollections.observableArrayList();
        while (pictureTypes.next()) {
            pictureTypeList.add(pictureTypes.getString("picture_type"));
        }
        return pictureTypeList;
    }

    public void addPictureType(String pictureType) throws SQLException {
        queryRunner.update(connection, "INSERT INTO MedicalPictures.PictureType VALUES(?)", pictureType);
    }

    public void deletePictureType(List<String> pictureTypes) throws SQLException {
        for (String pictureType : pictureTypes) {
            queryRunner.update(connection, "DELETE FROM MedicalPictures.PictureType WHERE  body_part=?", pictureType);
        }
    }

    public String getDoctorUsername(String nameAndSurname) {
        int index = nameAndSurname.indexOf(" ");
        String username = nameAndSurname.substring(0, index);
        return username;
    }
}
