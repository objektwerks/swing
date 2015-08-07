package com.ndr.app.stock.screener.button;

import java.awt.Dimension;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;

public enum ButtonSizer {
    instance;

    public JButton setDefaultToolBarSize(JButton button) {
        return (JButton) defaultToolBarSize(button);
    }

    public JToggleButton setDefaultToolBarSize(JToggleButton button) {
        return (JToggleButton) defaultToolBarSize(button);
    }

    public JButton setDefaultDialogSize(JButton button) {
        return defaultDialogSize(button);
    }

    private AbstractButton defaultToolBarSize(AbstractButton button) {
        button.setFocusPainted(false);
        button.setRolloverEnabled(true);
        button.setVerticalAlignment(SwingConstants.CENTER);
        button.setHorizontalAlignment(SwingConstants.CENTER);
        button.setMinimumSize(new Dimension(45, 30));
        button.setMaximumSize(new Dimension(45, 30));
        button.setPreferredSize(new Dimension(45, 30));
        return button;
    }

    private JButton defaultDialogSize(JButton button) {
        button.setVerticalAlignment(SwingConstants.CENTER);
        button.setHorizontalAlignment(SwingConstants.CENTER);
        button.setMinimumSize(new Dimension(110, 30));
        button.setMaximumSize(new Dimension(110, 30));
        button.setPreferredSize(new Dimension(110, 30));
        return button;
    }
}