package model.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author PeerZet
 */
public class DBPatientManager extends DBManager {

    public HashMap<String, String> getPatientInfo(String username) throws SQLException {
        ResultSet patientInfo = statement.executeQuery("SELECT * FROM MedicalPictures.Patient WHERE username='" + username + "'");
        HashMap<String, String> userMap = new HashMap<>();
        while (patientInfo.next()) {
            String name = patientInfo.getString("name");
            String surname = patientInfo.getString("surname");
            String age = patientInfo.getString("age");
            userMap.put("name", name);
            userMap.put("surname", surname);
            userMap.put("age", age);
        }
        return userMap;
    }

    public HashMap<String,byte[]> getPicturesData(String username) throws SQLException {
        ResultSet pictures = statement.executeQuery("SELECT picture_name,picture_data FROM MedicalPictures.Picture WHERE username='" + username + "'");
        HashMap<String,byte[]> pictureList = new HashMap<>();
        while (pictures.next()) {
            pictureList.put(pictures.getString("picture_name"),pictures.getBytes("picture_data"));
        }
        return pictureList;
    }

    public HashMap<String, HashMap<String, String>> getPicturesDescription(String username) throws SQLException {
        ResultSet pictureDescription = statement.executeQuery("SELECT p.id as id, p.picture_name as picture_name, p.capture_datetime AS capture_datetime, p.body_part as body_part,"
                + "p.picture_description as picture_description, d.name AS doctor_name ,d.surname as doctor_surname, t.name AS technician_name, t.surname AS technician_surname"
                + " FROM MedicalPictures.Picture p,MedicalPictures.Doctor d,MedicalPictures.Technician t WHERE p.username='" + username + "' AND p.doctor_username=d.username AND"
                + " p.technician_username=t.username");
        HashMap<String, HashMap<String, String>> picturesMap = new HashMap<>();
        HashMap<String, String> pictureDescriptionMap = new HashMap<>();
        while (pictureDescription.next()) {
            String id = pictureDescription.getString("id");
            String technician = pictureDescription.getString("technician_name") + " " + pictureDescription.getString("technician_surname");
            String doctor = pictureDescription.getString("doctor_name") + " " + pictureDescription.getString("doctor_surname");
            String capture_datetime = pictureDescription.getString("capture_datetime");
            String body_part = pictureDescription.getString("body_part");
            String picture_descrpiton = pictureDescription.getString("picture_description");
            String picture_name = pictureDescription.getString("picture_name");
            pictureDescriptionMap.put("id", id);
            pictureDescriptionMap.put("technician", technician);
            pictureDescriptionMap.put("doctor", doctor);
            pictureDescriptionMap.put("capture_datetime", capture_datetime);
            pictureDescriptionMap.put("body_part", body_part);
            pictureDescriptionMap.put("picture_description", picture_descrpiton);
            pictureDescriptionMap.put("picture_name", picture_name);
            picturesMap.put(id, pictureDescriptionMap);
            pictureDescriptionMap = new HashMap<>();
        }
        pictureDescription.close();
        return picturesMap;
    }

    public HashMap<Integer, HashMap<String, String>> getAllDiagnosis(String username) throws SQLException {
        ResultSet result = statement.executeQuery("SELECT diagnosis.id as id, d.name as doctor_name, d.surname as doctor_surname, diagnosis.description as description FROM"
                + " MedicalPictures.Diagnosis diagnosis JOIN MedicalPictures.Doctor d ON d.username=diagnosis.doctor_username WHERE diagnosis.username='" + username + "'");
        HashMap<String, String> diagnosisMap = new HashMap<>();
        HashMap<Integer, HashMap<String, String>> diagnosisFull = new HashMap<>();
        int i = 0;
        while (result.next()) {
            String doctor = result.getString("doctor_name") + " " + result.getString("doctor_surname");
            String description = result.getString("description");
            String id = result.getString("id");
            diagnosisMap.put("doctor", doctor);
            diagnosisMap.put("id", id);
            diagnosisMap.put("description", description);
            diagnosisFull.put(i, diagnosisMap);
            i++;
            diagnosisMap = new HashMap<>();
        }
        return diagnosisFull;
    }

    public void updatePictureDescription(String description, String username) throws SQLException {
        queryRunner.update(connection, "UPDATE MedicalPictures.Picture SET picture_description=? WHERE id=?", description, username);
    }

    public void removeDiagnosis(String id) throws SQLException {
        queryRunner.update(connection, "DELETE FROM MedicalPictures.Diagnosis WHERE id=?", id);
    }

    public void updateDiagnosis(String id, String description) throws SQLException {
        queryRunner.update(connection, "UPDATE MedicalPictures.Diagnosis SET description=? WHERE id=?", description, id);
    }

    public void insertDiagnosis(String diagnosis, String patientUsername, String doctorUsername) throws SQLException {
        queryRunner.update(connection, "INSERT INTO MedicalPictures.Diagnosis VALUES(UUID(),?,?,?)", patientUsername, doctorUsername, diagnosis);
    }
}
