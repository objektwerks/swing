package com.ndr.app.stock.screener.table;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import org.jdesktop.swingx.table.DefaultTableColumnModelExt;
import org.jdesktop.swingx.table.TableColumnExt;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public final class PropertiesTable extends JTable {
    public void setModel(String[] columns, List<Object[]> rows) {
        setColumnModel(new PropertiesTableColumnModel(columns));
        PropertiesTableModel model = new PropertiesTableModel(rows);
        setModel(model);
    }

    @PostConstruct
    protected void build() {
        setAutoCreateColumnsFromModel(false);
        setAutoCreateRowSorter(false);
        setFillsViewportHeight(true);
        setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
    }

    private class PropertiesTableColumnModel extends DefaultTableColumnModelExt {
        public PropertiesTableColumnModel(String[] columns) {
            for (int i = 0; i < columns.length; i++) {
                TableColumnExt tableColumn = new TableColumnExt(i);
                tableColumn.setHeaderValue(columns[i]);
                addColumn(tableColumn);
            }
        }
    }

    private class PropertiesTableModel extends DefaultTableModel {
        public PropertiesTableModel(List<Object[]> rows) {
            for (Object[] row : rows) {
                addRow(row);
            }
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
}