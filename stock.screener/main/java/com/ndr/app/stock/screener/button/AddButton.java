package com.ndr.app.stock.screener.button;

import com.ndr.app.stock.screener.resource.ResourceManager;

import javax.annotation.PostConstruct;
import javax.swing.JButton;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public final class AddButton extends JButton {
    private static final long serialVersionUID = -3069081164186737711L;
	@Autowired private ResourceManager resourceManager;

    @PostConstruct
    protected void build() {
        setIcon(resourceManager.getImageIcon("add.icon"));
        setToolTipText(resourceManager.getString("add"));
        ButtonSizer.instance.setDefaultToolBarSize(this);
    }
}