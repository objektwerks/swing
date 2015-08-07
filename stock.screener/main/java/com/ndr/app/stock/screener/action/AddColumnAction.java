package com.ndr.app.stock.screener.action;

import com.ndr.app.stock.screener.list.ColumnList;
import com.ndr.app.stock.screener.tree.CriteriaTree;
import com.ndr.model.stock.screener.Criteria;
import com.ndr.model.stock.screener.Name;

import java.awt.event.ActionEvent;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public final class AddColumnAction extends ConfigurableAction {
    private static final long serialVersionUID = -2458191657547185428L;

	@Autowired private CriteriaTree criteriaTree;
    @Autowired private ColumnList<Name> columnList;

    @PostConstruct
    protected void build() {
        setSmallIcon(resourceManager.getImageIcon("add.column.icon"));
        setTooltip(resourceManager.getString("add.column.tooltip"));
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        Criteria criteria = criteriaTree.getSelectedCriteria();
        columnList.addEntity(criteria.getName());
        criteriaTree.deselectCriteria(criteria);
    }
}