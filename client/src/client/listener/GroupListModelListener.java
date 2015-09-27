package client.listener;

import domain.Group;

public interface GroupListModelListener {
    public void onAdd(Group group);
    public void onRemove(Group group);
}