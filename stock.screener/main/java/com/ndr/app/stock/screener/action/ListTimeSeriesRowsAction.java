package com.ndr.app.stock.screener.action;

import com.ndr.app.stock.screener.cache.TimeSeriesRowsCache;
import com.ndr.app.stock.screener.frame.Frame;
import com.ndr.app.stock.screener.panel.TimeSeriesChartPanel;
import com.ndr.app.stock.screener.panel.TimeSeriesTablePanel;
import com.ndr.app.stock.screener.statusbar.TimeSeriesStatusBar;
import com.ndr.model.stock.screener.TimeSeriesRows;
import com.ndr.service.stock.screener.StockScreenerService;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public final class ListTimeSeriesRowsAction extends ConfigurableAction {
    private static final long serialVersionUID = -1398511517193814603L;
	private static final Logger logger = LoggerFactory.getLogger(ListTimeSeriesRowsAction.class);

    @Autowired private Frame frame;
    @Autowired private TimeSeriesChartPanel timeSeriesChartPanel;
    @Autowired private TimeSeriesTablePanel timeSeriesTablePanel;
    @Autowired private TimeSeriesRowsCache timeSeriesRowsCache;
    @Autowired private TimeSeriesStatusBar timeSeriesStatusBar;
    @Autowired private StockScreenerService stockScreenerServiceProxy;

    @PostConstruct
    protected void build() {
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        List<String> columns = timeSeriesTablePanel.getTimeSeriesModel().getColumns();
        Date currentRebalanceFrequencyDate = (Date) event.getSource();
        if (timeSeriesRowsCache.containsKey(currentRebalanceFrequencyDate)) {
            timeSeriesTablePanel.rebuild(currentRebalanceFrequencyDate, columns, timeSeriesRowsCache.get(currentRebalanceFrequencyDate));
        } else {
            timeSeriesStatusBar.setBusyStatus(true);
            timeSeriesStatusBar.setStatus(resourceManager.getString("status.bar.processing"));
            frame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            List<Integer> issueIds = timeSeriesChartPanel.listIssueIds(currentRebalanceFrequencyDate);
            Worker worker = new Worker(currentRebalanceFrequencyDate, issueIds, columns);
            worker.execute();            
        }
    }

    private class Worker extends SwingWorker<TimeSeriesRows, Void> {
        private Date currentRebalanceFrequencyDate;
        private List<Integer> issueIds;
        private List<String> columns;

        private Worker(Date currentRebalanceFrequencyDate, List<Integer> issueIds, List<String> columns) {
            this.currentRebalanceFrequencyDate = currentRebalanceFrequencyDate;
            this.issueIds = issueIds;
            this.columns = columns;
        }

        @Override
        protected TimeSeriesRows doInBackground() throws Exception {
            return stockScreenerServiceProxy.listTimeSeriesRows(currentRebalanceFrequencyDate, issueIds, columns);
        }

        @Override
        protected void done() {
            try {
                if (!isCancelled()) {
                    TimeSeriesRows rows = get();
                    timeSeriesRowsCache.put(currentRebalanceFrequencyDate, rows);
                    timeSeriesTablePanel.rebuild(currentRebalanceFrequencyDate, columns, rows);
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