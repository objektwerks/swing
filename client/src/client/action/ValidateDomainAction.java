package client.action;

import client.dialog.AddDomainDialog;
import client.resource.ResourceManager;
import client.validator.DomainValidator;
import client.validator.Validator;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;

public final class ValidateDomainAction extends AbstractAction {
    private AddDomainDialog addDomainDialog;
    private DomainValidator domainValidator;
    private ResourceManager resourceManager;

    public ValidateDomainAction() {
        super();
    }

    public void build() {
        putValue(Action.SMALL_ICON, resourceManager.getImageIcon("icon.add.domain"));
        putValue(Action.NAME, resourceManager.getString("action.add.domain"));
    }

    public void setAddDomainDialog(AddDomainDialog addDomainDialog) {
        this.addDomainDialog = addDomainDialog;
    }

    public void setDomainValidator(DomainValidator domainValidator) {
        this.domainValidator = domainValidator;
    }

    public void setResourceManager(ResourceManager resourceManager) {
        this.resourceManager = resourceManager;
    }

    public void actionPerformed(ActionEvent event) {
        addDomainDialog.setCancelled(false);
        String url = addDomainDialog.getUrl();
        List <String> errors = new ArrayList <String> ();
        if (domainValidator.isValid(errors, url)) {
            addDomainDialog.setVisible(false);
        } else {
            String title =  resourceManager.getString("validate.domain.error.title");
            Validator.showErrorsDialog(errors, title, addDomainDialog);
        }
    }
}