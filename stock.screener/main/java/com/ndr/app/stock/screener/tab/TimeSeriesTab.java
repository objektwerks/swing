package com.ndr.app.stock.screener.tab;

import com.ndr.app.stock.screener.panel.TimeSeriesChartPanel;
import com.ndr.app.stock.screener.panel.TimeSeriesChartStatisticsPanel;
import com.ndr.app.stock.screener.panel.TimeSeriesTablePanel;
import com.ndr.app.stock.screener.panel.TimeSeriesTableStatisticsPanel;

import java.awt.BorderLayout;

import javax.annotation.PostConstruct;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public final class TimeSeriesTab extends JPanel {
    private static final long serialVersionUID = 8116618076082121717L;
    
	@Autowired private TimeSeriesChartPanel timeSeriesChartPanel;
    @Autowired private TimeSeriesTablePanel timeSeriesTablePanel;
    @Autowired private TimeSeriesChartStatisticsPanel timeSeriesChartStatisticsPanel;
    @Autowired private TimeSeriesTableStatisticsPanel timeSeriesTableStatisticsPanel;

    @PostConstruct
    protected void build() {
        setLayout(new BorderLayout());
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, buildTimeSeriesChartSplitPane(), buildTimeSeriesTableSplitPane());
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(450);
        add(splitPane, BorderLayout.CENTER);
    }

    private JSplitPane buildTimeSeriesChartSplitPane() {
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, timeSeriesChartPanel, timeSeriesChartStatisticsPanel);
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(0.75);
        splitPane.setResizeWeight(0.75);
        return splitPane;
    }

    private JSplitPane buildTimeSeriesTableSplitPane() {
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, timeSeriesTablePanel, timeSeriesTableStatisticsPanel);
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(0.75);
        splitPane.setResizeWeight(0.75);        
        return splitPane;
    }
}