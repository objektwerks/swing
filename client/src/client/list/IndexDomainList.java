package client.list;

import client.listener.DomainListModelListener;
import client.listener.IndexModelListener;
import client.model.DomainListModel;
import client.renderer.DomainListCellRenderer;
import domain.Domain;
import domain.Index;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.ListSelectionModel;

public final class IndexDomainList extends JList implements DomainListModel, IndexModelListener {
    private DomainListCellRenderer domainListCellRenderer;
    private List <DomainListModelListener> domainListModelListeners;
    private SortedSet <Domain> domains;

    public IndexDomainList() {
        super();
        domainListModelListeners = new ArrayList<DomainListModelListener> ();
    }

    public void build() {
        setEnabled(false);
        setModel(new DefaultListModel());
        getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
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
        getDomains().add(domain);
        setDomains(getDomains());
        setSelectedValue(domain, true);
        for (DomainListModelListener listener : domainListModelListeners) {
            listener.onAdd(domain);
        }
    }

    public void removeDomain(Domain domain) {
        domain.getIndex().removeDomain(domain);
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

    public void onSelection(Index index) {
        setDomains(index.getDomains());
    }

    public void setDomainListModelListeners(List listeners) {
        for (Object listener : listeners) {
            domainListModelListeners.add((DomainListModelListener) listener);
        }
    }

    public void setDomainListCellRenderer(DomainListCellRenderer domainListCellRenderer) {
        this.domainListCellRenderer = domainListCellRenderer;
    }
}