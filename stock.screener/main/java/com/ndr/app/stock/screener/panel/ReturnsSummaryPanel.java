package com.ndr.app.stock.screener.panel;

import com.ndr.app.stock.screener.listener.ReturnsSummaryListener;
import com.ndr.app.stock.screener.table.ReturnsSummaryTable;
import com.ndr.app.stock.screener.text.TextToHtmlSplitter;
import com.ndr.model.stock.screener.ReturnsSummary;
import com.ndr.model.stock.screener.TimeSeriesModel;
import com.ndr.model.stock.screener.TimeSeriesType;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public final class ReturnsSummaryPanel extends JPanel {
    private static final long serialVersionUID = 2176114701969331998L;
    
    @Autowired private ReturnsSummaryTable returnsSummaryTable;
    private SummaryPanel summaryPanel;

    public void rebuild(TimeSeriesModel timeSeriesModel, boolean excessReturns) {
        boolean useTotalReturn = timeSeriesModel.getDateNumberPoints().keySet().contains(TimeSeriesType.portfolioTotalReturn);
        ReturnsSummary returnsSummary = ReturnsSummary.createReturnsSummary(timeSeriesModel.getDateNumberPoints(), timeSeriesModel.getRebalanceFrequencyDates(), useTotalReturn, excessReturns);
        returnsSummaryTable.setModel(returnsSummary);
        summaryPanel.setModel(returnsSummary);
    }

    public void addReturnsSummaryListener(ReturnsSummaryListener listener) {
        returnsSummaryTable.addReturnsSummaryListener(listener);
    }

    public void selectReturn(String year, int column) {
        returnsSummaryTable.selectReturn(year, column);
    }

    @PostConstruct
    protected void build() {
        summaryPanel = new SummaryPanel();
        setLayout(new BorderLayout());
        add(new JScrollPane(returnsSummaryTable), BorderLayout.CENTER);
        add(summaryPanel, BorderLayout.SOUTH);
    }

    private class SummaryPanel extends JPanel {
        private static final long serialVersionUID = -7473179616546039878L;

		public SummaryPanel() {
            setLayout(new GridLayout(1, 6));
        }

        public void setModel(ReturnsSummary returnsSummary) {
            removeAll();
            DecimalFormat decimalFormatter = new DecimalFormat("###,###,###,##0.00");
            List<List<Double>> rows = returnsSummary.getReturns();
            List<Double> row = rows.get(rows.size() - 1);
            List<Object> formattedRow = new ArrayList<Object>(1);
            List<String> rowHeaders = returnsSummary.getRowHeaders();
            formattedRow.add(rowHeaders.get(rowHeaders.size() - 1));
            for (Double column : row) {
                if (column == null) {
                    formattedRow.add("");
                } else {
                    formattedRow.add(decimalFormatter.format(column));
                }
            }
            for (int i = 0; i < formattedRow.size(); i++) {
                String value = formattedRow.get(i).toString();
                JLabel label = new JLabel();
                if (i == 0) {
                    label.setText(TextToHtmlSplitter.instance.split(value));
                } else {
                    label.setText(value);
                }
                add(label);
            }
            validate();
            repaint();
        }
    }
}