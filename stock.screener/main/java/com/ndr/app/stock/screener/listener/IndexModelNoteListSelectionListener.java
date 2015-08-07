package com.ndr.app.stock.screener.listener;

import com.ndr.app.stock.screener.list.IndexModelList;
import com.ndr.app.stock.screener.statusbar.StatusBar;
import com.ndr.app.stock.screener.text.NoteTextPane;
import com.ndr.model.stock.screener.IndexModel;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class IndexModelNoteListSelectionListener implements ListSelectionListener {
    private IndexModelList<IndexModel> indexModelList;
    private NoteTextPane noteTextPane;
    private StatusBar statusBar;

    public IndexModelNoteListSelectionListener(IndexModelList<IndexModel> indexModelList,
                                               NoteTextPane noteTextPane) {
        this.indexModelList = indexModelList;
        this.noteTextPane = noteTextPane;
    }

    public IndexModelNoteListSelectionListener(IndexModelList<IndexModel> indexModelList,
                                               NoteTextPane noteTextPane,
                                               StatusBar statusBar) {
        this.indexModelList = indexModelList;
        this.noteTextPane = noteTextPane;
        this.statusBar = statusBar;
    }

    @Override
    public void valueChanged(ListSelectionEvent event) {
        if (!event.getValueIsAdjusting()) {
            if (indexModelList.isNotEmpty()) {
                IndexModel selectedIndexModel = indexModelList.getSelectedEntity();
                if (selectedIndexModel != null) {
                    noteTextPane.setText(selectedIndexModel.getNote());
                    if (statusBar != null) {
                        statusBar.setIndexModelStatus(selectedIndexModel.getName());
                    }
                }
            }
        }
    }
}