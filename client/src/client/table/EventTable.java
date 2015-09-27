package client.table;

import domain.Event;

import java.util.SortedSet;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumnModel;

public final class EventTable extends JTable {
    private EventTableModel eventTableModel;

    public EventTable() {
        super();
    }

    public void build() {
        eventTableModel = new EventTableModel();
        setModel(eventTableModel);
        TableColumnModel columnModel = getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(60);
        columnModel.getColumn(0).setMinWidth(60);
        columnModel.getColumn(1).setPreferredWidth(50);
        columnModel.getColumn(1).setMinWidth(50);
        columnModel.getColumn(2).setPreferredWidth(70);
        columnModel.getColumn(2).setMinWidth(70);
        columnModel.getColumn(3).setPreferredWidth(500);
        columnModel.getColumn(3).setMinWidth(300);
        setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        setPreferredScrollableViewportSize(getPreferredSize());
    }

    public void set(SortedSet <Event> events) {
        eventTableModel.set(events);
    }

    private final class EventTableModel extends AbstractTableModel {
        private Object [] columns;
        private Object [] [] rows;

        public EventTableModel() {
            this.columns = new Object []{"Type", "Time", "Host", "Description"};
            this.rows = new Object [] []{};
        }

        public void set(SortedSet<Event> events) {
            int numberOfRows = events.size();
            int numberOfColumns = columns.length;
            rows = new Object [numberOfRows] [numberOfColumns];
            int row = 0;
            for (Event event : events) {
                rows[row][0] = event.getType();
                rows[row][1] = event.formatTime(event.getDate());
                rows[row][2] = event.getHost();
                rows[row][3] = event.getDescription();
                row++;
            }
            fireTableDataChanged();
        }

        public String getColumnName(int column) {
            return columns[column].toString();
        }

        public int getColumnCount() {
            return columns.length;
        }

        public int getRowCount() {
            return rows.length;
        }

        public Object getValueAt(int row,
                                 int column) {
            return rows[row][column];
        }
    }
}