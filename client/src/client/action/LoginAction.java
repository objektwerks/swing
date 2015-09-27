package client.action;

import client.dialog.LoginDialog;
import client.frame.Frame;
import client.resource.ResourceManager;
import service.DomainManager;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

public final class LoginAction extends AbstractAction {
    private ResourceManager resourceManager;
    private LoginDialog loginDialog;
    private DomainManager domainManager;
    private Frame frame;

    public LoginAction() {
        super();
    }

    public void build() {
        putValue(Action.SMALL_ICON, resourceManager.getImageIcon("icon.login"));
        putValue(Action.NAME, resourceManager.getString("action.login"));
    }

    public void setResourceManager(ResourceManager resourceManager) {
        this.resourceManager = resourceManager;
    }

    public void setLoginDialog(LoginDialog loginDialog) {
        this.loginDialog = loginDialog;
    }

    public void setDomainManager(DomainManager domainManager) {
        this.domainManager = domainManager;
    }

    public void setFrame(Frame frame) {
        this.frame = frame;
    }

    public synchronized void actionPerformed(ActionEvent event) {
        String userName;
        String password;
        boolean isLoggedIn;
        int attempts;
        while (true) {
            attempts = loginDialog.close();
            userName = loginDialog.getUserName();
            password = loginDialog.getPassword();
            isLoggedIn = domainManager.login(userName, password);
            if (isLoggedIn) {
                frame.setVisible(true);
                break;
            } else if (attempts != 0) {
                loginDialog.view();
            } else {
                System.exit(0);
            }
        }
    }
}