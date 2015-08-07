package com.ndr.app.stock.screener.statusbar;

import com.ndr.app.stock.screener.Colors;
import com.ndr.app.stock.screener.resource.ResourceManager;

import javax.annotation.PostConstruct;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import org.jdesktop.swingx.JXBusyLabel;
import org.jdesktop.swingx.JXStatusBar;
import org.jdesktop.swingx.painter.BusyPainter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StatusBar extends JXStatusBar {
    private static final long serialVersionUID = 5462926052803107514L;
    
	@Autowired private ResourceManager resourceManager;
    private JXBusyLabel busyLabel;
    private JLabel statusLabel;
    private JLabel userLabel;
    private JLabel criteriaModelStatusLabel;
    private JLabel indexModelStatusLabel;

    public void setBusyStatus(final boolean isBusy) {
        busyLabel.setBusy(isBusy);
    }

    public void setStatus(String message) {
        setText(statusLabel, message);
    }

    public void setUser(String name) {
        setText(userLabel, name);
    }

    public void setCriteriaModelStatus(String message) {
        setText(criteriaModelStatusLabel, message);
    }

    public void setIndexModelStatus(String message) {
        setText(indexModelStatusLabel, message);
    }

    @PostConstruct
    protected void build() {
        busyLabel = buildBusyLabel();
        statusLabel = new JLabel();
        userLabel = new JLabel(resourceManager.getImageIcon("user.icon"), SwingConstants.LEFT);
        criteriaModelStatusLabel = new JLabel(resourceManager.getImageIcon("criteria.model.icon"), SwingConstants.LEFT);
        indexModelStatusLabel = new JLabel(resourceManager.getImageIcon("index.model.icon"), SwingConstants.LEFT);
        add(busyLabel, Constraint.ResizeBehavior.FIXED);
        add(statusLabel, Constraint.ResizeBehavior.FILL);
        add(userLabel, Constraint.ResizeBehavior.FILL);
        add(criteriaModelStatusLabel, Constraint.ResizeBehavior.FILL);
        add(indexModelStatusLabel, Constraint.ResizeBehavior.FILL);
    }

    private JXBusyLabel buildBusyLabel() {
        JXBusyLabel busyLabel = new JXBusyLabel();
        BusyPainter busyPainter = new BusyPainter();
        busyPainter.setBaseColor(Colors.navy);
        busyPainter.setHighlightColor(Colors.tableRowColor);
        busyLabel.setBusyPainter(busyPainter);
        busyLabel.setDelay(1);
        return busyLabel;
    }
    
    private void setText(final JLabel label, final String text) {
        label.setText(text);
    }
}