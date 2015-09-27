package client.listener;

import domain.Repository;

public interface RepositoryListModelListener {
    public void onAdd(Repository repository);
    public void onRemove(Repository repository);
}