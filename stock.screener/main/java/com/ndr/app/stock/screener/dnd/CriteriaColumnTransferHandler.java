package com.ndr.app.stock.screener.dnd;

import com.ndr.app.stock.screener.action.AddColumnAction;
import com.ndr.app.stock.screener.action.AddCriteriaAction;
import com.ndr.app.stock.screener.action.DemoteCriteriaAction;
import com.ndr.app.stock.screener.action.PromoteColumnAction;
import com.ndr.app.stock.screener.action.RemoveColumnAction;
import com.ndr.app.stock.screener.action.RemoveCriteriaAction;
import com.ndr.app.stock.screener.list.ColumnList;
import com.ndr.app.stock.screener.table.CriteriaComponentTable;
import com.ndr.app.stock.screener.tree.CriteriaTree;

import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;

import javax.swing.JComponent;
import javax.swing.TransferHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public final class CriteriaColumnTransferHandler extends TransferHandler {
    private static final long serialVersionUID = -7891055880344621230L;
	private static final String criteriaTreeClassName = CriteriaTree.class.getName();
    private static final String columnListClassName = ColumnList.class.getName();
    private static final String criteriaComponentTableClassName = CriteriaComponentTable.class.getName();
    
    @Autowired private CriteriaTree criteriaTree;
    @Autowired private ColumnList<?> columnList;
    @Autowired private CriteriaComponentTable criteriaComponentTable;
    @Autowired private AddColumnAction addColumnAction;
    @Autowired private AddCriteriaAction addCriteriaAction;
    @Autowired private PromoteColumnAction promoteColumnAction;
    @Autowired private DemoteCriteriaAction demoteCriteriaAction;
    @Autowired private RemoveColumnAction removeColumnAction;
    @Autowired private RemoveCriteriaAction removeCriteriaAction;

    @Override
    public int getSourceActions(JComponent exporter) {
        return MOVE;
    }

    @Override
    protected Transferable createTransferable(JComponent exporter) {
        Transferable transferable = null;
        if (exporter instanceof CriteriaTree) {
            transferable = new CriteriaColumnTransferable(criteriaTreeClassName);
        } else if (exporter instanceof ColumnList) {
            transferable = new CriteriaColumnTransferable(columnListClassName);
        } else if (exporter instanceof CriteriaComponentTable) {
            transferable = new CriteriaColumnTransferable(criteriaComponentTableClassName);
        }
        return transferable;
    }

    @Override
    public boolean canImport(TransferSupport transferSupport) {
        transferSupport.setShowDropLocation(true);
        return true;
    }

    @Override
    public boolean importData(TransferSupport transferSupport) {
        boolean importedData = true;
        try {
            CriteriaColumnTransferable criteriaColumnTransferable = (CriteriaColumnTransferable) transferSupport.getTransferable().getTransferData(CriteriaColumnTransferable.criteriaColumnDataFlavor);
            String exporter = criteriaColumnTransferable.getExporter();
            java.awt.Component importer = transferSupport.getComponent();
            if (importer instanceof CriteriaTree) {
                if (exporter.equals(columnListClassName)) {
                    removeColumnAction.actionPerformed(new ActionEvent(columnList, 1, "removeColumnAction"));
                } else if (exporter.equals(criteriaComponentTableClassName)) {
                    removeCriteriaAction.actionPerformed(new ActionEvent(criteriaComponentTable, 2, "removeCriteriaAction"));
                }
            } else if (importer instanceof ColumnList) {
                if (exporter.equals(criteriaTreeClassName)) {
                    addColumnAction.actionPerformed(new ActionEvent(criteriaTree, 0, "addColumnAction"));
                } else if (exporter.equals(criteriaComponentTableClassName)) {
                    demoteCriteriaAction.actionPerformed(new ActionEvent(criteriaComponentTable, 2, "demoteCriteriaAction"));
                }
            } else if (importer instanceof CriteriaComponentTable) {
                if (exporter.equals(columnListClassName)) {
                    promoteColumnAction.actionPerformed(new ActionEvent(columnList, 1, "promoteColumnAction"));
                } else if (exporter.equals(criteriaTreeClassName)) {
                    addCriteriaAction.actionPerformed(new ActionEvent(criteriaTree, 0, "addCriteriaAction"));
                }
            }
        } catch (Exception ignore) {
            importedData = false;
        }
        return importedData;
    }
}