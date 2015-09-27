package client.model;

import domain.Domain;

import java.util.SortedSet;
import java.util.List;

public interface DomainListModel {
    public SortedSet <Domain> getDomains();
    public void setDomains(SortedSet <Domain> domains);
    public void reset();
    public void addDomain(Domain domain);
    public void removeDomain(Domain domain);
    public Domain getSelectedItem();
    public void setSelectedItem(Domain domain);
    public void setDomainListModelListeners(List domainListModelListeners);
}