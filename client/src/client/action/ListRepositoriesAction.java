package client.action;

import client.common.SwingWorker;
import client.model.RepositoryListModel;
import client.resource.ResourceManager;
import client.frame.Frame;
import domain.Repository;
import service.DomainManager;

import java.awt.event.ActionEvent;
import java.util.SortedSet;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;

public final class ListRepositoriesAction extends AbstractAction {
    private ResourceManager resourceManager;
    private DomainManager domainManager;
    private RepositoryListModel repositoryListModel;
    private Frame frame;

    public ListRepositoriesAction() {
        super();
    }

    public void build() {
        putValue(Action.SMALL_ICON, resourceManager.getImageIcon("icon.repository"));
        putValue(Action.NAME, resourceManager.getString("action.list.repositories"));
    }

    public void setResourceManager(ResourceManager resourceManager) {
        this.resourceManager = resourceManager;
    }

    public void setDomainManager(DomainManager domainManager) {
        this.domainManager = domainManager;
    }

    public void setRepositoryListModel(RepositoryListModel repositoryListModel) {
        this.repositoryListModel = repositoryListModel;
    }

    public void setFrame(Frame frame) {
        this.frame = frame;
    }

    public void actionPerformed(ActionEvent event) {
        new Worker().start();
    }

    private final class Worker extends SwingWorker {
        public Worker() {
        }

        public Object construct() {
            return domainManager.listRepositories();
        }

        public void finished() {
            SortedSet <Repository> repositories = (SortedSet <Repository>) get();
            repositoryListModel.setRepositories(repositories);
            if (repositories.size() == 0) {
                JOptionPane.showMessageDialog(frame, resourceManager.getString("list.repositories.empty"));
            }
            System.out.println("[ListRepositoriesAction] : " + repositories.size());
        }
    }
}