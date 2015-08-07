package com.ndr.app.stock.screener.action;

import com.ndr.app.stock.screener.frame.Frame;
import com.ndr.app.stock.screener.panel.IndexModelPanel;
import com.ndr.app.stock.screener.statusbar.TimeSeriesStatusBar;
import com.ndr.model.stock.screener.IndexModel;
import com.ndr.service.stock.screener.StockScreenerUserService;

import java.awt.Cursor;
import java.awt.event.ActionEvent;

import javax.annotation.PostConstruct;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public final class SaveIndexModelAction extends ConfigurableAction {
    private static final long serialVersionUID = 6970612729644243935L;
    private static final Logger logger = LoggerFactory.getLogger(SaveIndexModelAction.class);

    @Autowired private Frame frame;
	@Autowired private IndexModelPanel indexModelPanel;
    @Autowired private TimeSeriesStatusBar timeSeriesStatusBar;
    @Autowired private StockScreenerUserService stockScreenerUserServiceProxy;

    @PostConstruct
    protected void build() {
        setName(resourceManager.getString("save"));
        setSmallIcon(resourceManager.getImageIcon("save.icon"));
        setTooltip(resourceManager.getString("save"));
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        IndexModel indexModel = indexModelPanel.getModel();
        save(indexModel);
    }

    public void save(IndexModel indexModel) {
        if (indexModel != null) {
            timeSeriesStatusBar.setBusyStatus(true);
            timeSeriesStatusBar.setStatus(resourceManager.getString("status.bar.processing"));
            frame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            Worker worker = new Worker(indexModel);
            worker.execute();
        }
    }

    private class Worker extends SwingWorker<IndexModel, Void> {
        private IndexModel indexModel;

        private Worker(IndexModel indexModel) {
            this.indexModel = indexModel;
        }

        @Override
        protected IndexModel doInBackground() throws Exception {
            return stockScreenerUserServiceProxy.saveIndexModel(indexModel);
        }

        @Override
        protected void done() {
            try {
                if (!isCancelled()) {
                    indexModel = get();
                }
            } catch (Throwable t) {
                logger.error(t.getMessage(), t);
                JOptionPane.showMessageDialog(null, t.getMessage(), "Stock Screener", JOptionPane.ERROR_MESSAGE);
            } finally {
                timeSeriesStatusBar.setBusyStatus(false);
                timeSeriesStatusBar.setStatus(resourceManager.getString("status.bar.ready"));
                frame.setCursor(Cursor.getDefaultCursor());
            }
        }
    }
}