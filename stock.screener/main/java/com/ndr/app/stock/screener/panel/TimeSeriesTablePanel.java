package com.ndr.app.stock.screener.panel;

import com.ndr.app.stock.screener.resource.ResourceManager;
import com.ndr.app.stock.screener.table.StockTable;
import com.ndr.model.stock.screener.TimeSeriesModel;
import com.ndr.model.stock.screener.TimeSeriesRows;

import java.awt.BorderLayout;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public final class TimeSeriesTablePanel extends JPanel {
    private static final long serialVersionUID = -8652734526411867502L;
    
	@Autowired private ResourceManager resourceManager;
    @Autowired private StockTable stockTable;
    @Autowired private TimeSeriesTableStatisticsPanel timeSeriesTableStatisticsPanel;
    private TimeSeriesModel timeSeriesModel;

    public TimeSeriesModel getTimeSeriesModel() {
        return timeSeriesModel;
    }

    public void rebuild(TimeSeriesModel timeSeriesModel) {
        this.timeSeriesModel = timeSeriesModel;
        removeAll();
        Date currentRebalanceFrequencyDate = timeSeriesModel.getRebalanceFrequencyDates().get(0).getDate();
        rebuild(timeSeriesModel.getRows().getRows().size(), currentRebalanceFrequencyDate);
        stockTable.setModel(timeSeriesModel.getColumns(), timeSeriesModel.getRows().getRows());
        validate();
        repaint();
        Set<List<String>> addsTickersAndCompanies = timeSeriesModel.listAddsTickersAndCompanies(currentRebalanceFrequencyDate);
        Set<List<String>> deletesTickersAndCompanies = timeSeriesModel.listDeletesTickersAndCompanies(currentRebalanceFrequencyDate);
        timeSeriesTableStatisticsPanel.rebuild(currentRebalanceFrequencyDate,
                                               timeSeriesModel.getDateNumberPoints(),
                                               timeSeriesModel.getSubIndustryPoints(), 
                                               timeSeriesModel.getRebalanceFrequencyDates(),
                                               timeSeriesModel.getRows().getRebalanceStatistics(),
                                               addsTickersAndCompanies,
                                               deletesTickersAndCompanies);
    }

    public void rebuild(Date currentRebalanceFrequencyDate, List<String> columns, TimeSeriesRows rows) {
        removeAll();
        rebuild(rows.getRows().size(), currentRebalanceFrequencyDate);
        stockTable.setModel(columns, rows.getRows());
        validate();
        repaint();
        Set<List<String>> addsTickersAndCompanies = timeSeriesModel.listAddsTickersAndCompanies(currentRebalanceFrequencyDate);
        Set<List<String>> deletesTickersAndCompanies = timeSeriesModel.listDeletesTickersAndCompanies(currentRebalanceFrequencyDate);
        timeSeriesTableStatisticsPanel.rebuild(currentRebalanceFrequencyDate,
                                               timeSeriesModel.getDateNumberPoints(),
                                               timeSeriesModel.getSubIndustryPoints(), 
                                               timeSeriesModel.getRebalanceFrequencyDates(),
                                               rows.getRebalanceStatistics(),
                                               addsTickersAndCompanies,
                                               deletesTickersAndCompanies);
    }

    public void reset() {
        timeSeriesTableStatisticsPanel.reset();
        removeAll();
        validate();
        repaint();
    }

    public StockTable getStockTable() {
        return stockTable;
    }

    public TimeSeriesTableStatisticsPanel getTimeSeriesTableStatisticsPanel() {
        return timeSeriesTableStatisticsPanel;
    }

    @PostConstruct
    protected void build() {
        setLayout(new BorderLayout());
    }

    private void rebuild(int numberOfRows, Date currentRebalanceFrequencyDate) {
        add(buildStatisticsPanel(numberOfRows, currentRebalanceFrequencyDate), BorderLayout.NORTH);
        add(new JScrollPane(stockTable), BorderLayout.CENTER);
    }

    private JPanel buildStatisticsPanel(int numberOfRows, Date currentRebalanceFrequencyDate) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        JLabel numberOfStocksLabel = new JLabel(" " + resourceManager.getString("stocks") + ": " + numberOfRows);
        JLabel dateLabel = new JLabel(new SimpleDateFormat("yyyy-MM-dd").format(currentRebalanceFrequencyDate));
        panel.add(numberOfStocksLabel);
        panel.add(Box.createHorizontalGlue());
        panel.add(dateLabel);
        return panel;
    }
}