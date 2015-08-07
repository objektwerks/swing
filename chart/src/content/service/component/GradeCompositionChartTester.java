package content.service.component;

import content.service.component.GradeCompositionChart;

import java.awt.BorderLayout;
import java.awt.Color;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.BorderFactory;

public class GradeCompositionChartTester {
    private static final String TITLE = "Grade Composition Chart Tester";

    private JFrame frame;

    public GradeCompositionChartTester(JComponent component) {
        frame = build(component);
    }

    public static void main(String [] args) throws Exception {
        final GradeCompositionChart chart = TestGradeCompositionChartFactory.newInstance();
        final GradeCompositionChartTester tester = new GradeCompositionChartTester(chart);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                tester.show();
            }
        });
    }

    private JFrame build(JComponent component) {
        JFrame frame = new JFrame(TITLE);
        frame.setSize(650, 475);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        component.setBorder(BorderFactory.createLineBorder(Color.lightGray));
        frame.add(component, BorderLayout.CENTER);
        return frame;
    }

    public void show() {
        frame.setVisible(true);
    }
}