package client.dialog;

import client.common.SpringLayoutManager;
import client.frame.Frame;
import client.resource.ResourceManager;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.BorderFactory;

public final class LoginDialog extends JDialog {
    private ResourceManager resourceManager;
    private Action loginAction;
    private Action exitAction;
    private JFrame frame;
    private JTextField userTextField;
    private JPasswordField passwordTextField;
    private int attempts;

    public LoginDialog() {
        super((Frame)null, true);
        attempts = 3;
    }

    public void build() {
        setTitle(resourceManager.getString("login.title"));
        setSize(225, 160);
        buildModelPanel();
        buildActionPanel();
    }

    public void view() {
        userTextField.setText("");
        passwordTextField.setText("");
        setLocationRelativeTo(frame);
        setVisible(true);
    }

    public int close() {
        setVisible(false);
        return --attempts;
    }

    public String getUserName() {
        return userTextField.getText();
    }

    public String getPassword() {
        return new String(passwordTextField.getPassword());
    }

    public void setResourceManager(ResourceManager resourceManager) {
        this.resourceManager = resourceManager;
    }

    public void setFrame(JFrame frame) {
        this.frame = frame;
    }

    public void setLoginAction(Action loginAction) {
        this.loginAction = loginAction;
    }

    public void setExitAction(Action exitAction) {
        this.exitAction = exitAction;
    }

    private void buildModelPanel() {
        JPanel modelPanel = new JPanel();
        modelPanel.setBorder(BorderFactory.createTitledBorder(resourceManager.getString("login.border.title")));
        modelPanel.setLayout(new SpringLayout());
        JLabel userLabel = new JLabel(resourceManager.getString("login.user"), JLabel.TRAILING);
        JLabel passwordLabel = new JLabel(resourceManager.getString("login.password"), JLabel.TRAILING);
        userTextField = new JTextField(32);
        userTextField.setPreferredSize(new Dimension(100, 30));
        passwordTextField = new JPasswordField(32);
        passwordTextField.setPreferredSize(new Dimension(100, 30));
        JLabel [] labels = new JLabel [] { userLabel, passwordLabel };
        JComponent [] components = new JComponent [] { userTextField, passwordTextField };
        SpringLayoutManager.buildForm(modelPanel, labels, components);
        add(modelPanel, BorderLayout.CENTER);
    }

    private void buildActionPanel() {
        JPanel actionPanel = new JPanel();
        actionPanel.setLayout(new BorderLayout());
        JPanel buttonPanel = new JPanel();
        JButton loginButton = new JButton(loginAction);
        loginButton.setDefaultCapable(true);
        getRootPane().setDefaultButton(loginButton);
        loginButton.setPreferredSize(new Dimension(100, 30));
        JButton exitButton = new JButton(exitAction);
        exitButton.setPreferredSize(new Dimension(100, 30));
        buttonPanel.add(loginButton);
        buttonPanel.add(exitButton);
        actionPanel.add(buttonPanel, BorderLayout.EAST);
        add(actionPanel, BorderLayout.SOUTH);
    }
}