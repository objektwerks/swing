package client.list;

import client.listener.DomainListModelListener;
import client.listener.GroupDomainListSelectionListener;
import client.model.DomainListModel;
import client.renderer.DomainListCellRenderer;
import domain.Domain;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.ListSelectionModel;

public final class GroupDomainList extends JList implements DomainListModel {
    private GroupDomainListSelectionListener groupDomainListSelectionListener;
    private DomainListCellRenderer domainListCellRenderer;
    private List <DomainListModelListener> domainListModelListeners;
    private SortedSet <Domain> domains;

    public GroupDomainList() {
        super();
        domainListModelListeners = new ArrayList <DomainListModelListener> ();
    }

    public void build() {
        setModel(new DefaultListModel());
        getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        addListSelectionListener(groupDomainListSelectionListener);
        setCellRenderer(domainListCellRenderer);

    }

    public SortedSet <Domain> getDomains() {
        if (null == domains) {
            domains = new TreeSet <Domain> ();
        }
        return domains;
    }

    public void setDomains(SortedSet <Domain> domains) {
        this.domains = domains;
        DefaultListModel listModel = (DefaultListModel) getModel();
        listModel.clear();
        for (Domain domain : domains) {
            listModel.addElement(domain);
        }
        setModel(listModel);
    }

    public void reset() {
        setDomains(getDomains());
    }

    public void addDomain(Domain domain) {
        domain.getGroup().addDomain(domain);
        getDomains().add(domain);
        setDomains(getDomains());
        setSelectedValue(domain, true);
        for (DomainListModelListener listener : domainListModelListeners) {
            listener.onAdd(domain);
        }
    }

    public void removeDomain(Domain domain) {
        domain.getGroup().removeDomain(domain);
        getDomains().remove(domain);
        setDomains(getDomains());
        for (DomainListModelListener listener : domainListModelListeners) {
            listener.onRemove(domain);
        }
    }

    public Domain getSelectedItem() {
        int selectedIndex = getSelectedIndex();
        return (Domain) getModel().getElementAt(selectedIndex);
    }

    public void setSelectedItem(Domain domain) {
        setSelectedValue(domain, true);
    }

    public void setDomainListModelListeners(List listeners) {
        for (Object listener : listeners) {
            domainListModelListeners.add((DomainListModelListener) listener);
        }
    }

    public void setGroupDomainListSelectionListener(GroupDomainListSelectionListener groupDomainListSelectionListener) {
        this.groupDomainListSelectionListener = groupDomainListSelectionListener;
    }

    public void setDomainListCellRenderer(DomainListCellRenderer domainListCellRenderer) {
        this.domainListCellRenderer = domainListCellRenderer;
    }
}