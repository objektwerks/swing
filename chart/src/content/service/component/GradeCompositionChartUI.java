package content.service.component;

import java.awt.image.RenderedImage;
import java.awt.Dimension;

import javax.swing.plaf.ComponentUI;

public abstract class GradeCompositionChartUI extends ComponentUI {
    public static final String UI_CLASS_ID = "GradeCompositionChartUI";

    private Dimension minimumSize;
    private Dimension maximumSize;
    private Dimension preferredSize;

    public GradeCompositionChartUI() {
        super();
        this.minimumSize = new Dimension(625, 450);
        this.maximumSize = new Dimension(625, 450);
        this.preferredSize = new Dimension(625, 450);
    }

    public String getUIClassID() {
        return UI_CLASS_ID;
    }

    public Dimension getMinimumSize() {
        return minimumSize;
    }

    public Dimension getMaximumSize() {
        return maximumSize;
    }

    public Dimension getPreferredSize() {
        return preferredSize;
    }

    public abstract RenderedImage render(GradeCompositionChart chart);
}