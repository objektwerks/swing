package com.ndr.app.stock.screener.button;

import com.ndr.app.stock.screener.resource.ResourceManager;

import javax.annotation.PostConstruct;
import javax.swing.JButton;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public final class RemoveButton extends JButton {
    private static final long serialVersionUID = -5459311753437801033L;
	@Autowired private ResourceManager resourceManager;

    @PostConstruct
    protected void build() {
        setIcon(resourceManager.getImageIcon("remove.icon"));
        setToolTipText(resourceManager.getString("remove"));
        ButtonSizer.instance.setDefaultToolBarSize(this);
    }
}