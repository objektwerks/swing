package client.action;

import client.model.DomainListModel;
import domain.Domain;

import java.awt.event.ActionEvent;
import java.util.SortedSet;

import javax.swing.AbstractAction;
import javax.swing.JTextField;

public final class SearchDomainListModelAction extends AbstractAction {
    private DomainListModel domainListModel;

    public SearchDomainListModelAction() {
        super();
    }

    public void actionPerformed(ActionEvent event) {
        JTextField searchTextField = (JTextField) event.getSource();
        String searchExpression = searchTextField.getText();
        SortedSet <Domain> domains = domainListModel.getDomains();
        for (Domain domain : domains) {
            if (domain.getName().startsWith(searchExpression)) {
                domainListModel.setSelectedItem(domain);
                searchTextField.setText("");
            }
        }
    }

    public void setDomainListModel(DomainListModel domainListModel) {
        this.domainListModel = domainListModel;
    }
}