package com.ndr.app.stock.screener.panel;

import com.ndr.app.stock.screener.button.AddColumnButton;
import com.ndr.app.stock.screener.button.AddCriteriaButton;
import com.ndr.app.stock.screener.button.TreeExpandCollapseToggleButton;
import com.ndr.app.stock.screener.resource.ResourceManager;
import com.ndr.app.stock.screener.search.SearchBar;
import com.ndr.app.stock.screener.text.CriteriaTextPane;
import com.ndr.app.stock.screener.tree.CriteriaTree;
import com.ndr.model.stock.screener.Criteria;

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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public final class CriteriaPanel extends JPanel {
    private static final long serialVersionUID = -2484856811506912438L;

	@Autowired private ResourceManager resourceManager;
    @Autowired private CriteriaTree criteriaTree;
    @Autowired private CriteriaTextPane criteriaTextPane;
    @Autowired private AddCriteriaButton addCriteriaButton;
    @Autowired private AddColumnButton addColumnButton;
    @Autowired private TreeExpandCollapseToggleButton treeExpandCollapseToggleButton;
    @Autowired private SearchBar searchBar;

    public void setModel(List<Criteria> prototypeCriteria, List<Criteria> selectedCriteria) {
        criteriaTree.setModel(prototypeCriteria, selectedCriteria);
        treeExpandCollapseToggleButton.setSelected(false);
    }

    @PostConstruct
    protected void build() {
        setLayout(new BorderLayout());
        treeExpandCollapseToggleButton.setTree(criteriaTree);
        searchBar.setSearchable(criteriaTree);
        add(buildPanel(), BorderLayout.CENTER);
    }

    private JPanel buildPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel(resourceManager.getString("criteria")), BorderLayout.NORTH);
        panel.add(buildScrollPane(), BorderLayout.CENTER);
        panel.add(buildTextPane(), BorderLayout.SOUTH);
        return panel;
    }

    private JScrollPane buildScrollPane() {
        JScrollPane scrollPane = new JScrollPane(criteriaTree);
        JViewport viewport = new JViewport();
        viewport.setView(buildButtonPanel());
        scrollPane.setColumnHeader(viewport);
        return scrollPane;
    }

    private JPanel buildButtonPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.add(treeExpandCollapseToggleButton);
        panel.add(Box.createRigidArea(new Dimension(2,0)));
        panel.add(addColumnButton);
        panel.add(Box.createRigidArea(new Dimension(2,0)));
        panel.add(addCriteriaButton);
        panel.add(Box.createRigidArea(new Dimension(2,0)));
        panel.add(searchBar);
        return panel;
    }

    private JScrollPane buildTextPane() {
        return new JScrollPane(criteriaTextPane);
    }
}