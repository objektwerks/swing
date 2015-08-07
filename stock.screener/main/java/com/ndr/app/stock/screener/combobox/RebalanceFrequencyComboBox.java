package com.ndr.app.stock.screener.combobox;

import com.ndr.model.stock.screener.IndexModel;
import com.ndr.model.stock.screener.RebalanceFrequency;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.annotation.PostConstruct;
import javax.swing.JComboBox;

import org.jdesktop.swingx.combobox.EnumComboBoxModel;
import org.springframework.stereotype.Component;

@Component
public final class RebalanceFrequencyComboBox extends JComboBox {
    private static final long serialVersionUID = -9153238536728450309L;
    
	private EnumComboBoxModel<RebalanceFrequency> model;
    private IndexModel indexModel;

    public void setModel(IndexModel indexModel) {
        this.indexModel = indexModel;
        model.setSelectedItem(indexModel.getRebalanceFrequency());
    }

    @PostConstruct
    protected void build() {
        model = new EnumComboBoxModel<RebalanceFrequency>(RebalanceFrequency.class);
        setModel(model);
        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                if (indexModel != null) {
                    indexModel.setRebalanceFrequency(model.getSelectedItem());
                }
            }
        });
    }
}