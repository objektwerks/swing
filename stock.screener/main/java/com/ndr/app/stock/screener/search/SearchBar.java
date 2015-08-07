package com.ndr.app.stock.screener.search;

import com.ndr.app.stock.screener.resource.ResourceManager;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.annotation.PostConstruct;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public final class SearchBar extends JPanel {
    @Autowired ResourceManager resourceManager;
    private Searchable searchable;

    public void setSearchable(Searchable searchable) {
        this.searchable = searchable;
    }

    @PostConstruct
    protected void build() {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        add(buildLabel());
        add(buildTextField());
    }

    private JLabel buildLabel() {
        JLabel label = new JLabel(resourceManager.getImageIcon("search.icon"), JLabel.CENTER);
        label.setToolTipText(resourceManager.getString("search"));
        return label;
    }

    private JTextField buildTextField() {
        final JTextField textField = new JTextField();
        textField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                boolean textFound = searchable.search(textField.getText());
                if (textFound) {
                    textField.setBackground(Color.white);
                } else {
                    textField.setBackground(Color.lightGray);
                }
            }
        });
        return textField;
    }
}