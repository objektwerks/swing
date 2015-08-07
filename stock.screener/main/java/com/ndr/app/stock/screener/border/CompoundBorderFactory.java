package com.ndr.app.stock.screener.border;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.border.Border;

public enum CompoundBorderFactory {
    instance;

    public void create(JComponent component, Color color) {
        Border emptyBorder = BorderFactory.createEmptyBorder(0, 0, 0, 0);
        Border lineBorder = BorderFactory.createLineBorder(color);
        component.setBorder(BorderFactory.createCompoundBorder(emptyBorder, lineBorder));
    }

    public void create(JComponent component, String title) {
        Border titleBorder = BorderFactory.createTitledBorder(title);
        Border emptyBorder = BorderFactory.createEmptyBorder(0, 0, 0, 0);
        component.setBorder(BorderFactory.createCompoundBorder(titleBorder, emptyBorder));
    }
}