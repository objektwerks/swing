package content.service.component;

import content.service.report.Grade;

import java.io.Serializable;

public class GradeCompositionChartModel implements Serializable {
    private Grade growthGrade;
    private Grade profitabilityGrade;
    private Grade valueGrade;
    private Grade cashFlowGrade;
    
    public GradeCompositionChartModel() {
    }
    
    public GradeCompositionChartModel(Grade growthGrade,
                                      Grade profitabilityGrade,
                                      Grade valueGrade,
                                      Grade cashFlowGrade) {
        this.growthGrade = growthGrade;
        this.profitabilityGrade = profitabilityGrade;
        this.valueGrade = valueGrade;
        this.cashFlowGrade = cashFlowGrade;
    }

    public Grade getGrowthGrade() {
        return growthGrade;
    }

    public void setGrowthGrade(Grade growthGrade) {
        this.growthGrade = growthGrade;
    }

    public Grade getProfitabilityGrade() {
        return profitabilityGrade;
    }

    public void setProfitabilityGrade(Grade profitabilityGrade) {
        this.profitabilityGrade = profitabilityGrade;
    }

    public Grade getValueGrade() {
        return valueGrade;
    }

    public void setValueGrade(Grade valueGrade) {
        this.valueGrade = valueGrade;
    }

    public Grade getCashFlowGrade() {
        return cashFlowGrade;
    }

    public void setCashFlowGrade(Grade cashFlowGrade) {
        this.cashFlowGrade = cashFlowGrade;
    }
}