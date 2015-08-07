package com.ndr.app.stock.screener.action;

import com.ndr.app.stock.screener.list.ColumnList;
import com.ndr.app.stock.screener.tree.CriteriaTree;
import com.ndr.model.stock.screener.Name;

import java.awt.event.ActionEvent;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public final class RemoveColumnAction extends ConfigurableAction {
    private static final long serialVersionUID = -2180503549610393744L;
    
	@Autowired private ColumnList<Name> columnList;
    @Autowired private CriteriaTree criteriaTree;

    @PostConstruct
    protected void build() {
        setSmallIcon(resourceManager.getImageIcon("remove.icon"));
        setTooltip(resourceManager.getString("remove"));
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        Name column = columnList.getSelectedEntity();
        columnList.removeSelectedEntity();
        criteriaTree.deselectCriteriaByName(column);
    }
}