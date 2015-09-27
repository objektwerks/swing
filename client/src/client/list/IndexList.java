package client.list;

import client.listener.DomainModelListener;
import client.listener.IndexListModelListener;
import client.listener.IndexListSelectionListener;
import client.model.IndexListModel;
import client.model.IndexModel;
import client.renderer.IndexListCellRenderer;
import domain.Domain;
import domain.Index;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.ListSelectionModel;

public final class IndexList extends JList implements IndexListModel, DomainModelListener {
    private IndexModel indexModel;
    private IndexListSelectionListener indexListSelectionListener;
    private IndexListCellRenderer indexListCellRenderer;
    private List <IndexListModelListener> indexListModelListeners;
    private SortedSet <Index> indexes;

    public IndexList() {
        super();
        this.indexListModelListeners = new ArrayList <IndexListModelListener> ();
    }

    public void build() {
        setModel(new DefaultListModel());
        getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        addListSelectionListener(indexListSelectionListener);
        setCellRenderer(indexListCellRenderer);

    }

    public SortedSet <Index> getIndexes() {
        if (null == indexes) {
            indexes = new TreeSet <Index> ();
        }
        return indexes;
    }

    public void setIndexes(SortedSet <Index> indexes) {
        this.indexes = indexes;
        DefaultListModel listModel = (DefaultListModel) getModel();
        listModel.clear();
        for (Index index : indexes) {
            listModel.addElement(index);
        }
        setModel(listModel);
        if (indexes.size() > 0) {
            setSelectedIndex(0);
        }
     }

    public void reset() {
        setIndexes(getIndexes());
    }

    public void addIndex(Index index) {
        indexes.add(index);
        setIndexes(indexes);
        setSelectedValue(index, true);
        for (IndexListModelListener listener : indexListModelListeners) {
            listener.onAdd(index);
        }
    }

    public void removeIndex(Index index) {
        indexes.remove(index);
        setIndexes(indexes);
        for (IndexListModelListener listener : indexListModelListeners) {
            listener.onRemove(index);
        }
    }

    public Index getSelectedItem() {
        int selectedIndex = getSelectedIndex();
        return (Index) getModel().getElementAt(selectedIndex);
    }

    public void setSelectedItem(Index index) {
        setSelectedValue(index, true);
    }

    public void setSelectedItem(Domain domain) {
        Index index = domain.getIndex();
        if (null != index) {
            setSelectedItem(index);
            indexModel.setIndex(index);
        }
    }

    public void setIndexListModelListeners(List listeners) {
        for (Object listener : listeners) {
            indexListModelListeners.add((IndexListModelListener) listener);
        }
    }

    public void onSelection(Domain domain) {
        for (Index index : getIndexes()) {
            if (index.getDomains().contains(domain)) {
                index.removeDomain(domain);
            }
        }
        for (Index index : getIndexes()) {
            if (index.equals(domain.getIndex())) {
                index.addDomain(domain);
            }
        }
        reset();
        setSelectedItem(domain);
    }

    public void setIndexModel(IndexModel indexModel) {
        this.indexModel = indexModel;
    }

    public void setIndexListSelectionListener(IndexListSelectionListener indexListSelectionListener) {
        this.indexListSelectionListener = indexListSelectionListener;
    }

    public void setIndexListCellRenderer(IndexListCellRenderer indexListCellRenderer) {
        this.indexListCellRenderer = indexListCellRenderer;
    }
}