package client.listener;

import client.model.DomainModel;
import domain.Domain;

import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.Action;
import javax.swing.JList;

public final class GroupDomainListSelectionListener implements ListSelectionListener {
    private DomainModel domainModel;
    private Action updateDomainAction;
    private Action searchDomainAction;

    public GroupDomainListSelectionListener() {
    }

    public void valueChanged(ListSelectionEvent event) {
        JList list = (JList) event.getSource();
        int selectedIndex = list.getSelectedIndex();
        if (selectedIndex > -1) {
            Domain domain = (Domain) list.getModel().getElementAt(selectedIndex);
            domainModel.setDomain(domain);
            if (domain.isCrawlInProgress()) {
                updateDomainAction.setEnabled(false);
                searchDomainAction.setEnabled(false);
            } else {
                updateDomainAction.setEnabled(true);
                searchDomainAction.setEnabled(true);                
            }
        } else {
            updateDomainAction.setEnabled(false);
            searchDomainAction.setEnabled(false);
        }
    }

    public void setDomainModel(DomainModel domainModel) {
        this.domainModel = domainModel;
    }

    public void setUpdateDomainAction(Action updateDomainAction) {
        this.updateDomainAction = updateDomainAction;
    }

    public void setSearchDomainAction(Action searchDomainAction) {
        this.searchDomainAction = searchDomainAction;
    }
}