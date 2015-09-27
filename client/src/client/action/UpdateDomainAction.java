package client.action;

import client.common.SwingWorker;
import client.model.DomainModel;
import client.resource.ResourceManager;
import client.validator.Validator;
import domain.Domain;
import service.DomainManager;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

public final class UpdateDomainAction extends AbstractAction {
    private ResourceManager resourceManager;
    private DomainModel domainModel;
    private DomainManager domainManager;

    public UpdateDomainAction() {
        super();
    }

    public void build() {
        putValue(Action.SMALL_ICON, resourceManager.getImageIcon("icon.update.domain"));
        putValue(Action.NAME, resourceManager.getString("action.update.domain"));
    }

    public void setResourceManager(ResourceManager resourceManager) {
        this.resourceManager = resourceManager;
    }

    public void setDomainModel(DomainModel domainModel) {
        this.domainModel = domainModel;
    }

    public void setDomainManager(DomainManager domainManager) {
        this.domainManager = domainManager;
    }

    public void actionPerformed(ActionEvent event) {
        Domain domain = domainModel.getDomain();
        Worker worker = new Worker(domain);
        worker.start();
    }

    private final class Worker extends SwingWorker {
        private Domain domain;

        public Worker(Domain domain) {
            this.domain = domain;
        }

        public Object construct() {
            Throwable throwable = null;
            try {
                domainManager.updateDomain(domain);
            } catch (Throwable t) {
                System.out.println("[UpdateDomainAction] error: " + t.getMessage());
                throwable = t;
            }
            return throwable;
        }

        public void finished() {
            Throwable throwable = (Throwable) get();
            if (null != throwable) {
                String message = resourceManager.getString("update.domain.action.error") + throwable.getMessage();
                String title = resourceManager.getString("update.domain.action.error.title");
                Validator.showErrorDialog(message, title, (Component) domainModel);
            } else {
                domainModel.setDomain(domain);
            }
            System.out.println("[UpdateDomainAction] : " + domain);
        }
    }
}