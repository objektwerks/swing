package com.ndr.app.stock.screener.slider;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;

import javax.swing.JComponent;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicSliderUI;

public final class RangeSliderUI extends BasicSliderUI {
    private static final Color darkGray = Color.darkGray;

    private Color rangeColor = Color.lightGray;
    private Rectangle upperThumbRect;
    private boolean upperThumbSelected;
    private boolean isLowerKnobLocked;
    private boolean isUpperKnobLocked;
    private transient boolean lowerDragging;
    private transient boolean upperDragging;

    public RangeSliderUI(RangeSlider rangeSlider) {
        super(rangeSlider);
        isLowerKnobLocked = false;
        isUpperKnobLocked = false;
    }

    public int lowerX() {
        return xPositionForValue(slider.getValue());
    }

    public int upperX() {
        return xPositionForValue(slider.getValue() + slider.getExtent());
    }

    public int toValue(int x) {
        return valueForXPosition(x);   
    }

    public boolean isLowerKnobLocked() {
        return isLowerKnobLocked;
    }

    public void setLowerKnobLocked(boolean isLowerKnobLocked) {
        this.isLowerKnobLocked = isLowerKnobLocked;
    }

    public boolean isUpperKnobLocked() {
        return isUpperKnobLocked;
    }

    public void setUpperKnobLocked(boolean isUpperKnobLocked) {
        this.isUpperKnobLocked = isUpperKnobLocked;
    }

    @Override
    public void installUI(JComponent c) {
        upperThumbRect = new Rectangle();
        super.installUI(c);
    }

    @Override
    protected TrackListener createTrackListener(JSlider slider) {
        return new RangeTrackListener();
    }

    @Override
    protected ChangeListener createChangeListener(JSlider slider) {
        return new ChangeHandler();
    }

    @Override
    protected void calculateThumbSize() {
        super.calculateThumbSize();
        upperThumbRect.setSize(thumbRect.width, thumbRect.height);
    }

    @Override
    protected void calculateThumbLocation() {
        super.calculateThumbLocation();
        if (slider.getSnapToTicks()) {
            int upperValue = slider.getValue() + slider.getExtent();
            int snappedValue = upperValue;
            int majorTickSpacing = slider.getMajorTickSpacing();
            int minorTickSpacing = slider.getMinorTickSpacing();
            int tickSpacing = 0;
            if (minorTickSpacing > 0) {
                tickSpacing = minorTickSpacing;
            } else if (majorTickSpacing > 0) {
                tickSpacing = majorTickSpacing;
            }
            if (tickSpacing != 0) {
                if ((upperValue - slider.getMinimum()) % tickSpacing != 0) {
                    float temp = (float) (upperValue - slider.getMinimum()) / (float) tickSpacing;
                    int whichTick = Math.round(temp);
                    snappedValue = slider.getMinimum() + (whichTick * tickSpacing);
                }
                if (snappedValue != upperValue) {
                    slider.setExtent(snappedValue - slider.getValue());
                }
            }
        }
        if (slider.getOrientation() == JSlider.HORIZONTAL) {
            int upperPosition = xPositionForValue(slider.getValue() + slider.getExtent());
            upperThumbRect.x = upperPosition - (upperThumbRect.width / 2);
            upperThumbRect.y = trackRect.y;

        } else {
            int upperPosition = yPositionForValue(slider.getValue() + slider.getExtent());
            upperThumbRect.x = trackRect.x;
            upperThumbRect.y = upperPosition - (upperThumbRect.height / 2);
        }
    }

    @Override
    protected Dimension getThumbSize() {
        return new Dimension(12, 12);
    }

    @Override
    public void paint(Graphics g, JComponent c) {
        super.paint(g, c);
        Rectangle clipRect = g.getClipBounds();
        if (upperThumbSelected) {
            if (clipRect.intersects(thumbRect)) {
                paintLowerThumb(g);
            }
            if (clipRect.intersects(upperThumbRect)) {
                paintUpperThumb(g);
            }
        } else {
            if (clipRect.intersects(upperThumbRect)) {
                paintUpperThumb(g);
            }
            if (clipRect.intersects(thumbRect)) {
                paintLowerThumb(g);
            }
        }
    }

    @Override
    public void paintTrack(Graphics g) {
        super.paintTrack(g);
        Rectangle trackBounds = trackRect;
        if (slider.getOrientation() == JSlider.HORIZONTAL) {
            int lowerX = thumbRect.x + (thumbRect.width / 2);
            int upperX = upperThumbRect.x + (upperThumbRect.width / 2);
            int cy = (trackBounds.height / 2) - 2;
            Color oldColor = g.getColor();
            g.translate(trackBounds.x, trackBounds.y + cy);
            g.setColor(rangeColor);
            for (int y = 0; y <= 3; y++) {
                g.drawLine(lowerX - trackBounds.x, y, upperX - trackBounds.x, y);
            }
            g.translate(-trackBounds.x, -(trackBounds.y + cy));
            g.setColor(oldColor);
        } else {
            int lowerY = thumbRect.y + (thumbRect.height / 2);
            int upperY = upperThumbRect.y + (upperThumbRect.height / 2);
            int cx = (trackBounds.width / 2) - 2;
            Color oldColor = g.getColor();
            g.translate(trackBounds.x + cx, trackBounds.y);
            g.setColor(rangeColor);
            for (int x = 0; x <= 3; x++) {
                g.drawLine(x, lowerY - trackBounds.y, x, upperY - trackBounds.y);
            }
            g.translate(-(trackBounds.x + cx), -trackBounds.y);
            g.setColor(oldColor);
        }
    }

    @Override
    public void paintThumb(Graphics g) {
    }

    private void paintLowerThumb(Graphics g) {
        Rectangle knobBounds = thumbRect;
        int w = knobBounds.width;
        int h = knobBounds.height;
        Graphics2D g2d = (Graphics2D) g.create();
        Shape thumbShape = createThumbShape(w - 1, h - 1);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.translate(knobBounds.x, knobBounds.y);
        g2d.setColor(Color.white);
        g2d.fill(thumbShape);
        g2d.setColor(darkGray);
        g2d.draw(thumbShape);
        g2d.dispose();
    }

    private void paintUpperThumb(Graphics g) {
        Rectangle knobBounds = upperThumbRect;
        int w = knobBounds.width;
        int h = knobBounds.height;
        Graphics2D g2d = (Graphics2D) g.create();
        Shape thumbShape = createThumbShape(w - 1, h - 1);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.translate(knobBounds.x, knobBounds.y);
        g2d.setColor(darkGray);
        g2d.fill(thumbShape);
        g2d.setColor(darkGray);
        g2d.draw(thumbShape);
        g2d.dispose();
    }

    private Shape createThumbShape(int width, int height) {
        return new Ellipse2D.Double(0, 0, width, height);
    }

    private void setUpperThumbLocation(int x, int y) {
        Rectangle upperUnionRect = new Rectangle();
        upperUnionRect.setBounds(upperThumbRect);
        upperThumbRect.setLocation(x, y);
        SwingUtilities.computeUnion(upperThumbRect.x, upperThumbRect.y, upperThumbRect.width, upperThumbRect.height, upperUnionRect);
        slider.repaint(upperUnionRect.x, upperUnionRect.y, upperUnionRect.width, upperUnionRect.height);
    }

    @Override
    public void scrollByBlock(int direction) {
        int blockIncrement = (slider.getMaximum() - slider.getMinimum()) / 10;
        if (blockIncrement <= 0 && slider.getMaximum() > slider.getMinimum()) {
            blockIncrement = 1;
        }
        int delta = blockIncrement * ((direction > 0) ? POSITIVE_SCROLL : NEGATIVE_SCROLL);
        if (upperThumbSelected) {
            int oldValue = ((RangeSlider) slider).getUpperValue();
            ((RangeSlider) slider).setUpperValue(oldValue + delta);
        } else {
            int oldValue = slider.getValue();
            slider.setValue(oldValue + delta);
        }
    }

    @Override
    public void scrollByUnit(int direction) {
        int delta = ((direction > 0) ? POSITIVE_SCROLL : NEGATIVE_SCROLL);

        if (upperThumbSelected) {
            int oldValue = ((RangeSlider) slider).getUpperValue();
            ((RangeSlider) slider).setUpperValue(oldValue + delta);
        } else {
            int oldValue = slider.getValue();
            slider.setValue(oldValue + delta);
        }
    }

    public class ChangeHandler implements ChangeListener {
        @Override
        public void stateChanged(ChangeEvent event) {
            if (!lowerDragging && !upperDragging) {
                calculateThumbLocation();
                slider.repaint();
            }
        }
    }

    public class RangeTrackListener extends BasicSliderUI.TrackListener {
        @Override
        public void mousePressed(MouseEvent e) {
            if (!slider.isEnabled()) {
                return;
            }
            currentMouseX = e.getX();
            currentMouseY = e.getY();

            if (slider.isRequestFocusEnabled()) {
                slider.requestFocus();
            }
            boolean lowerPressed = false;
            boolean upperPressed = false;
            if (upperThumbSelected) {
                if (upperThumbRect.contains(currentMouseX, currentMouseY)) {
                    upperPressed = true;
                } else if (thumbRect.contains(currentMouseX, currentMouseY)) {
                    lowerPressed = true;
                }
            } else {
                if (thumbRect.contains(currentMouseX, currentMouseY)) {
                    lowerPressed = true;
                } else if (upperThumbRect.contains(currentMouseX, currentMouseY)) {
                    upperPressed = true;
                }
            }
            if (lowerPressed) {
                switch (slider.getOrientation()) {
                    case JSlider.VERTICAL:
                        offset = currentMouseY - thumbRect.y;
                        break;
                    case JSlider.HORIZONTAL:
                        offset = currentMouseX - thumbRect.x;
                        break;
                }
                upperThumbSelected = false;
                lowerDragging = true;
                return;
            }
            lowerDragging = false;
            if (upperPressed) {
                switch (slider.getOrientation()) {
                    case JSlider.VERTICAL:
                        offset = currentMouseY - upperThumbRect.y;
                        break;
                    case JSlider.HORIZONTAL:
                        offset = currentMouseX - upperThumbRect.x;
                        break;
                }
                upperThumbSelected = true;
                upperDragging = true;
                return;
            }
            upperDragging = false;
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            lowerDragging = false;
            upperDragging = false;
            slider.setValueIsAdjusting(false);
            super.mouseReleased(e);
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (!slider.isEnabled()) {
                return;
            }
            currentMouseX = e.getX();
            currentMouseY = e.getY();
            if (lowerDragging) {
                slider.setValueIsAdjusting(true);
                moveLowerThumb();

            } else if (upperDragging) {
                slider.setValueIsAdjusting(true);
                moveUpperThumb();
            }
        }

        @Override
        public boolean shouldScroll(int direction) {
            return false;
        }

        private void moveLowerThumb() {
            if (isLowerKnobLocked) {
                return;
            }
            int thumbMiddle = 0;
            switch (slider.getOrientation()) {
                case JSlider.VERTICAL:
                    int halfThumbHeight = thumbRect.height / 2;
                    int thumbTop = currentMouseY - offset;
                    int trackTop = trackRect.y;
                    int trackBottom = trackRect.y + (trackRect.height - 1);
                    int vMax = yPositionForValue(slider.getValue() + slider.getExtent());
                    if (drawInverted()) {
                        trackBottom = vMax;
                    } else {
                        trackTop = vMax;
                    }
                    thumbTop = Math.max(thumbTop, trackTop - halfThumbHeight);
                    thumbTop = Math.min(thumbTop, trackBottom - halfThumbHeight);
                    setThumbLocation(thumbRect.x, thumbTop);
                    thumbMiddle = thumbTop + halfThumbHeight;
                    slider.setValue(valueForYPosition(thumbMiddle));
                    break;
                case JSlider.HORIZONTAL:
                    int halfThumbWidth = thumbRect.width / 2;
                    int thumbLeft = currentMouseX - offset;
                    int trackLeft = trackRect.x;
                    int trackRight = trackRect.x + (trackRect.width - 1);
                    int hMax = xPositionForValue(slider.getValue() + slider.getExtent());
                    if (drawInverted()) {
                        trackLeft = hMax;
                    } else {
                        trackRight = hMax;
                    }
                    thumbLeft = Math.max(thumbLeft, trackLeft - halfThumbWidth);
                    thumbLeft = Math.min(thumbLeft, trackRight - halfThumbWidth);
                    setThumbLocation(thumbLeft, thumbRect.y);
                    thumbMiddle = thumbLeft + halfThumbWidth;
                    slider.setValue(valueForXPosition(thumbMiddle));
                    break;
                default:
            }
        }

        private void moveUpperThumb() {
            if (isUpperKnobLocked) {
                return;
            }
            int thumbMiddle = 0;
            switch (slider.getOrientation()) {
                case JSlider.VERTICAL:
                    int halfThumbHeight = thumbRect.height / 2;
                    int thumbTop = currentMouseY - offset;
                    int trackTop = trackRect.y;
                    int trackBottom = trackRect.y + (trackRect.height - 1);
                    int vMin = yPositionForValue(slider.getValue());
                    if (drawInverted()) {
                        trackTop = vMin;
                    } else {
                        trackBottom = vMin;
                    }
                    thumbTop = Math.max(thumbTop, trackTop - halfThumbHeight);
                    thumbTop = Math.min(thumbTop, trackBottom - halfThumbHeight);
                    setUpperThumbLocation(thumbRect.x, thumbTop);
                    thumbMiddle = thumbTop + halfThumbHeight;
                    slider.setExtent(valueForYPosition(thumbMiddle) - slider.getValue());
                    break;
                case JSlider.HORIZONTAL:
                    int halfThumbWidth = thumbRect.width / 2;
                    int thumbLeft = currentMouseX - offset;
                    int trackLeft = trackRect.x;
                    int trackRight = trackRect.x + (trackRect.width - 1);
                    int hMin = xPositionForValue(slider.getValue());
                    if (drawInverted()) {
                        trackRight = hMin;
                    } else {
                        trackLeft = hMin;
                    }
                    thumbLeft = Math.max(thumbLeft, trackLeft - halfThumbWidth);
                    thumbLeft = Math.min(thumbLeft, trackRight - halfThumbWidth);
                    setUpperThumbLocation(thumbLeft, thumbRect.y);
                    thumbMiddle = thumbLeft + halfThumbWidth;
                    slider.setExtent(valueForXPosition(thumbMiddle) - slider.getValue());
                    break;
                default:
            }
        }
    }
}