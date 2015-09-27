package client.table;

import domain.Domain;

import java.util.SortedSet;

import javax.swing.JTable;
import javax.swing.table.TableColumnModel;
import javax.swing.table.AbstractTableModel;

public final class CrawlsInProgressTable extends JTable {
    private CrawlsInProgressTableModel crawlsInProgressTableModel;

    public CrawlsInProgressTable() {
        super();
    }

    public void build() {
        crawlsInProgressTableModel = new CrawlsInProgressTableModel();
        setModel(crawlsInProgressTableModel);
        TableColumnModel columnModel = getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(60);
        columnModel.getColumn(0).setMinWidth(60);
        columnModel.getColumn(1).setPreferredWidth(60);
        columnModel.getColumn(1).setMinWidth(60);
        columnModel.getColumn(2).setPreferredWidth(120);
        columnModel.getColumn(2).setMinWidth(120);
        columnModel.getColumn(3).setPreferredWidth(120);
        columnModel.getColumn(3).setMinWidth(120);
        setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        setPreferredScrollableViewportSize(getPreferredSize());
    }

    public void set(SortedSet <Domain> crawlsInProgress) {
        crawlsInProgressTableModel.set(crawlsInProgress);
    }

    private final class CrawlsInProgressTableModel extends AbstractTableModel {
        private Object [] columns;
        private Object [] [] rows;

        public CrawlsInProgressTableModel() {
            this.columns = new Object [] { "Domain", "On Queue", "Crawl Date", "Elapsed Craw Time" };
            this.rows = new Object [] [] {};
        }

        public void set(SortedSet <Domain> domains) {
            int numberOfRows = domains.size();
            int numberOfColumns = columns.length;
            rows = new Object [numberOfRows] [numberOfColumns];
            int row = 0;
            for (Domain domain : domains) {
                rows[row] [0] = domain.getName();
                rows[row] [1] = domain.isOnQueue();
                rows[row] [2] = domain.format(domain.getCrawlDate());
                rows[row] [3] = domain.getElapsedCrawlTime();
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
            return rows[row] [column];
        }
    }
}