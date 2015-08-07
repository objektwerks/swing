package com.ndr.app.stock.screener.button;

import com.ndr.app.stock.screener.resource.ResourceManager;

import javax.annotation.PostConstruct;
import javax.swing.JButton;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public final class OpenButton extends JButton {
    private static final long serialVersionUID = 3165655727110054786L;
	@Autowired private ResourceManager resourceManager;

    @PostConstruct
    protected void build() {
        setIcon(resourceManager.getImageIcon("open.icon"));
        setToolTipText(resourceManager.getString("open"));
        ButtonSizer.instance.setDefaultToolBarSize(this);
    }
}