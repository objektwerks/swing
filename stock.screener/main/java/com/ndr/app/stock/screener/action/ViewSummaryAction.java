package com.ndr.app.stock.screener.action;

import com.ndr.app.stock.screener.dialog.SummaryDialog;

import java.awt.event.ActionEvent;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public final class ViewSummaryAction extends ConfigurableAction {
    @Autowired private SummaryDialog summaryDialog;
    
    @PostConstruct
    protected void build() {
        setSmallIcon(resourceManager.getImageIcon("summary.icon"));
        setTooltip(resourceManager.getString("summary.tooltip"));
        setEnabled(false);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        summaryDialog.view();
    }
}