package client.model;

import domain.Group;

import java.util.SortedSet;
import java.util.List;

public interface GroupListModel {
    public SortedSet <Group> getGroups();
    public void setGroups(SortedSet <Group> groups);
    public void reset();
    public void addGroup(Group group);
    public void removeGroup(Group group);
    public Group getSelectedItem();
    public void setSelectedItem(Group group);
    public void setGroupListModelListeners(List groupListModelListeners);
}