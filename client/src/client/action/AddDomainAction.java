package client.action;

import client.common.SwingWorker;
import client.dialog.AddDomainDialog;
import client.model.DomainListModel;
import client.model.DomainModel;
import client.resource.ResourceManager;
import client.validator.DomainValidator;
import client.validator.Validator;
import domain.Domain;
import service.DomainManager;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.net.MalformedURLException;

import javax.swing.AbstractAction;
import javax.swing.Action;

public final class AddDomainAction extends AbstractAction {
    private ResourceManager resourceManager;
    private AddDomainDialog addDomainDialog;
    private DomainValidator domainValidator;
    private DomainModel domainModel;
    private DomainManager domainManager;
    private DomainListModel domainListModel;

    public AddDomainAction() {
        super();
    }

    public void build() {
        putValue(Action.SMALL_ICON, resourceManager.getImageIcon("icon.add.domain"));
        putValue(Action.NAME, resourceManager.getString("action.add.domain"));
    }

    public void setResourceManager(ResourceManager resourceManager) {
        this.resourceManager = resourceManager;
    }

    public void setAddDomainDialog(AddDomainDialog addDomainDialog) {
        this.addDomainDialog = addDomainDialog;
    }

    public void setDomainValidator(DomainValidator domainValidator) {
        this.domainValidator = domainValidator;
    }

    public void setDomainModel(DomainModel domainModel) {
        this.domainModel = domainModel;
    }

    public void setDomainManager(DomainManager domainManager) {
        this.domainManager = domainManager;
    }

    public void setDomainListModel(DomainListModel domainListModel) {
        this.domainListModel = domainListModel;
    }

    public void actionPerformed(ActionEvent event) {
        addDomainDialog.view();
        if (!addDomainDialog.isCancelled()) {
            String url = addDomainDialog.getUrl();
            List <String> errors = new ArrayList <String>();
            if (domainValidator.isValid(errors, url)) {
                try {
                    Domain domain = new Domain(url);
                    domain.setGroup(domainModel.getGroup());
                    new Worker(domain).start();
                } catch (MalformedURLException ignore) {}
            }
        }
    }

    private final class Worker extends SwingWorker {
        private Domain domain;

        public Worker(Domain domain) {
            this.domain = domain;
        }

        public Object construct() {
            Throwable throwable = null;
            try {
                domainManager.addDomain(domain);
            } catch (Throwable t) {
                System.out.println("[AddDomainAction] error: " + t.getMessage());
                throwable = t;
            }
            return throwable;
        }

        public void finished() {
            Throwable throwable = (Throwable) get();
            if (null != throwable) {
                String message = resourceManager.getString("unique.name.error") + domain.getName();
                String title = resourceManager.getString("add.domain.action.error.title");
                Validator.showErrorDialog(message, title, addDomainDialog);
            } else {
                domain.getGroup().getDomains().add(domain);
                domainListModel.addDomain(domain);
                domainModel.setDomain(domain);
            }
            System.out.println("[AddDomainAction] : " + domain);
            System.out.println("[AddDomainAction] group contains domain: " + domain.getGroup().getDomains().contains(domain));
        }
    }
}