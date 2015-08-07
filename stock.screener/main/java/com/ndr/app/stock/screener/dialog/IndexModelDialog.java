package com.ndr.app.stock.screener.dialog;

import com.ndr.app.stock.screener.ApplicationProxy;
import com.ndr.app.stock.screener.action.RemoveIndexModelAction;
import com.ndr.app.stock.screener.action.SaveIndexModelAction;
import com.ndr.app.stock.screener.border.CompoundBorderFactory;
import com.ndr.app.stock.screener.button.AddButton;
import com.ndr.app.stock.screener.button.CloseButton;
import com.ndr.app.stock.screener.button.EditButton;
import com.ndr.app.stock.screener.button.OpenButton;
import com.ndr.app.stock.screener.button.RemoveButton;
import com.ndr.app.stock.screener.list.IndexModelList;
import com.ndr.app.stock.screener.listener.IndexModelNoteListSelectionListener;
import com.ndr.app.stock.screener.panel.IndexModelPanel;
import com.ndr.app.stock.screener.panel.TimeSeriesChartPanel;
import com.ndr.app.stock.screener.panel.TimeSeriesTablePanel;
import com.ndr.app.stock.screener.resource.ResourceManager;
import com.ndr.app.stock.screener.statusbar.TimeSeriesStatusBar;
import com.ndr.app.stock.screener.text.NoteTextPane;
import com.ndr.model.stock.screener.IndexModel;
import com.ndr.model.stock.screener.TestEntityFactory;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public final class IndexModelDialog extends JDialog {
    private static final long serialVersionUID = -2796234903306249105L;
    
	@Autowired private Frame frame;
    @Autowired private ResourceManager resourceManager;
    @Autowired private AddButton addButton;
    @Autowired private RemoveButton removeButton;
    @Autowired private EditButton editButton;
    @Autowired private OpenButton openButton;
    @Autowired private CloseButton closeButton;
    @Autowired private TimeSeriesChartPanel timeSeriesChartPanel;
    @Autowired private TimeSeriesTablePanel timeSeriesTablePanel;
    @Autowired private IndexModelPanel indexModelPanel;
    @Autowired private IndexModelList<IndexModel> indexModelList;
    @Autowired private NoteTextPane indexModelNoteTextPane;
    @Autowired private SaveIndexModelAction saveIndexModelAction;
    @Autowired private RemoveIndexModelAction removeIndexModelAction;
    @Autowired private CriteriaIndexModelDialog indexModelDialog;
    @Autowired private TimeSeriesStatusBar timeSeriesStatusBar;

    public void view() {
        indexModelList.setSelectedEntity(indexModelPanel.getModel());
        setLocationRelativeTo(frame);
        setVisible(true);
    }

    public List<IndexModel> getIndexModels() {
        return indexModelList.getEntities();
    }

    public void setIndexModels(List<IndexModel> indexModels) {
        indexModelList.setEntities(indexModels);
    }

    public IndexModel getSelectedIndexModel() {
        return indexModelList.getSelectedEntity();
    }

    @PostConstruct
    protected void build() {
        indexModelList.addListSelectionListener(new IndexModelNoteListSelectionListener(indexModelList, indexModelNoteTextPane));
        addWindowListener(new HideDialogWindowAdapter(this));
        setIconImage(resourceManager.getImage("index.model.icon"));
        setTitle(resourceManager.getString("index.model.dialog.title"));
        setLayout(new BorderLayout());
        setModal(true);
        add(buildPanel(), BorderLayout.CENTER);
        add(buildScrollableTextPane(), BorderLayout.SOUTH);
        pack();
    }

    private JPanel buildPanel() {
        JPanel panel = new JPanel();
        CompoundBorderFactory.instance.create(panel, resourceManager.getString("index.models"));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(buildScrollPane());
        return panel;
    }

    private JScrollPane buildScrollPane() {
        JScrollPane scrollPane = new JScrollPane(indexModelList);
        scrollPane.setPreferredSize(new Dimension(275, 200));
        JViewport viewport = new JViewport();
        viewport.setView(buildButtonPanel());
        scrollPane.setColumnHeader(viewport);
        return scrollPane;
    }

    private JPanel buildButtonPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.add(buildAddButton());
        panel.add(Box.createRigidArea(new Dimension(2, 0)));
        panel.add(buildRemoveButton());
        panel.add(Box.createRigidArea(new Dimension(2, 0)));
        panel.add(buildEditButton());
        panel.add(Box.createRigidArea(new Dimension(2, 0)));
        panel.add(buildOpenButton());
        panel.add(Box.createHorizontalGlue());
        panel.add(buildCloseButton());
        return panel;
    }

    private JScrollPane buildScrollableTextPane() {
        JScrollPane scrollPane = new JScrollPane(indexModelNoteTextPane);
        CompoundBorderFactory.instance.create(scrollPane, resourceManager.getString("note"));
        return scrollPane;
    }

    private JButton buildAddButton() {
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                indexModelDialog.setTitle(resourceManager.getString("add"));
                indexModelDialog.view((JButton) event.getSource(), "", "");
                if (indexModelDialog.wasNotCanceled()) {
                    IndexModel indexModel = TestEntityFactory.instance.newIndexModelTestInstance(indexModelDialog.getModelName(), ApplicationProxy.instance.getUser());
                    indexModel.setNote(indexModelDialog.getModelNote());
                    indexModelList.addEntity(indexModel);
                    saveIndexModelAction.save(indexModel);
                }
            }
        });
        return addButton;
    }

    private JButton buildRemoveButton() {
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                if (indexModelList.isNotEmpty()) {
                    IndexModel selectedIndexModel = indexModelList.getSelectedEntity();
                    int yes = JOptionPane.showConfirmDialog(frame,
                                                            resourceManager.getString("index.model.dialog.index.model.confirm"),
                                                            resourceManager.getString("index.model.dialog.index.model.title"),
                                                            JOptionPane.YES_NO_OPTION);
                    if (yes == JOptionPane.YES_OPTION) {
                        removeIndexModelAction.remove(selectedIndexModel);
                        indexModelList.removeEntity(selectedIndexModel);
                        if (indexModelList.isEmpty()) {
                            selectedIndexModel = TestEntityFactory.instance.newIndexModelTestInstance("default", ApplicationProxy.instance.getUser());
                            indexModelList.addEntity(selectedIndexModel);
                            saveIndexModelAction.save(selectedIndexModel);
                            indexModelPanel.setModel(selectedIndexModel);
                            timeSeriesStatusBar.setIndexModelStatus(selectedIndexModel.getName());
                        } else {
                            selectedIndexModel = (IndexModel) indexModelList.getSelectedValue();
                            indexModelPanel.setModel(selectedIndexModel);
                            timeSeriesStatusBar.setIndexModelStatus(selectedIndexModel.getName());
                            timeSeriesChartPanel.reset();
                            timeSeriesTablePanel.reset();
                        }
                    }
                }
            }
        });
        return removeButton;
    }

    private JButton buildEditButton() {
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                if (indexModelList.isNotEmpty()) {
                    IndexModel selectedIndexModel = indexModelList.getSelectedEntity();
                    indexModelDialog.setTitle(resourceManager.getString("edit"));
                    indexModelDialog.view((JButton) event.getSource(), selectedIndexModel.getName(), selectedIndexModel.getNote());
                    if (indexModelDialog.wasNotCanceled()) {
                        selectedIndexModel.setName(indexModelDialog.getModelName());
                        selectedIndexModel.setNote(indexModelDialog.getModelNote());
                        indexModelList.updateEntity(selectedIndexModel);
                        saveIndexModelAction.save(selectedIndexModel);
                        indexModelNoteTextPane.setText(indexModelDialog.getModelNote());
                        timeSeriesStatusBar.setIndexModelStatus(selectedIndexModel.getName());
                    }
                }
            }
        });
        return editButton;
    }

    private JButton buildOpenButton() {
        openButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                if (indexModelList.isNotEmpty()) {
                    IndexModel selectedIndexModel = indexModelList.getSelectedEntity();
                    indexModelPanel.setModel(selectedIndexModel);
                    timeSeriesStatusBar.setIndexModelStatus(selectedIndexModel.getName());
                    timeSeriesChartPanel.reset();
                    timeSeriesTablePanel.reset();
                    setVisible(false);
                }
            }
        });
        return openButton;
    }

    private JButton buildCloseButton() {
        getRootPane().setDefaultButton(closeButton);
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                setVisible(false);
            }
        });
        return closeButton;
    }
}