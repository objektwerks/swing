package com.ndr.app.stock.screener.dialog;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;

public class HideDialogWindowAdapter extends WindowAdapter {
    private JDialog dialog;

    public HideDialogWindowAdapter(JDialog dialog) {
        this.dialog = dialog;
    }

    public void windowClosing(WindowEvent event) {
        dialog.setVisible(false);
    }
}