package content.service.component;

import java.awt.Graphics;
import java.awt.image.RenderedImage;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;

public class BasicGradeCompositionChartUI extends GradeCompositionChartUI {
    private GradeCompositionChartPainter painter;

    public BasicGradeCompositionChartUI() {
        super();
        this.painter = new GradeCompositionChartPainter();
    }

    public static ComponentUI createUI(JComponent component) {
        return new BasicGradeCompositionChartUI();
    }

    public void installUI(JComponent component) {
    }

    public void uninstallUI(JComponent component) {
    }

    public void paint(Graphics graphics,
                      JComponent component) {
        painter.paint(graphics, component);
    }

    public RenderedImage render(GradeCompositionChart chart) {
        return painter.render(chart, super.getPreferredSize());
    }
}