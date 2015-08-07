package com.ndr.app.stock.screener.button;

import com.ndr.app.stock.screener.resource.ResourceManager;

import java.awt.Dimension;

import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.JToggleButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public final class LockedUnlockedToggleButton extends JToggleButton {
    private static final long serialVersionUID = 962383647044779073L;
    
	private ResourceManager resourceManager;

    public LockedUnlockedToggleButton(ResourceManager resourceManager) {
        super();
        this.resourceManager = resourceManager;
        build();
    }

    protected void build() {
        setIcon(resourceManager.getImageIcon("toggle.button.unlocked.icon"));
        setMinimumSize(new Dimension(35, 25));
        setMaximumSize(new Dimension(35, 25));
        setPreferredSize(new Dimension(35, 25));
        setFocusPainted(false);
        addChangeListener(new ToggleButtonChangeListener());
    }

    private class ToggleButtonChangeListener implements ChangeListener {
        @Override
        public void stateChanged(ChangeEvent event) {
            AbstractButton button = (AbstractButton) event.getSource();
            ButtonModel model = button.getModel();
            if (model.isSelected()) {
                button.setIcon(resourceManager.getImageIcon("toggle.button.locked.icon"));
            } else {
                button.setIcon(resourceManager.getImageIcon("toggle.button.unlocked.icon"));
            }
        }
    }  
}