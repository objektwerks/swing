package client.dialog;

import client.frame.Frame;
import client.resource.ResourceManager;
import client.table.DomainTable;
import domain.Domain;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public final class LoadDomainsDialog extends JDialog {
    private ResourceManager resourceManager;
    private DomainTable domainTable;
    private Frame frame;
    private boolean isCancelled;

    public LoadDomainsDialog() {
        super((Frame)null, true);
    }

    public void build() {
        setTitle(resourceManager.getString("load.domains.dialog.title"));
        buildModelPanel();
        buildActionPanel();
        pack();
    }

    public void view(List <String> urls) {
        domainTable.set(urls);
        setLocationRelativeTo(frame);
        setVisible(true);
    }

    public boolean isCancelled() {
        return isCancelled;
    }

    public void setCancelled(boolean cancelled) {
        isCancelled = cancelled;
    }

    public List <Domain> getDomains() {
        return domainTable.get();
    }

    public void setResourceManager(ResourceManager resourceManager) {
        this.resourceManager = resourceManager;
    }

    public void setDomainTable(DomainTable domainTable) {
        this.domainTable = domainTable;
    }

    public void setFrame(Frame frame) {
        this.frame = frame;
    }

    private void buildModelPanel() {
        JScrollPane scrollPane = new JScrollPane(domainTable);
        scrollPane.setPreferredSize(new Dimension(500, 500));
        JPanel modelPanel = new JPanel(new BorderLayout());
        modelPanel.setBorder(BorderFactory.createTitledBorder(resourceManager.getString("label.domains")));
        modelPanel.setBorder(BorderFactory.createEtchedBorder());
        modelPanel.add(scrollPane, BorderLayout.CENTER);
        add(modelPanel, BorderLayout.CENTER);
    }

    private void buildActionPanel() {
        JPanel actionPanel = new JPanel();
        actionPanel.setLayout(new BorderLayout());
        JPanel buttonPanel = new JPanel();
        JButton loadButton = new JButton(resourceManager.getString("label.load"),
                                         resourceManager.getImageIcon("icon.load.domains"));
        loadButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                setCancelled(false);
                setVisible(false);
            }
        });
        loadButton.setDefaultCapable(true);
        getRootPane().setDefaultButton(loadButton);
        loadButton.setPreferredSize(new Dimension(100, 25));
        JButton cancelButton = new JButton(resourceManager.getString("label.cancel"));
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                setCancelled(true);
                setVisible(false);
            }
        });
        cancelButton.setPreferredSize(new Dimension(100, 25));
        buttonPanel.add(loadButton);
        buttonPanel.add(cancelButton);
        actionPanel.add(buttonPanel, BorderLayout.EAST);
        add(actionPanel, BorderLayout.SOUTH);
    }
}