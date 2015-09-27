package client.panel;

import client.list.IndexDomainList;
import client.resource.ResourceManager;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JViewport;

public final class IndexDomainPanel extends JPanel {
    private ResourceManager resourceManager;
    private IndexDomainList indexDomainList;
    private Action searchIndexDomainListModelAction;

    public IndexDomainPanel() {
        super();
    }

    public void build() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createTitledBorder(resourceManager.getString("label.domains")));
        setPreferredSize(new Dimension(400, 300));
        buildIndexDomainList();
    }

    public void setResourceManager(ResourceManager resourceManager) {
        this.resourceManager = resourceManager;
    }

    public void setIndexDomainList(IndexDomainList indexDomainList) {
        this.indexDomainList = indexDomainList;
    }

    public void setSearchIndexDomainListModelAction(Action searchIndexDomainListModelAction) {
        this.searchIndexDomainListModelAction = searchIndexDomainListModelAction;
    }

    private void buildIndexDomainList() {
        indexDomainList.setBackground(getBackground());
        JScrollPane scrollPane = new JScrollPane(indexDomainList);
        JViewport viewport = new JViewport();
        JTextField searchTextField = new JTextField(32);
        searchTextField.setAction(searchIndexDomainListModelAction);
        searchTextField.setToolTipText(resourceManager.getString("tool.tip.search"));
        searchTextField.setBorder(BorderFactory.createEtchedBorder());
        viewport.setView(searchTextField);
        scrollPane.setColumnHeader(viewport);
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEtchedBorder());
        panel.add(scrollPane, BorderLayout.CENTER);
        add(panel);
    }
}