package client.poller;

import client.model.DomainListModel;
import client.model.RepositoryListModel;
import domain.Domain;
import domain.Group;
import domain.Repository;
import service.DomainManager;

import java.awt.EventQueue;
import java.util.SortedSet;
import java.util.TimerTask;

public final class DomainPoller extends TimerTask {
    private DomainManager domainManager;
    private RepositoryListModel repositoryListModel;
    private DomainListModel domainListModel;

    public DomainPoller() {
    }

    public void setDomainManager(DomainManager domainManager) {
        this.domainManager = domainManager;
    }

    public void setRepositoryListModel(RepositoryListModel repositoryListModel) {
        this.repositoryListModel = repositoryListModel;
    }

    public void setDomainListModel(DomainListModel domainListModel) {
        this.domainListModel = domainListModel;
    }

    public void run() {
        SortedSet <Repository> repositories = repositoryListModel.getRepositories();
        SortedSet <Group> groups;
        SortedSet <Domain> domains;
        for (Repository repository : repositories) {
            groups = repository.getGroups();
            for (Group group : groups) {
                domains = group.getDomains();
                for (final Domain domain : domains) {
                    if (!domain.isCrawled()) {
                        try {
                            final Domain polledDomain = domainManager.findDomain(domain.getId());
                            if (polledDomain.isCrawlInProgress() || polledDomain.isCrawled()) {
                                EventQueue.invokeLater(new Runnable() {
                                    public void run() {
                                        domainListModel.removeDomain(polledDomain);
                                        domainListModel.addDomain(polledDomain);
                                        System.out.println("[DomainPoller] updated domain: " + polledDomain.getName());
                                    }
                                });
                            }
                        } catch (Throwable t) {
                            System.out.println("[DomainPoller] finder exception: " + t.getMessage());
                        }
                    }
                }
            }
        }
    }
}