package com.ndr.app.stock.screener.panel;

import com.ndr.app.stock.screener.table.CriteriaStatisticsTable;
import com.ndr.app.stock.screener.table.RebalanceStatisticsTable;
import com.ndr.model.stock.screener.DateNumberPoint;
import com.ndr.model.stock.screener.RebalanceFrequencyDate;
import com.ndr.model.stock.screener.RebalanceStatistics;
import com.ndr.model.stock.screener.TimeSeriesType;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public final class RebalanceCriteriaStatisticsPanel extends JPanel {
    private static final long serialVersionUID = 3582135748211113986L;
    
	@Autowired private RebalanceStatisticsTable rebalanceStatisticsTable;
    @Autowired private CriteriaStatisticsTable criteriaStatisticsTable;

    public void setModel(Date currentRebalanceFrequencyDate,
                         Map<TimeSeriesType, List<DateNumberPoint>> dateNumberPoints,
                         List<RebalanceFrequencyDate> rebalanceFrequencyDates,
                         RebalanceStatistics rebalanceStatistics,
                         int numberOfAdds,
                         int numberOfDeletes) {
        removeAll();
        add(new JScrollPane(rebalanceStatisticsTable));
        add(new JScrollPane(criteriaStatisticsTable));
        rebalanceStatisticsTable.setModel(currentRebalanceFrequencyDate, dateNumberPoints, rebalanceFrequencyDates, rebalanceStatistics, numberOfAdds, numberOfDeletes);
        criteriaStatisticsTable.setModel(rebalanceStatistics.getCriteriaStatistics());
        validate();
        repaint();
    }

    public void reset() {
        removeAll();
        validate();
        repaint();
    }

    public CriteriaStatisticsTable getCriteriaStatisticsTable() {
        return criteriaStatisticsTable;
    }

    public RebalanceStatisticsTable getRebalanceStatisticsTable() {
        return rebalanceStatisticsTable;
    }

    @PostConstruct
    protected void build() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    }
}