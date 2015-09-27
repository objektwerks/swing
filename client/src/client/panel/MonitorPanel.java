package client.panel;

import client.model.MonitorListModel;
import client.resource.ResourceManager;
import client.table.CrawlsInProgressTable;
import domain.Domain;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public final class MonitorPanel extends JPanel implements MonitorListModel {
    private ResourceManager resourceManager;
    private CrawlsInProgressTable crawlsInProgressTable;
    private Action listCrawlsInProgressAction;
    private SortedSet <Domain> crawlsInProgress;

    public MonitorPanel() {
        super();
    }

    public void build() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder(resourceManager.getString("label.monitor")));
        buildModelPanel();
        buildActionPanel();
    }

    public SortedSet <Domain> getCrawlsInProgress() {
        if (null == crawlsInProgress) {
            crawlsInProgress = new TreeSet <Domain> ();
        }
        return crawlsInProgress;
    }

    public void setCrawlsInProgress(SortedSet <Domain> crawlsInProgress) {
        this.crawlsInProgress = crawlsInProgress;
        crawlsInProgressTable.set(crawlsInProgress);
    }

    public void setResourceManager(ResourceManager resourceManager) {
        this.resourceManager = resourceManager;
    }

    public void setCrawlsInProgressTable(CrawlsInProgressTable crawlsInProgressTable) {
        this.crawlsInProgressTable = crawlsInProgressTable;
    }

    public void setListCrawlsInProgressAction(Action listCrawlsInProgressAction) {
        this.listCrawlsInProgressAction = listCrawlsInProgressAction;
    }

    private void buildModelPanel() {
        JScrollPane scrollPane = new JScrollPane(crawlsInProgressTable);
        JPanel modelPanel = new JPanel(new BorderLayout());
        modelPanel.setBorder(BorderFactory.createEtchedBorder());
        modelPanel.add(scrollPane, BorderLayout.CENTER);
        add(modelPanel, BorderLayout.CENTER);
    }

    private void buildActionPanel() {
        JPanel actionPanel = new JPanel();
        actionPanel.setLayout(new BorderLayout());
        JButton listButton = new JButton(listCrawlsInProgressAction);
        listButton.setPreferredSize(new Dimension(165, 25));
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(listButton);
        actionPanel.add(buttonPanel, BorderLayout.WEST);
        add(actionPanel, BorderLayout.SOUTH);
    }
}