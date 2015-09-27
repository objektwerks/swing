package client.action;

import client.common.SwingWorker;
import client.dialog.AddRepositoryDialog;
import client.model.RepositoryListModel;
import client.resource.ResourceManager;
import client.validator.RepositoryValidator;
import client.validator.Validator;
import domain.Repository;
import service.DomainManager;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;

public final class AddRepositoryAction extends AbstractAction {
    private ResourceManager resourceManager;
    private AddRepositoryDialog addRepositoryDialog;
    private RepositoryValidator repositoryValidator;
    private DomainManager domainManager;
    private RepositoryListModel repositoryListModel;

    public AddRepositoryAction() {
        super();
    }

    public void build() {
        putValue(Action.SMALL_ICON, resourceManager.getImageIcon("icon.add.repository"));
        putValue(Action.NAME, resourceManager.getString("action.add.repository"));
    }

    public void setResourceManager(ResourceManager resourceManager) {
        this.resourceManager = resourceManager;
    }

    public void setAddRepositoryDialog(AddRepositoryDialog addRepositoryDialog) {
        this.addRepositoryDialog = addRepositoryDialog;
    }

    public void setRepositoryValidator(RepositoryValidator repositoryValidator) {
        this.repositoryValidator = repositoryValidator;
    }

    public void setDomainManager(DomainManager domainManager) {
        this.domainManager = domainManager;
    }

    public void setRepositoryListModel(RepositoryListModel repositoryListModel) {
        this.repositoryListModel = repositoryListModel;
    }

    public void actionPerformed(ActionEvent event) {
        addRepositoryDialog.view();
        if (!addRepositoryDialog.isCancelled()) {
            Repository repository = addRepositoryDialog.getRepository();
            List <String> errors = new ArrayList <String> ();
            if (repositoryValidator.isValid(errors, repository)) {
                new Worker(repository).start();
            }
        }
    }

    private final class Worker extends SwingWorker {
        private Repository repository;

        public Worker(Repository repository) {
            this.repository = repository;
        }

        public Object construct() {
            Throwable throwable = null;
            try {
                domainManager.addRepository(repository);
            } catch (Throwable t) {
                System.out.println("[AddRepositoryAction] error: " + t.getMessage());
                throwable = t;
            }
            return throwable;
        }

        public void finished() {
            Throwable throwable = (Throwable) get();
            if (null != throwable) {
                String message = resourceManager.getString("unique.name.error") +  repository.getName();
                String title = resourceManager.getString("add.repository.action.error.title");
                Validator.showErrorDialog(message, title, addRepositoryDialog);
            } else {
                repositoryListModel.addRepository(repository);
            }
            System.out.println("[AddRepositoriesAction] : " + repository);
        }
    }
}