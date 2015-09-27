package client.action;

import client.common.SwingWorker;
import client.dialog.AddGroupDialog;
import client.model.GroupListModel;
import client.model.GroupModel;
import client.resource.ResourceManager;
import client.validator.GroupValidator;
import client.validator.Validator;
import domain.Group;
import service.DomainManager;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;

public final class AddGroupAction extends AbstractAction {
    private ResourceManager resourceManager;
    private AddGroupDialog addGroupDialog;
    private GroupValidator groupValidator;
    private GroupModel groupModel;
    private DomainManager domainManager;
    private GroupListModel groupListModel;

    public AddGroupAction() {
        super();
    }

    public void build() {
        putValue(Action.SMALL_ICON, resourceManager.getImageIcon("icon.add.group"));
        putValue(Action.NAME, resourceManager.getString("action.add.group"));
    }

    public void setResourceManager(ResourceManager resourceManager) {
        this.resourceManager = resourceManager;
    }

    public void setAddGroupDialog(AddGroupDialog addGroupDialog) {
        this.addGroupDialog = addGroupDialog;
    }

    public void setGroupValidator(GroupValidator groupValidator) {
        this.groupValidator = groupValidator;
    }

    public void setGroupModel(GroupModel groupModel) {
        this.groupModel = groupModel;
    }

    public void setDomainManager(DomainManager domainManager) {
        this.domainManager = domainManager;
    }

    public void setGroupListModel(GroupListModel groupListModel) {
        this.groupListModel = groupListModel;
    }

    public void actionPerformed(ActionEvent event) {
        addGroupDialog.view();
        if (!addGroupDialog.isCancelled()) {
            Group group = addGroupDialog.getGroup();
            List <String> errors = new ArrayList <String>();
            if (groupValidator.isValid(errors, group)) {
                group.setRepository(groupModel.getRepository());
                new Worker(group).start();
            }
        }
    }

    private final class Worker extends SwingWorker {
        private Group group;

        public Worker(Group group) {
            this.group = group;
        }

        public Object construct() {
            Throwable throwable = null;
            try {
                domainManager.addGroup(group);
            } catch (Throwable t) {
                System.out.println("[AddGroupAction] error: " + t.getMessage());
                throwable = t;
            }
            return throwable;
        }

        public void finished() {
            Throwable throwable = (Throwable) get();
            if (null != throwable) {
                String message = resourceManager.getString("unique.name.error") + group.getName();
                String title = resourceManager.getString("add.group.action.error.title");
                Validator.showErrorDialog(message, title, addGroupDialog);
            } else {
                groupModel.setGroup(group);
                groupListModel.addGroup(group);
            }
            System.out.println("[AddGroupAction] : " + group);
        }
    }
}