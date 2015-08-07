package com.ndr.app.stock.screener.panel;

import com.ndr.app.stock.screener.button.PromoteColumnButton;
import com.ndr.app.stock.screener.button.RemoveColumnButton;
import com.ndr.app.stock.screener.list.ColumnList;
import com.ndr.app.stock.screener.resource.ResourceManager;
import com.ndr.app.stock.screener.search.SearchBar;
import com.ndr.model.stock.screener.Name;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public final class ColumnPanel extends JPanel {
    private static final long serialVersionUID = 6864254134313541467L;

	@Autowired private ResourceManager resourceManager;
    @Autowired private RemoveColumnButton removeColumnButton;
    @Autowired private PromoteColumnButton promoteColumnButton;
    @Autowired private ColumnList<Name> columnList;
    @Autowired private SearchBar searchBar;

    public void setModel(List<Name> columns) {
        columnList.setEntities(columns);
    }

    @PostConstruct
    protected void build() {
        setLayout(new BorderLayout());
        searchBar.setSearchable(columnList);
        add(buildPanel(), BorderLayout.CENTER);
        columnList.addListSelectionListener(new ColumnListSelectionListener());
    }

    private JPanel buildPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel(resourceManager.getString("selected.columns")), BorderLayout.NORTH);
        panel.add(buildScrollPane(), BorderLayout.CENTER);
        return panel;
    }

    private JScrollPane buildScrollPane() {
        JScrollPane scrollPane = new JScrollPane(columnList);
        JViewport viewport = new JViewport();
        viewport.setView(buildButtonPanel());
        scrollPane.setColumnHeader(viewport);
        return scrollPane;
    }

    private JPanel buildButtonPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.add(removeColumnButton);
        panel.add(Box.createRigidArea(new Dimension(2,0)));
        panel.add(promoteColumnButton);
        panel.add(Box.createRigidArea(new Dimension(2,0)));
        panel.add(searchBar);
        return panel;
    }

    private class ColumnListSelectionListener implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent event) {
            if (columnList.getSelectedIndex() > -1) {
                removeColumnButton.setEnabled(true);
                promoteColumnButton.setEnabled(true);
            } else {
                removeColumnButton.setEnabled(false);
                promoteColumnButton.setEnabled(false);
            }
        }
    }
}