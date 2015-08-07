package com.ndr.app.stock.screener.table;

import com.ndr.app.stock.screener.Colors;
import com.ndr.app.stock.screener.text.TextToHtmlSplitter;
import com.ndr.model.stock.screener.DateNumberPoint;
import com.ndr.model.stock.screener.RebalanceFrequencyDate;
import com.ndr.model.stock.screener.RebalanceStatistics;
import com.ndr.model.stock.screener.TimeSeriesType;

import java.awt.Color;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.decorator.ColorHighlighter;
import org.jdesktop.swingx.decorator.HighlightPredicate;
import org.jdesktop.swingx.decorator.HighlighterFactory;
import org.jdesktop.swingx.table.DefaultTableColumnModelExt;
import org.jdesktop.swingx.table.TableColumnExt;
import org.springframework.stereotype.Component;

@Component
public final class RebalanceStatisticsTable extends JXTable {
    private static final long serialVersionUID = 7306583970559219353L;

	public void setModel(Date currentRebalanceFrequencyDate,
                         Map<TimeSeriesType, List<DateNumberPoint>> dateNumberPoints,
                         List<RebalanceFrequencyDate> rebalanceFrequencyDates,
                         RebalanceStatistics rebalanceStatistics,
                         int numberOfAdds,
                         int numberOfDeletes) {
        setColumnModel(new StatisticsTableColumnModel("Statistic", "Value"));
        StatisticsTableModel model = new StatisticsTableModel(currentRebalanceFrequencyDate, dateNumberPoints, rebalanceFrequencyDates, rebalanceStatistics, numberOfAdds, numberOfDeletes);
        setModel(model);
        setRowSorter(new TableRowSorter<StatisticsTableModel>(model));
    }

    @PostConstruct
    protected void build() {
        setAutoCreateColumnsFromModel(false);
        setAutoCreateRowSorter(false);
        setEditable(false);
        addHighlighter(HighlighterFactory.createAlternateStriping(Color.white, Colors.tableRowColor));
        addHighlighter(new ColorHighlighter(HighlightPredicate.ROLLOVER_ROW, null, Color.blue));
    }

    private class StatisticsTableColumnModel extends DefaultTableColumnModelExt {
        private static final long serialVersionUID = -44105340261870957L;

		public StatisticsTableColumnModel(String... columns) {
            for (int i = 0; i < columns.length; i++) {
                TableColumnExt tableColumn = new TableColumnExt(i);
                tableColumn.setHeaderValue(TextToHtmlSplitter.instance.split(columns[i]));
                if (i == 0) tableColumn.setPreferredWidth(100);
                addColumn(tableColumn);
            }
        }
    }

    private class StatisticsTableModel extends DefaultTableModel {
        private static final long serialVersionUID = 1726535560369952016L;

		public StatisticsTableModel(Date currentRebalanceFrequencyDate,
                                    Map<TimeSeriesType, List<DateNumberPoint>> dateNumberPoints,
                                    List<RebalanceFrequencyDate> rebalanceFrequencyDates,
                                    RebalanceStatistics rebalanceStatistics,
                                    int numberOfAdds,
                                    int numberOfDeletes) {
            List<DateNumberPoint> portfolioDateNumberPoints = dateNumberPoints.get(TimeSeriesType.portfolioTotalReturn);
            List<DateNumberPoint> benchmarkDateNumberPoints = dateNumberPoints.get(TimeSeriesType.benchmarkTotalReturn);
            rebalanceStatistics.calculateReturnsAndDrawdown(portfolioDateNumberPoints,
                                                            benchmarkDateNumberPoints,
                                                            rebalanceStatistics.getRebalanceDate(),
                                                            RebalanceFrequencyDate.findNearestDate(rebalanceFrequencyDates, rebalanceStatistics.getRebalanceDate()));
            TimeSeriesType maxSectorType = DateNumberPoint.getHighestNumberByTimeSeriesTypes(currentRebalanceFrequencyDate, dateNumberPoints, DateNumberPoint.sectorTimeSeriesTypes);
            TimeSeriesType maxStyleType = DateNumberPoint.getHighestNumberByTimeSeriesTypes(currentRebalanceFrequencyDate, dateNumberPoints, DateNumberPoint.styleTimeSeriesTypes);
            SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
            DecimalFormat decimalFormatter = new DecimalFormat("###,###,###,###.##");
            addRow(new Object[] { "Rebalance Date", dateFormatter.format(rebalanceStatistics.getRebalanceDate()) });
            addRow(new Object[] { "Number of Adds", numberOfAdds });
            addRow(new Object[] { "Number of Deletes", numberOfDeletes });
            addRow(new Object[] { "Maximum Drawdown", decimalFormatter.format(rebalanceStatistics.getMaxDrawdown()) });
            addRow(new Object[] { "Maximum Sector", maxSectorType.getName() });
            addRow(new Object[] { "Maximum Style", maxStyleType.getName() });
            addRow(new Object[] { "Number of Stocks", rebalanceStatistics.getNumberOfStocks() });
            addRow(new Object[] { "Benchmark Return", decimalFormatter.format(rebalanceStatistics.getBenchmarkReturn()) });
            addRow(new Object[] { "Portfolio Return", decimalFormatter.format(rebalanceStatistics.getPortfolioReturn()) });
            addRow(new Object[] { "Excess Return", decimalFormatter.format(rebalanceStatistics.getPortfolioReturn() - rebalanceStatistics.getBenchmarkReturn()) });
        }

        @Override
        public int getColumnCount() {
            return columnModel.getColumnCount();
        }
    }
}