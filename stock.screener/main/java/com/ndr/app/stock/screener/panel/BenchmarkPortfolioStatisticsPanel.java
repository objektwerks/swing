package com.ndr.app.stock.screener.panel;

import com.ndr.app.stock.screener.table.BenchmarkPortfolioStatisticsTable;
import com.ndr.app.stock.screener.text.BenchmarkPortfolioStatisticsTextPane;
import com.ndr.model.stock.screener.PortfolioStatistics;
import com.ndr.model.stock.screener.RebalanceFrequencyDate;

import java.awt.BorderLayout;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public final class BenchmarkPortfolioStatisticsPanel extends JPanel {
    private static final long serialVersionUID = -627490983034962996L;

	@Autowired private BenchmarkPortfolioStatisticsTable benchmarkPortfolioStatisticsTable;
    @Autowired private BenchmarkPortfolioStatisticsTextPane benchmarkPortfolioStatisticsTextPane;

    public void setModel(List<RebalanceFrequencyDate> rebalanceFrequencyDates, PortfolioStatistics portfolioStatistics) {
        removeAll();
        add(new JScrollPane(benchmarkPortfolioStatisticsTable), BorderLayout.CENTER);
        add(new JScrollPane(benchmarkPortfolioStatisticsTextPane), BorderLayout.SOUTH);
        benchmarkPortfolioStatisticsTable.setModel(rebalanceFrequencyDates, portfolioStatistics);
        validate();
        repaint();
    }

    public void reset() {
        removeAll();
        validate();
        repaint();
    }

    public BenchmarkPortfolioStatisticsTable getBenchmarkPortfolioStatisticsTable() {
        return benchmarkPortfolioStatisticsTable;
    }

    @PostConstruct
    protected void build() {
        setLayout(new BorderLayout());
    }
}