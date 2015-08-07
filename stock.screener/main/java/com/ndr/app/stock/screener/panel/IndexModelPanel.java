package com.ndr.app.stock.screener.panel;

import com.ndr.app.stock.screener.action.ManageIndexModelsAction;
import com.ndr.app.stock.screener.action.SaveIndexModelAction;
import com.ndr.app.stock.screener.button.ButtonSizer;
import com.ndr.app.stock.screener.combobox.AlternateInvestmentComboBox;
import com.ndr.app.stock.screener.combobox.RebalanceFrequencyComboBox;
import com.ndr.app.stock.screener.combobox.WeightingComboBox;
import com.ndr.app.stock.screener.list.RebalanceFrequencyDateList;
import com.ndr.app.stock.screener.resource.ResourceManager;
import com.ndr.model.stock.screener.IndexModel;
import com.ndr.model.stock.screener.RebalanceFrequency;
import com.ndr.model.stock.screener.RebalanceFrequencyDate;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.annotation.PostConstruct;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.miginfocom.swing.MigLayout;
import org.jdesktop.swingx.JXDatePicker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public final class IndexModelPanel extends JPanel {
    private static final long serialVersionUID = -4675477531832181703L;

	@Autowired private ResourceManager resourceManager;
    @Autowired private ManageIndexModelsAction manageIndexModelsAction;
    @Autowired private SaveIndexModelAction saveIndexModelAction;
    @Autowired private WeightingComboBox weightingComboBox;
    @Autowired private AlternateInvestmentComboBox alternateInvestmentComboBox;
    @Autowired private RebalanceFrequencyComboBox rebalanceFrequencyComboBox;
    @Autowired private RebalanceFrequencyDateList<RebalanceFrequencyDate> rebalanceFrequencyDateList;
    private JCheckBox isTotalReturnCheckBox;
    private JXDatePicker startDatePicker;
    private JXDatePicker endDatePicker;
    private JScrollPane rebalanceFrequencyDatesScrollPane;
    private JXDatePicker selectedRebalanceFrequencyDatePicker;
    private JPanel modelPanel;

    private IndexModel indexModel;

    public IndexModel getModel() {
        if (indexModel != null) {
            indexModel.setDateRange(startDatePicker.getDate(), endDatePicker.getDate());
            indexModel.setTotalReturn(isTotalReturnCheckBox.isSelected());
        }
        return indexModel;
    }

    public void setModel(IndexModel indexModel) {
        this.indexModel = indexModel;
        isTotalReturnCheckBox.setSelected(indexModel.isTotalReturn());
        startDatePicker.setDate(indexModel.getStartDate());
        endDatePicker.setDate(indexModel.getEndDate());
        weightingComboBox.setModel(indexModel);
        rebalanceFrequencyComboBox.setModel(indexModel);
        alternateInvestmentComboBox.setModel(indexModel);
        rebalanceFrequencyDateList.setEntities(indexModel.getRebalanceFrequencyDates());
        saveIndexModelAction.setEnabled(true);
        modelPanel.setVisible(true);
    }
    
    @PostConstruct
    protected void build() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        isTotalReturnCheckBox = new JCheckBox(resourceManager.getString("total.return.label"));
        add(new JLabel(resourceManager.getString("index.model.selected")));
        add(buildActionPanel());
        add(buildModelPanel());
    }

    private JPanel buildActionPanel() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createLineBorder(Color.lightGray, 1));
        panel.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.add(buildManageButton());
        panel.add(Box.createRigidArea(new Dimension(2, 0)));
        panel.add(buildSaveButton());
        return panel;
    }

    private JButton buildManageButton() {
        JButton button = new JButton(manageIndexModelsAction);
        button.setText("");
        ButtonSizer.instance.setDefaultToolBarSize(button);
        return button;
    }

    private JButton buildSaveButton() {
        saveIndexModelAction.setEnabled(false);
        JButton button = new JButton(saveIndexModelAction);
        button.setText("");
        ButtonSizer.instance.setDefaultToolBarSize(button);
        return button;
    }

    private JPanel buildModelPanel() {
        modelPanel = new JPanel();
        modelPanel.setBorder(BorderFactory.createLineBorder(Color.lightGray, 1));
        modelPanel.setLayout(new MigLayout("ins 2, fillx"));
        modelPanel.setVisible(false);
        modelPanel.add(new JLabel(resourceManager.getString("date.range.label")));
        modelPanel.add(buildStartDatePicker());
        modelPanel.add(buildEndDatePicker(), "wrap");
        modelPanel.add(new JLabel(resourceManager.getString("total.return.label")));
        modelPanel.add(isTotalReturnCheckBox, "span, grow, wrap");
        modelPanel.add(new JLabel(resourceManager.getString("weighting.label")));
        modelPanel.add(weightingComboBox, "span, grow, wrap");
        modelPanel.add(new JLabel(resourceManager.getString("alternate.investment.label")));
        modelPanel.add(alternateInvestmentComboBox, "span, grow, wrap");
        modelPanel.add(new JLabel(resourceManager.getString("rebalance.frequency.label")));
        modelPanel.add(rebalanceFrequencyComboBox, "span, grow, wrap");
        modelPanel.add(buildRebalanceFrequencyDatesScrollPane(), "span, grow");
        rebalanceFrequencyComboBox.addActionListener(new RebalanceFrequencyComboBoxActionListener());
        return modelPanel;
    }

    private JXDatePicker buildStartDatePicker() {
        startDatePicker = new JXDatePicker();
        startDatePicker.setFormats("yyyy-MM-dd");
        return startDatePicker;
    }

    private JXDatePicker buildEndDatePicker() {
        endDatePicker = new JXDatePicker();
        endDatePicker.setFormats("yyyy-MM-dd");
        return endDatePicker;
    }

    private JScrollPane buildRebalanceFrequencyDatesScrollPane() {
        rebalanceFrequencyDateList.getSelectionModel().addListSelectionListener(new RebalanceFrequencyDateListSelectionListener());
        rebalanceFrequencyDateList.addMouseListener(new RebalanceFrequencyDateListMouseAdapter());
        rebalanceFrequencyDatesScrollPane = new JScrollPane(rebalanceFrequencyDateList);
        JViewport viewport = new JViewport();
        viewport.setView(buildRebalanceFrequencyDatesPanel());
        rebalanceFrequencyDatesScrollPane.setColumnHeader(viewport);
        rebalanceFrequencyDatesScrollPane.setVisible(false);
        return rebalanceFrequencyDatesScrollPane;
    }

    private JPanel buildRebalanceFrequencyDatesPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.add(buildAddRebalanceFrequencyDateButton());
        panel.add(Box.createRigidArea(new Dimension(2, 0)));
        panel.add(buildRemoveRebalanceFrequencyDateButton());
        panel.add(Box.createRigidArea(new Dimension(2, 0)));
        panel.add(buildSelectedRebalanceFrequencyDatePicker());
        return panel;
    }

    private JButton buildAddRebalanceFrequencyDateButton() {
        JButton button = new JButton("", resourceManager.getImageIcon("add.icon"));
        button.setToolTipText(resourceManager.getString("index.panel.add.rebalance.frequency.date.tooltip"));
        ButtonSizer.instance.setDefaultToolBarSize(button);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                RebalanceFrequencyDate rebalanceFrequencyDate = rebalanceFrequencyDateList.getNextRebalanceFrequencyDate();
                rebalanceFrequencyDateList.addEntity(rebalanceFrequencyDate);
                selectedRebalanceFrequencyDatePicker.setDate(rebalanceFrequencyDate.getDate());
            }
        });
        return button;
    }

    private JButton buildRemoveRebalanceFrequencyDateButton() {
        JButton button = new JButton("", resourceManager.getImageIcon("remove.icon"));
        button.setToolTipText(resourceManager.getString("index.panel.remove.rebalance.frequency.date.tooltip"));
        ButtonSizer.instance.setDefaultToolBarSize(button);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                if (rebalanceFrequencyDateList.isNotEmpty()) {
                    rebalanceFrequencyDateList.removeSelectedEntity();
                }
            }
        });
        return button;
    }

    private JXDatePicker buildSelectedRebalanceFrequencyDatePicker() {
        selectedRebalanceFrequencyDatePicker = new JXDatePicker();
        selectedRebalanceFrequencyDatePicker.setFormats("yyyy-MM-dd");
        selectedRebalanceFrequencyDatePicker.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                if (rebalanceFrequencyDateList.isNotEmpty()) {
                    RebalanceFrequencyDate date = new RebalanceFrequencyDate(selectedRebalanceFrequencyDatePicker.getDate());
                    rebalanceFrequencyDateList.updateRebalanceFrequencyDate(date);
                }
            }
        });
        return selectedRebalanceFrequencyDatePicker;
    }

    private class RebalanceFrequencyComboBoxActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            RebalanceFrequency rebalanceFrequency = (RebalanceFrequency) rebalanceFrequencyComboBox.getSelectedItem();
            if (rebalanceFrequency.equals(RebalanceFrequency.custom)) {
                rebalanceFrequencyDatesScrollPane.setVisible(true);
            } else {
                rebalanceFrequencyDatesScrollPane.setVisible(false);
                rebalanceFrequencyDateList.removeEntities();
            }
        }
    }
    
    private class RebalanceFrequencyDateListMouseAdapter extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent event) {
            int index = rebalanceFrequencyDateList.locationToIndex(event.getPoint());
            if (index > -1) {
                RebalanceFrequencyDate rebalanceFrequencyDate = (RebalanceFrequencyDate) rebalanceFrequencyDateList.getModel().getElementAt(index);
                selectedRebalanceFrequencyDatePicker.setDate(rebalanceFrequencyDate.getDate());
            }
        }
    }

    private class RebalanceFrequencyDateListSelectionListener implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent event) {
            if (rebalanceFrequencyDateList.isNotEmpty()) {
                RebalanceFrequencyDate selectedRebalanceFrequencyDate = rebalanceFrequencyDateList.getSelectedEntity();
                if (selectedRebalanceFrequencyDate != null) {
                    selectedRebalanceFrequencyDatePicker.setDate(selectedRebalanceFrequencyDate.getDate());                                    
                }
            }
        }
    }
}