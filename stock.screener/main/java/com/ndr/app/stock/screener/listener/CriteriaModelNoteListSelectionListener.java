package com.ndr.app.stock.screener.listener;

import com.ndr.app.stock.screener.list.CriteriaModelList;
import com.ndr.app.stock.screener.statusbar.StatusBar;
import com.ndr.app.stock.screener.text.NoteTextPane;
import com.ndr.model.stock.screener.CriteriaModel;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class CriteriaModelNoteListSelectionListener implements ListSelectionListener {
    private CriteriaModelList<CriteriaModel> criteriaModelList;
    private NoteTextPane noteTextPane;
    private StatusBar statusBar;

    public CriteriaModelNoteListSelectionListener(CriteriaModelList<CriteriaModel> criteriaModelList,
                                                  NoteTextPane noteTextPane) {
        this.criteriaModelList = criteriaModelList;
        this.noteTextPane = noteTextPane;
    }

    public CriteriaModelNoteListSelectionListener(CriteriaModelList<CriteriaModel> criteriaModelList,
                                                  NoteTextPane noteTextPane,
                                                  StatusBar statusBar) {
        this.criteriaModelList = criteriaModelList;
        this.noteTextPane = noteTextPane;
        this.statusBar = statusBar;
    }

    @Override
    public void valueChanged(ListSelectionEvent event) {
        if (!event.getValueIsAdjusting()) {
            if (criteriaModelList.isNotEmpty()) {
                CriteriaModel selectedCriteriaModel = criteriaModelList.getSelectedEntity();
                if (selectedCriteriaModel != null) {
                    noteTextPane.setText(selectedCriteriaModel.getNote());
                    if (statusBar != null) {
                        statusBar.setCriteriaModelStatus(selectedCriteriaModel.getName());
                    }
                }
            }
        }
    }
}