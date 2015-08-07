package com.ndr.app.stock.screener.action;

import com.ndr.app.stock.screener.frame.Frame;

import java.awt.event.ActionEvent;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public final class ExitAction extends ConfigurableAction {
    private static final long serialVersionUID = 2952118490437226048L;
    
	@Autowired private Frame frame;

    @PostConstruct
    protected void build() {
        setSmallIcon(resourceManager.getImageIcon("exit.icon"));
        setTooltip(resourceManager.getString("exit"));
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        frame.onClose();
    }
}