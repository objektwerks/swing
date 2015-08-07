package com.ndr.app.stock.screener.panel;

import com.ndr.app.stock.screener.Colors;
import com.ndr.app.stock.screener.action.ListTimeSeriesRowsAction;
import com.ndr.app.stock.screener.button.ButtonSizer;
import com.ndr.app.stock.screener.listener.ReturnsSummaryListener;
import com.ndr.app.stock.screener.resource.ResourceManager;
import com.ndr.model.stock.screener.DateCalculator;
import com.ndr.model.stock.screener.DateNumberPoint;
import com.ndr.model.stock.screener.RebalanceFrequencyDate;
import com.ndr.model.stock.screener.TimeSeriesModel;
import com.ndr.model.stock.screener.TimeSeriesType;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.annotation.PostConstruct;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.LegendItem;
import org.jfree.chart.LegendItemCollection;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.LogAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.event.ChartProgressEvent;
import org.jfree.chart.event.ChartProgressListener;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.renderer.xy.XYAreaRenderer2;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimePeriod;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.TimeSeriesDataItem;
import org.jfree.data.time.TimeTableXYDataset;
import org.jfree.data.xy.XYDataset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public final class TimeSeriesChartPanel extends JPanel implements ChangeListener, ChartProgressListener {
	private static final long serialVersionUID = 2176114701969491998L;
	private static final EnumSet<TimeSeriesType> timeSeriesTypes = EnumSet.of(TimeSeriesType.benchmark, TimeSeriesType.portfolio);
	private static final EnumSet<TimeSeriesType> totalReturnTimeSeriesTypes = EnumSet.of(TimeSeriesType.benchmarkTotalReturn, TimeSeriesType.portfolioTotalReturn);
	private static final EnumSet<TimeSeriesType> memberCountTimeSeriesTypes = EnumSet.of(TimeSeriesType.memberCount);
    private static final EnumSet<TimeSeriesType> smallMidLargeAreaTimeSeriesTypes = EnumSet.of(TimeSeriesType.smallArea, TimeSeriesType.midArea, TimeSeriesType.largeArea);
    private static final EnumSet<TimeSeriesType> valueCoreGrowthAreaTimeSeriesTypes = EnumSet.of(TimeSeriesType.valueArea, TimeSeriesType.coreArea, TimeSeriesType.growthArea);
    public static final String benchmarkPortfolio = "Benchmark Portfolio";
    public static final String members = "Members";
    public static final String smallMidLargeCap = "Small Mid Large Cap";
    public static final String valueCoreGrowth = "Value Core Growth";

	@Autowired private ResourceManager resourceManager;
    @Autowired private ReturnsSummaryPanel returnsSummaryPanel;
    @Autowired private ReturnsSummaryPanel excessReturnsSummaryPanel;
	@Autowired private TimeSeriesChartStatisticsPanel timeSeriesChartStatisticsPanel;
	@Autowired private ListTimeSeriesRowsAction listTimeSeriesRowsAction;
	private JLabel statisticsLabel;
	private JSlider slider;
	private TimeSeriesModel timeSeriesModel;
	private List<Short> sliderDateValues;
    private List<XYPlot> xyPlots;
    private SortedMap<String, JFreeChart> charts;

	public void rebuild(TimeSeriesModel timeSeriesModel) {
        this.timeSeriesModel = timeSeriesModel;
        xyPlots.clear();
        charts.clear();
		removeAll();
        returnsSummaryPanel.rebuild(timeSeriesModel, false);
        excessReturnsSummaryPanel.rebuild(timeSeriesModel, true);
        statisticsLabel = buildStatisticsLabel();
        sliderDateValues = toDateValues(timeSeriesModel.getRebalanceFrequencyDates());
        slider = buildSlider(timeSeriesModel.getRebalanceFrequencyDates(), sliderDateValues);
		add(buildChartsTabbedPane(timeSeriesModel));
		add(buildSliderPanel(slider));
        add(buildStatisticsLabelPanel(statisticsLabel));
		updateStatistics(0);
		validate();
		repaint();
        timeSeriesChartStatisticsPanel.rebuild(timeSeriesModel);
	}

	public void reset() {
        timeSeriesChartStatisticsPanel.reset();
		removeAll();
		validate();
		repaint();
	}

	public List<Integer> listIssueIds(Date date) {
		return TimeSeriesModel.listIssueIds(timeSeriesModel.getMembershipSet(), date);
	}

    public SortedMap<String, JFreeChart> getCharts() {
        return charts;
    }

    public TimeSeriesChartStatisticsPanel getTimeSeriesChartStatisticsPanel() {
        return timeSeriesChartStatisticsPanel;
    }

    public void setSliderValue(short value) {
        slider.setValue(value);
    }

    @PostConstruct
	protected void build() {
        xyPlots = new ArrayList<XYPlot> (4);
        charts = new TreeMap<String, JFreeChart>();
        addReturnsSummaryListeners();
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
	}

    private void addReturnsSummaryListeners() {
        returnsSummaryPanel.addReturnsSummaryListener(new ReturnsSummaryListener() {
            @Override
            public void returnSelected(String year, int column) {
                excessReturnsSummaryPanel.selectReturn(year, column);    
            }
        });
        excessReturnsSummaryPanel.addReturnsSummaryListener(new ReturnsSummaryListener() {
            @Override
            public void returnSelected(String year, int column) {
                returnsSummaryPanel.selectReturn(year, column);    
            }
        });
    }

    private JTabbedPane buildChartsTabbedPane(TimeSeriesModel timeSeriesModel) {
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab(resourceManager.getString("performance"), buildPerformanceChartsPanel(timeSeriesModel));
        tabbedPane.addTab(resourceManager.getString("composition"), buildCompositionChartsPanel(timeSeriesModel));
        tabbedPane.addTab(resourceManager.getString("returns"), returnsSummaryPanel);
        tabbedPane.addTab(resourceManager.getString("excess.returns"), excessReturnsSummaryPanel);
        return tabbedPane;
    }

    private JPanel buildPerformanceChartsPanel(TimeSeriesModel timeSeriesModel) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(buildPortfolioBenchmarkChart(timeSeriesModel));
        panel.add(buildMembersChart(timeSeriesModel));
        return panel;
    }

    private JPanel buildCompositionChartsPanel(TimeSeriesModel timeSeriesModel) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(buildSmallMidLargeCapChart(timeSeriesModel));
        panel.add(buildValueCoreGrowthChart(timeSeriesModel));
        return panel;
    }

    private ChartPanel buildChartPanel(JFreeChart chart) {
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setDomainZoomable(false);
        chartPanel.setRangeZoomable(false);
        chartPanel.setPopupMenu(null);
        return chartPanel;
    }

    private JFreeChart buildChart(String name, XYPlot xyPlot) {
        JFreeChart chart =  new JFreeChart("", Font.decode(Font.DIALOG), xyPlot, true);
        chart.addProgressListener(this);
        charts.put(name, chart);
        return chart;
    }

	private ChartPanel buildPortfolioBenchmarkChart(TimeSeriesModel timeSeriesModel) {
		XYDataset xyDataset = buildXYDataset(timeSeriesModel, totalReturnTimeSeriesTypes);
		LegendItemCollection legendItems = new LegendItemCollection();
		if (xyDataset.getSeriesCount() == 0) {
			xyDataset = buildXYDataset(timeSeriesModel, timeSeriesTypes);
			legendItems.add(new LegendItem(resourceManager.getString("portfolio"), Color.blue));
			legendItems.add(new LegendItem(resourceManager.getString("benchmark"), Colors.navy));
		} else {
			legendItems.add(new LegendItem(resourceManager.getString("portfolio.total.return"), Color.blue));
			legendItems.add(new LegendItem(resourceManager.getString("benchmark.total.return"), Colors.navy));
		}
		XYItemRenderer xyItemRenderer = buildXYItemRenderer(Color.blue, Colors.navy);
		XYPlot xyPlot = new XYPlot(xyDataset, buildDateAxis(), buildLogAxis(), xyItemRenderer);
		xyPlot.setRangeAxis(1, buildLogAxis());
		xyPlot.mapDatasetToRangeAxes(0, buildRangeAxes());
		xyPlot.setFixedLegendItems(legendItems);
		applyXYPlotLookAndFeel(xyPlot, true);
        xyPlots.add(xyPlot);
        return buildChartPanel(buildChart(benchmarkPortfolio, xyPlot));
	}

	private ChartPanel buildMembersChart(TimeSeriesModel timeSeriesModel) {
		XYDataset xyDataset = buildXYDataset(timeSeriesModel, memberCountTimeSeriesTypes);
		LegendItemCollection legendItems = new LegendItemCollection();
		legendItems.add(new LegendItem(resourceManager.getString("members"), Color.red));
		XYItemRenderer xyItemRenderer = buildXYItemRenderer(Color.red);
		XYPlot xyPlot = new XYPlot(xyDataset, buildDateAxis(), buildLogAxis(), xyItemRenderer);
		xyPlot.setRangeAxis(1, buildLogAxis());
        xyPlot.mapDatasetToRangeAxes(0, buildRangeAxes());
		xyPlot.setFixedLegendItems(legendItems);
		applyXYPlotLookAndFeel(xyPlot, true);
        xyPlots.add(xyPlot);
        return buildChartPanel(buildChart(members, xyPlot));
	}

    private ChartPanel buildSmallMidLargeCapChart(TimeSeriesModel timeSeriesModel) {
        TimeTableXYDataset xyDataset = buildTimeTableXYDataset(timeSeriesModel, smallMidLargeAreaTimeSeriesTypes);
        LegendItemCollection legendItems = new LegendItemCollection();
        legendItems.add(new LegendItem(resourceManager.getString("smallcap"), Color.blue));
        legendItems.add(new LegendItem(resourceManager.getString("midcap"), Color.magenta));
        legendItems.add(new LegendItem(resourceManager.getString("largecap"), Color.green));
        XYItemRenderer xyItemRenderer = buildXYAreaRenderer(Color.blue, Color.magenta, Color.green);
        XYPlot xyPlot = new XYPlot(xyDataset, buildDateAxis(), buildNumberAxis(), xyItemRenderer);
        xyPlot.setRangeAxis(1, buildNumberAxis());
        xyPlot.mapDatasetToRangeAxes(0, buildRangeAxes());
        xyPlot.setFixedLegendItems(legendItems);
        applyXYPlotLookAndFeel(xyPlot, false);
        xyPlots.add(xyPlot);
        return buildChartPanel(buildChart(smallMidLargeCap, xyPlot));
    }

    private ChartPanel buildValueCoreGrowthChart(TimeSeriesModel timeSeriesModel) {
        TimeTableXYDataset xyDataset = buildTimeTableXYDataset(timeSeriesModel, valueCoreGrowthAreaTimeSeriesTypes);
        LegendItemCollection legendItems = new LegendItemCollection();
        legendItems.add(new LegendItem(resourceManager.getString("value"), Color.cyan));
        legendItems.add(new LegendItem(resourceManager.getString("core"), Color.yellow));
        legendItems.add(new LegendItem(resourceManager.getString("growth"), Color.black));
        XYItemRenderer xyItemRenderer = buildXYAreaRenderer(Color.cyan, Color.yellow, Color.black);
        XYPlot xyPlot = new XYPlot(xyDataset, buildDateAxis(), buildNumberAxis(), xyItemRenderer);
        xyPlot.setRangeAxis(1, buildNumberAxis());
        xyPlot.mapDatasetToRangeAxes(0, buildRangeAxes());
        xyPlot.setFixedLegendItems(legendItems);
        applyXYPlotLookAndFeel(xyPlot, false);
        xyPlots.add(xyPlot);
        return buildChartPanel(buildChart(valueCoreGrowth, xyPlot));
    }

    private DateAxis buildDateAxis() {
        DateAxis dateAxis = new DateAxis();
        dateAxis.setDateFormatOverride(new SimpleDateFormat("yyyy-MM"));
        dateAxis.setLowerMargin(.02);
        dateAxis.setUpperMargin(.02);
        return dateAxis;
    }

    private List<Number> buildRangeAxes() {
        List<Number> rangeAxes = new ArrayList<Number>();
        rangeAxes.add(0);
        rangeAxes.add(1);
        return rangeAxes;
    }

    private LogAxis buildLogAxis() {
        LogAxis logAxis = new LogAxis();
        logAxis.setNumberFormatOverride(new DecimalFormat("###,###,###,###"));
        logAxis.setLowerMargin(.02);
        logAxis.setUpperMargin(.02);
        return logAxis;
    }

	private NumberAxis buildNumberAxis() {
		NumberAxis numberAxis = new NumberAxis();
		numberAxis.setNumberFormatOverride(new DecimalFormat("###,###,###,###"));
        numberAxis.setLowerMargin(.02);
        numberAxis.setUpperMargin(.02);
 		return numberAxis;
	}

	private XYItemRenderer buildXYItemRenderer(Color... colors) {
		XYItemRenderer renderer = new StandardXYItemRenderer();
		for (int i = 0; i < colors.length; i++) {
			renderer.setSeriesPaint(i, colors[i]);
		}
		return renderer;
	}

    private XYItemRenderer buildXYAreaRenderer(Color... colors) {
        XYItemRenderer renderer = new XYAreaRenderer2();
        for (int i = 0; i < colors.length; i++) {
            renderer.setSeriesPaint(i, colors[i]);
        }
        return renderer;
    }

	private void applyXYPlotLookAndFeel(XYPlot xyPlot, boolean lockOnData) {
		xyPlot.setBackgroundPaint(Color.white);
		xyPlot.setDomainGridlinePaint(Color.white);
		xyPlot.setRangeGridlinePaint(Color.white);
		xyPlot.setDomainCrosshairPaint(Colors.navy);
		xyPlot.setDomainCrosshairVisible(true);
		xyPlot.setDomainCrosshairLockedOnData(lockOnData);
		xyPlot.setRangeCrosshairPaint(Colors.navy);
		xyPlot.setRangeCrosshairVisible(true);
		xyPlot.setRangeCrosshairLockedOnData(lockOnData);
        xyPlot.setDomainPannable(false);
	}

    private TimeTableXYDataset buildTimeTableXYDataset(TimeSeriesModel timeSeriesModel, EnumSet<TimeSeriesType> timeSeriesTypes) {
        TimeTableXYDataset timeTableXYDataset = new TimeTableXYDataset();
        Map<Date,Day> dayCache = new HashMap<Date,Day>();
        Map<TimeSeriesType, List<DateNumberPoint>> dateNumberPoints = timeSeriesModel.getDateNumberPoints();
        for (Map.Entry<TimeSeriesType, List<DateNumberPoint>> entry : dateNumberPoints.entrySet()) {
            if (timeSeriesTypes.contains(entry.getKey())) {
                String timeSeries = entry.getKey().toString();
                for (DateNumberPoint dateNumberPoint : entry.getValue()) {
                	Day day = dayCache.get(dateNumberPoint.getDate());
                	if (day == null) {
                		day = new Day(dateNumberPoint.getDate());
                		dayCache.put(dateNumberPoint.getDate(), day);
                	}
                    timeTableXYDataset.add(day, dateNumberPoint.getNumberAsDouble(), timeSeries);
                }
            }
        }
        return timeTableXYDataset;
    }

	private XYDataset buildXYDataset(TimeSeriesModel timeSeriesModel, EnumSet<TimeSeriesType> timeSeriesTypes) {
		TimeSeriesCollection timeSeriesCollection = new TimeSeriesCollection();
		Map<TimeSeriesType, List<DateNumberPoint>> dateNumberPoints = timeSeriesModel.getDateNumberPoints();
		for (Map.Entry<TimeSeriesType, List<DateNumberPoint>> entry : dateNumberPoints.entrySet()) {
			if (timeSeriesTypes.contains(entry.getKey())) {
				TimeSeries timeSeries = new TimeSeries(entry.getKey().toString());
				for (DateNumberPoint dateNumberPoint : entry.getValue()) {
					timeSeries.add(new Day(dateNumberPoint.getDate()), dateNumberPoint.getNumberAsDouble());
				}
				timeSeriesCollection.addSeries(timeSeries);
			}
		}
		return timeSeriesCollection;
	}

    private JPanel buildStatisticsLabelPanel(JLabel label) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.add(label);
        return panel;
    }

    private JLabel buildStatisticsLabel() {
        return new JLabel("", SwingConstants.LEADING);
    }

    private JPanel buildSliderPanel(JSlider slider) {
        final JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.add(buildSliderDecrementButton());
        panel.add(Box.createHorizontalStrut(2));
        panel.add(slider);
        panel.add(Box.createHorizontalStrut(2));
        panel.add(buildSliderIncrementButton());
        return panel;
    }

    private JButton buildSliderDecrementButton() {
        JButton button = new JButton(resourceManager.getImageIcon("left.icon"));
        ButtonSizer.instance.setDefaultToolBarSize(button);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                updateStatistics(-1);
            }
        });
        return button;
    }

    private JButton buildSliderIncrementButton() {
        JButton button = new JButton(resourceManager.getImageIcon("right.icon"));
        ButtonSizer.instance.setDefaultToolBarSize(button);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                updateStatistics(1);
            }
        });
        return button;
    }

    private void updateStatistics(int adjustedIndex) {
        Short currentValue = (short) slider.getValue();
        int index = Collections.binarySearch(sliderDateValues, currentValue);
        int newIndex = index + adjustedIndex;
        if ((newIndex >= 0) && (newIndex < sliderDateValues.size())) {
            currentValue = sliderDateValues.get(newIndex);
        }
        slider.setValue(currentValue);
    }

	private JSlider buildSlider(List<RebalanceFrequencyDate> rebalanceFrequencyDates, List<Short> dateValues) {
		short minDateValue = DateCalculator.calculateDaysSince1960(rebalanceFrequencyDates.get(0).getDate());
		short maxDateValue = DateCalculator.calculateDaysSince1960(rebalanceFrequencyDates.get(rebalanceFrequencyDates.size() - 1).getDate());
		slider = new JSlider(minDateValue, maxDateValue, minDateValue);
		slider.addChangeListener(this);
		adjustCrosshairs(DateCalculator.calculateDateSince1960(dateValues.get(0)));
		return slider;
	}

    @Override
	public void chartProgress(ChartProgressEvent event) {
        JFreeChart chart = event.getChart();
		if ((event.getType() == ChartProgressEvent.DRAWING_FINISHED) && (chart != null)) {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			Date timeSeriesSliderDate = DateCalculator.calculateDateSince1960((short) slider.getValue());
			XYPlot xyPlot = chart.getXYPlot();
            Date domainCrosshairDate = new Date((long) xyPlot.getDomainCrosshairValue());
            if (!formatter.format(domainCrosshairDate).equals(formatter.format(timeSeriesSliderDate))) {
                slider.setValue(DateCalculator.calculateDaysSince1960(domainCrosshairDate));
            }
		}
	}

	@Override
	public void stateChanged(ChangeEvent event) {
		Short currentValue = (short) slider.getValue();
		Short nearestValue = findNearestValue(sliderDateValues, currentValue);
		Date selectedDate;
		if (currentValue.equals(nearestValue)) {
			selectedDate = DateCalculator.calculateDateSince1960(currentValue);
			adjustCrosshairs(selectedDate);
			if (!slider.getValueIsAdjusting()) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(selectedDate);
                calendar.set(Calendar.HOUR_OF_DAY, 0);
				listTimeSeriesRowsAction.actionPerformed(new ActionEvent(calendar.getTime(), 0, getClass().getCanonicalName()));
			}
		} else {
			slider.setValue(nearestValue);
		}
	}

	private void adjustCrosshairs(Date selectedDate) {
		SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
		DecimalFormat decimalFormatter = new DecimalFormat("###,###,###,###.#");
		String formattedSelectedDate = dateFormatter.format(selectedDate);
		List<String> statistics = new ArrayList<String>();
		statistics.add(formattedSelectedDate);
        for (XYPlot xyPlot : xyPlots) {
            XYDataset xyDataset = xyPlot.getDataset();
            if (xyDataset instanceof TimeTableXYDataset) {
                TimeTableXYDataset timeTableXYDataset = (TimeTableXYDataset) xyDataset;
                for (int j = 0; j < timeTableXYDataset.getItemCount(); j++) {
                    TimePeriod timePeriod = timeTableXYDataset.getTimePeriod(j);
                    Date timeSeriesDate = timePeriod.getEnd();
                    if (formattedSelectedDate.equals(dateFormatter.format(timeSeriesDate))) {
                        xyPlot.setDomainCrosshairValue(selectedDate.getTime());
                    }
                }
            } else {
                TimeSeriesCollection timeSeriesCollection = (TimeSeriesCollection) xyDataset;
                for (int k = 0; k < timeSeriesCollection.getSeriesCount(); k++) {
                    TimeSeries timeSeries = timeSeriesCollection.getSeries(k);
                    for (int l = 0; l < timeSeries.getItemCount(); l++) {
                        TimeSeriesDataItem timeSeriesDataItem = timeSeries.getDataItem(l);
                        Date timeSeriesDate = timeSeriesDataItem.getPeriod().getEnd();
                        double value = timeSeriesDataItem.getValue().doubleValue();
                        if (formattedSelectedDate.equals(dateFormatter.format(timeSeriesDate))) {
                            xyPlot.setDomainCrosshairValue(selectedDate.getTime());
                            if (timeSeries.getKey().equals(TimeSeriesType.memberCount.toString())) {
                                xyPlot.setRangeCrosshairValue(value);
                            }
                            statistics.add(decimalFormatter.format(value));
                        }
                    }
                }
            }
		}
		setStatisticsLabel(statistics);
	}

	private void setStatisticsLabel(List<String> statistics) {
		StringBuilder builder = new StringBuilder();
		builder.append("Date: ");
		builder.append(statistics.get(0));
        if (statistics.size() > 1) {
            builder.append("  Portfolio: ");
            builder.append(statistics.get(1));
            builder.append("  Benchmark: ");
            builder.append(statistics.get(2));
            builder.append("  Members: ");
            builder.append(statistics.get(3));
        }
		statisticsLabel.setText(builder.toString());
	}

	private List<Short> toDateValues(List<RebalanceFrequencyDate> rebalanceFrequencyDates) {
		List<Short> dateValues = new ArrayList<Short>(rebalanceFrequencyDates.size());
		for (RebalanceFrequencyDate rebalanceFrequencyDate : rebalanceFrequencyDates) {
			dateValues.add(DateCalculator.calculateDaysSince1960(rebalanceFrequencyDate.getDate()));
		}
		return dateValues;
	}

    private Short findNearestValue(List<Short> values, Short currentValue) {
		Short nearestValue = currentValue;
		int index = Collections.binarySearch(values, currentValue);
		if (index < 0) {
			int realIndex = -1 * (index + 1);
			Short maxValue = (realIndex == values.size()) ? null : values.get(realIndex);
			Short minValue = (realIndex == 0) ? null : values.get(realIndex - 1);
			if ((minValue == null) && (maxValue != null)) {
				nearestValue = maxValue;
			} else if ((minValue != null) && (maxValue == null)) {
				nearestValue = minValue;
			} else if ((minValue != null) && (maxValue != null)) {
				nearestValue = (Math.abs(currentValue - minValue) > Math.abs(currentValue - maxValue)) ? maxValue : minValue;
			}
		}
		return nearestValue;
	}
}