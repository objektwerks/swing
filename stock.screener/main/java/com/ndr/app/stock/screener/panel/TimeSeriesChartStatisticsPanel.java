package com.ndr.app.stock.screener.panel;

import com.ndr.app.stock.screener.resource.ResourceManager;
import com.ndr.model.stock.screener.TimeSeriesModel;

import java.awt.Dimension;

import javax.annotation.PostConstruct;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public final class TimeSeriesChartStatisticsPanel extends JPanel {
    private static final long serialVersionUID = -1040847752936552220L;
    
	@Autowired private ResourceManager resourceManager;
    @Autowired private BenchmarkPortfolioStatisticsPanel benchmarkPortfolioStatisticsPanel;
    @Autowired private RegimeStatisticsPanel regimeStatisticsPanel;

    public void rebuild(TimeSeriesModel timeSeriesModel) {
        benchmarkPortfolioStatisticsPanel.setModel(timeSeriesModel.getRebalanceFrequencyDates(), timeSeriesModel.getPortfolioStatistics());
        regimeStatisticsPanel.setModel();
    }

    public void reset() {
        benchmarkPortfolioStatisticsPanel.reset();
        regimeStatisticsPanel.reset();
    }

    public BenchmarkPortfolioStatisticsPanel getBenchmarkPortfolioStatisticsPanel() {
        return benchmarkPortfolioStatisticsPanel;
    }

    public RegimeStatisticsPanel getRegimesPanel() {
        return regimeStatisticsPanel;
    }

    @PostConstruct
    protected void build() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(125, 450));
        add(buildTab());
    }

    private JTabbedPane buildTab() {
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab(resourceManager.getString("statistics"), benchmarkPortfolioStatisticsPanel);
        tabbedPane.addTab(resourceManager.getString("regimes"), regimeStatisticsPanel);
        return tabbedPane;
    }
}