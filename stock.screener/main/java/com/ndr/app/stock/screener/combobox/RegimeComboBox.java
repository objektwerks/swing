package com.ndr.app.stock.screener.combobox;

import com.ndr.model.stock.screener.Regime;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JList;

import org.springframework.stereotype.Component;

@Component
public final class RegimeComboBox extends JComboBox {
    private static final long serialVersionUID = 4149632011124570150L;
	private Regime customRegime;

    public void setModel(List<Regime> regimes) {
        for (Regime regime : regimes) {
            addItem(regime);
        }
        customRegime = new Regime("Custom");
        addItem(customRegime);
    }

    public Regime getCustomRegime() {
        return customRegime;
    }

    public Regime getSelectedRegime() {
        return (Regime) getSelectedItem();
    }

    @PostConstruct
    protected void build() {
        setEditable(false);
        setRenderer(new RegimeListCellRenderer());
    }

    private class RegimeListCellRenderer extends DefaultListCellRenderer {
        private static final long serialVersionUID = 8841673278899913073L;

		public RegimeListCellRenderer() {
            setOpaque(true);
            setHorizontalAlignment(LEFT);
            setVerticalAlignment(CENTER);
        }

        @Override
		public java.awt.Component getListCellRendererComponent(JList list,
                                                               Object value,
                                                               int index,
                                                               boolean isSelected,
                                                               boolean cellHasFocus) {
            Regime regime = (Regime) value;
            setText(regime.getName());
            if (isSelected) {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            } else {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }
            return this;
        }
    }
}