package com.ndr.app.stock.screener.action;

import com.ndr.app.stock.screener.panel.CriteriaModelPanel;
import com.ndr.app.stock.screener.statusbar.TimeSeriesStatusBar;
import com.ndr.model.stock.screener.CriteriaModel;
import com.ndr.service.stock.screener.StockScreenerUserService;

import java.awt.event.ActionEvent;

import javax.annotation.PostConstruct;
import javax.swing.SwingWorker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public final class RemoveCriteriaModelAction extends ConfigurableAction {
    private static final long serialVersionUID = -6240866624403864340L;
    
	@Autowired private CriteriaModelPanel criteriaModelPanel;
    @Autowired private TimeSeriesStatusBar timeSeriesStatusBar;
    @Autowired private StockScreenerUserService stockScreenerUserServiceProxy;

    @PostConstruct
    protected void build() {
        setName(resourceManager.getString("remove"));
        setSmallIcon(resourceManager.getImageIcon("remove.icon"));
        setTooltip(resourceManager.getString("remove"));
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        CriteriaModel criteriaModel = criteriaModelPanel.getModel();
        remove(criteriaModel);
    }

    public void remove(CriteriaModel criteriaModel) {
        if (criteriaModel != null) {
            Worker worker = new Worker(criteriaModel);
            worker.execute();
        }
    }

    private class Worker extends SwingWorker<Void, Void> {
        private CriteriaModel criteriaModel;

        private Worker(CriteriaModel criteriaModel) {
            this.criteriaModel = criteriaModel;
        }

        @Override
        protected Void doInBackground() throws Exception {
            timeSeriesStatusBar.setBusyStatus(true);
            timeSeriesStatusBar.setStatus(resourceManager.getString("status.bar.processing"));
            stockScreenerUserServiceProxy.removeCriteriaModel(criteriaModel);
            return null;
        }

        @Override
        protected void done() {
            timeSeriesStatusBar.setBusyStatus(false);
            timeSeriesStatusBar.setStatus(resourceManager.getString("status.bar.ready"));
        }
    }
}