package com.ndr.app.stock.screener.table;

import com.ndr.app.stock.screener.Colors;
import com.ndr.app.stock.screener.text.BenchmarkPortfolioStatisticsTextPane;
import com.ndr.app.stock.screener.text.TextToHtmlSplitter;
import com.ndr.model.stock.screener.PortfolioStatistics;
import com.ndr.model.stock.screener.RebalanceFrequencyDate;

import java.awt.Color;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import org.jdesktop.swingx.table.DefaultTableColumnModelExt;
import org.jdesktop.swingx.table.TableColumnExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public final class BenchmarkPortfolioStatisticsTable extends JTable {
    private static final long serialVersionUID = 2027801721950041264L;
    
	@Autowired private BenchmarkPortfolioStatisticsTextPane benchmarkPortfolioStatisticsTextPane;
    private StatisticsTableCellRenderer statisticsTableCellRenderer;
    private StatisticsListSelectionListener statisticsListSelectionListener;

    public void setModel(List<RebalanceFrequencyDate> rebalanceFrequencyDates, PortfolioStatistics portfolioStatistics) {
        setColumnModel(new StatisticsTableColumnModel("Statistic", "Value"));
        setModel(new StatisticsTableModel(rebalanceFrequencyDates, portfolioStatistics));
    }

    @PostConstruct
    protected void build() {
        statisticsTableCellRenderer = new StatisticsTableCellRenderer();
        statisticsListSelectionListener = new StatisticsListSelectionListener(this, benchmarkPortfolioStatisticsTextPane);
        setAutoCreateColumnsFromModel(false);
        setAutoCreateRowSorter(false);
        setFillsViewportHeight(true);
        setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        getSelectionModel().addListSelectionListener(statisticsListSelectionListener);
    }

    private class StatisticsTableColumnModel extends DefaultTableColumnModelExt {
		private static final long serialVersionUID = 6781475229270740574L;

		public StatisticsTableColumnModel(String... columns) {
            for (int i = 0; i < columns.length; i++) {
                TableColumnExt tableColumn = new TableColumnExt(i);
                tableColumn.setCellRenderer(statisticsTableCellRenderer);
                tableColumn.setHeaderValue(TextToHtmlSplitter.instance.split(columns[i]));
                if (i == 0) {
                    tableColumn.setPreferredWidth(165);
                }
                tableColumn.setSortable(false);
                addColumn(tableColumn);
            }
            getSelectionModel().addListSelectionListener(statisticsListSelectionListener);
        }
    }

    private class StatisticsTableModel extends DefaultTableModel {
        private static final long serialVersionUID = -588436941891825672L;

		public StatisticsTableModel(List<RebalanceFrequencyDate> rebalanceFrequencyDates, PortfolioStatistics portfolioStatistics) {
            DecimalFormat decimalFormatter = new DecimalFormat("###,###,###,###.##");
            SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
            Date startDate = rebalanceFrequencyDates.get(0).getDate();
            Date endDate = rebalanceFrequencyDates.get(rebalanceFrequencyDates.size() - 1).getDate();
            addRow(new Object[] { "General", "" });
            addRow(new Object[] { "Start Date", dateFormatter.format(startDate) });
            addRow(new Object[] { "End Date", dateFormatter.format(endDate) });    
            addRow(new Object[] { "Years and Months", calculateDateRange(startDate, endDate) });
            addRow(new Object[] { "Average Stock Holding Period", decimalFormatter.format(portfolioStatistics.getAvgStockHoldingPeriod()) });
            addRow(new Object[] { "Maximum Drawdown", decimalFormatter.format(portfolioStatistics.getMaxDrawdown()) });
            addRow(new Object[] { "GPAs", "" });
            addRow(new Object[] { "Benchmark GPA", decimalFormatter.format(portfolioStatistics.getBenchmarkGPA()) });
            addRow(new Object[] { "Portfolio GPA", decimalFormatter.format(portfolioStatistics.getPortfolioGPA()) });
            addRow(new Object[] { "Ratios", "" });
            addRow(new Object[] { "Information Ratio", decimalFormatter.format(portfolioStatistics.getInformationRatio()) });
            addRow(new Object[] { "Sharpe Ratio", decimalFormatter.format(portfolioStatistics.getSharpeRatio()) });
            addRow(new Object[] { "Turnover Ratio", decimalFormatter.format(portfolioStatistics.getTurnoverRatio()) });
            addRow(new Object[] { "Returns", "" });
            addRow(new Object[] { "% of Periods Negative Returns", decimalFormatter.format(portfolioStatistics.getPctOfPeriodsNegReturns()) });
            addRow(new Object[] { "% of Periods Positive Returns", decimalFormatter.format(portfolioStatistics.getPctOfPeriodsPosReturns()) });
            addRow(new Object[] { "Std. Dev. of Returns", decimalFormatter.format(portfolioStatistics.getStdDevOfReturns()) });
        }

        private String calculateDateRange(Date startDate, Date endDate) {
            Calendar startCalendar = Calendar.getInstance();
            startCalendar.setTime(startDate);
            Calendar endCalendar = Calendar.getInstance();
            endCalendar.setTime(endDate);
            int years = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);
            int startMonths = 12 - (startCalendar.get(Calendar.MONTH) + 1);
            int endMonths = endCalendar.get(Calendar.MONTH) + 1;
            int totalMonths = startMonths + endMonths;
            if (totalMonths > 12) {
                years += 1;
                totalMonths -= 12;
            }
            return years + " : " + totalMonths;
        }

        @Override
        public int getColumnCount() {
            return columnModel.getColumnCount();
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    }

    private static class StatisticsTableCellRenderer extends JLabel implements TableCellRenderer {
        private static final long serialVersionUID = 3976868454184911291L;
		private Set<String> categories;


        private StatisticsTableCellRenderer() {
            setOpaque(true);
            categories = new HashSet<String>(4);
            categories.add("General");
            categories.add("GPAs");
            categories.add("Ratios");
            categories.add("Returns");
        }

        @Override
        public java.awt.Component getTableCellRendererComponent(JTable table,
                                                                Object value,
                                                                boolean isSelected,
                                                                boolean hasFocus,
                                                                int row,
                                                                int column) {
            setForeground(Color.black);
            String category = (String) value;
            setText(category);
            if (categories.contains(category) || category.isEmpty()) {
                setBackground(Colors.tableRowColor);
            } else {
                setBackground(Color.white);
            }
            if (isSelected) {
                setBackground(Colors.navy);
                setForeground(Color.white);
            }
            return this;
        }
    }

    private static class StatisticsListSelectionListener implements ListSelectionListener {
        private BenchmarkPortfolioStatisticsTable benchmarkPortfolioStatisticsTable;
        private BenchmarkPortfolioStatisticsTextPane benchmarkPortfolioStatisticsTextPane;

        public StatisticsListSelectionListener(BenchmarkPortfolioStatisticsTable benchmarkPortfolioStatisticsTable,
                                               BenchmarkPortfolioStatisticsTextPane benchmarkPortfolioStatisticsTextPane) {
            this.benchmarkPortfolioStatisticsTable = benchmarkPortfolioStatisticsTable;
            this.benchmarkPortfolioStatisticsTextPane = benchmarkPortfolioStatisticsTextPane;
        }

        @Override
        public void valueChanged(ListSelectionEvent event) {
            if (!event.getValueIsAdjusting()) {
                benchmarkPortfolioStatisticsTextPane.setText("row: " + benchmarkPortfolioStatisticsTable.getSelectedRow());
            }
        }
    }
}