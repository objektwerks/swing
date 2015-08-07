package com.ndr.app.stock.screener.action;

import com.ndr.app.stock.screener.resource.ResourceManager;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;

import org.springframework.beans.factory.annotation.Autowired;

public abstract class ConfigurableAction extends AbstractAction {
    private static final long serialVersionUID = 3330370708765094923L;

	@Autowired protected ResourceManager resourceManager;

    protected void setName(String name) {
        putValue(Action.NAME, name);
    }

    protected void setSmallIcon(Icon icon) {
        putValue(Action.SMALL_ICON, icon);
    }

    protected void setTooltip(String tooltip) {
        putValue(Action.SHORT_DESCRIPTION, tooltip);
    }
}