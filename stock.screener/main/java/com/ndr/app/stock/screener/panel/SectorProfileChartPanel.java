package com.ndr.app.stock.screener.panel;

import com.ndr.app.stock.screener.ApplicationProxy;
import com.ndr.app.stock.screener.resource.ResourceManager;
import com.ndr.model.stock.screener.DateNumberPoint;
import com.ndr.model.stock.screener.TimeSeriesType;

import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRendererState;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public final class SectorProfileChartPanel extends JPanel {
    private static final long serialVersionUID = 4097690334935095530L;
    
    @Autowired private ResourceManager resourceManager;
    private final SectorProfileLabelGenerator sectorProfileLabelGenerator;
    private final SectorProfileCategoryToolTipGenerator sectorProfileCategoryToolTipGenerator;
    private final SectorProfileBarRenderer sectorProfileBarRenderer;
    private Map<Integer, Map<String, Double>> rowToSubIndustriesMap;
    private JFreeChart chart;

    public SectorProfileChartPanel() {
        sectorProfileLabelGenerator = new SectorProfileLabelGenerator();
        sectorProfileCategoryToolTipGenerator = new SectorProfileCategoryToolTipGenerator();
        sectorProfileBarRenderer = new SectorProfileBarRenderer();
    }

    public void setModel(Date currentRebalanceFrequencyDate,
                         Map<TimeSeriesType, List<DateNumberPoint>> dateNumberPoints,
                         Map<Integer, List<DateNumberPoint>> subIndustryPoints) {
        removeAll();
        rebuild(currentRebalanceFrequencyDate, dateNumberPoints, subIndustryPoints);
        validate();
        repaint();
    }

    public void reset() {
        removeAll();
    }

    public JFreeChart getChart() {
        return chart;
    }

    @PostConstruct
    protected void build() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    }

    private void rebuild(Date currentRebalanceFrequencyDate,
                         Map<TimeSeriesType, List<DateNumberPoint>> dateNumberPoints,
                         Map<Integer, List<DateNumberPoint>> subIndustryPoints) {
        add(buildChartPanel(currentRebalanceFrequencyDate, dateNumberPoints));
        rowToSubIndustriesMap = buildRowToSubIndustriesMap(currentRebalanceFrequencyDate, subIndustryPoints, ApplicationProxy.instance.getSubIndustries());
    }

    private ChartPanel buildChartPanel(Date currentRebalanceFrequencyDate,
                                       Map<TimeSeriesType, List<DateNumberPoint>> dateNumberPoints) {
        ChartPanel chartPanel = new ChartPanel(buildChart(buildDataset(currentRebalanceFrequencyDate, dateNumberPoints)));
        chartPanel.setDomainZoomable(false);
        chartPanel.setRangeZoomable(false);
        chartPanel.setPopupMenu(null);
        return chartPanel;
    }

    private JFreeChart buildChart(CategoryDataset categoryDataset) {
        chart = ChartFactory.createBarChart("", null, null, categoryDataset, PlotOrientation.VERTICAL, false, true, false);
        CategoryPlot categoryPlot = (CategoryPlot) chart.getPlot();
        configureCategoryPlot(categoryPlot);
        BarRenderer barRenderer = (BarRenderer) categoryPlot.getRenderer();
        configureBarRenderer(barRenderer);
        return chart;
    }

    private CategoryDataset buildDataset(Date currentRebalanceFrequencyDate, Map<TimeSeriesType, List<DateNumberPoint>> dateNumberPoints) {
        DefaultCategoryDataset categoryDataset = new DefaultCategoryDataset();
        int row = 0;
        for (TimeSeriesType timeSeriesType : DateNumberPoint.sectorTimeSeriesTypes) {
            double number = DateNumberPoint.getNumberByDateAndTimeSeriesType(currentRebalanceFrequencyDate, dateNumberPoints, timeSeriesType);
            row++;
            String timeSeries = resourceManager.getString(timeSeriesType.toString());
            categoryDataset.addValue(number, String.valueOf(row), timeSeries);
        }
        return categoryDataset;
    }

    private void configureCategoryPlot(CategoryPlot categoryPlot) {
        categoryPlot.setRenderer(sectorProfileBarRenderer);
        categoryPlot.setBackgroundPaint(Color.white);
        CategoryAxis categoryAxis = categoryPlot.getDomainAxis();
        categoryAxis.setLowerMargin(0.06);
        categoryAxis.setUpperMargin(0.06);
        categoryAxis.setCategoryMargin(0.18);
        categoryAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);
        ValueAxis valueAxis = categoryPlot.getRangeAxis();
        valueAxis.setUpperMargin(0.3);
    }

    private void configureBarRenderer(BarRenderer barRenderer) {
        barRenderer.setBarPainter(new StandardBarPainter());
        barRenderer.setSeriesPaint(0, Color.black);
        barRenderer.setSeriesPaint(1, Color.blue);
        barRenderer.setSeriesPaint(2, Color.cyan);
        barRenderer.setSeriesPaint(3, Color.darkGray);
        barRenderer.setSeriesPaint(4, Color.gray);
        barRenderer.setSeriesPaint(5, Color.green);
        barRenderer.setSeriesPaint(6, Color.lightGray);
        barRenderer.setSeriesPaint(7, Color.magenta);
        barRenderer.setSeriesPaint(8, Color.orange);
        barRenderer.setSeriesPaint(9, Color.red);
        barRenderer.setDrawBarOutline(false);
        barRenderer.setShadowVisible(false);
        barRenderer.setBaseItemLabelGenerator(sectorProfileLabelGenerator);
        barRenderer.setBaseItemLabelsVisible(true);
    }

    private Map<Integer, Map<String, Double>> buildRowToSubIndustriesMap(Date currentRebalanceFrequencyDate,
                                                                         Map<Integer, List<DateNumberPoint>> subIndustryPoints,
                                                                         Map<Integer, String> subIndustries) {
        Map<Integer, Map<String, Double>> rowToSubIndustriesMap = new HashMap<Integer, Map<String, Double>>();
        rowToSubIndustriesMap.put(1, buildRowToSubIndustriesMap(currentRebalanceFrequencyDate, subIndustryPoints, subIndustries, "10"));
        rowToSubIndustriesMap.put(2, buildRowToSubIndustriesMap(currentRebalanceFrequencyDate, subIndustryPoints, subIndustries, "15"));
        rowToSubIndustriesMap.put(3, buildRowToSubIndustriesMap(currentRebalanceFrequencyDate, subIndustryPoints, subIndustries, "20"));
        rowToSubIndustriesMap.put(4, buildRowToSubIndustriesMap(currentRebalanceFrequencyDate, subIndustryPoints, subIndustries, "25"));
        rowToSubIndustriesMap.put(5, buildRowToSubIndustriesMap(currentRebalanceFrequencyDate, subIndustryPoints, subIndustries, "30"));
        rowToSubIndustriesMap.put(6, buildRowToSubIndustriesMap(currentRebalanceFrequencyDate, subIndustryPoints, subIndustries, "35"));
        rowToSubIndustriesMap.put(7, buildRowToSubIndustriesMap(currentRebalanceFrequencyDate, subIndustryPoints, subIndustries, "40"));
        rowToSubIndustriesMap.put(8, buildRowToSubIndustriesMap(currentRebalanceFrequencyDate, subIndustryPoints, subIndustries, "45"));
        rowToSubIndustriesMap.put(9, buildRowToSubIndustriesMap(currentRebalanceFrequencyDate, subIndustryPoints, subIndustries, "50"));
        rowToSubIndustriesMap.put(10, buildRowToSubIndustriesMap(currentRebalanceFrequencyDate, subIndustryPoints, subIndustries, "55"));
        return rowToSubIndustriesMap;
    }

    private Map<String, Double> buildRowToSubIndustriesMap(Date currentRebalanceFrequencyDate,
                                                           Map<Integer, List<DateNumberPoint>> subIndustryPoints,
                                                           Map<Integer, String> subIndustries,
                                                           String sectorCode) {
        List<Map.Entry<String, Double>> entryList = new ArrayList<Map.Entry<String, Double>>(subIndustryPoints.size());
        for (Map.Entry<Integer, List<DateNumberPoint>> entry : subIndustryPoints.entrySet()) {
            String key = entry.getKey().toString();
            if (key.startsWith(sectorCode)) {
                List<DateNumberPoint> dateNumberPoints = entry.getValue();
                Double value = null;
                for (DateNumberPoint dateNumberPoint : dateNumberPoints) {
                    if (currentRebalanceFrequencyDate.equals(dateNumberPoint.getDate())) {
                        value = dateNumberPoint.getNumber();
                        break;
                    }
                }
                if (value != null && value > 0.0) {
                    String name = subIndustries.get(entry.getKey());
                    entryList.add(new SimpleImmutableEntry<String, Double>(name, value));
                }
            }
        }
        Collections.sort(entryList, EntryComparator.instance);
        Map<String, Double> rowToSubIndustriesMap = new LinkedHashMap<String, Double>(entryList.size());
        for (Map.Entry<String, Double> entry : entryList) {
            rowToSubIndustriesMap.put(entry.getKey(), entry.getValue());
        }
        return rowToSubIndustriesMap;
    }

    private static class EntryComparator implements Comparator<Map.Entry<String, Double>>, Serializable {
        private static final long serialVersionUID = -772763109617723128L;
        public static final EntryComparator instance = new EntryComparator();

        public int compare(Entry<String, Double> entry, Entry<String, Double> otherEntry) {
            int returnValue = 0;
            if (entry == null && otherEntry != null) {
                returnValue = -1;
            } else if (entry != null && otherEntry == null) {
                returnValue = 1;
            } else if (entry != null && otherEntry != null) {
                if (entry.getValue() == null && otherEntry.getValue() != null) {
                    returnValue = -1;
                } else if (entry.getValue() != null && otherEntry.getValue() == null) {
                    returnValue = 1;
                } else if (entry.getValue() != null && otherEntry.getValue() != null) { // Backward for decending sort
                    returnValue = otherEntry.getValue().compareTo(entry.getValue());
                }
            }
            return returnValue;
        }
    }

    private class SectorProfileLabelGenerator extends StandardCategoryItemLabelGenerator {
        private static final long serialVersionUID = -1363102584770669920L;

        @Override
        protected String generateLabelString(CategoryDataset dataset, int row, int column) {
            Number value = dataset.getValue(row, column);
            DecimalFormat decimalFormatter = new DecimalFormat("##0.0");
            return decimalFormatter.format(value.doubleValue());
        }
    }

    private class SectorProfileCategoryToolTipGenerator extends StandardCategoryToolTipGenerator {
        private static final long serialVersionUID = -8316304082174323096L;

        @Override
        public String generateToolTip(CategoryDataset dataset, int row, int column) {
            Map<String, Double> subIndustries = rowToSubIndustriesMap.get(row + 1);
            StringBuilder builder = new StringBuilder();
            builder.append("<html><h3>");
            builder.append(dataset.getColumnKey(column));
            builder.append("</h3><table border=\"0\" cellspacing=\"0\" cellpadding=\"0\">");
            for (Map.Entry<String, Double> entry : subIndustries.entrySet()) {
                builder.append(String.format("<tr><td align=\"right\">%.2f%%</td><td>&nbsp;- %s</td></tr>", entry.getValue(), entry.getKey()));
            }
            builder.append("</table></html>");
            return builder.toString();
        }
    }

    private class SectorProfileBarRenderer extends BarRenderer {
        private static final long serialVersionUID = -6429494839588836054L;

        private SectorProfileBarRenderer() {
            setBaseToolTipGenerator(sectorProfileCategoryToolTipGenerator);
        }

        @Override
        protected void calculateBarWidth(CategoryPlot plot, Rectangle2D dataArea, int rendererIndex, CategoryItemRendererState state) {
            CategoryAxis domainAxis = getDomainAxis(plot, rendererIndex);
            double usedArea = dataArea.getWidth() * (1 - domainAxis.getLowerMargin() - domainAxis.getUpperMargin() - domainAxis.getCategoryMargin());
            double barWidth = usedArea / plot.getDataset(rendererIndex).getColumnCount();
            state.setBarWidth(barWidth);
        }
    }
}