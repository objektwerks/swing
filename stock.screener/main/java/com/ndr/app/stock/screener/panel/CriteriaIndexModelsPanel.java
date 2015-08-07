package com.ndr.app.stock.screener.panel;

import com.ndr.app.stock.screener.ApplicationProxy;
import com.ndr.app.stock.screener.Colors;
import com.ndr.app.stock.screener.action.SaveCriteriaModelAction;
import com.ndr.app.stock.screener.action.SaveIndexModelAction;
import com.ndr.app.stock.screener.action.TransitionToTimeSeriesPanelAction;
import com.ndr.app.stock.screener.button.AddButton;
import com.ndr.app.stock.screener.button.EditButton;
import com.ndr.app.stock.screener.dialog.CriteriaIndexModelDialog;
import com.ndr.app.stock.screener.frame.OnCloseHandler;
import com.ndr.app.stock.screener.list.CriteriaModelList;
import com.ndr.app.stock.screener.list.IndexModelList;
import com.ndr.app.stock.screener.listener.CriteriaModelNoteListSelectionListener;
import com.ndr.app.stock.screener.listener.IndexModelNoteListSelectionListener;
import com.ndr.app.stock.screener.resource.ResourceManager;
import com.ndr.app.stock.screener.search.SearchBar;
import com.ndr.app.stock.screener.statusbar.HomeStatusBar;
import com.ndr.app.stock.screener.text.NoteTextPane;
import com.ndr.app.stock.screener.toolbar.HomeToolBar;
import com.ndr.model.stock.screener.CriteriaModel;
import com.ndr.model.stock.screener.IndexModel;
import com.ndr.model.stock.screener.TestEntityFactory;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;

import org.jdesktop.swingx.JXList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public final class CriteriaIndexModelsPanel extends JPanel {
    private static final long serialVersionUID = 3599978173239424118L;

	@Autowired private OnCloseHandler onCloseHandler;
    @Autowired private ResourceManager resourceManager;
    @Autowired private HomeToolBar homeToolBar;
    @Autowired private TransitionToTimeSeriesPanelAction transitionToTimeSeriesPanelAction;
    @Autowired private CriteriaModelList<CriteriaModel> criteriaModelList;
    @Autowired private IndexModelList<IndexModel> indexModelList;
    @Autowired private NoteTextPane criteriaModelNoteTextPane;
    @Autowired private NoteTextPane indexModelNoteTextPane;
    @Autowired private SearchBar criteriaModelSearchBar;
    @Autowired private SearchBar indexModelSearchBar;
    @Autowired private AddButton addCriteriaModelButton;
    @Autowired private EditButton editCriteriaModelButton;
    @Autowired private AddButton addIndexModelButton;
    @Autowired private EditButton editIndexModelButton;
    @Autowired private CriteriaIndexModelDialog criteriaModelDialog;
    @Autowired private CriteriaIndexModelDialog indexModelDialog;
    @Autowired private SaveCriteriaModelAction saveCriteriaModelAction;
    @Autowired private SaveIndexModelAction saveIndexModelAction;
    @Autowired private HomeStatusBar homeStatusBar;
 
    public void setModel(List<CriteriaModel> criteriaModels, List<IndexModel> indexModels) {
        criteriaModelList.setEntities(criteriaModels);
        indexModelList.setEntities(indexModels);
    }

    public void setSelectedModels(CriteriaModel criteriaModel, IndexModel indexModel) {
        criteriaModelList.setSelectedEntity(criteriaModel);
        indexModelList.setSelectedEntity(indexModel);
    }

    public CriteriaModel getSelectedCriteriaModel() {
        return criteriaModelList.getSelectedEntity();
    }

    public IndexModel getSelectedIndexModel() {
        return indexModelList.getSelectedEntity();
    }

    @PostConstruct
    protected void build() {
        criteriaModelList.addListSelectionListener(new CriteriaModelNoteListSelectionListener(criteriaModelList, criteriaModelNoteTextPane, homeStatusBar));
        indexModelList.addListSelectionListener(new IndexModelNoteListSelectionListener(indexModelList, indexModelNoteTextPane, homeStatusBar));
        criteriaModelSearchBar.setSearchable(criteriaModelList);
        indexModelSearchBar.setSearchable(indexModelList);
        setLayout(new BorderLayout());
        add(buildImagePanel(), BorderLayout.WEST);
        add(buildPanel(), BorderLayout.CENTER);
    }

    private JPanel buildImagePanel() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.white);
        panel.setBorder(BorderFactory.createLineBorder(Color.lightGray, 6));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(buildTitle());
        panel.add(Box.createVerticalGlue());
        panel.add(buildImage("stock.analyst.3.icon"));
        panel.add(Box.createVerticalGlue());
        panel.add(buildImage("stock.analyst.2.icon"));
        panel.add(Box.createVerticalGlue());
        panel.add(buildImage("stock.analyst.1.icon"));
        return panel;
    }

    private JLabel buildTitle() {
        JLabel label = new JLabel(resourceManager.getImageIcon("app.icon"));
        label.setText(resourceManager.getString("app.title"));
        label.setAlignmentX(JLabel.RIGHT_ALIGNMENT);
        label.setBackground(Color.white);
        label.setForeground(Colors.navy);
        label.setFont(label.getFont().deriveFont(Font.BOLD, 21));
        label.setOpaque(true);
        return label;
    }
    
    private JLabel buildImage(String name) {
        JLabel label = new JLabel(resourceManager.getImageIcon(name));
        label.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        label.setBackground(Color.white);
        label.setOpaque(true);
        return label;
    }
    
    private JPanel buildPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(homeToolBar, BorderLayout.NORTH);
        panel.add(buildSelectPanel(), BorderLayout.CENTER);
        panel.add(homeStatusBar, BorderLayout.SOUTH);
        return panel;
    }
    
    private JPanel buildSelectPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
        panel.add(buildScrollablePanel(criteriaModelList, buildButtonPanel(buildAddCriteriaModelButton(), buildEditCriteriaModelButton(), criteriaModelSearchBar), "criteria.models", criteriaModelNoteTextPane));
        panel.add(Box.createRigidArea(new Dimension(4,0))); 
        panel.add(buildScrollablePanel(indexModelList, buildButtonPanel(buildAddIndexModelButton(), buildEditIndexModelButton(), indexModelSearchBar), "index.models", indexModelNoteTextPane));
        return panel;
    }

    private JPanel buildScrollablePanel(JXList list, JPanel buttonPanel, String title, NoteTextPane textPane) {
        JScrollPane scrollPane = new JScrollPane(list);
        scrollPane.setPreferredSize(new Dimension(450, 900));
        JViewport viewport = new JViewport();
        viewport.setView(buttonPanel);
        scrollPane.setColumnHeader(viewport);
        return buildScrollablePanel(scrollPane, title, textPane);
    }

    private JPanel buildScrollablePanel(JScrollPane scrollPane, String title, NoteTextPane textPane) {
        JLabel label = new JLabel(resourceManager.getString(title));
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(label, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buildScrollableTextPane(textPane), BorderLayout.SOUTH);
        return panel;
    }

    private JScrollPane buildScrollableTextPane(NoteTextPane textPane) {
        return new JScrollPane(textPane);
    }

    private JPanel buildButtonPanel(JButton addButton, JButton editButton, SearchBar searchBar) {
        JPanel panel = new JPanel();
        panel.setAlignmentY(0f);
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.add(addButton);
        panel.add(Box.createRigidArea(new Dimension(2, 0)));
        panel.add(editButton);
        panel.add(Box.createRigidArea(new Dimension(2, 0)));
        panel.add(searchBar);
        return panel;
    }

    private JButton buildAddCriteriaModelButton() {
        addCriteriaModelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                criteriaModelDialog.setTitle(resourceManager.getString("add"));
                criteriaModelDialog.view((JButton) event.getSource(), "", "");
                if (criteriaModelDialog.wasNotCanceled()) {
                    CriteriaModel criteriaModel = TestEntityFactory.instance.newCriteriaModelTestInstance(criteriaModelDialog.getModelName(), ApplicationProxy.instance.getUser());
                    criteriaModel.setNote(criteriaModelDialog.getModelNote());
                    criteriaModelList.addEntity(criteriaModel);
                    saveCriteriaModelAction.save(criteriaModel);
                    homeStatusBar.setCriteriaModelStatus(criteriaModel.getName());
                }
            }
        });
        return addCriteriaModelButton;
    }

    private JButton buildEditCriteriaModelButton() {
        editCriteriaModelButton.addActionListener(new ActionListener() {
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
                        homeStatusBar.setCriteriaModelStatus(selectedCriteriaModel.getName());
                    }
                }
            }
        });
        return editCriteriaModelButton;
    }

    private JButton buildAddIndexModelButton() {
        addIndexModelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                indexModelDialog.setTitle(resourceManager.getString("add"));
                indexModelDialog.view((JButton) event.getSource(), "", "");
                if (indexModelDialog.wasNotCanceled()) {
                    IndexModel indexModel = TestEntityFactory.instance.newIndexModelTestInstance(indexModelDialog.getModelName(), ApplicationProxy.instance.getUser());
                    indexModel.setNote(indexModelDialog.getModelNote());
                    indexModelList.addEntity(indexModel);
                    saveIndexModelAction.save(indexModel);
                    homeStatusBar.setIndexModelStatus(indexModel.getName());
                }
            }
        });
        return addIndexModelButton;
    }

    private JButton buildEditIndexModelButton() {
        editIndexModelButton.addActionListener(new ActionListener() {
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
                        homeStatusBar.setIndexModelStatus(selectedIndexModel.getName());
                    }
                }
            }
        });
        return editIndexModelButton;
    }
}