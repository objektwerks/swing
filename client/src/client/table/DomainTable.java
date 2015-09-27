package client.table;

import domain.Domain;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.ArrayList;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumnModel;

public final class DomainTable extends JTable {
    private DomainTableModel domainTableModel;

    public DomainTable() {
        super();
    }

    public void build() {
        domainTableModel = new DomainTableModel();
        setModel(domainTableModel);
        TableColumnModel columnModel = getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(100);
        columnModel.getColumn(0).setMinWidth(100);
        columnModel.getColumn(1).setPreferredWidth(400);
        columnModel.getColumn(1).setMinWidth(400);
        setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        setPreferredScrollableViewportSize(getPreferredSize());
    }

    public List <Domain> get() {
        return domainTableModel.get();
    }

    public void set(List <String> urls) {
        domainTableModel.set(urls);
    }

    private final class DomainTableModel extends AbstractTableModel {
        private Object [] columns;
        private Object [] [] rows;

        public DomainTableModel() {
            this.columns = new Object []{"Name", "Url"};
            this.rows = new Object [] []{};
        }

        public List <Domain> get() {
            List <Domain> domains = new ArrayList<Domain> ();
            String name;
            String url;
            for (Object [] row : rows) {
                name = (String) row [0];
                url = (String) row [1];
                addDomain(name, url, domains);
            }
            return domains;
        }

        public void set(List <String> urls) {
            int numberOfRows = urls.size();
            int numberOfColumns = columns.length;
            rows = new Object [numberOfRows] [numberOfColumns];
            int row = 0;
            for (String url : urls) {
                rows[row][0] = getName(url);
                rows[row][1] = url;
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

        public void setValueAt(Object value,
                               int row,
                               int column) {
            rows[row][column] = value;
         }

        public boolean isCellEditable(int row,
                                      int column) {
            return true;
        }

        public Class getColumnClass(int column) {
            return String.class;
        }

        private String getName(String url) {
            String name;
            try {
                name = new URL(url).getHost();
            } catch (MalformedURLException e) {
                name = "";
            }
            return name;
        }

        private void addDomain(String name,
                               String url,
                               List <Domain> domains) {
            try {
                Domain domain = new Domain(url);
                domain.setName(name);
                domains.add(domain);
            } catch (MalformedURLException ignore) {}
        }
    }
}