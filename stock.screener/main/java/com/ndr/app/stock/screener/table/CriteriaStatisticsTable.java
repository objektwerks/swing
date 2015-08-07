package com.ndr.app.stock.screener.table;

import com.ndr.app.stock.screener.Colors;
import com.ndr.app.stock.screener.text.TextToHtmlSplitter;
import com.ndr.model.stock.screener.RebalanceStatistics;

import java.awt.Color;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.swing.JTable;
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
public final class CriteriaStatisticsTable extends JXTable {
    private static final long serialVersionUID = -2411936291978399076L;

	public void setModel(Map<String, Map<RebalanceStatistics.StatisticsItem, Object>> statistics) {
        setColumnModel(new StatisticsTableColumnModel("Statistic", statistics.keySet()));
        StatisticsTableModel model = new StatisticsTableModel(statistics);
        setModel(model);
        setRowSorter(new TableRowSorter<StatisticsTableModel>(model));
    }

    @PostConstruct
    protected void build() {
        setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        setHorizontalScrollEnabled(true);
        setAutoCreateColumnsFromModel(false);
        setAutoCreateRowSorter(false);
        setEditable(false);
        addHighlighter(HighlighterFactory.createAlternateStriping(Color.white, Colors.tableRowColor));
        addHighlighter(new ColorHighlighter(HighlightPredicate.ROLLOVER_ROW, null, Color.blue));
    }

    private class StatisticsTableColumnModel extends DefaultTableColumnModelExt {
        private static final long serialVersionUID = -3389764254779445485L;

		public StatisticsTableColumnModel(String defaultColumn, Set<String> columns) {
            List<String> tableColumns = new ArrayList<String>(columns);
            tableColumns.add(0, defaultColumn);
            for (int i = 0; i < tableColumns.size(); i++) {
                TableColumnExt tableColumn = new TableColumnExt(i);
                tableColumn.setHeaderValue(TextToHtmlSplitter.instance.split(tableColumns.get(i)));
                addColumn(tableColumn);
            }
        }
    }

    private class StatisticsTableModel extends DefaultTableModel {
        private static final long serialVersionUID = -2778356652609963025L;

		public StatisticsTableModel(Map<String, Map<RebalanceStatistics.StatisticsItem, Object>> statistics) {
            DecimalFormat decimalFormatter = new DecimalFormat("###,###,###,###.##");
            List<Object[]> rows = new ArrayList<Object[]>(6);
            for (RebalanceStatistics.StatisticsItem statisticsItem : RebalanceStatistics.StatisticsItem.values()) {
                Object[] row = new Object[statistics.size() + 1];
                row[0] = statisticsItem;
                int i = 1;
                for (Map.Entry<String, Map<RebalanceStatistics.StatisticsItem, Object>> entry : statistics.entrySet()) {
                    row[i] = decimalFormatter.format(entry.getValue().get(statisticsItem));
                    i++;
                }
                rows.add(row);
            }
            for (Object[] row : rows) {
                addRow(row);
            }
        }

        @Override
        public int getColumnCount() {
            return columnModel.getColumnCount();
        }
    }
}