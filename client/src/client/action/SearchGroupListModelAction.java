package client.action;

import client.model.GroupListModel;
import domain.Group;

import java.awt.event.ActionEvent;
import java.util.SortedSet;

import javax.swing.AbstractAction;
import javax.swing.JTextField;

public final class SearchGroupListModelAction extends AbstractAction {
    private GroupListModel groupListModel;

    public SearchGroupListModelAction() {
        super();
    }

    public void actionPerformed(ActionEvent event) {
        JTextField searchTextField = (JTextField) event.getSource();
        String searchExpression = searchTextField.getText();
        SortedSet <Group> groups = groupListModel.getGroups();
        for (Group group : groups) {
            if (group.getName().startsWith(searchExpression)) {
                groupListModel.setSelectedItem(group);
                searchTextField.setText("");
            }
        }
    }

    public void setGroupListModel(GroupListModel groupListModel) {
        this.groupListModel = groupListModel;
    }
}