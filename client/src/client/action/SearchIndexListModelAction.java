package client.action;

import client.model.IndexListModel;
import domain.Index;

import java.awt.event.ActionEvent;
import java.util.SortedSet;

import javax.swing.AbstractAction;
import javax.swing.JTextField;

public final class SearchIndexListModelAction extends AbstractAction {
    private IndexListModel indexListModel;

    public SearchIndexListModelAction() {
        super();
    }

    public void actionPerformed(ActionEvent event) {
        JTextField searchTextField = (JTextField) event.getSource();
        String searchExpression = searchTextField.getText();
        SortedSet <Index> indexes = indexListModel.getIndexes();
        for (Index index : indexes) {
            if (index.getName().startsWith(searchExpression)) {
                indexListModel.setSelectedItem(index);
                searchTextField.setText("");
            }
        }
    }

    public void setIndexListModel(IndexListModel indexListModel) {
        this.indexListModel = indexListModel;
    }
}