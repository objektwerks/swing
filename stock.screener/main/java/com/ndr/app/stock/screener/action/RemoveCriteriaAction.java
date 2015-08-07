package com.ndr.app.stock.screener.action;

import com.ndr.app.stock.screener.panel.CriteriaModelPanel;
import com.ndr.app.stock.screener.tree.CriteriaTree;
import com.ndr.model.stock.screener.Criteria;

import java.awt.event.ActionEvent;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public final class RemoveCriteriaAction extends ConfigurableAction {
    private static final long serialVersionUID = -2148285945293371679L;
    
	@Autowired private CriteriaModelPanel criteriaModelPanel;
    @Autowired private CriteriaTree criteriaTree;

    @PostConstruct
    protected void build() {
        setSmallIcon(resourceManager.getImageIcon("remove.icon"));
        setTooltip(resourceManager.getString("remove"));
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        Criteria criteria = criteriaModelPanel.removeSelectedCriteria();
        criteriaTree.deselectCriteria(criteria);
    }
}