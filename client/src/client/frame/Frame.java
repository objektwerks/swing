package client.frame;

import client.panel.EventPanel;
import client.panel.GroupPanel;
import client.panel.IndexPanel;
import client.panel.MonitorPanel;
import client.panel.RepositoryPanel;
import client.resource.ResourceManager;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;

public final class Frame extends JFrame {
    private ResourceManager resourceManager;
    private MenuBar menuBar;
    private RepositoryPanel repositoryPanel;
    private GroupPanel groupPanel;
    private IndexPanel indexPanel;
    private EventPanel eventPanel;
    private MonitorPanel monitorPanel;

    public Frame() {
        super();
    }

    public void build() {
        setTitle(resourceManager.getString("frame.title"));
        setIconImage(new ImageIcon(getClass().getResource(resourceManager.getString("icon.app"))).getImage());
        setLayout(new BorderLayout());
        setSize(resourceManager.getInt("frame.width"), resourceManager.getInt("frame.height"));
        this.getRootPane().setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setJMenuBar(menuBar);
        JTabbedPane eastTabbedPane = new JTabbedPane();
        eastTabbedPane.addTab(resourceManager.getString("label.groups"),
                              resourceManager.getImageIcon("icon.group"),
                              groupPanel);
        eastTabbedPane.addTab(resourceManager.getString("label.indexes"),
                              resourceManager.getImageIcon("icon.index"),
                              indexPanel);
        eastTabbedPane.addTab(resourceManager.getString("label.events"),
                              resourceManager.getImageIcon("icon.event"),
                              eventPanel);
        eastTabbedPane.addTab(resourceManager.getString("label.monitor"),
                              resourceManager.getImageIcon("icon.monitor"),
                              monitorPanel);
        JTabbedPane westTabbedPane = new JTabbedPane();
        westTabbedPane.addTab(resourceManager.getString("label.repositories"),
                              resourceManager.getImageIcon("icon.repository"),
                              repositoryPanel);
        JSplitPane horizontalSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, westTabbedPane, eastTabbedPane);
        horizontalSplitPane.setOneTouchExpandable(true);
        horizontalSplitPane.setDividerLocation(195);
        add(horizontalSplitPane, BorderLayout.CENTER);
    }

    public void setResourceManager(ResourceManager resourceManager) {
        this.resourceManager = resourceManager;
    }

    public void setMenu(MenuBar menuBar) {
        this.menuBar = menuBar;
    }

    public void setRepositoryPanel(RepositoryPanel repositoryPanel) {
        this.repositoryPanel = repositoryPanel;
    }

    public void setGroupPanel(GroupPanel groupPanel) {
        this.groupPanel = groupPanel;
    }

    public void setIndexPanel(IndexPanel indexPanel) {
        this.indexPanel = indexPanel;
    }

    public void setEventPanel(EventPanel eventPanel) {
        this.eventPanel = eventPanel;
    }

    public void setMonitorPanel(MonitorPanel monitorPanel) {
        this.monitorPanel = monitorPanel;
    }
}