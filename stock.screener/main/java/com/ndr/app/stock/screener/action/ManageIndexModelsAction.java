package com.ndr.app.stock.screener.action;

import com.ndr.app.stock.screener.dialog.IndexModelDialog;

import java.awt.event.ActionEvent;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public final class ManageIndexModelsAction extends ConfigurableAction {
    private static final long serialVersionUID = -1326649152424509208L;

	@Autowired private IndexModelDialog indexModelDialog;

    @PostConstruct
    protected void build() {
        setName(resourceManager.getString("edit"));
        setSmallIcon(resourceManager.getImageIcon("edit.icon"));
        setTooltip(resourceManager.getString("edit"));
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        indexModelDialog.view();
    }
}