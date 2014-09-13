package model.tableentries;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author PeerZet
 */
public class PictureEntry {

    public SimpleStringProperty id;
    public SimpleStringProperty capture_datetime;
    public SimpleStringProperty username;
    public SimpleStringProperty technician_username;
    public SimpleStringProperty doctor_username;
    public SimpleStringProperty body_part;
    public SimpleStringProperty picture_type;
    public BooleanProperty selected;

    public PictureEntry(String id, String capture_datetime, String username, String technician_username, String doctor_username,
            String body_part, String picture_type) {
        this.id = new SimpleStringProperty(id);
        this.capture_datetime = new SimpleStringProperty(capture_datetime);
        this.username = new SimpleStringProperty(username);
        this.technician_username = new SimpleStringProperty(technician_username);
        this.doctor_username = new SimpleStringProperty(doctor_username);
        this.body_part = new SimpleStringProperty(body_part);
        this.picture_type = new SimpleStringProperty(picture_type);

    }

    public String getId() {
        return id.get();
    }

    public String getCapture_datetime() {
        return capture_datetime.get();
    }

    public String getUsername() {
        return username.get();
    }

    public String getTechnician_username() {
        return technician_username.get();
    }

    public String getDoctor_username() {
        return doctor_username.get();
    }

    public String getBody_part() {
        return body_part.get();
    }

    public String getPicture_type() {
        return picture_type.get();
    }
    public BooleanProperty selectedProperty(){
        return selected;
    }

}
