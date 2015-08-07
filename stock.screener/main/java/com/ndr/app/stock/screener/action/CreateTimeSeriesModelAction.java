package com.ndr.app.stock.screener.action;

import com.ndr.app.stock.screener.cache.TimeSeriesRowsCache;
import com.ndr.app.stock.screener.frame.Frame;
import com.ndr.app.stock.screener.panel.CriteriaModelPanel;
import com.ndr.app.stock.screener.panel.IndexModelPanel;
import com.ndr.app.stock.screener.panel.TimeSeriesChartPanel;
import com.ndr.app.stock.screener.panel.TimeSeriesTablePanel;
import com.ndr.app.stock.screener.statusbar.TimeSeriesStatusBar;
import com.ndr.model.stock.screener.CriteriaModel;
import com.ndr.model.stock.screener.IndexModel;
import com.ndr.model.stock.screener.TimeSeriesModel;
import com.ndr.service.stock.screener.StockScreenerService;

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
public final class CreateTimeSeriesModelAction extends ConfigurableAction {
    private static final long serialVersionUID = 2609838870651463231L;
	private static final Logger logger = LoggerFactory.getLogger(CreateTimeSeriesModelAction.class);

    @Autowired private Frame frame;
    @Autowired private BuildReportAction buildReportAction;
    @Autowired private ViewSummaryAction viewSummaryAction;
    @Autowired private TimeSeriesChartPanel timeSeriesChartPanel;
    @Autowired private TimeSeriesTablePanel timeSeriesTablePanel;
    @Autowired private IndexModelPanel indexModelPanel;
    @Autowired private CriteriaModelPanel criteriaModelPanel;
    @Autowired private TimeSeriesRowsCache timeSeriesRowsCache;
    @Autowired private TimeSeriesStatusBar timeSeriesStatusBar;
    @Autowired private StockScreenerService stockScreenerServiceProxy;

    @PostConstruct
    protected void build() {
        setSmallIcon(resourceManager.getImageIcon("execute.icon"));
        setTooltip(resourceManager.getString("create.time.series.model.tooltip"));
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        IndexModel indexModel = indexModelPanel.getModel();
        CriteriaModel criteriaModel = criteriaModelPanel.getModel();
        if (indexModel != null && criteriaModel != null) {
            timeSeriesStatusBar.setBusyStatus(true);
            timeSeriesStatusBar.setStatus(resourceManager.getString("status.bar.processing"));
            frame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            Worker worker = new Worker(indexModel, criteriaModel);
            worker.execute();
        } else {
            JOptionPane.showMessageDialog(frame,
                                          resourceManager.getString("create.time.series.model.action.error"),
                                          resourceManager.getString("create.time.series.model.action.error.title"),
                                          JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private class Worker extends SwingWorker<TimeSeriesModel, Void> {
        private IndexModel indexModel;
        private CriteriaModel criteriaModel;

        private Worker(IndexModel indexModel, CriteriaModel criteriaModel) {
            this.indexModel = indexModel;
            this.criteriaModel = criteriaModel;
        }

        @Override
        protected TimeSeriesModel doInBackground() throws Exception {
            return stockScreenerServiceProxy.createTimeSeriesModel(indexModel, criteriaModel);
        }

        @Override
        protected void done() {
            try {
                if (!isCancelled()) {
                    TimeSeriesModel timeSeriesModel = get();
                    timeSeriesRowsCache.clear();
                    timeSeriesTablePanel.rebuild(timeSeriesModel);
                    timeSeriesChartPanel.rebuild(timeSeriesModel);
                    buildReportAction.setEnabled(true);
                    viewSummaryAction.setEnabled(true);
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