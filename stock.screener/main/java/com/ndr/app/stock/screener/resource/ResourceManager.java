package com.ndr.app.stock.screener.resource;

import java.awt.Image;

import javax.swing.ImageIcon;

import org.springframework.context.support.ResourceBundleMessageSource;

public final class ResourceManager extends ResourceBundleMessageSource {
    public String getString(String resource) {
        return getMessage(resource, null, null);
    }

    public int getInt(String resource) {
        return Integer.parseInt(getString(resource));
    }

    public double getDouble(String resource) {
        return Double.parseDouble(getString(resource));
    }

    public Image getImage(String resource) {
        return getImageIcon(resource).getImage();
    }

    public ImageIcon getImageIcon(String resource) {
        return new ImageIcon(getClass().getResource(getString(resource)));
    }
}