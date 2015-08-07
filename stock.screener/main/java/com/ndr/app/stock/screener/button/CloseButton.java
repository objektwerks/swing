package com.ndr.app.stock.screener.button;

import com.ndr.app.stock.screener.resource.ResourceManager;

import javax.annotation.PostConstruct;
import javax.swing.JButton;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public final class CloseButton extends JButton {
    private static final long serialVersionUID = -1835578516176977170L;
	@Autowired private ResourceManager resourceManager;

    @PostConstruct
    protected void build() {
        setIcon(resourceManager.getImageIcon("close.icon"));
        setToolTipText(resourceManager.getString("close"));
        ButtonSizer.instance.setDefaultToolBarSize(this);
        setDefaultCapable(true);
    }
}