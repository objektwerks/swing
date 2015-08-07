package com.ndr.app.stock.screener.button;

import com.ndr.app.stock.screener.resource.ResourceManager;

import javax.annotation.PostConstruct;
import javax.swing.JButton;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public final class EditButton extends JButton {
    private static final long serialVersionUID = -2853649356817601646L;
	@Autowired private ResourceManager resourceManager;

    @PostConstruct
    protected void build() {
        setIcon(resourceManager.getImageIcon("edit.icon"));
        setToolTipText(resourceManager.getString("edit"));
        ButtonSizer.instance.setDefaultToolBarSize(this);
    }
}