package client.dialog;

import client.common.SpringLayoutManager;
import client.frame.Frame;
import client.resource.ResourceManager;
import domain.Repository;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.BorderFactory;

public final class AddRepositoryDialog extends JDialog {
    private ResourceManager resourceManager;
    private Action validateRepositoryAction;
    private JFrame frame;
    private JTextField nameTextField;
    private JTextField contextTextField;
    private boolean isCancelled;

    public AddRepositoryDialog() {
        super((Frame)null, true);
    }

    public void build() {
        setTitle(resourceManager.getString("add.repository.dialog.title"));
        buildModelPanel();
        buildActionPanel();
        pack();
    }

    public void view() {
        nameTextField.setText("");
        contextTextField.setText("");
        setLocationRelativeTo(frame);
        setVisible(true);
    }

    public boolean isCancelled() {
        return isCancelled;
    }

    public void setCancelled(boolean cancelled) {
        isCancelled = cancelled;
    }

    public Repository getRepository() {
        return new Repository(nameTextField.getText(), contextTextField.getText());
    }

    public void setResourceManager(ResourceManager resourceManager) {
        this.resourceManager = resourceManager;
    }

    public void setFrame(JFrame frame) {
        this.frame = frame;
    }

    public void setValidateRepositoryAction(Action validateRepositoryAction) {
        this.validateRepositoryAction = validateRepositoryAction;
    }

    private void buildModelPanel() {
        JPanel modelPanel = new JPanel();
        modelPanel.setBorder(BorderFactory.createTitledBorder(resourceManager.getString("add.repository.dialog.border.title")));
        modelPanel.setLayout(new SpringLayout());
        JLabel nameLabel = new JLabel(resourceManager.getString("label.name"), JLabel.TRAILING);
        JLabel contextLabel = new JLabel(resourceManager.getString("label.context"), JLabel.TRAILING);
        nameTextField = new JTextField();
        nameTextField.setPreferredSize(new Dimension(240, 25));
        contextTextField = new JTextField();
        contextTextField.setPreferredSize(new Dimension(240, 25));
        JLabel [] labels = new JLabel [] { nameLabel, contextLabel };
        JTextField [] textFields = new JTextField [] { nameTextField, contextTextField };
        SpringLayoutManager.buildForm(modelPanel, labels, textFields);
        add(modelPanel, BorderLayout.CENTER);
    }

    private void buildActionPanel() {
        JPanel actionPanel = new JPanel();
        actionPanel.setLayout(new BorderLayout());
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton(validateRepositoryAction);
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