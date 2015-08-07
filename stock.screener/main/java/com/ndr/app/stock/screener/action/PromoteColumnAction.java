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
public final class PromoteColumnAction extends ConfigurableAction {
    private static final long serialVersionUID = -2925161575772568480L;
    
	@Autowired private ColumnList<Name> columnList;
    @Autowired private CriteriaTree criteriaTree;
    @Autowired private CriteriaModelPanel criteriaModelPanel;

    @PostConstruct
    protected void build() {
        setSmallIcon(resourceManager.getImageIcon("promote.icon"));
        setTooltip(resourceManager.getString("promote.column.tooltip"));
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        Name column = columnList.getSelectedEntity();
        Criteria criteria = criteriaTree.getCriteriaByName(column);
        criteriaModelPanel.promoteCriteria(criteria);
        columnList.removeSelectedEntity();
    }
}