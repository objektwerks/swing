package content.service.component;

import content.service.component.GradeCompositionChart;

import java.awt.image.RenderedImage;
import java.io.File;

import com.cete.dynamicpdf.Document;
import com.cete.dynamicpdf.Page;
import com.cete.dynamicpdf.pageelements.Image;

import junit.framework.TestCase;
import junit.textui.TestRunner;

public class GradeCompositionChartTest extends TestCase {
    private Timer timer;

    public GradeCompositionChartTest(String name) throws Exception {
        super(name);
        timer = new Timer();
    }

    public static void main(String [] args) {
        TestRunner.main(new String [] {GradeCompositionChartTest.class.getName()});
    }

    public void testChart() throws Exception {
        timer.start();
        GradeCompositionChart chart = TestGradeCompositionChartFactory.newInstance();
        timer.printTimeSeconds("GradeCompositionChart create time: ");
        assertNotNull("Chart is null.", chart);

        timer.start();
        RenderedImage renderedImage = chart.render();
        timer.printTimeSeconds("GradeCompositionChart render image time: ");
        assertNotNull("Rendered image of chart is null.", renderedImage);

        timer.start();
        byte [] bytes = chart.toByteArray(renderedImage);
        timer.printTimeSeconds("GradeCompositionChart rendered image to byte array time: ");
        assertNotNull("Byte array of rendered image is null.", bytes);
        assertTrue("Byte array of rendered image is zero length.", bytes.length > 0);

        timer.start();
        File file = new File("zzz.chart.png");
        chart.toFile(renderedImage, file);
        timer.printTimeSeconds("GradeCompositionChart to file time: ");
        assertTrue("PNG file was not created.", file.exists());

        timer.start();
        Document document = new Document();
        Page page1 = new Page();
        document.getPages().add(page1);
        Image image1 = new Image(bytes, 0, 0, 0.75f);
        page1.getElements().add(image1);
        Page page2 = new Page();
        document.getPages().add(page2);
        Image image2 = new Image(file.toString(), 0, 0, 0.75f);
        page2.getElements().add(image2);
        document.draw("zzz.report.pdf");
        timer.printTimeSeconds("GradeCompositionChart to pdf time: ");

        GradeCompositionChart chartUTX = TestGradeCompositionChartFactory.newUTXInstance();
        File utxFile = new File("zzz.utx.png");
        chartUTX.toFile(chartUTX.render(), utxFile);
        assertTrue("UTX png file was not created.", utxFile.exists());

        GradeCompositionChart chartTAP = TestGradeCompositionChartFactory.newTAPInstance();
        File tapFile = new File("zzz.tap.png");
        chartTAP.toFile(chartTAP.render(), tapFile);
        assertTrue("TAP png file was not created.", tapFile.exists());

        GradeCompositionChart chartWIND = TestGradeCompositionChartFactory.newWINDInstance();
        File windFile = new File("zzz.wind.png");
        chartWIND.toFile(chartWIND.render(), windFile);
        assertTrue("WIND png file was not created.", windFile.exists());

        GradeCompositionChart chartDIOD = TestGradeCompositionChartFactory.newDIODInstance();
        File diodFile = new File("zzz.diod.png");
        chartDIOD.toFile(chartDIOD.render(), diodFile);
        assertTrue("DIOD png file was not created.", diodFile.exists());
	}
}