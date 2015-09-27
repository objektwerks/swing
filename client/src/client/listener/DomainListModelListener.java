package client.listener;

import domain.Domain;

public interface DomainListModelListener {
    public void onAdd(Domain domain);
    public void onRemove(Domain domain);
}