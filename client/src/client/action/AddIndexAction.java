package client.action;

import client.common.SwingWorker;
import client.dialog.AddIndexDialog;
import client.model.IndexListModel;
import client.model.IndexModel;
import client.resource.ResourceManager;
import client.validator.IndexValidator;
import client.validator.Validator;
import domain.Index;
import service.DomainManager;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;

public final class AddIndexAction extends AbstractAction {
    private ResourceManager resourceManager;
    private AddIndexDialog addIndexDialog;
    private IndexValidator indexValidator;
    private IndexModel indexModel;
    private DomainManager domainManager;
    private IndexListModel indexListModel;

    public AddIndexAction() {
        super();
    }

    public void build() {
        putValue(Action.SMALL_ICON, resourceManager.getImageIcon("icon.add.index"));
        putValue(Action.NAME, resourceManager.getString("action.add.index"));
    }

    public void setResourceManager(ResourceManager resourceManager) {
        this.resourceManager = resourceManager;
    }

    public void setAddIndexDialog(AddIndexDialog addIndexDialog) {
        this.addIndexDialog = addIndexDialog;
    }

    public void setIndexValidator(IndexValidator indexValidator) {
        this.indexValidator = indexValidator;
    }

    public void setIndexModel(IndexModel indexModel) {
        this.indexModel = indexModel;
    }

    public void setDomainManager(DomainManager domainManager) {
        this.domainManager = domainManager;
    }

    public void setIndexListModel(IndexListModel indexListModel) {
        this.indexListModel = indexListModel;
    }

    public void actionPerformed(ActionEvent event) {
        addIndexDialog.view();
        if (!addIndexDialog.isCancelled()) {
            Index index = addIndexDialog.getIndex();
            List <String> errors = new ArrayList <String>();
            if (indexValidator.isValid(errors, index)) {
                index.setRepository(indexModel.getRepository());
                new Worker(index).start();
            }
        }
    }

    private final class Worker extends SwingWorker {
        private Index index;

        public Worker(Index index) {
            this.index = index;
        }

        public Object construct() {
            Throwable throwable = null;
            try {
                domainManager.addIndex(index);
            } catch (Throwable t) {
                System.out.println("[AddIndexAction] error: " + t.getMessage());
                throwable = t;
            }
            return throwable;
        }

        public void finished() {
            Throwable throwable = (Throwable) get();
            if (null != throwable) {
                String message = resourceManager.getString("unique.name.error") + index.getName();
                String title = resourceManager.getString("add.index.action.error.title");
                Validator.showErrorDialog(message, title, addIndexDialog);
            } else {
                indexModel.setIndex(index);
                indexListModel.addIndex(index);
            }
            System.out.println("[AddIndexAction] : " + index);
        }
    }
}