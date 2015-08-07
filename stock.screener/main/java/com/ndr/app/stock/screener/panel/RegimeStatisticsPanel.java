package com.ndr.app.stock.screener.panel;

import com.ndr.app.stock.screener.button.ButtonSizer;
import com.ndr.app.stock.screener.combobox.RegimeComboBox;
import com.ndr.app.stock.screener.list.DateRangeList;
import com.ndr.app.stock.screener.resource.ResourceManager;
import com.ndr.app.stock.screener.table.RegimeStatisticsTable;
import com.ndr.model.stock.screener.DateRange;
import com.ndr.model.stock.screener.Regime;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
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
public class RegimeStatisticsPanel extends JPanel {
    private static final long serialVersionUID = -5488679862643081766L;
	@Autowired private ResourceManager resourceManager;
    @Autowired private RegimeComboBox regimeComboBox;
    @Autowired private DateRangeList<DateRange> dateRangeList;
    @Autowired private RegimeStatisticsTable inRegimeStatisticsTable;
    @Autowired private RegimeStatisticsTable outRegimeStatisticsTable;
    private JLabel dateRangeLabel;
    private JXDatePicker startDatePicker;
    private JXDatePicker endDatePicker;
    private JButton addDateRangeButton;
    private JButton removeDateRangeButton;

    public void setModel() {
        removeAll();
        regimeComboBox.setModel(new ArrayList<Regime>());
        inRegimeStatisticsTable.setModel(new HashSet<List<String>>());
        outRegimeStatisticsTable.setModel(new HashSet<List<String>>());
        rebuild();
        validate();
        repaint();
    }

    public void reset() {
        removeAll();
        validate();
        repaint();
    }

    @PostConstruct
    protected void build() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    }

    private void rebuild() {
        add(buildPanel());
    }

    private JPanel buildPanel() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createLineBorder(Color.lightGray, 1));
        panel.setLayout(new MigLayout("ins 2, fillx"));
        panel.add(new JLabel(resourceManager.getString("regimes.label")));
        panel.add(regimeComboBox, "span, grow, wrap");
        JPanel subPanel = new JPanel();
        subPanel.setLayout(new BoxLayout(subPanel, BoxLayout.Y_AXIS));
        subPanel.add(buildDateRangeScrollPane());
        subPanel.add(buildInRegimeStatisticsScrollPane());
        subPanel.add(buildOutRegimeStatisticsScrollPane());
        panel.add(subPanel, "span, grow");
        regimeComboBox.addActionListener(new RegimeComboBoxActionListener());
        return panel;
    }

    private JPanel buildDateRangeScrollPane() {
        dateRangeList.getSelectionModel().addListSelectionListener(new DateRangeListSelectionListener());
        dateRangeList.addMouseListener(new DateRangeListMouseAdapter());
        JScrollPane scrollPane = new JScrollPane(dateRangeList);
        JViewport viewport = new JViewport();
        viewport.setView(buildDateRangePanel());
        scrollPane.setColumnHeader(viewport);
        scrollPane.setPreferredSize(new Dimension(200, 200));
        dateRangeLabel = new JLabel();
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(dateRangeLabel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildDateRangePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.add(buildAddDateRangeButton());
        panel.add(Box.createRigidArea(new Dimension(2, 0)));
        panel.add(buildRemoveDateRangeButton());
        panel.add(Box.createRigidArea(new Dimension(2, 0)));
        panel.add(buildStartDatePicker());
        panel.add(Box.createRigidArea(new Dimension(2, 0)));
        panel.add(new JLabel("-"));
        panel.add(buildEndDatePicker());
        return panel;
    }

    private JXDatePicker buildStartDatePicker() {
        startDatePicker = buildDatePicker(resourceManager.getString("start.date"));
        startDatePicker.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                if (dateRangeList.isNotEmpty()) {
                    Date startDate = startDatePicker.getDate();
                    DateRange dateRange = dateRangeList.getSelectedEntity();
                    try {
                        dateRange.setStartDate(startDate);
                    } catch (IllegalArgumentException e) {
                        showDatePickerErrorMessage(startDatePicker, e);
                    }
                }
            }
        });
        return startDatePicker;
    }

    private JXDatePicker buildEndDatePicker() {
        endDatePicker = buildDatePicker(resourceManager.getString("end.date"));
        endDatePicker.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                if (dateRangeList.isNotEmpty()) {
                    Date endDate = endDatePicker.getDate();
                    DateRange dateRange = dateRangeList.getSelectedEntity();
                    try {
                        dateRange.setEndDate(endDate);
                    } catch (IllegalArgumentException e) {
                        showDatePickerErrorMessage(endDatePicker, e);
                    }
                }
            }
        });
        return endDatePicker;
    }

    private JXDatePicker buildDatePicker(String tooltip) {
        JXDatePicker datePicker = new JXDatePicker();
        datePicker.setEnabled(false);
        datePicker.setFormats("yyyy-MM-dd");
        datePicker.setToolTipText(tooltip);
        datePicker.getEditor().setVisible(false);
        datePicker.setMinimumSize(new Dimension(45, 30));
        datePicker.setMaximumSize(new Dimension(45, 30));
        datePicker.setPreferredSize(new Dimension(45, 30));
        return datePicker;
    }

    private void setEnabled(JXDatePicker datePicker, boolean isEnabled) {
        datePicker.setEnabled(isEnabled);
    }

    private void showDatePickerErrorMessage(JXDatePicker datePicker, IllegalArgumentException e) {
        JOptionPane.showMessageDialog(datePicker, e.getMessage(), "Stock Screener", JOptionPane.ERROR_MESSAGE);
    }

    private JPanel buildInRegimeStatisticsScrollPane() {
        JScrollPane scrollPane = new JScrollPane(inRegimeStatisticsTable);
        scrollPane.setPreferredSize(new Dimension(200, 200));
        return buildScrollPanePanel(scrollPane, resourceManager.getString("in.regime.statistics"));
    }

    private JPanel buildOutRegimeStatisticsScrollPane() {
        JScrollPane scrollPane = new JScrollPane(outRegimeStatisticsTable);
        scrollPane.setPreferredSize(new Dimension(200, 200));
        return buildScrollPanePanel(scrollPane, resourceManager.getString("out.regime.statistics"));
    }

    private JButton buildAddDateRangeButton() {
        addDateRangeButton = new JButton("", resourceManager.getImageIcon("add.icon"));
        addDateRangeButton.setEnabled(false);
        addDateRangeButton.setToolTipText(resourceManager.getString("regime.statistics.panel.add.date.range.tooltip"));
        ButtonSizer.instance.setDefaultToolBarSize(addDateRangeButton);
        addDateRangeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                DateRange dateRange = dateRangeList.getNextDateRange();
                dateRangeList.addEntity(dateRange);
                startDatePicker.setDate(dateRange.getStartDate());
                endDatePicker.setDate(dateRange.getEndDate());
            }
        });
        return addDateRangeButton;
    }

    private JButton buildRemoveDateRangeButton() {
        removeDateRangeButton = new JButton("", resourceManager.getImageIcon("remove.icon"));
        removeDateRangeButton.setEnabled(false);
        removeDateRangeButton.setToolTipText(resourceManager.getString("regime.statistics.panel.remove.date.range.tooltip"));
        ButtonSizer.instance.setDefaultToolBarSize(removeDateRangeButton);
        removeDateRangeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                if (dateRangeList.isNotEmpty()) {
                    dateRangeList.removeSelectedEntity();
                }
            }
        });
        return removeDateRangeButton;
    }

    private JPanel buildScrollPanePanel(JScrollPane scrollPane, String title) {
        JLabel label = new JLabel(title);
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(label, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private class RegimeComboBoxActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            Regime selectedRegime = regimeComboBox.getSelectedRegime();
            if (selectedRegime.equals(regimeComboBox.getCustomRegime())) {
                addDateRangeButton.setEnabled(true);
                removeDateRangeButton.setEnabled(true);
            } else {
                addDateRangeButton.setEnabled(false);
                removeDateRangeButton.setEnabled(false);
            }
            dateRangeLabel.setText(selectedRegime.getName());
            dateRangeList.setEntities(selectedRegime.getDateRanges());
        }
    }

    private class DateRangeListMouseAdapter extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent event) {
            int index = dateRangeList.locationToIndex(event.getPoint());
            if (index > -1) {
                DateRange dateRange = (DateRange) dateRangeList.getModel().getElementAt(index);
                startDatePicker.setDate(dateRange.getStartDate());
                endDatePicker.setDate(dateRange.getEndDate());
                setEnabled(startDatePicker, true);
                setEnabled(endDatePicker, true);
            } else {
                setEnabled(startDatePicker, false);
                setEnabled(endDatePicker, false);
            }
        }
    }

    private class DateRangeListSelectionListener implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent event) {
            if (dateRangeList.isNotEmpty()) {
                DateRange dateRange = dateRangeList.getSelectedEntity();
                if (dateRange != null) {
                    startDatePicker.setDate(dateRange.getStartDate());
                    endDatePicker.setDate(dateRange.getEndDate());
                    setEnabled(startDatePicker, true);
                    setEnabled(endDatePicker, true);
                } else {
                    setEnabled(startDatePicker, false);
                    setEnabled(endDatePicker, false);                    
                }
            }
        }
    }
}