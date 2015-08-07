package com.ndr.app.stock.screener.tab;

import com.ndr.app.stock.screener.panel.ColumnPanel;
import com.ndr.app.stock.screener.panel.CriteriaModelPanel;
import com.ndr.app.stock.screener.panel.CriteriaPanel;

import java.awt.BorderLayout;

import javax.annotation.PostConstruct;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public final class CriteriaModelTab extends JPanel {
    private static final long serialVersionUID = 2494117328695063837L;
    
	@Autowired private CriteriaModelPanel criteriaModelPanel;
    @Autowired private ColumnPanel columnPanel;
    @Autowired private CriteriaPanel criteriaPanel;

    @PostConstruct
    protected void build() {
        setLayout(new BorderLayout());
        JSplitPane northSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, criteriaModelPanel, columnPanel);
        northSplitPane.setDividerLocation(300);
        JSplitPane southSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, northSplitPane, criteriaPanel);
        southSplitPane.setDividerLocation(450);
        add(southSplitPane, BorderLayout.CENTER);
    }
}