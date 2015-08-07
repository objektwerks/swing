package com.ndr.app.stock.screener.component;

import com.ndr.app.stock.screener.Colors;
import com.ndr.model.stock.screener.FrequencyDistribution;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.text.DecimalFormat;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JLabel;

import org.apache.commons.math.stat.descriptive.rank.Max;
import org.apache.commons.math.stat.descriptive.rank.Min;

public final class Histogram extends JLabel {
    private static final long serialVersionUID = -5950153576905059672L;
    
	private FrequencyDistribution frequencyDistribution;
    private double[] frequencies;
    private double minFrequency;
    private double maxFrequency;
    private int medianBin;
    private Set<Integer> quartileBins;
    private Set<Integer> quintileBins;
    private Set<Integer> decileBins;
    private Polygon overlay;

    public Histogram(FrequencyDistribution frequencyDistribution) {
        this.frequencyDistribution = frequencyDistribution;
        frequencies = frequencyDistribution.frequencies();
        minFrequency = new Min().evaluate(frequencies);
        maxFrequency = new Max().evaluate(frequencies);
        medianBin = frequencyDistribution.medianBin();
        quartileBins = frequencyDistribution.quartileBins();
        quintileBins = frequencyDistribution.quintileBins();
        decileBins = frequencyDistribution.decileBins();
        overlay = new Polygon();
        build();
    }

    public void paintOverlay(int lowerX, int upperX) {
        int lowerOverlayX;
        int upperOverlayX;
        if (lowerX == upperX) {
            lowerOverlayX = lowerX;
            upperOverlayX = upperX;
        } else {
            lowerOverlayX = lowerX;
            int width = getWidth();
            upperOverlayX = (upperX >= (width)) ? width : upperX;
        }
        overlay.reset();
        overlay.addPoint(lowerOverlayX, 0);
        overlay.addPoint(upperOverlayX, 0);
        int height = getHeight();
        overlay.addPoint(upperOverlayX, height);
        overlay.addPoint(lowerOverlayX, height);
        validate();
        repaint();
    }

    protected void build() {
        setMinimumSize(new Dimension(390, 100));
        setMaximumSize(new Dimension(390, 100));
        setPreferredSize(new Dimension(390, 100));
        setOpaque(true);
        setBackground(Color.white);
        setBorder(BorderFactory.createMatteBorder(1, 8, 1, 8, Color.gray));
        setToolTipText(buildToolTipText());
    }

    private String buildToolTipText() {
        DecimalFormat formatter = new DecimalFormat("##,###,###,###.##");
        return "<html>Mean: " + formatter.format(frequencyDistribution.mean()) +
               "<br>StdDev +1: " + formatter.format(frequencyDistribution.standardDeviationPlus1()) +
               "</br><br>StdDev -1: " + formatter.format(frequencyDistribution.standardDeviationMinus1()) +
               "</br><br>Count: " + frequencyDistribution.count() +
               "</br><hr/>Median: Green" +
               "<br>Quartile: Magenta</br>" +
               "<br>Quintile: Red</br>" +
               "<br>Decile: Blue</br>" +
               "<br>Percentile: Navy</br>" +
               "</html>";
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D graphics = (Graphics2D) g;
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        paintGraph(graphics);
        paintOverlay(graphics);
    }

    private void paintGraph(Graphics2D graphics) {
        int top = getInsets().top;
        double scale = (getHeight() - top) / (maxFrequency - minFrequency);
        int barWidth = 2;
        int xOffset = 3;
        for (int i = 0; i < frequencies.length; i++) {
            double x = (i == 0) ? i : i * xOffset;
            double y = top;
            double height = (frequencies[i] * scale);
            if (frequencies[i] >= 0) {
                y += ((maxFrequency - frequencies[i]) * scale);
            } else {
                y += (maxFrequency * scale);
                height = -height;
            }
            Color color = selectColor(i);
            if (!color.equals(Colors.navy)) {
                y = top;
                height = getHeight();
            }
            fillRectangle(graphics, color, x, y, barWidth, height);
        }
    }

    private Color selectColor(int i) {
        Color color;
        if (medianBin == i) {
            color = Color.green;
        } else if (quartileBins.contains(i)) {
            color = Color.magenta;
        } else if (quintileBins.contains(i)) {
            color = Color.red;
        } else if (decileBins.contains(i)) {
            color = Color.blue;
        } else {
            color = Colors.navy;
        }
        return color;
    }

    private void fillRectangle(Graphics2D graphics, Color color, double x, double y, double width, double height) {
        Rectangle2D rectangle = new Rectangle2D.Double(x, y, width, height);
        graphics.setPaint(color);
        graphics.fill(rectangle);
    }

    private void paintOverlay(Graphics2D graphics) {
        graphics.setComposite(AlphaComposite.SrcOver.derive(0.33f));
        graphics.setPaint(Color.lightGray);
        graphics.fill(overlay);
        graphics.setPaint(Color.darkGray);
        graphics.draw(overlay.getBounds2D());
    }
}