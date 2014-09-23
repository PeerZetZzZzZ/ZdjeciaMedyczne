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
public class PictureTypeEntry {

    public SimpleStringProperty pictureType;
    public BooleanProperty selected;

    public PictureTypeEntry(String pictureTyp, boolean select) {
        this.pictureType = new SimpleStringProperty(pictureTyp);
        this.selected = new SimpleBooleanProperty(select);
    }

    public boolean getSelected() {
        return selected.get();
    }

    public String getPictureType() {
        return pictureType.get();
    }

    public BooleanProperty selectedProperty() {
        return selected;
    }
}
