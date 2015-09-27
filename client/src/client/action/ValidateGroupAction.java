package client.action;

import client.dialog.AddGroupDialog;
import client.resource.ResourceManager;
import client.validator.GroupValidator;
import client.validator.Validator;
import domain.Group;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;

public final class ValidateGroupAction extends AbstractAction {
    private AddGroupDialog addGroupDialog;
    private GroupValidator groupValidator;
    private ResourceManager resourceManager;

    public ValidateGroupAction() {
        super();
    }

    public void build() {
        putValue(Action.SMALL_ICON, resourceManager.getImageIcon("icon.add.group"));
        putValue(Action.NAME, resourceManager.getString("action.add.group"));
    }

    public void setAddGroupDialog(AddGroupDialog addGroupDialog) {
        this.addGroupDialog = addGroupDialog;
    }

    public void setGroupValidator(GroupValidator groupValidator) {
        this.groupValidator = groupValidator;
    }

    public void setResourceManager(ResourceManager resourceManager) {
        this.resourceManager = resourceManager;
    }

    public void actionPerformed(ActionEvent event) {
        addGroupDialog.setCancelled(false);
        Group group = addGroupDialog.getGroup();
        List <String> errors = new ArrayList <String> ();
        if (groupValidator.isValid(errors, group)) {
            addGroupDialog.setVisible(false);
        } else {
            String title =  resourceManager.getString("validate.group.error.title");
            Validator.showErrorsDialog(errors, title, addGroupDialog);
        }
    }
}