package client.action;

import client.model.DomainListModel;
import client.dialog.SearchDialog;
import domain.Domain;
import service.DomainManager;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JTextField;

public final class SearchDomainAction extends AbstractAction {
    public static int limit = 100;

    private DomainListModel domainListModel;
    private DomainManager domainManager;
    private SearchDialog searchDialog;

    public SearchDomainAction() {
        super();
    }

    public void actionPerformed(ActionEvent event) {
        JTextField searchTextField = (JTextField) event.getSource();
        String query = searchTextField.getText();
        Domain domain = domainListModel.getSelectedItem();
        String [] results = domainManager.search(domain, query, limit);
        System.out.println("[SearchIndexAction] search index on: " + domain.getName() + " : " + query);
        System.out.println("[SearchIndexAction] results: " + results.length);
        searchTextField.setText("");
        searchDialog.view(results);
    }

    public void setDomainListModel(DomainListModel domainListModel) {
        this.domainListModel = domainListModel;
    }

    public void setDomainManager(DomainManager domainManager) {
        this.domainManager = domainManager;
    }

    public void setSearchDialog(SearchDialog searchDialog) {
        this.searchDialog = searchDialog;
    }
}