package content.service.component;

import java.awt.image.RenderedImage;
import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.io.File;

import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.imageio.ImageIO;

public class GradeCompositionChart extends JComponent {
    private static final String PNG = "png";
    
    private GradeCompositionChartModel model;
    private GradeCompositionChartUI ui;

    public GradeCompositionChart() {
        super();
    }

    public GradeCompositionChart(GradeCompositionChartModel model) {
        this();
        this.model = model;
        this.ui = new BasicGradeCompositionChartUI();
        UIManager.put(ui.getUIClassID(), BasicGradeCompositionChartUI.class.getName());
        updateUI();
    }

    public GradeCompositionChartModel getModel() {
        return model;
    }

    public void setModel(GradeCompositionChartModel model) {
        this.model = model;
    }

    public GradeCompositionChartUI getUI() {
        return ui;
    }

    public void setUI(GradeCompositionChartUI ui) {
        super.setUI(ui);
    }

    public void updateUI() {
        setUI((GradeCompositionChartUI)UIManager.getUI(this));
    }

    public String getUIClassID() {
        return ui.getUIClassID();
    }

    public RenderedImage render() {
        return ui.render(this);
    }

    public byte [] toByteArray(RenderedImage image) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(image, PNG, outputStream);
        return outputStream.toByteArray();
    }

    public void toFile(RenderedImage image,
                       File file) throws IOException {
        ImageIO.write(image, PNG, file);
    }
}