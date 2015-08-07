package com.ndr.app.stock.screener.action;

import com.ndr.app.stock.screener.dialog.CriteriaModelDialog;
import com.ndr.app.stock.screener.dialog.IndexModelDialog;
import com.ndr.app.stock.screener.panel.CriteriaIndexModelsPanel;
import com.ndr.app.stock.screener.transition.Transition;
import com.ndr.app.stock.screener.transition.TransitionListener;

import java.awt.event.ActionEvent;

import javax.annotation.PostConstruct;
import javax.swing.JPanel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TransitionToCriteriaIndexModelsPanelAction extends ConfigurableAction implements Transition {
    private static final long serialVersionUID = -2464622893831127819L;
    
    @Autowired private BuildReportAction buildReportAction;
    @Autowired private ViewSummaryAction viewSummaryAction;
	@Autowired private CriteriaModelDialog criteriaModelDialog;
    @Autowired private IndexModelDialog indexModelDialog;
    @Autowired private CriteriaIndexModelsPanel criteriaIndexModelsPanel;
    private TransitionListener transitionListener;

    @Override
    public String getKey() {
        return criteriaIndexModelsPanel.getClass().getName();
    }

    @Override
    public JPanel transitionTo(TransitionListener transitionListener) {
        this.transitionListener = transitionListener;
        return criteriaIndexModelsPanel;
    }

    @PostConstruct
    protected void build() {
        setSmallIcon(resourceManager.getImageIcon("home.icon"));
        setTooltip(resourceManager.getString("home"));
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        buildReportAction.setEnabled(false);
        viewSummaryAction.setEnabled(false);
        criteriaIndexModelsPanel.setModel(criteriaModelDialog.getCriteriaModels(), indexModelDialog.getIndexModels());
        criteriaIndexModelsPanel.setSelectedModels(criteriaModelDialog.getSelectedCriteriaModel(), indexModelDialog.getSelectedIndexModel());
        transitionListener.transitionTo(this);
    }
}