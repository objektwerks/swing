package client.action;

import client.dialog.AddIndexDialog;
import client.resource.ResourceManager;
import client.validator.IndexValidator;
import client.validator.Validator;
import domain.Index;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;

public final class ValidateIndexAction extends AbstractAction {
    private AddIndexDialog addIndexDialog;
    private IndexValidator indexValidator;
    private ResourceManager resourceManager;

    public ValidateIndexAction() {
        super();
    }

    public void build() {
        putValue(Action.SMALL_ICON, resourceManager.getImageIcon("icon.add.index"));
        putValue(Action.NAME, resourceManager.getString("action.add.index"));
    }

    public void setAddIndexDialog(AddIndexDialog addIndexDialog) {
        this.addIndexDialog = addIndexDialog;
    }

    public void setIndexValidator(IndexValidator indexValidator) {
        this.indexValidator = indexValidator;
    }

    public void setResourceManager(ResourceManager resourceManager) {
        this.resourceManager = resourceManager;
    }

    public void actionPerformed(ActionEvent event) {
        addIndexDialog.setCancelled(false);
        Index index = addIndexDialog.getIndex();
        List <String> errors = new ArrayList <String> ();
        if (indexValidator.isValid(errors, index)) {
            addIndexDialog.setVisible(false);
        } else {
            String title =  resourceManager.getString("validate.index.error.title");
            Validator.showErrorsDialog(errors, title, addIndexDialog);
        }
    }
}