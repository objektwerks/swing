package com.ndr.app.stock.screener.dialog;

import com.ndr.app.stock.screener.ApplicationProxy;
import com.ndr.app.stock.screener.action.RemoveCriteriaModelAction;
import com.ndr.app.stock.screener.action.SaveCriteriaModelAction;
import com.ndr.app.stock.screener.border.CompoundBorderFactory;
import com.ndr.app.stock.screener.button.AddButton;
import com.ndr.app.stock.screener.button.CloseButton;
import com.ndr.app.stock.screener.button.EditButton;
import com.ndr.app.stock.screener.button.OpenButton;
import com.ndr.app.stock.screener.button.RemoveButton;
import com.ndr.app.stock.screener.list.CriteriaModelList;
import com.ndr.app.stock.screener.listener.CriteriaModelNoteListSelectionListener;
import com.ndr.app.stock.screener.panel.CriteriaModelPanel;
import com.ndr.app.stock.screener.panel.TimeSeriesChartPanel;
import com.ndr.app.stock.screener.panel.TimeSeriesTablePanel;
import com.ndr.app.stock.screener.resource.ResourceManager;
import com.ndr.app.stock.screener.statusbar.TimeSeriesStatusBar;
import com.ndr.app.stock.screener.text.NoteTextPane;
import com.ndr.model.stock.screener.CriteriaModel;
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
public final class CriteriaModelDialog extends JDialog {
    private static final long serialVersionUID = -8116583074411809143L;
    
	@Autowired private Frame frame;
    @Autowired private ResourceManager resourceManager;
    @Autowired private AddButton addButton;
    @Autowired private RemoveButton removeButton;
    @Autowired private EditButton editButton;
    @Autowired private OpenButton openButton;
    @Autowired private CloseButton closeButton;
    @Autowired private TimeSeriesChartPanel timeSeriesChartPanel;
    @Autowired private TimeSeriesTablePanel timeSeriesTablePanel;
    @Autowired private CriteriaModelPanel criteriaModelPanel;
    @Autowired private CriteriaModelList<CriteriaModel> criteriaModelList;
    @Autowired private NoteTextPane criteriaModelNoteTextPane;
    @Autowired private SaveCriteriaModelAction saveCriteriaModelAction;
    @Autowired private RemoveCriteriaModelAction removeCriteriaModelAction;
    @Autowired private CriteriaIndexModelDialog criteriaModelDialog;
    @Autowired private TimeSeriesStatusBar timeSeriesStatusBar;

    public void view() {
        criteriaModelList.setSelectedEntity(criteriaModelPanel.getModel());
        setLocationRelativeTo(frame);
        setVisible(true);
    }

    public List<CriteriaModel> getCriteriaModels() {
        return criteriaModelList.getEntities();
    }

    public void setCriteriaModels(List<CriteriaModel> criteriaModels) {
        criteriaModelList.setEntities(criteriaModels);
    }

    public CriteriaModel getSelectedCriteriaModel() {
        return criteriaModelList.getSelectedEntity();
    }
    
    @PostConstruct
    protected void build() {
        criteriaModelList.addListSelectionListener(new CriteriaModelNoteListSelectionListener(criteriaModelList, criteriaModelNoteTextPane));
        addWindowListener(new HideDialogWindowAdapter(this));
        setIconImage(resourceManager.getImage("criteria.model.icon"));
        setTitle(resourceManager.getString("criteria.model.dialog.title"));
        setLayout(new BorderLayout());
        setModal(true);
        add(buildPanel(), BorderLayout.CENTER);
        add(buildScrollableTextPane(), BorderLayout.SOUTH);
        pack();
    }

    private JPanel buildPanel() {
        JPanel panel = new JPanel();
        CompoundBorderFactory.instance.create(panel, resourceManager.getString("criteria.models"));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(buildScrollPane());
        return panel;
    }

    private JScrollPane buildScrollPane() {
        JScrollPane scrollPane = new JScrollPane(criteriaModelList);
        scrollPane.setPreferredSize(new Dimension(275, 200));
        JViewport viewport = new JViewport();
        viewport.setView(buildButtonPanel());
        scrollPane.setColumnHeader(viewport);
        return scrollPane;
    }

    private JPanel buildButtonPanel() {
        JPanel panel = new JPanel();
        panel.setAlignmentY(0f);
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
        JScrollPane scrollPane = new JScrollPane(criteriaModelNoteTextPane);
        CompoundBorderFactory.instance.create(scrollPane, resourceManager.getString("note"));
        return scrollPane;
    }

    private JButton buildAddButton() {
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                criteriaModelDialog.setTitle(resourceManager.getString("add"));
                criteriaModelDialog.view((JButton) event.getSource(), "", "");
                if (criteriaModelDialog.wasNotCanceled()) {
                    CriteriaModel criteriaModel = TestEntityFactory.instance.newCriteriaModelTestInstance(criteriaModelDialog.getModelName(), ApplicationProxy.instance.getUser());
                    criteriaModel.setNote(criteriaModelDialog.getModelNote());
                    criteriaModelList.addEntity(criteriaModel);
                    saveCriteriaModelAction.save(criteriaModel);
                }
            }
        });
        return addButton;
    }

    private JButton buildRemoveButton() {
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                if (criteriaModelList.isNotEmpty()) {
                    CriteriaModel selectedCriteriaModel = criteriaModelList.getSelectedEntity();
                    int yes = JOptionPane.showConfirmDialog(frame,
                                                            resourceManager.getString("criteria.model.dialog.criteria.model.confirm"),
                                                            resourceManager.getString("criteria.model.dialog.criteria.model.title"),
                                                            JOptionPane.YES_NO_OPTION);
                    if (yes == JOptionPane.YES_OPTION) {
                        removeCriteriaModelAction.remove(selectedCriteriaModel);
                        criteriaModelList.removeEntity(selectedCriteriaModel);
                        if (criteriaModelList.isEmpty()) {
                            selectedCriteriaModel = TestEntityFactory.instance.newCriteriaModelTestInstance("default", ApplicationProxy.instance.getUser());
                            criteriaModelList.addEntity(selectedCriteriaModel);
                            saveCriteriaModelAction.save(selectedCriteriaModel);
                            timeSeriesChartPanel.reset();
                            timeSeriesTablePanel.reset();
                            criteriaModelPanel.setModel(selectedCriteriaModel);
                            timeSeriesStatusBar.setCriteriaModelStatus(selectedCriteriaModel.getName());
                        } else {
                            selectedCriteriaModel = (CriteriaModel) criteriaModelList.getSelectedValue();
                            criteriaModelPanel.setModel(selectedCriteriaModel);
                            timeSeriesStatusBar.setCriteriaModelStatus(selectedCriteriaModel.getName());
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
                if (criteriaModelList.isNotEmpty()) {
                    CriteriaModel selectedCriteriaModel = criteriaModelList.getSelectedEntity();
                    criteriaModelDialog.setTitle(resourceManager.getString("edit"));
                    criteriaModelDialog.view((JButton) event.getSource(), selectedCriteriaModel.getName(), selectedCriteriaModel.getNote());
                    if (criteriaModelDialog.wasNotCanceled()) {
                        selectedCriteriaModel.setName(criteriaModelDialog.getModelName());
                        selectedCriteriaModel.setNote(criteriaModelDialog.getModelNote());
                        criteriaModelList.updateEntity(selectedCriteriaModel);
                        saveCriteriaModelAction.save(selectedCriteriaModel);
                        criteriaModelNoteTextPane.setText(criteriaModelDialog.getModelNote());
                        timeSeriesStatusBar.setCriteriaModelStatus(selectedCriteriaModel.getName());
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
                if (criteriaModelList.isNotEmpty()) {
                    CriteriaModel selectedCriteriaModel = criteriaModelList.getSelectedEntity();
                    criteriaModelPanel.setModel(selectedCriteriaModel);
                    timeSeriesStatusBar.setCriteriaModelStatus(selectedCriteriaModel.getName());
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