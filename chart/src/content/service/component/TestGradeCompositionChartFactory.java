package content.service.component;

import content.service.component.GradeCompositionChart;
import content.service.component.GradeCompositionChartModel;
import content.service.report.Grade;
import content.service.report.GradeItem;
import content.service.report.Indicator;

import java.util.List;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;

public class TestGradeCompositionChartFactory {
    private static final TestGradeCompositionChartFactory instance = new TestGradeCompositionChartFactory();
    private static final String SUN = "SUN";
    private static final String UTX = "UTX";
    private static final String TAP = "TAP";
    private static final String WIND = "WIND";
    private static final String DIOD = "DIOD";

    private TestGradeCompositionChartFactory() {
    }

    public static GradeCompositionChart newInstance() {
        Grade growthGrade = createGrowthGrade();
        Grade profitabilityGrade = createProfitabilityGrade();
        Grade valueGrade = createValueGrade();
        Grade cashFlowGrade = createCashFlowGrade();
        GradeCompositionChartModel model = new GradeCompositionChartModel(growthGrade, profitabilityGrade, valueGrade, cashFlowGrade);
        return new GradeCompositionChart(model);
    }

    public static GradeCompositionChart newUTXInstance() {
        Grade growthGrade = createUTXGrowthGrade();
        Grade profitabilityGrade = createUTXProfitabilityGrade();
        Grade valueGrade = createUTXValueGrade();
        Grade cashFlowGrade = createUTXCashFlowGrade();
        GradeCompositionChartModel model = new GradeCompositionChartModel(growthGrade, profitabilityGrade, valueGrade, cashFlowGrade);
        return new GradeCompositionChart(model);
    }

    public static GradeCompositionChart newTAPInstance() {
        Grade growthGrade = createTAPGrowthGrade();
        Grade profitabilityGrade = createTAPProfitabilityGrade();
        Grade valueGrade = createTAPValueGrade();
        Grade cashFlowGrade = createTAPCashFlowGrade();
        GradeCompositionChartModel model = new GradeCompositionChartModel(growthGrade, profitabilityGrade, valueGrade, cashFlowGrade);
        return new GradeCompositionChart(model);
    }

    public static GradeCompositionChart newWINDInstance() {
        Grade growthGrade = createWINDGrowthGrade();
        Grade profitabilityGrade = createWINDProfitabilityGrade();
        Grade valueGrade = createWINDValueGrade();
        Grade cashFlowGrade = createWINDCashFlowGrade();
        GradeCompositionChartModel model = new GradeCompositionChartModel(growthGrade, profitabilityGrade, valueGrade, cashFlowGrade);
        return new GradeCompositionChart(model);
    }

    public static GradeCompositionChart newDIODInstance() {
        Grade growthGrade = createDIODGrowthGrade();
        Grade profitabilityGrade = createDIODProfitabilityGrade();
        Grade valueGrade = createDIODValueGrade();
        Grade cashFlowGrade = createDIODCashFlowGrade();
        GradeCompositionChartModel model = new GradeCompositionChartModel(growthGrade, profitabilityGrade, valueGrade, cashFlowGrade);
        return new GradeCompositionChart(model);
    }

    public static Map <String, Indicator> createIndicators() {
        Map <String, Indicator> indicators = new HashMap <String, Indicator> ();
        indicators.put("1", new Indicator("1", 1.0d));
        indicators.put("2", new Indicator("2", 2.0d));
        indicators.put("3", new Indicator("3", 3.0d));
        indicators.put("4", new Indicator("4", 4.0d));
        indicators.put("5", new Indicator("5", 5.0d));
        indicators.put("6", new Indicator("6", 6.0d));
        return indicators;
    }

    public static Grade createGrowthGrade() {
        Map <String, Indicator> indicators = createIndicators();
        List gradeItems = new LinkedList();
        gradeItems.add(new GradeItem("Market Growth LT", Grade.A_MINUS));
        gradeItems.add(new GradeItem("Market Growth ST", Grade.A_MINUS));
        gradeItems.add(new GradeItem("Rel. Price Strenth", Grade.B_PLUS));
        gradeItems.add(new GradeItem("Growth Potential", Grade.C));
        gradeItems.add(new GradeItem("Earnings Momentum", Grade.B_PLUS));
        gradeItems.add(new GradeItem("Earnings Surprise", Grade.B));
        String title = "Growth: Positive Growth Continues";
        String paragraph = "Growth analysis...";
        return new Grade(SUN, Grade.GROWTH, Grade.B_PLUS, gradeItems, indicators, title, paragraph);
    }

    public static Grade createProfitabilityGrade() {
        Map <String, Indicator> indicators = createIndicators();
        List gradeItems = new LinkedList();
        gradeItems.add(new GradeItem("Asset Utilization", Grade.A));
        gradeItems.add(new GradeItem("Captial Utilization", Grade.B_PLUS));
        gradeItems.add(new GradeItem("Operating Margins", Grade.A));
        gradeItems.add(new GradeItem("Relative Margins", Grade.A_PLUS));
        gradeItems.add(new GradeItem("Return on Equity", Grade.A));
        gradeItems.add(new GradeItem("Quality of Revenues", Grade.B_MINUS));
        String title = "Profitability: Outstanding Operating Results";
        String paragraph = "Profitability analysis...";
        return new Grade(SUN, Grade.PROFITABILITY, Grade.A_MINUS, gradeItems, indicators, title, paragraph);
    }

    public static Grade createValueGrade() {
        Map <String, Indicator> indicators = createIndicators();
        List gradeItems = new LinkedList();
        gradeItems.add(new GradeItem("Capital Structure", Grade.B));
        gradeItems.add(new GradeItem("P/E Analysis", Grade.C));
        gradeItems.add(new GradeItem("Price/Book Ratio", Grade.F));
        gradeItems.add(new GradeItem("Price/Cash Flow Ratio", Grade.F));
        gradeItems.add(new GradeItem("Price/Sales Ratio", Grade.A));
        gradeItems.add(new GradeItem("Market Value", Grade.F));
        String title = "Value: Value Deteriorates";
        String paragraph = "Value analysis...";
        return new Grade(SUN, Grade.VALUE, Grade.C,  gradeItems, indicators, title, paragraph);
    }

    public static Grade createCashFlowGrade() {
        Map <String, Indicator> indicators = createIndicators();
        List gradeItems = new LinkedList();
        gradeItems.add(new GradeItem("Cash Flow Growth", Grade.B));
        gradeItems.add(new GradeItem("EBIDTA Margin", Grade.B_PLUS));
        gradeItems.add(new GradeItem("Debt/Cash Flow Ratio", Grade.A));
        gradeItems.add(new GradeItem("Interest Cov. Capacity", Grade.A));
        gradeItems.add(new GradeItem("Economic Value", Grade.A));
        gradeItems.add(new GradeItem("Retention Rate", Grade.A));
        String title = "Cash Flow: Strong Cash Position Suggest Healthy Outlook";
        String paragraph = "Cash Flow analysis...";
        return new Grade(SUN, Grade.CASH_FLOW, Grade.A,  gradeItems, indicators, title, paragraph);
    }

    public static Grade createUTXGrowthGrade() {
        Map <String, Indicator> indicators = createIndicators();
        List gradeItems = new LinkedList();
        gradeItems.add(new GradeItem("Market Growth LT", Grade.A_MINUS));
        gradeItems.add(new GradeItem("Market Growth ST", Grade.B_PLUS));
        gradeItems.add(new GradeItem("Rel. Price Strenth", Grade.B));
        gradeItems.add(new GradeItem("Growth Potential", Grade.C));
        gradeItems.add(new GradeItem("Earnings Momentum", Grade.C));
        gradeItems.add(new GradeItem("Earnings Surprise", Grade.B_MINUS));
        String title = "Growth: Positive Growth Continues";
        String paragraph = "Growth analysis...";
        return new Grade(UTX, Grade.GROWTH, Grade.B, gradeItems, indicators, title, paragraph);
    }

    public static Grade createUTXProfitabilityGrade() {
        Map <String, Indicator> indicators = createIndicators();
        List gradeItems = new LinkedList();
        gradeItems.add(new GradeItem("Asset Utilization", Grade.B));
        gradeItems.add(new GradeItem("Captial Utilization", Grade.B_MINUS));
        gradeItems.add(new GradeItem("Operating Margins", Grade.B));
        gradeItems.add(new GradeItem("Relative Margins", Grade.A_MINUS));
        gradeItems.add(new GradeItem("Return on Equity", Grade.B_MINUS));
        gradeItems.add(new GradeItem("Quality of Revenues", Grade.B_MINUS));
        String title = "Profitability: Outstanding Operating Results";
        String paragraph = "Profitability analysis...";
        return new Grade(UTX, Grade.PROFITABILITY, Grade.B, gradeItems, indicators, title, paragraph);
    }

    public static Grade createUTXValueGrade() {
        Map <String, Indicator> indicators = createIndicators();
        List gradeItems = new LinkedList();
        gradeItems.add(new GradeItem("Capital Structure", Grade.B_PLUS));
        gradeItems.add(new GradeItem("P/E Analysis", Grade.F));
        gradeItems.add(new GradeItem("Price/Book Ratio", Grade.F));
        gradeItems.add(new GradeItem("Price/Cash Flow Ratio", Grade.F));
        gradeItems.add(new GradeItem("Price/Sales Ratio", Grade.A_MINUS));
        gradeItems.add(new GradeItem("Market Value", Grade.F));
        String title = "Value: Value Deteriorates";
        String paragraph = "Value analysis...";
        return new Grade(UTX, Grade.VALUE, Grade.C,  gradeItems, indicators, title, paragraph);
    }

    public static Grade createUTXCashFlowGrade() {
        Map <String, Indicator> indicators = createIndicators();
        List gradeItems = new LinkedList();
        gradeItems.add(new GradeItem("Cash Flow Growth", Grade.C));
        gradeItems.add(new GradeItem("EBIDTA Margin", Grade.B_MINUS));
        gradeItems.add(new GradeItem("Debt/Cash Flow Ratio", Grade.A));
        gradeItems.add(new GradeItem("Interest Cov. Capacity", Grade.B));
        gradeItems.add(new GradeItem("Economic Value", Grade.B));
        gradeItems.add(new GradeItem("Retention Rate", Grade.B_PLUS));
        String title = "Cash Flow: Strong Cash Position Suggest Healthy Outlook";
        String paragraph = "Cash Flow analysis...";
        return new Grade(UTX, Grade.CASH_FLOW, Grade.B,  gradeItems, indicators, title, paragraph);
    }

    public static Grade createTAPGrowthGrade() {
        Map <String, Indicator> indicators = createIndicators();
        List gradeItems = new LinkedList();
        gradeItems.add(new GradeItem("Market Growth LT", Grade.A_PLUS));
        gradeItems.add(new GradeItem("Market Growth ST", Grade.B_PLUS));
        gradeItems.add(new GradeItem("Rel. Price Strenth", Grade.B));
        gradeItems.add(new GradeItem("Growth Potential", Grade.C));
        gradeItems.add(new GradeItem("Earnings Momentum", Grade.D));
        gradeItems.add(new GradeItem("Earnings Surprise", Grade.B_MINUS));
        String title = "Growth: Positive Growth Continues";
        String paragraph = "Growth analysis...";
        return new Grade(TAP, Grade.GROWTH, Grade.B, gradeItems, indicators, title, paragraph);
    }

    public static Grade createTAPProfitabilityGrade() {
        Map <String, Indicator> indicators = createIndicators();
        List gradeItems = new LinkedList();
        gradeItems.add(new GradeItem("Asset Utilization", Grade.B_MINUS));
        gradeItems.add(new GradeItem("Captial Utilization", Grade.D));
        gradeItems.add(new GradeItem("Operating Margins", Grade.B_MINUS));
        gradeItems.add(new GradeItem("Relative Margins", Grade.F));
        gradeItems.add(new GradeItem("Return on Equity", Grade.D));
        gradeItems.add(new GradeItem("Quality of Revenues", Grade.C));
        String title = "Profitability: Outstanding Operating Results";
        String paragraph = "Profitability analysis...";
        return new Grade(TAP, Grade.PROFITABILITY, Grade.C, gradeItems, indicators, title, paragraph);
    }

    public static Grade createTAPValueGrade() {
        Map <String, Indicator> indicators = createIndicators();
        List gradeItems = new LinkedList();
        gradeItems.add(new GradeItem("Capital Structure", Grade.B_MINUS));
        gradeItems.add(new GradeItem("P/E Analysis", Grade.C));
        gradeItems.add(new GradeItem("Price/Book Ratio", Grade.D));
        gradeItems.add(new GradeItem("Price/Cash Flow Ratio", Grade.B));
        gradeItems.add(new GradeItem("Price/Sales Ratio", Grade.A_PLUS));
        gradeItems.add(new GradeItem("Market Value", Grade.B_PLUS));
        String title = "Value: Value Deteriorates";
        String paragraph = "Value analysis...";
        return new Grade(TAP, Grade.VALUE, Grade.B,  gradeItems, indicators, title, paragraph);
    }

    public static Grade createTAPCashFlowGrade() {
        Map <String, Indicator> indicators = createIndicators();
        List gradeItems = new LinkedList();
        gradeItems.add(new GradeItem("Cash Flow Growth", Grade.B));
        gradeItems.add(new GradeItem("EBIDTA Margin", Grade.B_MINUS));
        gradeItems.add(new GradeItem("Debt/Cash Flow Ratio", Grade.A_MINUS));
        gradeItems.add(new GradeItem("Interest Cov. Capacity", Grade.B_MINUS));
        gradeItems.add(new GradeItem("Economic Value", Grade.C));
        gradeItems.add(new GradeItem("Retention Rate", Grade.B));
        String title = "Cash Flow: Strong Cash Position Suggest Healthy Outlook";
        String paragraph = "Cash Flow analysis...";
        return new Grade(TAP, Grade.CASH_FLOW, Grade.B,  gradeItems, indicators, title, paragraph);
    }

    public static Grade createWINDGrowthGrade() {
        Map <String, Indicator> indicators = createIndicators();
        List gradeItems = new LinkedList();
        gradeItems.add(new GradeItem("Market Growth LT", Grade.F));
        gradeItems.add(new GradeItem("Market Growth ST", Grade.B_PLUS));
        gradeItems.add(new GradeItem("Rel. Price Strenth", Grade.A));
        gradeItems.add(new GradeItem("Growth Potential", Grade.C));
        gradeItems.add(new GradeItem("Earnings Momentum", Grade.A_MINUS));
        gradeItems.add(new GradeItem("Earnings Surprise", Grade.A_PLUS));
        String title = "Growth: Positive Growth Continues";
        String paragraph = "Growth analysis...";
        return new Grade(WIND, Grade.GROWTH, Grade.B_PLUS, gradeItems, indicators, title, paragraph);
    }

    public static Grade createWINDProfitabilityGrade() {
        Map <String, Indicator> indicators = createIndicators();
        List gradeItems = new LinkedList();
        gradeItems.add(new GradeItem("Asset Utilization", Grade.F));
        gradeItems.add(new GradeItem("Captial Utilization", Grade.F));
        gradeItems.add(new GradeItem("Operating Margins", Grade.B));
        gradeItems.add(new GradeItem("Relative Margins", Grade.F));
        gradeItems.add(new GradeItem("Return on Equity", Grade.F));
        gradeItems.add(new GradeItem("Quality of Revenues", Grade.F));
        String title = "Profitability: Outstanding Operating Results";
        String paragraph = "Profitability analysis...";
        return new Grade(WIND, Grade.PROFITABILITY, Grade.D, gradeItems, indicators, title, paragraph);
    }

    public static Grade createWINDValueGrade() {
        Map <String, Indicator> indicators = createIndicators();
        List gradeItems = new LinkedList();
        gradeItems.add(new GradeItem("Capital Structure", Grade.A_MINUS));
        gradeItems.add(new GradeItem("P/E Analysis", Grade.F));
        gradeItems.add(new GradeItem("Price/Book Ratio", Grade.A_MINUS));
        gradeItems.add(new GradeItem("Price/Cash Flow Ratio", Grade.F));
        gradeItems.add(new GradeItem("Price/Sales Ratio", Grade.A_PLUS));
        gradeItems.add(new GradeItem("Market Value", Grade.F));
        String title = "Value: Value Deteriorates";
        String paragraph = "Value analysis...";
        return new Grade(WIND, Grade.VALUE, Grade.B_MINUS,  gradeItems, indicators, title, paragraph);
    }

    public static Grade createWINDCashFlowGrade() {
        Map <String, Indicator> indicators = createIndicators();
        List gradeItems = new LinkedList();
        gradeItems.add(new GradeItem("Cash Flow Growth", Grade.F));
        gradeItems.add(new GradeItem("EBIDTA Margin", Grade.B_MINUS));
        gradeItems.add(new GradeItem("Debt/Cash Flow Ratio", Grade.B));
        gradeItems.add(new GradeItem("Interest Cov. Capacity", Grade.C));
        gradeItems.add(new GradeItem("Economic Value", Grade.F));
        gradeItems.add(new GradeItem("Retention Rate", Grade.C));
        String title = "Cash Flow: Strong Cash Position Suggest Healthy Outlook";
        String paragraph = "Cash Flow analysis...";
        return new Grade(WIND, Grade.CASH_FLOW, Grade.C,  gradeItems, indicators, title, paragraph);
    }

    public static Grade createDIODGrowthGrade() {
        Map <String, Indicator> indicators = createIndicators();
        List gradeItems = new LinkedList();
        gradeItems.add(new GradeItem("Market Growth LT", Grade.A_PLUS));
        gradeItems.add(new GradeItem("Market Growth ST", Grade.A));
        gradeItems.add(new GradeItem("Rel. Price Strenth", Grade.B));
        gradeItems.add(new GradeItem("Growth Potential", Grade.B));
        gradeItems.add(new GradeItem("Earnings Momentum", Grade.A));
        gradeItems.add(new GradeItem("Earnings Surprise", Grade.B_PLUS));
        String title = "Growth: Positive Growth Continues";
        String paragraph = "Growth analysis...";
        return new Grade(DIOD, Grade.GROWTH, Grade.A_MINUS, gradeItems, indicators, title, paragraph);
    }

    public static Grade createDIODProfitabilityGrade() {
        Map <String, Indicator> indicators = createIndicators();
        List gradeItems = new LinkedList();
        gradeItems.add(new GradeItem("Asset Utilization", Grade.A));
        gradeItems.add(new GradeItem("Captial Utilization", Grade.B));
        gradeItems.add(new GradeItem("Operating Margins", Grade.A_MINUS));
        gradeItems.add(new GradeItem("Relative Margins", Grade.A_PLUS));
        gradeItems.add(new GradeItem("Return on Equity", Grade.B));
        gradeItems.add(new GradeItem("Quality of Revenues", Grade.A_MINUS));
        String title = "Profitability: Outstanding Operating Results";
        String paragraph = "Profitability analysis...";
        return new Grade(DIOD, Grade.PROFITABILITY, Grade.A_MINUS, gradeItems, indicators, title, paragraph);
    }

    public static Grade createDIODValueGrade() {
        Map <String, Indicator> indicators = createIndicators();
        List gradeItems = new LinkedList();
        gradeItems.add(new GradeItem("Capital Structure", Grade.A_MINUS));
        gradeItems.add(new GradeItem("P/E Analysis", Grade.A_PLUS));
        gradeItems.add(new GradeItem("Price/Book Ratio", Grade.A));
        gradeItems.add(new GradeItem("Price/Cash Flow Ratio", Grade.C));
        gradeItems.add(new GradeItem("Price/Sales Ratio", Grade.A_PLUS));
        gradeItems.add(new GradeItem("Market Value", Grade.B_MINUS));
        String title = "Value: Value Deteriorates";
        String paragraph = "Value analysis...";
        return new Grade(DIOD, Grade.VALUE, Grade.A_MINUS,  gradeItems, indicators, title, paragraph);
    }

    public static Grade createDIODCashFlowGrade() {
        Map <String, Indicator> indicators = createIndicators();
        List gradeItems = new LinkedList();
        gradeItems.add(new GradeItem("Cash Flow Growth", Grade.A_PLUS));
        gradeItems.add(new GradeItem("EBIDTA Margin", Grade.B));
        gradeItems.add(new GradeItem("Debt/Cash Flow Ratio", Grade.A_PLUS));
        gradeItems.add(new GradeItem("Interest Cov. Capacity", Grade.A_PLUS));
        gradeItems.add(new GradeItem("Economic Value", Grade.F));
        gradeItems.add(new GradeItem("Retention Rate", Grade.A_MINUS));
        String title = "Cash Flow: Strong Cash Position Suggest Healthy Outlook";
        String paragraph = "Cash Flow analysis...";
        return new Grade(DIOD, Grade.CASH_FLOW, Grade.A_MINUS,  gradeItems, indicators, title, paragraph);
    }
}