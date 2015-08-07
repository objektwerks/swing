package com.ndr.app.stock.screener.tab;

import com.ndr.app.stock.screener.resource.ResourceManager;

import javax.annotation.PostConstruct;
import javax.swing.JTabbedPane;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public final class ModelTabbedPane extends JTabbedPane {
    private static final long serialVersionUID = -2777859641323758945L;
    
	@Autowired private ResourceManager resourceManager;
    @Autowired private CriteriaModelTab criteriaModelTab;
    @Autowired private IndexModelTab indexModelTab;
    
    @PostConstruct
    protected void build() {
        addTab(resourceManager.getString("criteria.model.tab.title"), criteriaModelTab);
        addTab(resourceManager.getString("index.model.tab.title"), indexModelTab);
    }
}