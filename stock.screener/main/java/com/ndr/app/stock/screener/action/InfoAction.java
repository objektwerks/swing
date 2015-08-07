package com.ndr.app.stock.screener.action;

import com.ndr.app.stock.screener.dialog.InfoDialog;

import java.awt.event.ActionEvent;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public final class InfoAction extends ConfigurableAction {
    private static final long serialVersionUID = -5144568536973298826L;
    
	@Autowired private InfoDialog infoDialog;

    @PostConstruct
    protected void build() {
        setSmallIcon(resourceManager.getImageIcon("info.icon"));
        setTooltip(resourceManager.getString("info"));
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        infoDialog.view();
    }
}