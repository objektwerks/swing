package com.ndr.app.stock.screener.combobox;

import com.ndr.model.stock.screener.IndexModel;
import com.ndr.model.stock.screener.Weighting;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.annotation.PostConstruct;
import javax.swing.JComboBox;

import org.jdesktop.swingx.combobox.EnumComboBoxModel;
import org.springframework.stereotype.Component;

@Component
public final class WeightingComboBox extends JComboBox {
    private static final long serialVersionUID = 2330809832289642839L;
    
	private EnumComboBoxModel<Weighting> model;
    private IndexModel indexModel;

    public void setModel(IndexModel indexModel) {
        this.indexModel = indexModel;
        model.setSelectedItem(indexModel.getWeighting());
    }

    @PostConstruct
    protected void build() {
        model = new EnumComboBoxModel<Weighting>(Weighting.class);
        setModel(model);
        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                if (indexModel != null) {
                    indexModel.setWeighting(model.getSelectedItem());
                }
            }
        });
    }
}