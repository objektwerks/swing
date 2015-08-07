package com.ndr.app.stock.screener.action;

import com.ndr.app.stock.screener.panel.CriteriaIndexModelsPanel;
import com.ndr.app.stock.screener.panel.CriteriaModelPanel;
import com.ndr.app.stock.screener.panel.IndexModelPanel;
import com.ndr.app.stock.screener.panel.TimeSeriesChartPanel;
import com.ndr.app.stock.screener.panel.TimeSeriesPanel;
import com.ndr.app.stock.screener.panel.TimeSeriesTablePanel;
import com.ndr.app.stock.screener.statusbar.TimeSeriesStatusBar;
import com.ndr.app.stock.screener.transition.Transition;
import com.ndr.app.stock.screener.transition.TransitionListener;
import com.ndr.model.stock.screener.CriteriaModel;
import com.ndr.model.stock.screener.IndexModel;

import java.awt.event.ActionEvent;

import javax.annotation.PostConstruct;
import javax.swing.JPanel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public final class TransitionToTimeSeriesPanelAction extends ConfigurableAction implements Transition {
    private static final long serialVersionUID = 2514343876257167519L;
    
	@Autowired private CriteriaIndexModelsPanel criteriaIndexModelsPanel;
    @Autowired private TimeSeriesChartPanel timeSeriesChartPanel;
    @Autowired private TimeSeriesTablePanel timeSeriesTablePanel;
    @Autowired private TimeSeriesPanel timeSeriesPanel;
    @Autowired private CriteriaModelPanel criteriaModelPanel;
    @Autowired private IndexModelPanel indexModelPanel;
    @Autowired private TimeSeriesStatusBar timeSeriesStatusBar;
    private TransitionListener transitionListener;

    @Override
    public String getKey() {
        return timeSeriesPanel.getClass().getName();
    }

    @Override
    public JPanel transitionTo(TransitionListener transitionListener) {
        this.transitionListener = transitionListener;
        return timeSeriesPanel;
    }

    @PostConstruct
    protected void build() {
        setSmallIcon(resourceManager.getImageIcon("open.icon"));
        setTooltip(resourceManager.getString("open"));
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        timeSeriesChartPanel.reset();
        timeSeriesTablePanel.reset();
        CriteriaModel criteriaModel = criteriaIndexModelsPanel.getSelectedCriteriaModel();
        criteriaModelPanel.setModel(criteriaModel);
        timeSeriesStatusBar.setCriteriaModelStatus(criteriaModel.getName());
        IndexModel indexModel = criteriaIndexModelsPanel.getSelectedIndexModel();
        indexModelPanel.setModel(indexModel);
        timeSeriesStatusBar.setIndexModelStatus(indexModel.getName());
        transitionListener.transitionTo(this);
    }
}