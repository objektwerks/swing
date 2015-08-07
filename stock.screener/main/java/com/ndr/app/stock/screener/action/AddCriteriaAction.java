package com.ndr.app.stock.screener.action;

import com.ndr.app.stock.screener.panel.CriteriaModelPanel;
import com.ndr.app.stock.screener.tree.CriteriaTree;
import com.ndr.model.stock.screener.Criteria;

import java.awt.event.ActionEvent;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public final class AddCriteriaAction extends ConfigurableAction {
    private static final long serialVersionUID = -6418139734885085596L;

	@Autowired private CriteriaModelPanel criteriaModelPanel;
    @Autowired private CriteriaTree criteriaTree;

    @PostConstruct
    protected void build() {
        setSmallIcon(resourceManager.getImageIcon("add.criteria.icon"));
        setTooltip(resourceManager.getString("add.criteria.tooltip"));
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        Criteria criteria = criteriaTree.getSelectedCriteria();
        criteriaModelPanel.addCriteria(criteria);
        criteriaTree.deselectCriteria(criteria);
    }
}