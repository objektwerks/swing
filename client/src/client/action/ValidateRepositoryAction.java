package client.action;

import client.dialog.AddRepositoryDialog;
import client.validator.RepositoryValidator;
import client.validator.Validator;
import client.resource.ResourceManager;
import domain.Repository;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;

public final class ValidateRepositoryAction extends AbstractAction {
    private AddRepositoryDialog addRepositoryDialog;
    private RepositoryValidator repositoryValidator;
    private ResourceManager resourceManager;

    public ValidateRepositoryAction() {
        super();
    }

    public void build() {
        putValue(Action.SMALL_ICON, resourceManager.getImageIcon("icon.add.repository"));
        putValue(Action.NAME, resourceManager.getString("action.add.repository"));
    }

    public void setAddRepositoryDialog(AddRepositoryDialog addRepositoryDialog) {
        this.addRepositoryDialog = addRepositoryDialog;
    }

    public void setRepositoryValidator(RepositoryValidator repositoryValidator) {
        this.repositoryValidator = repositoryValidator;
    }

    public void setResourceManager(ResourceManager resourceManager) {
        this.resourceManager = resourceManager;
    }

    public void actionPerformed(ActionEvent event) {
        addRepositoryDialog.setCancelled(false);
        Repository repository = addRepositoryDialog.getRepository();
        List <String> errors = new ArrayList <String> ();
        if (repositoryValidator.isValid(errors, repository)) {
            addRepositoryDialog.setVisible(false);
        } else {
            String title =  resourceManager.getString("validate.repository.error.title");
            Validator.showErrorsDialog(errors, title, addRepositoryDialog);
        }
    }
}