package client.action;

import client.resource.ResourceManager;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

public final class ExitAction extends AbstractAction {
    private ResourceManager resourceManager;

    public ExitAction() {
        super();
    }

    public void build() {
        putValue(Action.SMALL_ICON, resourceManager.getImageIcon("icon.exit"));
        putValue(Action.NAME, resourceManager.getString("action.exit"));
    }

    public void actionPerformed(ActionEvent event) {
        System.exit(0);
    }

    public void setResourceManager(ResourceManager resourceManager) {
        this.resourceManager = resourceManager;
    }
}