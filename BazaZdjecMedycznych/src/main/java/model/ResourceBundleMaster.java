/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.ResourceBundle;

/**
 * Class is responsible for handling translations methods. SINGLETON.
 *
 * @author peer
 */
public class ResourceBundleMaster {

    public final static ResourceBundleMaster TRANSLATOR = new ResourceBundleMaster();
    private final ResourceBundle resourceBundle;
    private final String resourceBundleName = "messages_EN";

    private ResourceBundleMaster() {
        resourceBundle = ResourceBundle.getBundle(resourceBundleName);
    }

    /**
     * Method returns translation for the given key, taken from ResourceBundle
     *
     * @param key Key for which tanslation will be returned
     * @return Translation for the given key
     */
    public String getTranslation(String key) {
        try {
            String message = resourceBundle.getString(key);
            return message;
        } catch (Exception e) {
            System.err.println("Problem with finding translation for: " + key);
            return "";
        }
    }

    public ResourceBundle getResourceBundle() {
        return resourceBundle;
    }
}
