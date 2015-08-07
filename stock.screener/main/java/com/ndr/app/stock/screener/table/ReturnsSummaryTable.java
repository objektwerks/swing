package com.ndr.app.stock.screener.table;

import com.ndr.app.stock.screener.Colors;
import com.ndr.app.stock.screener.comparator.DoubleAsStringComparator;
import com.ndr.app.stock.screener.highlighter.NegativeNumberHighlighter;
import com.ndr.app.stock.screener.listener.ReturnsSummaryListener;
import com.ndr.app.stock.screener.panel.TimeSeriesChartPanel;
import com.ndr.app.stock.screener.text.TextToHtmlSplitter;
import com.ndr.model.stock.screener.DateCalculator;
import com.ndr.model.stock.screener.ReturnsSummary;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.ToolTipManager;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableRowSorter;

import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.decorator.ColorHighlighter;
import org.jdesktop.swingx.decorator.HighlightPredicate;
import org.jdesktop.swingx.decorator.HighlighterFactory;
import org.jdesktop.swingx.table.DefaultTableColumnModelExt;
import org.jdesktop.swingx.table.TableColumnExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public final class ReturnsSummaryTable extends JXTable {
    private static final long serialVersionUID = 4500157548890866146L;
    private static final Pattern dateRangeSeparatorPattern = Pattern.compile(" to ");
    
    @Autowired private TimeSeriesChartPanel timeSeriesChartPanel;
    private SelectedReturnCellRenderer selectedReturnCellRenderer;
    private TotalReturnCellRenderer totalReturnCellRenderer;
    private List<ReturnsSummaryListener> returnsSummaryListeners;

    public ReturnsSummaryTable() {
        selectedReturnCellRenderer = new SelectedReturnCellRenderer();
        totalReturnCellRenderer = new TotalReturnCellRenderer();
        returnsSummaryListeners = new ArrayList<ReturnsSummaryListener>();
    }

    public void setModel(ReturnsSummary returnsSummary) {
        List<String> columns = returnsSummary.getColumnHeaders();
        setColumnModel(new ReturnsSummaryTableColumnModel(columns));
        ReturnsSummaryTableModel model = new ReturnsSummaryTableModel(returnsSummary.getRowHeaders(), returnsSummary.getReturns());
        setModel(model);
        setRowSorter(createRowSorter(model, columns));
    }

    public void selectReturn(String year, int column) {
        ReturnsSummaryTableModel model = (ReturnsSummaryTableModel) getModel();
        int row;
        for (row = 0; row < model.getRowCount(); row++) {
            String currentYear = (String) model.getValueAt(row, 0);
            if (year.equals(currentYear)) {
                String selectedReturn = (String) model.getValueAt(row, column);
                if (!selectedReturn.isEmpty()) {
                    changeSelection(row, column, false, false);
                }
                break;
            }
        }
    }

    public void addReturnsSummaryListener(ReturnsSummaryListener listener) {
        returnsSummaryListeners.add(listener);
    }

    private void notifyReturnsSummaryListeners(String year, int column) {
        for (ReturnsSummaryListener listener : returnsSummaryListeners) {
            listener.returnSelected(year, column);
        }
    }

    @PostConstruct
    protected void build() {
        ToolTipManager.sharedInstance().unregisterComponent(this);
        addKeyListener(new SelectedReturnKeyListener());
        addMouseListener(new SelectedReturnMouseListener());
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setCellSelectionEnabled(true);
        setColumnSelectionAllowed(true);
        setRowSelectionAllowed(true); 
        setAutoCreateColumnsFromModel(false);
        setAutoCreateRowSorter(false);
        setFillsViewportHeight(true);
        setEditable(false);
        addHighlighter(HighlighterFactory.createAlternateStriping(Color.white, Colors.tableRowColor));
        addHighlighter(new ColorHighlighter(HighlightPredicate.ROLLOVER_ROW, null, Color.blue));
        addHighlighter(NegativeNumberHighlighter.instance);
    }

    private TableRowSorter<ReturnsSummaryTableModel> createRowSorter(ReturnsSummaryTableModel model, List<String> columns) {
        TableRowSorter<ReturnsSummaryTableModel> rowSorter = new TableRowSorter<ReturnsSummaryTableModel>(model);
        for (int i = 0; i < columns.size(); i++) {
            rowSorter.setComparator(i, DoubleAsStringComparator.instance);
        }
        return rowSorter;
    }

    private Date createDate(String source) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        Date selectedDate = null;
        try {
            selectedDate = dateFormatter.parse(source);
        } catch (ParseException ignoreInvalidDate) {}
        return selectedDate;
    }

    private void selectReturn(int row, int column) {
        String selectedYear = (String) getValueAt(row, 0);
        String selectedColumnHeader = (String) getColumn(column).getIdentifier();
        String selectedDateRangePart = dateRangeSeparatorPattern.split(selectedColumnHeader)[0];
        Date selectedDate = createDate(selectedYear + "-" + selectedDateRangePart);
        String selectedReturn = (String) getValueAt(row, column);
        if (selectedDate != null && (!selectedReturn.isEmpty())) {
            selectedDate = correctFor4QYearOffset(selectedDate, column);
            short value = DateCalculator.calculateDaysSince1960(selectedDate);
            timeSeriesChartPanel.setSliderValue(value);
            notifyReturnsSummaryListeners(selectedYear, column);
        }
    }

    private Date correctFor4QYearOffset(Date date, int column) {
        if (column == 1) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.YEAR, -1);
            date.setTime(calendar.getTimeInMillis());
        }
        return date;
    }

    private class ReturnsSummaryTableColumnModel extends DefaultTableColumnModelExt {
        private static final long serialVersionUID = -247317874216571920L;

        public ReturnsSummaryTableColumnModel(List<String> columns) {
            for (int i = 0; i < columns.size(); i++) {
                TableColumnExt tableColumn = new TableColumnExt(i);
                if (i > 0 && i < columns.size() - 1) {
                    tableColumn.setCellRenderer(selectedReturnCellRenderer);
                } else if (i == (columns.size() - 1)) {
                    tableColumn.setCellRenderer(totalReturnCellRenderer);
                }
                tableColumn.setIdentifier(columns.get(i));
                tableColumn.setHeaderValue(TextToHtmlSplitter.instance.split(columns.get(i)));
                addColumn(tableColumn);
            }
        }
    }

    private class ReturnsSummaryTableModel extends DefaultTableModel {
        private static final long serialVersionUID = -151230533020891146L;

        public ReturnsSummaryTableModel(List<String> rowHeaders, List<List<Double>> rows) {
            DecimalFormat decimalFormatter = new DecimalFormat("###,###,###,##0.00");
            for (int i = 0; i < rows.size() - 1; i++) {
                List<Double> row = rows.get(i);
                List<Object> formattedRow = new ArrayList<Object>(row.size() + 1);
                formattedRow.add(rowHeaders.get(i));
                for (Double column : row) {
                    if (column == null) {
                        formattedRow.add("");
                    } else {
                        formattedRow.add(decimalFormatter.format(column));
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

    private class SelectedReturnMouseListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent event) {
            Point point = event.getPoint();
            int column = columnAtPoint(point);
            if (column > 0 && column < (getModel().getColumnCount() - 1)) {
                int row = rowAtPoint(point);
                selectReturn(row, column);
            }
        }
    }

    // WARNING: Does not work on Apple OS X, running an executable jar.
    private class SelectedReturnKeyListener extends KeyAdapter {
        @Override
        public void keyReleased(KeyEvent event) {
            int row = getSelectedRow();
            int column = getSelectedColumn();
            if (column > 0 && column < (getModel().getColumnCount() - 1)) {
                selectReturn(row, column);
            }
        }
    }

    private class SelectedReturnCellRenderer extends JLabel implements TableCellRenderer {
        private static final long serialVersionUID = 2638862889033183439L;
        
		private Border defaultBorder;
        private Border selectedBorder;

        public SelectedReturnCellRenderer() {
            defaultBorder = getBorder();
            selectedBorder = BorderFactory.createLineBorder(Color.yellow, 2);
            setHorizontalAlignment(JLabel.CENTER);
            setOpaque(true);
        }

        public java.awt.Component getTableCellRendererComponent(JTable table,
                                                                Object value,
                                                                boolean isSelected,
                                                                boolean hasFocus,
                                                                int row,
                                                                int column) {
            if (isSelected) {
                setForeground(table.getSelectionForeground());
                setBackground(table.getSelectionBackground());
            } else {
                setForeground(table.getForeground());
                setBackground(table.getBackground());
            }
            if (row == table.getSelectedRow() && column == table.getSelectedColumn()) {
                setBorder(selectedBorder);       
            } else {
                setBorder(defaultBorder);
            }
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }

    private class TotalReturnCellRenderer extends DefaultTableCellRenderer {
        public TotalReturnCellRenderer() {
            setHorizontalAlignment(JLabel.RIGHT);
        }

        @Override
        public java.awt.Component getTableCellRendererComponent(JTable table,
                                                                Object value,
                                                                boolean isSelected,
                                                                boolean hasFocus,
                                                                int row,
                                                                int column) {
            String text = value.toString();
            setText(text + " ");
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