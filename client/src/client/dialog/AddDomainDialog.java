package client.dialog;

import client.common.SpringLayoutManager;
import client.frame.Frame;
import client.resource.ResourceManager;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

public final class AddDomainDialog extends JDialog {
    private ResourceManager resourceManager;
    private Action validateDomainAction;
    private Frame frame;
    private JTextField urlTextField;
    private boolean isCancelled;

    public AddDomainDialog() {
        super((Frame)null, true);
    }

    public void build() {
        setTitle(resourceManager.getString("add.domain.dialog.title"));
        buildModelPanel();
        buildActionPanel();
        pack();
    }

    public void view() {
        urlTextField.setText("");
        setLocationRelativeTo(frame);
        setVisible(true);
    }

    public boolean isCancelled() {
        return isCancelled;
    }

    public void setCancelled(boolean cancelled) {
        isCancelled = cancelled;
    }

    public String getUrl() {
        return urlTextField.getText();
    }

    public void setResourceManager(ResourceManager resourceManager) {
        this.resourceManager = resourceManager;
    }

    public void setValidateDomainAction(Action validateDomainAction) {
        this.validateDomainAction = validateDomainAction;
    }

    public void setFrame(Frame frame) {
        this.frame = frame;
    }

    private void buildModelPanel() {
        JPanel modelPanel = new JPanel();
        modelPanel.setBorder(BorderFactory.createTitledBorder(resourceManager.getString("add.domain.dialog.border.title")));
        modelPanel.setLayout(new SpringLayout());
        JLabel urlLabel = new JLabel(resourceManager.getString("label.url"), JLabel.TRAILING);
        urlTextField = new JTextField();
        urlTextField.setPreferredSize(new Dimension(240, 25));
        JLabel [] labels = new JLabel [] { urlLabel };
        JTextField [] textFields = new JTextField [] { urlTextField };
        SpringLayoutManager.buildForm(modelPanel, labels, textFields);
        add(modelPanel, BorderLayout.CENTER);
    }

    private void buildActionPanel() {
        JPanel actionPanel = new JPanel();
        actionPanel.setLayout(new BorderLayout());
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton(validateDomainAction);
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