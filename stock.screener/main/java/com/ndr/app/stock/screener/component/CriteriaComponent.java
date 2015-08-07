package com.ndr.app.stock.screener.component;

import com.ndr.app.stock.screener.Colors;
import com.ndr.app.stock.screener.border.CompoundBorderFactory;
import com.ndr.app.stock.screener.resource.ResourceManager;
import com.ndr.model.stock.screener.Criteria;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

import org.apache.commons.lang.builder.EqualsBuilder;

public abstract class CriteriaComponent extends JPanel implements Comparable<CriteriaComponent> {
    private static final long serialVersionUID = 9106356882704086271L;
    private static final Border selectedBorder = BorderFactory.createMatteBorder(2, 2, 2, 2, Colors.navy);
    private static final Border unselectedBorder = BorderFactory.createMatteBorder(1, 1, 1, 1, Color.lightGray);
    
	private Criteria criteria;
    protected ResourceManager resourceManager;

    public CriteriaComponent(ResourceManager resourceManager, Criteria criteria) {
        this.resourceManager = resourceManager;
        this.criteria = criteria;
        build();
    }

    public Criteria getModel() {
        return criteria;
    }

    public void setModel(Criteria criteria) {
        this.criteria = criteria;
    }

    public void setSelected(boolean isSelected) {
        if (isSelected) {
            setBorder(selectedBorder);
        } else {
            setBorder(unselectedBorder);            
        }
    }

    public Object[] toArray() {
        return new Object[] { this };
    }

    @Override
    public String getName() {
        return (criteria == null) ? super.getName() : criteria.getNameAsText();
    }

    private void build() {
        setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
        setLayout(new BorderLayout());
        CompoundBorderFactory.instance.create(this, Color.lightGray);
        add(buildPanel(), BorderLayout.NORTH);
    }

    private JPanel buildPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(buildLabel(), BorderLayout.WEST);
        return panel;
    }

    private JLabel buildLabel() {
        String text = " " + criteria.getCategoryAsText() + " : " + criteria.getName();
        JLabel label = new JLabel(text);
        label.setToolTipText(criteria.getDescriptionAsText());
        return label;
    }

    @Override
    public int compareTo(CriteriaComponent other) {
        return criteria.compareTo(other.getModel());
    }

    @Override
    public int hashCode() {
        return criteria.hashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (object == null) return false;
        if (object == this) return true;
        if (object.getClass() != getClass()) return false;
        CriteriaComponent other = (CriteriaComponent) object;
        return new EqualsBuilder()
                .append(criteria, other.getModel())
                .isEquals();
    }

    @Override
    public String toString() {
        return getName();
    }
}