package com.ndr.app.stock.screener.panel;

import com.ndr.app.stock.screener.ApplicationProxy;
import com.ndr.app.stock.screener.action.ManageCriteriaModelsAction;
import com.ndr.app.stock.screener.action.SaveCriteriaModelAction;
import com.ndr.app.stock.screener.button.ButtonSizer;
import com.ndr.app.stock.screener.button.DemoteCriteriaButton;
import com.ndr.app.stock.screener.button.RemoveCriteriaButton;
import com.ndr.app.stock.screener.component.CriteriaComponent;
import com.ndr.app.stock.screener.component.CriteriaComponentFactory;
import com.ndr.app.stock.screener.dialog.StockUniverseDialog;
import com.ndr.app.stock.screener.resource.ResourceManager;
import com.ndr.app.stock.screener.search.SearchBar;
import com.ndr.app.stock.screener.table.CriteriaComponentTable;
import com.ndr.model.stock.screener.Criteria;
import com.ndr.model.stock.screener.CriteriaModel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public final class CriteriaModelPanel extends JPanel {
    private static final long serialVersionUID = -8404200086794763719L;

	@Autowired private ResourceManager resourceManager;
    @Autowired private CriteriaComponentTable criteriaComponentTable;
    @Autowired private CriteriaPanel criteriaPanel;
    @Autowired private ColumnPanel columnPanel;
    @Autowired private ManageCriteriaModelsAction manageCriteriaModelsAction;
    @Autowired private SaveCriteriaModelAction saveCriteriaModelAction;
    @Autowired private RemoveCriteriaButton removeCriteriaButton;
    @Autowired private DemoteCriteriaButton demoteCriteriaButton;
    @Autowired private StockUniverseDialog stockUniverseDialog;
    @Autowired private SearchBar searchBar;
    private JButton stockUniverseButton;
    private CriteriaModel criteriaModel;

    public CriteriaModel getModel() {
        return criteriaModel;
    }

    public void setModel(CriteriaModel criteriaModel) {
        this.criteriaModel = criteriaModel;
        stockUniverseButton.setEnabled(true);
        criteriaPanel.setModel(ApplicationProxy.instance.getPrototypeCriteria(), criteriaModel.getCriteria());
        columnPanel.setModel(criteriaModel.getColumns());
        List<Criteria> criteria = criteriaModel.getCriteria();
        List<CriteriaComponent> criteriaComponents = new ArrayList<CriteriaComponent>(criteria.size());
        for (Criteria criterion : criteria) {
            CriteriaComponent criteriaComponent = CriteriaComponentFactory.instance.newInstance(resourceManager, criterion);
            criteriaComponents.add(criteriaComponent);
        }
        criteriaComponentTable.setModel(criteriaComponents);
        saveCriteriaModelAction.setEnabled(true);
    }

    public boolean containsCriteria(Criteria criteria) {
        return criteriaComponentTable.containsCriteria(criteria);
    }

    public void addCriteria(Criteria criteria) {
        Criteria newCriteria = criteria.copy();
        criteriaModel.addCriteria(newCriteria);
        CriteriaComponent criteriaComponent = CriteriaComponentFactory.instance.newInstance(resourceManager, newCriteria);
        criteriaComponentTable.addCriteriaComponent(criteriaComponent);
    }

    public Criteria removeSelectedCriteria() {
        CriteriaComponent criteriaComponent = criteriaComponentTable.getSelectedCriteriaComponent();
        Criteria criteria = criteriaComponent.getModel();
        criteriaModel.removeCriteria(criteria);
        criteriaComponentTable.removeCriteriaComponent(criteriaComponent);
        removeCriteriaButton.setEnabled(false);
        demoteCriteriaButton.setEnabled(false);
        return criteria;
    }

    public void promoteCriteria(Criteria criteria) {
        addCriteria(criteria);
    }

    @PostConstruct
    protected void build() {
        setLayout(new BorderLayout());
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        searchBar.setSearchable(criteriaComponentTable);
        panel.add(buildActionPanel(), BorderLayout.NORTH);
        panel.add(buildScrollPane(), BorderLayout.CENTER);
        add(new JLabel(resourceManager.getString("selected.criteria")), BorderLayout.NORTH);
        add(panel, BorderLayout.CENTER);
    }

    private JScrollPane buildScrollPane() {
        JScrollPane scrollPane = new JScrollPane(criteriaComponentTable);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        return scrollPane;
    }

    private JPanel buildActionPanel() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createLineBorder(Color.lightGray, 1));
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.add(buildManageButton());
        panel.add(Box.createRigidArea(new Dimension(2,0)));
        panel.add(buildSaveButton());
        panel.add(Box.createRigidArea(new Dimension(2, 0)));
        panel.add(removeCriteriaButton);
        panel.add(Box.createRigidArea(new Dimension(2,0)));
        panel.add(demoteCriteriaButton);
        panel.add(Box.createRigidArea(new Dimension(2,0)));
        panel.add(buildStockUniverseButton());
        panel.add(Box.createRigidArea(new Dimension(2,0)));
        panel.add(searchBar);
        return panel;
    }

    private JButton buildManageButton() {
        JButton button = new JButton(manageCriteriaModelsAction);
        button.setText("");
        ButtonSizer.instance.setDefaultToolBarSize(button);
        return button;
    }

    private JButton buildSaveButton() {
        saveCriteriaModelAction.setEnabled(false);
        JButton button = new JButton(saveCriteriaModelAction);
        button.setText("");
        ButtonSizer.instance.setDefaultToolBarSize(button);
        return button;
    }

    private JButton buildStockUniverseButton() {
        stockUniverseButton = new JButton(resourceManager.getImageIcon("stock.universe.icon"));
        stockUniverseButton.setEnabled(false);
        stockUniverseButton.setToolTipText(resourceManager.getString("stock.universe.tooltip"));
        ButtonSizer.instance.setDefaultToolBarSize(stockUniverseButton);
        stockUniverseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                stockUniverseDialog.setModel(criteriaModel.getStockUniverse());
                stockUniverseDialog.view((JComponent) event.getSource());
            }
        });
        return stockUniverseButton;
    }
}