package client.listener;

import domain.Index;

public interface IndexListModelListener {
    public void onAdd(Index index);
    public void onRemove(Index index);
}