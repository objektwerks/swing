package com.ndr.app.stock.screener;

import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;

public enum LookAndFeelManager {
    instance;

    public void configure() throws Exception {
        ToolTipManager.sharedInstance().setDismissDelay(Integer.MAX_VALUE);
        UIManager.put("info", new ColorUIResource(255, 255, 255));
        UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
    }
}