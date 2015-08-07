package com.ndr.app.stock.screener.dialog;

import com.ndr.app.stock.screener.button.ButtonSizer;
import com.ndr.app.stock.screener.resource.ResourceManager;
import com.ndr.app.stock.screener.text.NoteTextPane;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.annotation.PostConstruct;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public final class CriteriaIndexModelDialog extends JDialog {
    private static final long serialVersionUID = 6997203357559953281L;

	@Autowired private ResourceManager resourceManager;
    @Autowired private NoteTextPane noteTextPane;
    private JTextField nameTextField;
    private boolean wasNotCanceled;

    public CriteriaIndexModelDialog() {
        nameTextField = new JTextField();
    }

    public void view(JComponent component, String name, String note) {
        nameTextField.setText(name);
        noteTextPane.setText(note);
        nameTextField.setFocusable(true);
        setLocationRelativeTo(component);
        setVisible(true);
    }

    public boolean wasNotCanceled() {
        return wasNotCanceled;
    }

    public String getModelName() {
        return nameTextField.getText();
    }

    public String getModelNote() {
        return noteTextPane.getText();
    }

    @PostConstruct
    protected void build() {
        noteTextPane.setEditable(true);
        addWindowListener(new HideDialogWindowAdapter(this));
        setIconImage(resourceManager.getImage("app.icon"));
        setLayout(new BorderLayout());
        setModal(true);
        add(buildPanel(), BorderLayout.CENTER);
        pack();
    }

    private JPanel buildPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new MigLayout());
        panel.add(new JLabel(resourceManager.getString("name")));
        panel.add(nameTextField, "span, grow, wrap");
        panel.add(new JLabel(resourceManager.getString("note")));
        panel.add(new JScrollPane(noteTextPane), "span, grow, wrap");
        panel.add(buildButtonPanel(), "skip, center");
        return panel;
    }

    private JPanel buildButtonPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.add(buildOkButton());
        panel.add(Box.createRigidArea(new Dimension(2, 0)));
        panel.add(buildCancelButton());
        return panel;
    }

    private JButton buildOkButton() {
        JButton button = new JButton(resourceManager.getString("ok"));
        ButtonSizer.instance.setDefaultDialogSize(button);
        button.setDefaultCapable(true);
        getRootPane().setDefaultButton(button);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                wasNotCanceled = true;
                setVisible(false);
            }
        });
        return button;
    }

    private JButton buildCancelButton() {
        JButton button = new JButton(resourceManager.getString("cancel"));
        ButtonSizer.instance.setDefaultDialogSize(button);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                wasNotCanceled = false;
                setVisible(false);
            }
        });
        return button;
    }
}