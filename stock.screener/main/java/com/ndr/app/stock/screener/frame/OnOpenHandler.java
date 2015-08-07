package com.ndr.app.stock.screener.frame;

import com.ndr.app.stock.screener.resource.ResourceManager;

import java.awt.Point;
import java.util.prefs.Preferences;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public final class OnOpenHandler {
    @Autowired private Frame frame;
    @Autowired private ResourceManager resourceManager;

    public void onOpen() {
        Preferences preferences = Preferences.userNodeForPackage(Frame.class);
        int defaultWidth = resourceManager.getInt("frame.default.width");
        int defaultHeight = resourceManager.getInt("frame.default.height");
        frame.setSize(preferences.getInt("frame.width", defaultWidth),
                      preferences.getInt("frame.height", defaultHeight));
        Point defaultLocation = frame.getDefaultScreenLocation();
        frame.setLocation(preferences.getInt("frame.location.x", defaultLocation.x),
                          preferences.getInt("frame.location.y", defaultLocation.y));
    }
}