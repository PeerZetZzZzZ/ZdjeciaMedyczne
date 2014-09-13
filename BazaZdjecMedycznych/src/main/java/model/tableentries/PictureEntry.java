package model.tableentries;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author PeerZet
 */
public class PictureEntry {

    public SimpleStringProperty id;
    public SimpleStringProperty picture_name;
    public SimpleStringProperty capture_datetime;
    public SimpleStringProperty username;
    public SimpleStringProperty technician_name;
    public SimpleStringProperty doctor_name;
    public SimpleStringProperty body_part;
    public SimpleStringProperty picture_type;
    public String description;
    public String technician_username;
    public String doctor_username;
    public BooleanProperty selected;
    public boolean justAdded;//informs if this entry exists in table or was just added

    public PictureEntry(String id, String picture_name, String capture_datetime, String username, String technician_name, String doctor_name,
            String body_part, String picture_type, String description, String technician_username, String doctor_username, boolean selected, boolean justAdded) {
        this.id = new SimpleStringProperty(id);
        this.picture_name = new SimpleStringProperty(picture_name);
        this.capture_datetime = new SimpleStringProperty(capture_datetime);
        this.username = new SimpleStringProperty(username);
        this.technician_name = new SimpleStringProperty(technician_name);
        this.doctor_name = new SimpleStringProperty(doctor_name);
        this.body_part = new SimpleStringProperty(body_part);
        this.picture_type = new SimpleStringProperty(picture_type);
        this.selected = new SimpleBooleanProperty(selected);
        this.justAdded = justAdded;
        this.description = description;
        this.doctor_username = doctor_username;
        this.technician_username = technician_username;

    }

    public boolean getJustAdded() {
        return this.justAdded;
    }

    public String getId() {
        return id.get();
    }

    public String getPictureName() {
        return picture_name.get();
    }

    public boolean getSelected() {
        return selected.get();
    }

    public String getCapture_datetime() {
        return capture_datetime.get();
    }

    public String getUsername() {
        return username.get();
    }

    public String getTechnician_name() {
        return technician_name.get();
    }

    public String getDoctor_name() {
        return doctor_name.get();
    }

    public String getBody_part() {
        return body_part.get();
    }

    public String getPicture_type() {
        return picture_type.get();
    }

    public BooleanProperty selectedProperty() {
        return selected;
    }

    public StringProperty doctor_nameProperty() {
        return doctor_name;
    }

    public StringProperty body_partProperty() {
        return body_part;
    }

    public StringProperty picture_typeProperty() {
        return picture_type;
    }

    public String getDescription() {
        return description;
    }

    public String getTechnician_username() {
        return technician_username;
    }

    public String getDoctor_username() {
        return doctor_username;
    }

    public void setDoctorName(String name) {
        this.doctor_name.setValue(name);
    }

    public void setBodyPart(String name) {
        this.body_part.setValue(name);
    }

    public void setPictureType(String name) {
        this.picture_type.setValue(name);
    }
}
