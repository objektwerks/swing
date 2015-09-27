package client.action;

import client.model.IndexListModel;
import client.dialog.SearchDialog;
import domain.Index;
import service.DomainManager;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JTextField;

public final class SearchIndexAction extends AbstractAction {
    public static int limit = 100;

    private IndexListModel indexListModel;
    private DomainManager domainManager;
    private SearchDialog searchDialog;

    public SearchIndexAction() {
        super();
    }

    public void actionPerformed(ActionEvent event) {
        JTextField searchTextField = (JTextField) event.getSource();
        String query = searchTextField.getText();
        Index index = indexListModel.getSelectedItem();
        String [] results = domainManager.search(index, query, limit);
        System.out.println("[SearchIndexAction] search index on: " + index.getName() + " : " + query);
        System.out.println("[SearchIndexAction] results: " + results.length);
        searchTextField.setText("");
        searchDialog.view(results);
    }

    public void setIndexListModel(IndexListModel indexListModel) {
        this.indexListModel = indexListModel;
    }

    public void setDomainManager(DomainManager domainManager) {
        this.domainManager = domainManager;
    }

    public void setSearchDialog(SearchDialog searchDialog) {
        this.searchDialog = searchDialog;
    }

}