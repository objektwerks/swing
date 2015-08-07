package com.ndr.app.stock.screener.action;

import com.ndr.app.stock.screener.dialog.CriteriaModelDialog;

import java.awt.event.ActionEvent;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public final class ManageCriteriaModelsAction extends ConfigurableAction {
    private static final long serialVersionUID = 91787305228250927L;

	@Autowired private CriteriaModelDialog criteriaModelDialog;

    @PostConstruct
    protected void build() {
        setName(resourceManager.getString("edit"));
        setSmallIcon(resourceManager.getImageIcon("edit.icon"));
        setTooltip(resourceManager.getString("edit"));
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        criteriaModelDialog.view();
    }
}