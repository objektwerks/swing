package com.ndr.app.stock.screener.panel;

import com.ndr.app.stock.screener.resource.ResourceManager;
import com.ndr.model.stock.screener.DateNumberPoint;
import com.ndr.model.stock.screener.RebalanceFrequencyDate;
import com.ndr.model.stock.screener.RebalanceStatistics;
import com.ndr.model.stock.screener.TimeSeriesType;

import java.awt.Dimension;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public final class TimeSeriesTableStatisticsPanel extends JPanel {
    private static final long serialVersionUID = 5365747075892290919L;
    
	@Autowired private ResourceManager resourceManager;
    @Autowired private RebalanceCriteriaStatisticsPanel rebalanceCriteriaStatisticsPanel;
    @Autowired private AddsDeletesPanel addsDeletesPanel;
    @Autowired private StyleProfilePanel styleProfilePanel;
    @Autowired private SectorProfileChartPanel sectorProfileChartPanel;

    public void rebuild(Date currentRebalanceFrequencyDate,
                        Map<TimeSeriesType, List<DateNumberPoint>> dateNumberPoints, 
                        Map<Integer, List<DateNumberPoint>> subIndustryPoints, 
                        List<RebalanceFrequencyDate> rebalanceFrequencyDates,
                        RebalanceStatistics rebalanceStatistics,
                        Set<List<String>> addsTickersAndCompanies,
                        Set<List<String>> deletesTickersAndCompanies) {
        int numberOfAdds = addsTickersAndCompanies.size();
        int numberOfDeletes = deletesTickersAndCompanies.size();
        rebalanceCriteriaStatisticsPanel.setModel(currentRebalanceFrequencyDate, dateNumberPoints, rebalanceFrequencyDates, rebalanceStatistics, numberOfAdds, numberOfDeletes);
        addsDeletesPanel.setModel(addsTickersAndCompanies, deletesTickersAndCompanies);
        styleProfilePanel.setModel(currentRebalanceFrequencyDate, dateNumberPoints);
        sectorProfileChartPanel.setModel(currentRebalanceFrequencyDate, dateNumberPoints, subIndustryPoints);
    }

    public void reset() {
        rebalanceCriteriaStatisticsPanel.reset();
        addsDeletesPanel.reset();
        styleProfilePanel.reset();
        sectorProfileChartPanel.reset();        
    }

    public AddsDeletesPanel getAddsDeletesPanel() {
        return addsDeletesPanel;
    }

    public RebalanceCriteriaStatisticsPanel getRebalanceCriteriaStatisticsPanel() {
        return rebalanceCriteriaStatisticsPanel;
    }

    public SectorProfileChartPanel getSectorProfileChartPanel() {
        return sectorProfileChartPanel;
    }

    public StyleProfilePanel getStyleProfilePanel() {
        return styleProfilePanel;
    }

    @PostConstruct
    protected void build() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(125, 450));
        add(buildTabs());
    }

    private JTabbedPane buildTabs() {
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab(resourceManager.getString("statistics"), rebalanceCriteriaStatisticsPanel);
        tabbedPane.addTab(resourceManager.getString("adds.deletes"), addsDeletesPanel);
        tabbedPane.addTab(resourceManager.getString("style.profile"), styleProfilePanel);
        tabbedPane.addTab(resourceManager.getString("sector.profile"), sectorProfileChartPanel);
        return tabbedPane;
    }
}