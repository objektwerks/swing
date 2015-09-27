package client.action;

import client.common.SwingWorker;
import client.model.MonitorListModel;
import client.resource.ResourceManager;
import client.frame.Frame;
import domain.Domain;
import service.DomainManager;

import java.awt.event.ActionEvent;
import java.util.SortedSet;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;

public final class ListCrawlsInProgressAction extends AbstractAction {
    private ResourceManager resourceManager;
    private DomainManager domainManager;
    private MonitorListModel monitorListModel;
    private Frame frame;

    public ListCrawlsInProgressAction() {
        super();
    }

    public void build() {
        putValue(Action.SMALL_ICON, resourceManager.getImageIcon("icon.monitor"));
        putValue(Action.NAME, resourceManager.getString("action.list.crawls.in.progress"));
    }

    public void actionPerformed(ActionEvent event) {
        new Worker().start();
    }

    public void setResourceManager(ResourceManager resourceManager) {
        this.resourceManager = resourceManager;
    }

    public void setDomainManager(DomainManager domainManager) {
        this.domainManager = domainManager;
    }

    public void setMonitorListModel(MonitorListModel monitorListModel) {
        this.monitorListModel = monitorListModel;
    }

    public void setFrame(Frame frame) {
        this.frame = frame;
    }

    private final class Worker extends SwingWorker {
        public Worker() {
        }

        public Object construct() {
            return domainManager.listCrawlsInProgress();
        }

        public void finished() {
            SortedSet <Domain> crawlsInProgress = (SortedSet <Domain>) get();
            monitorListModel.setCrawlsInProgress(crawlsInProgress);
            if (crawlsInProgress.size() == 0) {
                JOptionPane.showMessageDialog(frame, resourceManager.getString("list.crawls.in.progress.empty"));
            }
            System.out.println("[ListCrawlsInProgressAction] : " + crawlsInProgress.size());
        }
    }
}