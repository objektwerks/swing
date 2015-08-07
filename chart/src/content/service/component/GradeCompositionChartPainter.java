package content.service.component;

import content.service.report.Grade;
import content.service.report.GradeItem;

import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Shape;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.RenderingHints;
import java.awt.Font;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Arc2D;
import java.awt.geom.GeneralPath;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JComponent;

public class GradeCompositionChartPainter {
    private static final Font compositeGradeFont = new Font("Times New Roman", Font.BOLD, 20);
    private static final Font quadrantGradeFont = new Font("Times New Roman", Font.PLAIN, 9);
    private static final Font quadrantGradeItemFont = new Font("Times New Roman", Font.PLAIN, 10);
    private static final Font gradeItemFont = new Font("Times New Roman", Font.PLAIN, 12);
    private static final Font gradeFont = new Font("Times New Roman", Font.BOLD, 16);
    private static final Font titleFont = new Font("Times New Roman", Font.BOLD, 20);
    private static final Color navy = new Color(25, 25, 112);
    private static final Color quadrantGradeColor = navy;
    private static final Color quadrantGradeItemColor = navy;
    private static final Color quadrantFillColor = Color.lightGray;
    private static final Color quadrantGridColor = Color.white;
    private static final double quadrantExtent = 90;
    private static final double quadrantSector = 18;
    private static final double quadrantLatitude = 31;
    private static final double quadrantLongitude = quadrantLatitude / 2;
    private static final Color centerLineSegmentColor = Color.lightGray;
    private static final float centerLineSegmentLength = 25.0f;
    private static final double quadrantWidth = 300;
    private static final double quadrantHeight = 300;
    private static final float gradeItemOffset = 20.0f;
    private static final float gradeItemMarginOffset = 130.0f;
    private static final int renderedImageBorder = 30;
    private static final String A_PLUS = "A+", A = "A", A_MINUS = "A-", B_PLUS = "B+", B = "B", B_MINUS = "B-", C = "C", D = "D", F = "F";
    private static final String [] grades = new String [] {A_PLUS, A, A_MINUS, B_PLUS, B, B_MINUS, C, D, F};
    private static final Map <String, Integer> gradeToIndexMap = new HashMap <String, Integer> ();
    private static final String title = " Grade Composition";
    private static final String growth = "Growth";
    private static final String profitability = "Profitability";
    private static final String value = "Value";
    private static final String cashFlow = "Cash Flow";

    static {
        for (int i = 0; i < grades.length; i++) {
            gradeToIndexMap.put(grades[i], i);
        }
    }

    public GradeCompositionChartPainter() {
    }

    public RenderedImage render(GradeCompositionChart chart,
                                Dimension preferredSize) {
        int width = (int)preferredSize.getWidth() + renderedImageBorder;
        int height = (int)preferredSize.getHeight() + renderedImageBorder;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = image.createGraphics();
        chart.setBounds(0, 0, width, height);
        paint(graphics2D, chart);
        return image;
    }

    public void paint(Graphics graphics,
                      JComponent component) {
        Graphics2D graphics2D = (Graphics2D)graphics;
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        GradeCompositionChart chart = (GradeCompositionChart)component;
        GradeCompositionChartModel model = chart.getModel();
        Grade growthGrade = model.getGrowthGrade();
        Grade profitabilityGrade = model.getProfitabilityGrade();
        Grade valueGrade = model.getValueGrade();
        Grade cashFlowGrade = model.getCashFlowGrade();
        List <GradeItem> growthGradeItems = growthGrade.listGradeItems();
        List <GradeItem> profitabilityGradeItems = profitabilityGrade.listGradeItems();
        List <GradeItem> valueGradeItems = valueGrade.listGradeItems();
        List <GradeItem> cashFlowGradeItems = cashFlowGrade.listGradeItems();

        Insets insets = component.getInsets();
        double width = component.getWidth() - (insets.left + insets.right);
        double height = component.getHeight() - (insets.top + insets.bottom);

        Rectangle2D canvas = drawCanvas(graphics2D, width, height);
        Point2D center = new Point2D.Double(canvas.getCenterX(), canvas.getCenterY());

        drawGrowthGradeQuadrant(graphics2D, center, growthGradeItems);
        drawProfitabilityGradeQuadrant(graphics2D, center, profitabilityGradeItems);
        drawValueGradeQuadrant(graphics2D, center, valueGradeItems);
        drawCashFlowGradeQuadrant(graphics2D, center, cashFlowGradeItems);

        drawVerticalCenterLine(graphics2D, center);
        drawHorizontalCenterLine(graphics2D, center);

        drawGrowthCompositeGrade(graphics2D, center, growthGrade.getCompositeGrade());
        drawProfitabilityCompositeGrade(graphics2D, center, profitabilityGrade.getCompositeGrade());
        drawValueCompositeGrade(graphics2D, center, valueGrade.getCompositeGrade());
        drawCashFlowCompositeGrade(graphics2D, center, cashFlowGrade.getCompositeGrade());

        drawGrowthGradeItems(graphics2D, center, growthGradeItems);
        drawProfitabilityGradeItems(graphics2D, center, profitabilityGradeItems);
        drawValueGradeItems(graphics2D, (float)width, center, valueGradeItems);
        drawCashFlowGradeItems(graphics2D, (float)width, center, cashFlowGradeItems);

        drawTitle(graphics2D, growthGrade.getContentKey());
    }

    private Rectangle2D drawCanvas(Graphics2D graphics2D,
                                   double width,
                                   double height) {
        double x = 0.0d;
        double y = 0.0d;
        Rectangle2D rectangle = new Rectangle2D.Double(x, y, width, height);
        graphics2D.setPaint(Color.white);
        graphics2D.fill(rectangle);
        graphics2D.draw(rectangle);
        return rectangle;
    }

    private void drawGrowthGradeQuadrant(Graphics2D graphics2D,
                                         Point2D center,
                                         List <GradeItem> gradeItems) {
        double x = center.getX() - 160;
        double y = center.getY() - 155.5;
        double angle = 90;
        fillQuadrant(graphics2D, x, y, angle);
        List <Point2D []> axes = calculateQuadrantGridPoints(x, y, angle);
        fillGradeItemArea(graphics2D, axes, gradeItems, -9.0f, - 1.5f);
        drawQuadrantGrid(graphics2D, x, y, angle);
        drawQuadrantGradeScale(graphics2D, axes.get(0), 5.0f, 0.0f);
        subtractQuadrantApex(graphics2D, x, y, angle, grades.length - 1);
    }

    private void drawProfitabilityGradeQuadrant(Graphics2D graphics2D,
                                                Point2D center,
                                                List <GradeItem> gradeItems) {
        double x = center.getX() - 160;
        double y = center.getY() - 140.5;
        double angle = 180;
        fillQuadrant(graphics2D, x, y, angle);
        List <Point2D []> axes = calculateQuadrantGridPoints(x, y, angle);
        fillGradeItemArea(graphics2D, axes, gradeItems, -5.0f, 9.5f);
        drawQuadrantGrid(graphics2D, x, y, angle);
        drawQuadrantGradeScale(graphics2D, axes.get(0), -3.5f, -2.5f);
        subtractQuadrantApex(graphics2D, x, y, angle, grades.length - 1);
   }

    private void drawValueGradeQuadrant(Graphics2D graphics2D,
                                        Point2D center,
                                        List <GradeItem> gradeItems) {
        double x = center.getX() - 140;
        double y = center.getY() - 155.5;
        double angle = 0;
        fillQuadrant(graphics2D, x, y, angle);
        List <Point2D []> axes = calculateQuadrantGridPoints(x, y, angle);
        Collections.reverse(axes);
        fillGradeItemArea(graphics2D, axes, gradeItems, 1.5f, - 1.5f);
        drawQuadrantGrid(graphics2D, x, y, angle);
        Collections.reverse(axes);
        drawQuadrantGradeScale(graphics2D, axes.get(0), 0.0f, 10.0f);
        subtractQuadrantApex(graphics2D, x, y, angle, grades.length - 1);
    }

    private void drawCashFlowGradeQuadrant(Graphics2D graphics2D,
                                           Point2D center,
                                           List <GradeItem> gradeItems) {
        double x = center.getX() - 140;
        double y = center.getY() - 140.5;
        double angle = 270;
        fillQuadrant(graphics2D, x, y, angle);
        List <Point2D []> axes = calculateQuadrantGridPoints(x, y, angle);
        Collections.reverse(axes);
        fillGradeItemArea(graphics2D, axes, gradeItems, 3.5f, 8.5f);
        drawQuadrantGrid(graphics2D, x, y, angle);
        Collections.reverse(axes);
        drawQuadrantGradeScale(graphics2D, axes.get(0), -12.5f, 8.5f);
        subtractQuadrantApex(graphics2D, x, y, angle, grades.length - 1);
    }

    private void drawVerticalCenterLine(Graphics2D graphics2D,
                                        Point2D center) {
        double centerX = center.getX();
        double centerY = center.getY();
        double startX = centerX;
        double startY = centerY - centerLineSegmentLength;
        double endX = centerX;
        double endY = centerY + centerLineSegmentLength;
        Shape line = new Line2D.Double(startX, startY, endX, endY);
        graphics2D.setPaint(centerLineSegmentColor);
        graphics2D.draw(line);
    }

    private void drawHorizontalCenterLine(Graphics2D graphics2D,
                                          Point2D center) {
        double centerX = center.getX();
        double centerY = center.getY();
        double startX = centerX - centerLineSegmentLength;
        double startY = centerY;
        double endX = centerX + centerLineSegmentLength;
        double endY = centerY;
        Shape line = new Line2D.Double(startX, startY, endX, endY);
        graphics2D.setPaint(centerLineSegmentColor);
        graphics2D.draw(line);
    }

    private void drawGrowthCompositeGrade(Graphics2D graphics2D,
                                          Point2D center,
                                          String growthCompositeGrade) {
        graphics2D.setFont(compositeGradeFont);
        graphics2D.setPaint(navy);
        float x = (float)center.getX() - 25.0f;
        float y = (float)center.getY() - 5.0f;
        graphics2D.drawString(growthCompositeGrade, x, y);
    }

    private void drawProfitabilityCompositeGrade(Graphics2D graphics2D,
                                                 Point2D center,
                                                 String profitabilityCompositeGrade) {
        graphics2D.setFont(compositeGradeFont);
        graphics2D.setPaint(navy);
        float x = (float)center.getX() - 25.0f;
        float y = (float)center.getY() + 20.0f;
        graphics2D.drawString(profitabilityCompositeGrade, x, y);
    }

    private void drawValueCompositeGrade(Graphics2D graphics2D,
                                         Point2D center,
                                         String valueCompositeGrade) {
        graphics2D.setFont(compositeGradeFont);
        graphics2D.setPaint(navy);
        float x = (float)center.getX() + 5.0f;
        float y = (float)center.getY() - 5.0f;
        graphics2D.drawString(valueCompositeGrade, x, y);
    }

    private void drawCashFlowCompositeGrade(Graphics2D graphics2D,
                                            Point2D center,
                                            String cashFlowCompositeGrade) {
        graphics2D.setFont(compositeGradeFont);
        graphics2D.setPaint(navy);
        float x = (float)center.getX() + 5.0f;
        float y = (float)center.getY() + 20.0f;
        graphics2D.drawString(cashFlowCompositeGrade, x, y);
    }

    private void drawGrowthGradeItems(Graphics2D graphics2D,
                                      Point2D center,
                                      List <GradeItem> gradeItems) {
        graphics2D.setPaint(navy);
        float x = gradeItemMarginOffset;
        float y = (float)center.getY();
        int i = gradeItems.size() + 1;
        int j = 0;
        TextLayout textLayout = new TextLayout(growth, gradeFont, graphics2D.getFontRenderContext());
        textLayout.draw(graphics2D, x - textLayout.getVisibleAdvance(), y - (i * gradeItemOffset));
        for (GradeItem gradeItem : gradeItems) {
            i--;
            j++;
            textLayout = new TextLayout(gradeItem.getName() + " (" + (j) + ")", gradeItemFont, graphics2D.getFontRenderContext());
            textLayout.draw(graphics2D, x - textLayout.getVisibleAdvance(), y - (i * gradeItemOffset));
        }
    }

    private void drawProfitabilityGradeItems(Graphics2D graphics2D,
                                             Point2D center,
                                             List <GradeItem> gradeItems) {
        graphics2D.setPaint(navy);
        float x = gradeItemMarginOffset;
        float y = (float)center.getY() + (gradeItemOffset / 2);
        int i = 1;
        int j = 0;
        TextLayout textLayout = new TextLayout(profitability, gradeFont, graphics2D.getFontRenderContext());
        textLayout.draw(graphics2D, x - textLayout.getVisibleAdvance(), y + (i * gradeItemOffset));
        for (GradeItem gradeItem : gradeItems) {
            i++;
            j++;
            textLayout = new TextLayout(gradeItem.getName() + " (" + (j) + ")", gradeItemFont, graphics2D.getFontRenderContext());
            textLayout.draw(graphics2D, x - textLayout.getVisibleAdvance(), y + (i * gradeItemOffset));
        }
    }

    private void drawValueGradeItems(Graphics2D graphics2D,
                                     float width,
                                     Point2D center,
                                     List <GradeItem> gradeItems) {
        graphics2D.setPaint(navy);
        float x = width - gradeItemMarginOffset;
        float y = (float)center.getY();
        int i = gradeItems.size() + 1;
        int j = 0;
        TextLayout textLayout = new TextLayout(value, gradeFont, graphics2D.getFontRenderContext());
        textLayout.draw(graphics2D, x + (textLayout.getVisibleAdvance() - textLayout.getAdvance()), y - (i * gradeItemOffset));
        for (GradeItem gradeItem : gradeItems) {
            i--;
            j++;
            textLayout = new TextLayout("(" + (j) + ") " + gradeItem.getName(), gradeItemFont, graphics2D.getFontRenderContext());
            textLayout.draw(graphics2D, x + (textLayout.getVisibleAdvance() - textLayout.getAdvance()), y - (i * gradeItemOffset));
        }
    }

    private void drawCashFlowGradeItems(Graphics2D graphics2D,
                                        float width,
                                        Point2D center,
                                        List <GradeItem> gradeItems) {
        graphics2D.setPaint(navy);
        float x = width - gradeItemMarginOffset;
        float y = (float)center.getY() + (gradeItemOffset / 2);;
        int i = 1;
        int j = 0;
        TextLayout textLayout = new TextLayout(cashFlow, gradeFont, graphics2D.getFontRenderContext());
        textLayout.draw(graphics2D, x + (textLayout.getVisibleAdvance() - textLayout.getAdvance()), y + (i * gradeItemOffset));
        for (GradeItem gradeItem : gradeItems) {
            i++;
            j++;
            textLayout = new TextLayout("(" + (j) + ") " + gradeItem.getName(), gradeItemFont, graphics2D.getFontRenderContext());
            textLayout.draw(graphics2D, x + (textLayout.getVisibleAdvance() - textLayout.getAdvance()), y + (i * gradeItemOffset));
        }
    }

    private void drawTitle(Graphics2D graphics2D,
                           String contentKey) {
        graphics2D.setFont(titleFont);
        graphics2D.setPaint(navy);
        graphics2D.drawString(contentKey.toUpperCase() + title, 10.0f, 25.0f);
    }
    
    private void fillQuadrant(Graphics2D graphics2D,
                              double x,
                              double y,
                              double angle) {
        Arc2D quadrant = new Arc2D.Double(x,
                                          y,
                                          quadrantWidth,
                                          quadrantHeight,
                                          angle,
                                          quadrantExtent,
                                          Arc2D.PIE);
        graphics2D.setPaint(quadrantFillColor);
        graphics2D.fill(quadrant);        
    }

    private List <Point2D []> calculateQuadrantGridPoints(double x,
                                                          double y,
                                                          double angle) {
        int numberOfAxes = 6;
        List <Point2D []> axes = new ArrayList <Point2D []> (numberOfAxes);
        Arc2D longitudeArc = null;
        Arc2D latitudeArc = null;
        for (int i = 1; i < numberOfAxes + 1; i++) {
            longitudeArc = new Arc2D.Double(x,
                                            y,
                                            quadrantWidth,
                                            quadrantHeight,
                                            (angle - quadrantSector) + (quadrantSector * i),
                                            quadrantSector,
                                            Arc2D.PIE);
            Point2D [] points = new Point2D [grades.length];
            points[0] = new Point2D.Double(longitudeArc.getStartPoint().getX(), longitudeArc.getStartPoint().getY());
            for (int j = 1; j < grades.length; j++) {
                latitudeArc = new Arc2D.Double(x + (j * quadrantLongitude),
                                               y + (j * quadrantLongitude),
                                               quadrantWidth - (j * quadrantLatitude),
                                               quadrantHeight - (j * quadrantLatitude),
                                               (angle - quadrantSector) + (quadrantSector * i),
                                               quadrantSector,
                                               Arc2D.OPEN);
                points[j] = new Point2D.Double(latitudeArc.getStartPoint().getX(), latitudeArc.getStartPoint().getY());
            }
            axes.add(points);
        }
        return axes;
    }
    
    private void fillGradeItemArea(Graphics2D graphics2D,
                                   List <Point2D []> axes,
                                   List <GradeItem> gradeItems,
                                   float xGradeOffset,
                                   float yGradeOffset) {
        Point2D [] points = null;
        GeneralPath gradeItemArea = new GeneralPath();
        points = axes.get(0);
        gradeItemArea.moveTo((float)points[points.length - 1].getX(), (float)points[points.length - 1].getY());
        GradeItem gradeItem = null;
        int index = 0;
        System.out.println("*********");
        for (int i = 0; i < gradeItems.size(); i++) {
            gradeItem = gradeItems.get(i);
            points = axes.get(i);
            index = gradeToIndexMap.get(gradeItem.getGrade());
            System.out.println("index: " + index + " item: " + gradeItem.getName() + " grade: " + gradeItem.getGrade());
            gradeItemArea.lineTo((float)points[index].getX(), (float)points[index].getY());
        }
        points = axes.get(axes.size() - 1);
        gradeItemArea.lineTo((float)points[points.length - 1].getX(), (float)points[points.length - 1].getY());
        graphics2D.setPaint(navy);
        graphics2D.fill(gradeItemArea);

        graphics2D.setFont(quadrantGradeItemFont);
        graphics2D.setPaint(quadrantGradeItemColor);
        for (int i = 0; i < gradeItems.size(); i++) {
            gradeItem = gradeItems.get(i);
            points = axes.get(i);
            index = gradeToIndexMap.get(gradeItem.getGrade());
            graphics2D.drawString(String.valueOf(i + 1),
                                 (float)points[0].getX() + xGradeOffset,
                                 (float)points[0].getY() + yGradeOffset);
        }        
    }

    private void drawQuadrantGrid(Graphics2D graphics2D,
                                  double x,
                                  double y,
                                  double angle) {
        Arc2D arc = null;
        for (int i = 1; i < grades.length; i++) {
            arc = new Arc2D.Double(x + (i * quadrantLongitude),
                                   y + (i * quadrantLongitude),
                                   quadrantWidth - (i * quadrantLatitude),
                                   quadrantHeight - (i * quadrantLatitude),
                                   angle,
                                   quadrantExtent,
                                   Arc2D.PIE);
            graphics2D.setPaint(quadrantGridColor);
            graphics2D.draw(arc);
        }
        arc = new Arc2D.Double(x, y, quadrantWidth, quadrantHeight, angle, quadrantSector, Arc2D.PIE);
        graphics2D.setPaint(quadrantGridColor);
        graphics2D.draw(arc);
        for (int i = 1; i < 5; i++) {
            arc = new Arc2D.Double(x, y, quadrantWidth, quadrantHeight, angle + (quadrantSector * i), quadrantSector, Arc2D.PIE);
            graphics2D.setPaint(quadrantGridColor);
            graphics2D.draw(arc);
        }
    }
    
    private void drawQuadrantGradeScale(Graphics2D graphics2D,
                                        Point2D [] points,
                                        float xOffset,
                                        float yOffset) {
        graphics2D.setFont(quadrantGradeFont);
        graphics2D.setPaint(quadrantGradeColor);
        int i = 0;
        for (Point2D point : points) {
            graphics2D.drawString(grades[i], (float)point.getX() + xOffset, (float)point.getY() + yOffset);
            i++;
        }

    }

    private void subtractQuadrantApex(Graphics2D graphics2D,
                                      double x,
                                      double y,
                                      double angle,
                                      int multiplier) {
        Arc2D apex = new Arc2D.Double(x + (multiplier * quadrantLongitude),
                                      y + (multiplier * quadrantLongitude),
                                      quadrantWidth - (multiplier * quadrantLatitude),
                                      quadrantHeight - (multiplier * quadrantLatitude),
                                      angle,
                                      quadrantExtent,
                                      Arc2D.PIE);
        graphics2D.setPaint(quadrantGridColor);
        graphics2D.fill(apex);
    }
}