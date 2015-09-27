package client.listener;

import client.model.GroupModel;
import domain.Group;

import javax.swing.JList;
import javax.swing.Action;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public final class GroupListSelectionListener implements ListSelectionListener {
    private GroupModel groupModel;
    private Action addDomainAction;
    private Action loadDomainsAction;

    public GroupListSelectionListener() {
    }

    public void valueChanged(ListSelectionEvent event) {
        JList list = (JList) event.getSource();
        int selectedIndex = list.getSelectedIndex();
        if (selectedIndex > -1) {
            Group group = (Group) list.getModel().getElementAt(selectedIndex);
            groupModel.setGroup(group);
            addDomainAction.setEnabled(true);
            loadDomainsAction.setEnabled(true);
        } else {
            addDomainAction.setEnabled(false);
            loadDomainsAction.setEnabled(false);
        }
    }

    public void setGroupModel(GroupModel groupModel) {
        this.groupModel = groupModel;
    }

    public void setAddDomainAction(Action addDomainAction) {
        this.addDomainAction = addDomainAction;
    }

    public void setLoadDomainsAction(Action loadDomainsAction) {
        this.loadDomainsAction = loadDomainsAction;
    }
}