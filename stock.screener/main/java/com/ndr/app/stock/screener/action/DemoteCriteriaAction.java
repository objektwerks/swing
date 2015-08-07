package com.ndr.app.stock.screener.action;

import com.ndr.app.stock.screener.list.ColumnList;
import com.ndr.app.stock.screener.panel.CriteriaModelPanel;
import com.ndr.app.stock.screener.tree.CriteriaTree;
import com.ndr.model.stock.screener.Criteria;
import com.ndr.model.stock.screener.Name;

import java.awt.event.ActionEvent;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public final class DemoteCriteriaAction extends ConfigurableAction {
    private static final long serialVersionUID = 529983356530007440L;

	@Autowired private CriteriaModelPanel criteriaModelPanel;
    @Autowired private ColumnList<Name> columnList;
    @Autowired private CriteriaTree criteriaTree;
 
    @PostConstruct
    protected void build() {
        setSmallIcon(resourceManager.getImageIcon("demote.icon"));
        setTooltip(resourceManager.getString("demote.criteria.tooltip"));
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        Criteria criteria = criteriaModelPanel.removeSelectedCriteria();
        columnList.addEntity(criteria.getName());
        criteriaTree.deselectCriteria(criteria);
    }
}