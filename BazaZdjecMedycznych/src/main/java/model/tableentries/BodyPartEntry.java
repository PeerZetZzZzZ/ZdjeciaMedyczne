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
 * @author PeerZet
 */
public class BodyPartEntry {

    public SimpleStringProperty bodyPart;
    public BooleanProperty selected;

    public BodyPartEntry(String bodyP, boolean select) {
        this.bodyPart = new SimpleStringProperty(bodyP);
        this.selected = new SimpleBooleanProperty(select);
    }

    public boolean getSelected() {
        return selected.get();
    }

    public String getBodyPart() {
        return bodyPart.get();
    }

    public BooleanProperty selectedProperty() {
        return selected;
    }
}
