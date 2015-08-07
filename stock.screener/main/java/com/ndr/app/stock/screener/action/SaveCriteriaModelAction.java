package com.ndr.app.stock.screener.action;

import com.ndr.app.stock.screener.frame.Frame;
import com.ndr.app.stock.screener.panel.CriteriaModelPanel;
import com.ndr.app.stock.screener.statusbar.TimeSeriesStatusBar;
import com.ndr.model.stock.screener.CriteriaModel;
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
public final class SaveCriteriaModelAction extends ConfigurableAction {
    private static final long serialVersionUID = 3270336462336826856L;
    private static final Logger logger = LoggerFactory.getLogger(SaveCriteriaModelAction.class);

    @Autowired private Frame frame;
	@Autowired private CriteriaModelPanel criteriaModelPanel;
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
        CriteriaModel criteriaModel = criteriaModelPanel.getModel();
        save(criteriaModel);
    }

    public void save(CriteriaModel criteriaModel) {
        if (criteriaModel != null) {
            timeSeriesStatusBar.setBusyStatus(true);
            timeSeriesStatusBar.setStatus(resourceManager.getString("status.bar.processing"));
            frame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            Worker worker = new Worker(criteriaModel);
            worker.execute();
        }
    }

    private class Worker extends SwingWorker<CriteriaModel, Void> {
        private CriteriaModel criteriaModel;

        private Worker(CriteriaModel criteriaModel) {
            this.criteriaModel = criteriaModel;
        }

        @Override
        protected CriteriaModel doInBackground() throws Exception {
            return stockScreenerUserServiceProxy.saveCriteriaModel(criteriaModel);
        }

        @Override
        protected void done() {
            try {
                if (!isCancelled()) {
                    criteriaModel = get();
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