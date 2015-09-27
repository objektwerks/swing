package client.listener;

import client.model.IndexModel;
import client.action.SearchIndexAction;
import domain.Index;

import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public final class IndexListSelectionListener implements ListSelectionListener {
    private IndexModel indexModel;
    private SearchIndexAction searchIndexAction;

    public IndexListSelectionListener() {
    }

    public void valueChanged(ListSelectionEvent event) {
        JList list = (JList) event.getSource();
        int selectedIndex = list.getSelectedIndex();
        if (selectedIndex > -1) {
            Index index = (Index) list.getModel().getElementAt(selectedIndex);
            indexModel.setIndex(index);
            searchIndexAction.setEnabled(true);
        } else {
            searchIndexAction.setEnabled(false);
        }
    }

    public void setIndexModel(IndexModel indexModel) {
        this.indexModel = indexModel;
    }

    public void setSearchIndexAction(SearchIndexAction searchIndexAction) {
        this.searchIndexAction = searchIndexAction;
    }
}