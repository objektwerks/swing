package com.ndr.app.stock.screener.frame;

import com.ndr.app.stock.screener.ApplicationProxy;
import com.ndr.service.stock.screener.StockScreenerUserService;

import java.awt.Dimension;
import java.awt.Point;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public final class OnCloseHandler {
    @Autowired private Frame frame;
    @Autowired private StockScreenerUserService stockScreenerUserServiceProxy;

    public void onClose() {
        Preferences preferences = Preferences.userNodeForPackage(Frame.class);
        Dimension size = frame.getSize();
        preferences.putInt("frame.width", size.width);
        preferences.putInt("frame.height", size.height);
        Point location = frame.getLocation();
        preferences.putInt("frame.location.x", location.x);
        preferences.putInt("frame.location.y", location.y);
        try {
            preferences.flush();
        } catch (BackingStoreException e) {
            e.printStackTrace();
        }
        stockScreenerUserServiceProxy.logout(ApplicationProxy.instance.getUser());
        frame.setVisible(false);
        frame.dispose();
        System.exit(0);
    }
}