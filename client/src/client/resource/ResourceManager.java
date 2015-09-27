package client.resource;

import java.util.ResourceBundle;

import javax.swing.ImageIcon;

public final class ResourceManager {
    private ResourceBundle resourceBundle;

    public ResourceManager(ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
    }

    public String getString(String resource) {
        return resourceBundle.getString(resource);
    }

    public ImageIcon getImageIcon(String resource) {
        return new ImageIcon(getClass().getResource(resourceBundle.getString(resource)));
    }

    public int getInt(String resource) {
        return Integer.parseInt(getString(resource));
    }
}