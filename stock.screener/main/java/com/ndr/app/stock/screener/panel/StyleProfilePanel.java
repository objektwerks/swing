package com.ndr.app.stock.screener.panel;

import com.ndr.model.stock.screener.DateNumberPoint;
import com.ndr.model.stock.screener.TimeSeriesType;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.springframework.stereotype.Component;

@Component
public final class StyleProfilePanel extends JPanel {
    private static final long serialVersionUID = -8683953147351238957L;

	public void setModel(Date currentRebalanceFrequencyDate, Map<TimeSeriesType, List<DateNumberPoint>> dateNumberPoints) {
        rebuild(currentRebalanceFrequencyDate, dateNumberPoints);
    }

    public void reset() {
        removeAll();
    }

    public RenderedImage getImage() {
        BufferedImage image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();
        graphics.setClip(0, 0, getWidth(), getHeight());
        paint(graphics);
        return image;
    }

    @PostConstruct
    protected void build() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
        setBackground(Color.white);
    }

    private void rebuild(Date currentRebalanceFrequencyDate, Map<TimeSeriesType, List<DateNumberPoint>> dateNumberPoints) {
        removeAll();
        JPanel panel = new JPanel(new GridLayout(4, 4));
        panel.setBackground(Color.white);
        addRow(panel, true, "Large", getValue(currentRebalanceFrequencyDate, dateNumberPoints, TimeSeriesType.largeGrowth),
                                     getValue(currentRebalanceFrequencyDate, dateNumberPoints, TimeSeriesType.largeCore),
                                     getValue(currentRebalanceFrequencyDate, dateNumberPoints, TimeSeriesType.largeValue));
        addRow(panel, true, "Midcap", getValue(currentRebalanceFrequencyDate, dateNumberPoints, TimeSeriesType.midGrowth),
                                      getValue(currentRebalanceFrequencyDate, dateNumberPoints, TimeSeriesType.midCore),
                                      getValue(currentRebalanceFrequencyDate, dateNumberPoints, TimeSeriesType.midValue));
        addRow(panel, true, "Small", getValue(currentRebalanceFrequencyDate, dateNumberPoints, TimeSeriesType.smallGrowth),
                                     getValue(currentRebalanceFrequencyDate, dateNumberPoints, TimeSeriesType.smallCore),
                                     getValue(currentRebalanceFrequencyDate, dateNumberPoints, TimeSeriesType.smallValue));
        addRow(panel, false, "", "Growth", "Core", "Value");
        decorateLabelWithHighestValue(panel);
        add(Box.createVerticalStrut(16));
        add(panel);
        add(Box.createVerticalStrut(16));
    }

    private void addRow(JPanel panel, boolean drawBorder, String... items) {
        for (int i = 0; i < items.length; i++) {
            JLabel label = new JLabel(items[i], JLabel.CENTER);
            label.setBackground(Color.white);
            if (i == 0) {
                label.setHorizontalAlignment(JLabel.LEFT);
            }
            if (drawBorder && i > 0) {
                label.setBorder(BorderFactory.createLineBorder(Color.lightGray, 1));                
            }
            panel.add(label);
        }
    }

    private String getValue(Date currentRebalanceFrequencyDate,
                            Map<TimeSeriesType, List<DateNumberPoint>> dateNumberPoints,
                            TimeSeriesType timeSeriesType) {
        double number = DateNumberPoint.getNumberByDateAndTimeSeriesType(currentRebalanceFrequencyDate, dateNumberPoints, timeSeriesType);
        DecimalFormat formatter = new DecimalFormat("##0.00");
        return formatter.format(number);
    }

    private void decorateLabelWithHighestValue(JPanel panel) {
        JLabel labelWithHigestValue = new JLabel("0.0");
        for (java.awt.Component component : panel.getComponents()) {
            if (component instanceof JLabel) {
                JLabel label = (JLabel) component;
                try {
                    double value = Double.parseDouble(label.getText());
                    if (value > Double.parseDouble(labelWithHigestValue.getText())) {
                        labelWithHigestValue = label;
                    }
                } catch (NumberFormatException ignore) {}
            }
        }
        labelWithHigestValue.setOpaque(true);
        labelWithHigestValue.setBackground(Color.lightGray);
    }
}