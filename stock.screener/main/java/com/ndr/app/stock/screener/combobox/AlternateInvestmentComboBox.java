package com.ndr.app.stock.screener.combobox;

import com.ndr.model.stock.screener.AlternateInvestment;
import com.ndr.model.stock.screener.IndexModel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.annotation.PostConstruct;
import javax.swing.JComboBox;

import org.jdesktop.swingx.combobox.EnumComboBoxModel;
import org.springframework.stereotype.Component;

@Component
public final class AlternateInvestmentComboBox extends JComboBox {
    private static final long serialVersionUID = 2330806832289642839L;
    
	private EnumComboBoxModel<AlternateInvestment> model;
    private IndexModel indexModel;

    public void setModel(IndexModel indexModel) {
        this.indexModel = indexModel;
        model.setSelectedItem(indexModel.getAlternateInvestment());
    }

    @PostConstruct
    protected void build() {
        model = new EnumComboBoxModel<AlternateInvestment>(AlternateInvestment.class);
        setModel(model);
        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                if (indexModel != null) {
                    indexModel.setAlternateInvestment(model.getSelectedItem());
                }
            }
        });
    }
}