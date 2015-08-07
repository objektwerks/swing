package com.ndr.app.stock.screener.table;

import com.ndr.app.stock.screener.Colors;
import com.ndr.app.stock.screener.comparator.DoubleAsStringComparator;
import com.ndr.app.stock.screener.highlighter.NegativeNumberHighlighter;
import com.ndr.app.stock.screener.menu.FindPopupMenu;
import com.ndr.app.stock.screener.resource.ResourceManager;
import com.ndr.app.stock.screener.text.TextToHtmlSplitter;

import java.awt.Color;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.decorator.ColorHighlighter;
import org.jdesktop.swingx.decorator.HighlightPredicate;
import org.jdesktop.swingx.decorator.HighlighterFactory;
import org.jdesktop.swingx.table.DefaultTableColumnModelExt;
import org.jdesktop.swingx.table.TableColumnExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public final class StockTable extends JXTable {
    private static final long serialVersionUID = 1242596083329705484L;
    
	@Autowired private ResourceManager resourceManager;
    private final StockTableColumnCellRenderer stockTableColumnCellRenderer;

    public StockTable() {
        stockTableColumnCellRenderer = new StockTableColumnCellRenderer();
    }

    public void setModel(List<String> columns, List<List<Object>> rows) {
        setColumnModel(new StockTableColumnModel(columns));
        StockTableModel model = new StockTableModel(rows);
        setModel(model);
        setRowSorter(createRowSorter(model, columns));
        packAll();
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
        addHighlighter(NegativeNumberHighlighter.instance);
        setComponentPopupMenu(new FindPopupMenu(resourceManager, getActionMap().get("find")));
        setFillsViewportHeight(true);
    }

    private TableRowSorter<StockTableModel> createRowSorter(StockTableModel model, List<String> columns) {
        TableRowSorter<StockTableModel> rowSorter = new TableRowSorter<StockTableModel>(model);
        for (int i = 0; i < columns.size(); i++) {
            rowSorter.setComparator(i, DoubleAsStringComparator.instance);
        }
        rowSorter.toggleSortOrder(0);
        return rowSorter;
    }

    private class StockTableColumnModel extends DefaultTableColumnModelExt {
        private static final long serialVersionUID = -7893844560750388101L;

		public StockTableColumnModel(List<String> columns) {
            for (int i = 0; i < columns.size(); i++) {
                TableColumnExt tableColumn = new TableColumnExt(i);
                tableColumn.setCellRenderer(stockTableColumnCellRenderer);
                tableColumn.setHeaderValue(TextToHtmlSplitter.instance.split(columns.get(i)));
                addColumn(tableColumn);
            }
        }
    }

    private class StockTableModel extends DefaultTableModel {
        private static final long serialVersionUID = 2268127987354767983L;

		public StockTableModel(List<List<Object>> rows) {
            DecimalFormat decimalFormatter = new DecimalFormat("###,###,###,##0.00");
            for (List<Object> row : rows) {
                List<Object> formattedRow = new ArrayList<Object>(row.size());
                for (Object column : row) {
                    if (column == null) {
                        formattedRow.add("");
                    } else if (column instanceof Number) {
                        formattedRow.add(decimalFormatter.format(column));
                    } else {
                        formattedRow.add(column);
                    }
                }
                addRow(formattedRow.toArray());
            }
        }

        @Override
        public int getColumnCount() {
            return columnModel.getColumnCount();
        }
    }

    private class StockTableColumnCellRenderer extends DefaultTableCellRenderer {
        private static final long serialVersionUID = 7700687857423506246L;

		@Override
        public java.awt.Component getTableCellRendererComponent(JTable table,
                                                                Object value,
                                                                boolean isSelected,
                                                                boolean hasFocus,
                                                                int row,
                                                                int column) {
            String text = value.toString();
            try {
                Double.parseDouble(text);
                setHorizontalAlignment(JLabel.RIGHT);
                setText(text + " ");
            } catch (NumberFormatException e) {
                setHorizontalAlignment(JLabel.LEFT);
                setText(text);
            }
            if (isSelected) {
                setBackground(table.getSelectionBackground());
                setForeground(table.getSelectionForeground());
            } else {
                setBackground(table.getBackground());
                setForeground(table.getForeground());
            }
            return this;
        }
    }
}