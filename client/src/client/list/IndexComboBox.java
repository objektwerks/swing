package client.list;

import client.listener.IndexListModelListener;
import client.model.IndexListModel;
import client.renderer.IndexComboBoxCellRenderer;
import domain.Index;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

public final class IndexComboBox extends JComboBox implements IndexListModel, IndexListModelListener {
    private IndexComboBoxCellRenderer indexComboBoxCellRenderer;
    private List <IndexListModelListener> indexListModelListeners;
    private Index noSelectedIndex;
    private SortedSet <Index> indexes;

    public IndexComboBox() {
        super();
        this.indexListModelListeners = new ArrayList <IndexListModelListener> ();
        this.noSelectedIndex = new Index("no index selected");
        this.noSelectedIndex.setId(noSelectedIndex.getName());
    }

    public void build() {
        setModel(new DefaultComboBoxModel());
        setRenderer(indexComboBoxCellRenderer);
    }

    public SortedSet <Index> getIndexes() {
        if (null == indexes) {
            indexes = new TreeSet <Index> ();
        }
        return indexes;
    }

    public void setIndexes(SortedSet <Index> indexes) {
        this.indexes = indexes;
        DefaultComboBoxModel comboBoxModel = (DefaultComboBoxModel) getModel();
        comboBoxModel.removeAllElements();
        comboBoxModel.addElement(noSelectedIndex);
        for (Index index : indexes) {
            comboBoxModel.addElement(index);
        }
        setSelectedItem(noSelectedIndex);
        setModel(comboBoxModel);
    }

    public void reset() {
        setIndexes(getIndexes());
    }

    public void addIndex(Index index) {
        getIndexes().add(index);
        setIndexes(getIndexes());
        setSelectedItem(index);
        for (IndexListModelListener listener : indexListModelListeners) {
            listener.onAdd(index);
        }
    }

    public void removeIndex(Index index) {
        getIndexes().remove(index);
        setIndexes(getIndexes());
        for (IndexListModelListener listener : indexListModelListeners) {
            listener.onRemove(index);
        }
    }

    public Index getSelectedItem() {
        int selectedIndex = getSelectedIndex();
        return (Index) getModel().getElementAt(selectedIndex);
    }

    public void setSelectedItem(Index index) {
        if (null != index) {
            super.setSelectedItem(index);
        } else {
            super.setSelectedIndex(0);
        }
    }

    public void setIndexListModelListeners(List listeners) {
        for (Object listener : listeners) {
            indexListModelListeners.add((IndexListModelListener) listener);
        }
    }

    public void onAdd(Index index) {
        Index currentIndex = getSelectedItem();
        addIndex(index);
        setSelectedItem(currentIndex);
    }

    public void onRemove(Index index) {
        removeIndex(index);
    }

    public void onUpdate(Index index) {
    }

    public void setIndexComboBoxCellRenderer(IndexComboBoxCellRenderer indexComboBoxCellRenderer) {
        this.indexComboBoxCellRenderer = indexComboBoxCellRenderer;
    }
}