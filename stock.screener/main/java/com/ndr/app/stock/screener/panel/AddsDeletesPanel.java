package com.ndr.app.stock.screener.panel;

import com.ndr.app.stock.screener.resource.ResourceManager;
import com.ndr.app.stock.screener.table.TickerCompanyTable;

import java.awt.BorderLayout;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public final class AddsDeletesPanel extends JPanel {
    private static final long serialVersionUID = -1235003578162336149L;

	@Autowired private ResourceManager resourceManager;
    @Autowired private TickerCompanyTable addsTable;
    @Autowired private TickerCompanyTable deletesTable;

    public void setModel(Set<List<String>> addsTickersAndCompanies, Set<List<String>> deletesTickersAndCompanies) {
        removeAll();
        rebuild(addsTickersAndCompanies.size(), deletesTickersAndCompanies.size());
        addsTable.setModel(addsTickersAndCompanies);
        deletesTable.setModel(deletesTickersAndCompanies);
        validate();
        repaint();
    }

    public void reset() {
        removeAll();
        validate();
        repaint();
    }

    public TickerCompanyTable getAddsTable() {
        return addsTable;
    }

    public TickerCompanyTable getDeletesTable() {
        return deletesTable;
    }

    @PostConstruct
    protected void build() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    }

    private void rebuild(int numberOfAdds, int numberOfDeletes) {
        add(buildPanel(numberOfAdds, numberOfDeletes));
    }

    private JPanel buildPanel(int numberOfAdds, int numberOfDeletes) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(buildAddsPane(numberOfAdds));
        panel.add(buildDeletesPane(numberOfDeletes));
        return panel;
    }

    private JPanel buildAddsPane(int numberOfAdds) {
        JScrollPane scrollPane = new JScrollPane(addsTable);
        return buildPanel(scrollPane, " " + resourceManager.getString("adds") + ": " + numberOfAdds);
    }

    private JPanel buildDeletesPane(int numberOfDeletes) {
        JScrollPane scrollPane = new JScrollPane(deletesTable);
        return buildPanel(scrollPane, " " + resourceManager.getString("deletes") + ": " + numberOfDeletes);
    }

    private JPanel buildPanel(JScrollPane scrollPane, String title) {
        JLabel label = new JLabel(title);
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(label, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }
}