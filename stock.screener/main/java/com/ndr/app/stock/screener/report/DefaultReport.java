package com.ndr.app.stock.screener.report;

import com.ndr.app.stock.screener.panel.TimeSeriesChartPanel;
import com.ndr.app.stock.screener.panel.TimeSeriesTablePanel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DefaultReport extends Report {
    @Autowired private TimeSeriesChartPanel timeSeriesChartPanel;
    @Autowired private TimeSeriesTablePanel timeSeriesTablePanel;

    @Override
    public void build(File file) throws IOException {
        HSSFWorkbook workbook = new HSSFWorkbook();
        addTable(workbook, timeSeriesChartPanel.getTimeSeriesChartStatisticsPanel().getBenchmarkPortfolioStatisticsPanel().getBenchmarkPortfolioStatisticsTable(), "Stastics");
        addTable(workbook, timeSeriesTablePanel.getStockTable(), "Stocks");
        addTable(workbook, timeSeriesTablePanel.getTimeSeriesTableStatisticsPanel().getAddsDeletesPanel().getAddsTable(), "Adds");
        addTable(workbook, timeSeriesTablePanel.getTimeSeriesTableStatisticsPanel().getAddsDeletesPanel().getDeletesTable(), "Deletes");
        addTable(workbook, timeSeriesTablePanel.getTimeSeriesTableStatisticsPanel().getRebalanceCriteriaStatisticsPanel().getCriteriaStatisticsTable(), "Criteria Statistics");
        addTable(workbook, timeSeriesTablePanel.getTimeSeriesTableStatisticsPanel().getRebalanceCriteriaStatisticsPanel().getRebalanceStatisticsTable(), "Rebalance Statistics");
        addChart(workbook, timeSeriesTablePanel.getTimeSeriesTableStatisticsPanel().getSectorProfileChartPanel().getChart(), "Sector Profile");
        addChart(workbook, toByteArray(timeSeriesTablePanel.getTimeSeriesTableStatisticsPanel().getStyleProfilePanel().getImage()), "Style Profile");
        addCharts(workbook, timeSeriesChartPanel.getCharts());
        workbook.write(new FileOutputStream(file));
    }
}