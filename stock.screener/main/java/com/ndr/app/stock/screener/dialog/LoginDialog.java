package com.ndr.app.stock.screener.dialog;

import com.ndr.app.stock.screener.Colors;
import com.ndr.app.stock.screener.action.LoginAction;
import com.ndr.app.stock.screener.button.ButtonSizer;
import com.ndr.app.stock.screener.resource.ResourceManager;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.annotation.PostConstruct;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;
import org.jdesktop.swingx.JXBusyLabel;
import org.jdesktop.swingx.painter.BusyPainter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public final class LoginDialog extends JDialog {
    private static final long serialVersionUID = 6577202157559953281L;
    
	@Autowired private ResourceManager resourceManager;
    @Autowired private LoginAction loginAction;
    private JTextField userTextField;
    private JPasswordField passwordTextField;
    private JButton loginButton;
    private JXBusyLabel busyLabel;

    public void view() {
        userTextField.setText("funky");
        passwordTextField.setText("monkey");
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void close() {
        setVisible(false);
    }

    public void reset() {
        loginButton.setEnabled(true);
        busyLabel.setBusy(false);
        busyLabel.setVisible(false);
    }

    public String getUser() {
        return userTextField.getText();
    }

    public String getPassword() {
        return new String(passwordTextField.getPassword());
    }

    @PostConstruct
    protected void build() {
        addWindowListener(new SystemExitDialogWindowAdapter());
        setIconImage(resourceManager.getImage("app.icon"));
        setTitle(resourceManager.getString("app.title"));
        setLayout(new BorderLayout());
        setModal(true);
        add(buildImagePanel(), BorderLayout.NORTH);
        add(buildDialogPanel(), BorderLayout.CENTER);
        pack();
    }

    private JPanel buildImagePanel() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.white);
        panel.setBorder(BorderFactory.createLineBorder(Color.white, 3));
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.add(buildTitle());
        panel.add(Box.createHorizontalGlue());
        panel.add(buildBusyLabel());
        return panel;
    }

    private JLabel buildTitle() {
        JLabel label = new JLabel(resourceManager.getImageIcon("app.icon"));
        label.setText(resourceManager.getString("app.title"));
        label.setAlignmentX(JLabel.LEFT_ALIGNMENT);
        label.setBackground(Color.white);
        label.setForeground(Colors.navy);
        label.setFont(label.getFont().deriveFont(Font.BOLD, 21));
        label.setOpaque(true);
        return label;
    }

    private JXBusyLabel buildBusyLabel() {
        busyLabel = new JXBusyLabel();
        BusyPainter busyPainter = new BusyPainter();
        busyPainter.setBaseColor(Colors.navy);
        busyPainter.setHighlightColor(Colors.tableRowColor);
        busyLabel.setBusyPainter(busyPainter);
        busyLabel.setDelay(1);
        busyLabel.setVisible(false);
        return busyLabel;
    }

    private JPanel buildDialogPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new MigLayout());
        panel.add(new JLabel(resourceManager.getString("login.dialog.label.user")));
        userTextField = new JTextField(16);
        panel.add(userTextField, "wrap");
        panel.add(new JLabel(resourceManager.getString("login.dialog.label.password")));
        passwordTextField = new JPasswordField(16);
        panel.add(passwordTextField, "wrap");
        panel.add(buildLoginButton(), "skip, center");
        return panel;
    }

    private JButton buildLoginButton() {
        loginButton = new JButton(loginAction);
        ButtonSizer.instance.setDefaultDialogSize(loginButton);
        loginButton.setDefaultCapable(true);
        getRootPane().setDefaultButton(loginButton);
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                loginButton.setEnabled(false);
                busyLabel.setBusy(true);
                busyLabel.setVisible(true);
            }
        });
        return loginButton;
    }

    private class SystemExitDialogWindowAdapter extends WindowAdapter {
        @Override
        public void windowClosing(WindowEvent event) {
            System.exit(0);
        }
    }
}