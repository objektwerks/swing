package client.dialog;

import client.common.SpringLayoutManager;
import client.frame.Frame;
import client.resource.ResourceManager;
import domain.Group;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.BorderFactory;

public final class AddGroupDialog extends JDialog {
    private ResourceManager resourceManager;
    private Action validateGroupAction;
    private Frame frame;
    private JTextField nameTextField;
    private boolean isCancelled;

    public AddGroupDialog() {
        super((Frame)null, true);
    }

    public void build() {
        setTitle(resourceManager.getString("add.group.dialog.title"));
        buildModelPanel();
        buildActionPanel();
        pack();
    }

    public void view() {
        nameTextField.setText("");
        setLocationRelativeTo(frame);
        setVisible(true);
    }

    public boolean isCancelled() {
        return isCancelled;
    }

    public void setCancelled(boolean cancelled) {
        isCancelled = cancelled;
    }

    public void setResourceManager(ResourceManager resourceManager) {
        this.resourceManager = resourceManager;
    }

    public void setValidateGroupAction(Action validateGroupAction) {
        this.validateGroupAction = validateGroupAction;
    }

    public void setFrame(Frame frame) {
        this.frame = frame;
    }

    public Group getGroup() {
        return new Group(nameTextField.getText());
    }

    private void buildModelPanel() {
        JPanel modelPanel = new JPanel();
        modelPanel.setBorder(BorderFactory.createTitledBorder(resourceManager.getString("add.group.dialog.border.title")));
        modelPanel.setLayout(new SpringLayout());
        JLabel nameLabel = new JLabel(resourceManager.getString("label.name"), JLabel.TRAILING);
        nameTextField = new JTextField(32);
        nameTextField.setPreferredSize(new Dimension(240, 25));
        JLabel [] labels = new JLabel [] { nameLabel };
        JTextField [] textFields = new JTextField [] { nameTextField };
        SpringLayoutManager.buildForm(modelPanel, labels, textFields);
        add(modelPanel, BorderLayout.CENTER);
    }

    private void buildActionPanel() {
        JPanel actionPanel = new JPanel();
        actionPanel.setLayout(new BorderLayout());
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton(validateGroupAction);
        addButton.setDefaultCapable(true);
        getRootPane().setDefaultButton(addButton);
        addButton.setPreferredSize(new Dimension(100, 25));
        JButton cancelButton = new JButton(resourceManager.getString("label.cancel"));
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                setCancelled(true);
                setVisible(false);
            }
        });
        cancelButton.setPreferredSize(new Dimension(100, 25));
        buttonPanel.add(addButton);
        buttonPanel.add(cancelButton);
        actionPanel.add(buttonPanel, BorderLayout.EAST);
        add(actionPanel, BorderLayout.SOUTH);
    }
}