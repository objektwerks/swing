package com.ndr.app.stock.screener.table;

import com.ndr.app.stock.screener.button.DemoteCriteriaButton;
import com.ndr.app.stock.screener.button.RemoveCriteriaButton;
import com.ndr.app.stock.screener.component.CriteriaComponent;
import com.ndr.app.stock.screener.resource.ResourceManager;
import com.ndr.app.stock.screener.search.Searchable;
import com.ndr.app.stock.screener.text.TextToHtmlSplitter;
import com.ndr.model.stock.screener.Criteria;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collections;
import java.util.Comparator;
import java.util.EventObject;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.CellEditorListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;

import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.table.DefaultTableColumnModelExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public final class CriteriaComponentTable extends JXTable implements Searchable {
    private static final long serialVersionUID = 4026074579473033864L;
    
	@Autowired private ResourceManager resourceManager;
    @Autowired private RemoveCriteriaButton removeCriteriaButton;
    @Autowired private DemoteCriteriaButton demoteCriteriaButton;
    private CriteriaComponentComparator criteriaComponentComparator;

    public CriteriaComponentTable() {
        criteriaComponentComparator = new CriteriaComponentComparator();
    }

    public void setModel(List<CriteriaComponent> criteriaComponents) {
        setColumnModel(new CriteriaComponentTableColumnModel(new String[] { resourceManager.getString("criteria") }));
        CriteriaComponentTableModel model = new CriteriaComponentTableModel(criteriaComponents);
        setModel(model);
        setRowSorter(createRowSorter(model));
    }

    public CriteriaComponent getSelectedCriteriaComponent() {
        return (CriteriaComponent) getModel().getValueAt(getSelectedRow(), getSelectedColumn());
    }

    public void setSelectedCriteriaComponent(CriteriaComponent selectedCriteriaComponent) {
        CriteriaComponentTableModel model = (CriteriaComponentTableModel) getModel();
        for (int i = 0; i < model.getRowCount(); i++) {
            CriteriaComponent criteriaComponent = (CriteriaComponent) model.getValueAt(i, 0);
            criteriaComponent.setSelected(false);
        }
        selectedCriteriaComponent.setSelected(true);
        removeCriteriaButton.setEnabled(true);
        demoteCriteriaButton.setEnabled(true);
    }

    public void addCriteriaComponent(CriteriaComponent criteriaComponent) {
        CriteriaComponentTableModel model = (CriteriaComponentTableModel) getModel();
        model.addRow(criteriaComponent.toArray());
        criteriaComponent.addMouseListener(new CriteriaComponentMouseListener());
    }

    public void removeCriteriaComponent(CriteriaComponent criteriaComponent) {
        CriteriaComponentTableModel model = (CriteriaComponentTableModel) getModel();
        CriteriaComponent other;
        for (int i = 0; i < model.getRowCount(); i++) {
            other = (CriteriaComponent) model.getValueAt(i, 0);
            if (other.equals(criteriaComponent)) {
                model.removeRow(i);
                break;
            }
        }
    }

    public boolean containsCriteria(Criteria criteria) {
        boolean containsCriteria = false;
        CriteriaComponentTableModel model = (CriteriaComponentTableModel) getModel();
        for (int i = 0; i < model.getRowCount(); i++) {
            CriteriaComponent component = (CriteriaComponent) model.getValueAt(i, 0);
            if (criteria.equals(component.getModel())) {
                containsCriteria = true;
                break;
            }
        }
        return containsCriteria;
    }

    public boolean search(String text) {
        boolean textFound = false;
        CriteriaComponentTableModel model = (CriteriaComponentTableModel) getModel();
        for (int i = 0; i < model.getRowCount(); i++) {
            String item = model.getValueAt(i, 0).toString();
            if (item != null && (item.indexOf(text) > -1 || item.toLowerCase().indexOf(text) > -1)) {
                scrollRowToVisible(i);
                getSelectionModel().setSelectionInterval(i, i);
                textFound = true;
                break;
            }
        }
        return textFound;
    }

    @PostConstruct
    protected void build() {
        setAutoCreateColumnsFromModel(false);
        setAutoCreateRowSorter(false);
        setDragEnabled(false);
        setFillsViewportHeight(true);
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    private TableRowSorter<CriteriaComponentTableModel> createRowSorter(CriteriaComponentTableModel model) {
        TableRowSorter<CriteriaComponentTableModel> rowSorter = new TableRowSorter<CriteriaComponentTableModel>(model);
        rowSorter.setComparator(0, criteriaComponentComparator);
        rowSorter.toggleSortOrder(0);
        return rowSorter;
    }
    
    private class CriteriaComponentTableColumnModel extends DefaultTableColumnModelExt {
        private static final long serialVersionUID = 1283943114658730738L;

		public CriteriaComponentTableColumnModel(String[] columns) {
            for (int i = 0; i < columns.length; i++) {
                TableColumn tableColumn = new TableColumn(i, 390);
                tableColumn.setCellEditor(new CriteriaComponentCellEditor());
                tableColumn.setCellRenderer(new CriteriaComponentCellRenderer());
                tableColumn.setHeaderValue(TextToHtmlSplitter.instance.split(columns[i]));
                addColumn(tableColumn);
            }
        }
    }

    private class CriteriaComponentTableModel extends DefaultTableModel {
        private static final long serialVersionUID = 8516171842910143135L;

		public CriteriaComponentTableModel(List<CriteriaComponent> criteria) {
            Collections.sort(criteria, criteriaComponentComparator);
            for (CriteriaComponent criterion : criteria) {
                criterion.addMouseListener(new CriteriaComponentMouseListener());
                addRow(criterion.toArray());
            }
        }

        @Override
        public int getColumnCount() {
            return columnModel.getColumnCount();
        }
    }

    private static class CriteriaComponentComparator implements Comparator<CriteriaComponent> {
        @Override
        public int compare(CriteriaComponent first, CriteriaComponent second) {
            return first.getName().compareTo(second.getName());
        }
    }
    
    private class CriteriaComponentCellRenderer implements TableCellRenderer {
        public java.awt.Component getTableCellRendererComponent(JTable table,
                                                                Object value,
                                                                boolean isSelected,
                                                                boolean hasFocus,
                                                                int row,
                                                                int column) {
            CriteriaComponent criteriaComponent = (CriteriaComponent) value;
            setRowHeight(row, criteriaComponent.getPreferredSize().height);
            criteriaComponent.setSelected(isSelected);
            return criteriaComponent;
        }
    }

    private class CriteriaComponentCellEditor implements TableCellEditor {
        public java.awt.Component getTableCellEditorComponent(JTable table,
                                                              Object value,
                                                              boolean isSelected,
                                                              int row,
                                                              int column) {
            return (CriteriaComponent) value;
        }

        public Object getCellEditorValue() {
            return new Object();
        }

        public boolean isCellEditable(EventObject event) {
            return true;
        }

        public boolean shouldSelectCell(EventObject event) {
            return true;
        }

        public boolean stopCellEditing() {
            return true;
        }

        public void cancelCellEditing() {}

        public void addCellEditorListener(CellEditorListener listener) {}

        public void removeCellEditorListener(CellEditorListener listener) {}
    }

    private class CriteriaComponentMouseListener extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent event) {
            CriteriaComponent criteriaComponent = (CriteriaComponent) event.getSource();
            setSelectedCriteriaComponent(criteriaComponent);
        }
    }
}