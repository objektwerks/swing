package com.ndr.app.stock.screener.action;

import com.ndr.app.stock.screener.panel.IndexModelPanel;
import com.ndr.app.stock.screener.statusbar.TimeSeriesStatusBar;
import com.ndr.model.stock.screener.IndexModel;
import com.ndr.service.stock.screener.StockScreenerUserService;

import java.awt.event.ActionEvent;

import javax.annotation.PostConstruct;
import javax.swing.SwingWorker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public final class RemoveIndexModelAction extends ConfigurableAction {
    private static final long serialVersionUID = 2914718800418404568L;
    
	@Autowired private IndexModelPanel indexModelPanel;
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
        IndexModel indexModel = indexModelPanel.getModel();
        remove(indexModel);
    }

    public void remove(IndexModel indexModel) {
        if (indexModel != null) {
            Worker worker = new Worker(indexModel);
            worker.execute();
        }
    }

    private class Worker extends SwingWorker<Void, Void> {
        private IndexModel indexModel;

        private Worker(IndexModel indexModel) {
            this.indexModel = indexModel;
        }

        @Override
        protected Void doInBackground() throws Exception {
            timeSeriesStatusBar.setBusyStatus(true);
            timeSeriesStatusBar.setStatus(resourceManager.getString("status.bar.processing"));
            stockScreenerUserServiceProxy.removeIndexModel(indexModel);
            return null;
        }

        @Override
        protected void done() {
            timeSeriesStatusBar.setBusyStatus(false);
            timeSeriesStatusBar.setStatus(resourceManager.getString("status.bar.ready"));
        }
    }
}