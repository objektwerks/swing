package content.service.component;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Font;
import java.awt.Color;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.util.List;

public class Graphics2DUtility {
    private static final Graphics2DUtility instance = new Graphics2DUtility();

    public Graphics2DUtility() {
    }
    
    public static void debugPoint(Graphics2D graphics2D,
                                  Point2D point) {
        double x = point.getX();
        double y = point.getY();
        System.out.println("x: " + x + " y: " + y);
        graphics2D.setFont(new Font("Dialog", Font.BOLD, 13));
        graphics2D.setPaint(Color.red);
        graphics2D.drawString(String.valueOf(Math.round(x) + "/" + Math.round(y)),
                              (float)x,
                              (float)y);
    }

    public static void debugPath(Graphics2D graphics2D,
                                 Shape shape) {
        PathIterator path = shape.getPathIterator(null);
        double [] coordinates = new double [6];
        int segmentType = 0;
        System.out.println("Begin shape.");
        while (!path.isDone()) {
            segmentType = path.currentSegment(coordinates);
            switch (segmentType) {
                case (PathIterator.SEG_CLOSE):
                    System.out.println("\tseg close.");
                    break;
                case (PathIterator.SEG_CUBICTO):
                    System.out.println("\tseg cubic to.");
                    break;
                case (PathIterator.SEG_LINETO):
                    System.out.println("\tseg line to.");
                    break;
                case (PathIterator.SEG_MOVETO):
                    System.out.println("\tseg move to.");
                    break;
                case (PathIterator.SEG_QUADTO):
                    System.out.println("\tseg quad to.");
                    break;
                default:
                    System.out.println("\tNo segment type found.");
            }
            graphics2D.setFont(new Font("Dialog", Font.BOLD, 11));
            graphics2D.setPaint(Color.red);
            for (int i = 0, j = 1; i < coordinates.length; i = i + 2, j = j + 2) {
                graphics2D.drawString(String.valueOf(Math.round(coordinates[i]) + "/" + Math.round(coordinates[j])),
                                      (float)coordinates[i],
                                      (float)coordinates[j]);
                System.out.println("\tx: " + coordinates[i] + " y: " + coordinates[j]);
            }
            path.next();
        }
        System.out.println("End shape.\n");
    }
    
    public static void debugQuadrantGrid(Graphics2D graphics2D,
                                         List<Point2D []> axes) {
        for (Point2D [] points : axes) {
            for (Point2D point : points) {
                graphics2D.setFont(new Font("Dialog", Font.BOLD, 21));
                graphics2D.setPaint(Color.red);
                graphics2D.drawString(".", (float)point.getX(), (float)point.getY());
            }
        }
        
    }
}