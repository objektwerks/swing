package client.list;

import client.listener.GroupListModelListener;
import client.listener.GroupListSelectionListener;
import client.model.GroupListModel;
import client.renderer.GroupListCellRenderer;
import domain.Group;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.ListSelectionModel;

public final class GroupList extends JList implements GroupListModel {
    private GroupListSelectionListener groupListSelectionListener;
    private GroupListCellRenderer groupListCellRenderer;
    private List <GroupListModelListener> groupListModelListeners;
    private SortedSet <Group> groups;

    public GroupList() {
        super();
        this.groupListModelListeners = new ArrayList <GroupListModelListener> ();
    }

    public void build() {
        setModel(new DefaultListModel());
        getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        addListSelectionListener(groupListSelectionListener);
        setCellRenderer(groupListCellRenderer);

    }

    public SortedSet <Group> getGroups() {
        if (null == groups) {
            groups = new TreeSet <Group> ();
        }
        return groups;
    }

    public void setGroups(SortedSet <Group> groups) {
        this.groups = groups;
        DefaultListModel listModel = (DefaultListModel) getModel();
        listModel.clear();
        for (Group group : groups) {
            listModel.addElement(group);
        }
        setModel(listModel);
        if (groups.size() > 0) {
            setSelectedIndex(0);
        }
    }

    public void reset() {
        setGroups(getGroups());
    }

    public void addGroup(Group group) {
        groups.add(group);
        setGroups(groups);
        setSelectedValue(group, true);
        for (GroupListModelListener listener : groupListModelListeners) {
            listener.onAdd(group);
        }
    }

    public void removeGroup(Group group) {
        groups.remove(group);
        setGroups(groups);
        for (GroupListModelListener listener : groupListModelListeners) {
            listener.onRemove(group);
        }
    }

    public Group getSelectedItem() {
        int selectedIndex = getSelectedIndex();
        return (Group) getModel().getElementAt(selectedIndex);
    }

    public void setSelectedItem(Group group) {
        this.setSelectedValue(group, true);
    }

    public void setGroupListModelListeners(List listeners) {
        for (Object listener : listeners) {
            groupListModelListeners.add((GroupListModelListener) listener);
        }
    }

    public void setGroupListSelectionListener(GroupListSelectionListener groupListSelectionListener) {
        this.groupListSelectionListener = groupListSelectionListener;
    }

    public void setGroupListCellRenderer(GroupListCellRenderer groupListCellRenderer) {
        this.groupListCellRenderer = groupListCellRenderer;
    }
}