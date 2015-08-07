package com.ndr.app.stock.screener.action;

import com.ndr.app.stock.screener.ApplicationProxy;
import com.ndr.app.stock.screener.dialog.CriteriaModelDialog;
import com.ndr.app.stock.screener.dialog.IndexModelDialog;
import com.ndr.app.stock.screener.dialog.LoginDialog;
import com.ndr.app.stock.screener.panel.CriteriaIndexModelsPanel;
import com.ndr.app.stock.screener.statusbar.HomeStatusBar;
import com.ndr.app.stock.screener.statusbar.TimeSeriesStatusBar;
import com.ndr.model.stock.screener.Application;
import com.ndr.service.stock.screener.StockScreenerUserService;

import java.awt.Cursor;
import java.awt.event.ActionEvent;

import javax.annotation.PostConstruct;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public final class LoginAction extends ConfigurableAction {
    private static final long serialVersionUID = -2854926510668060155L;
    private static final Logger logger = LoggerFactory.getLogger(LoginAction.class);

	@Autowired private LoginDialog loginDialog;
    @Autowired private CriteriaModelDialog criteriaModelDialog;
    @Autowired private IndexModelDialog indexModelDialog;
    @Autowired private CriteriaIndexModelsPanel criteriaIndexModelsPanel;
    @Autowired private HomeStatusBar homeStatusBar;
    @Autowired private TimeSeriesStatusBar timeSeriesStatusBar;
    @Autowired private StockScreenerUserService stockScreenerUserServiceProxy;
    private int maxNumberOfLoginAttempts;
    private int numberOfLoginAttempts;

    @PostConstruct
    protected void build() {
        setName(resourceManager.getString("login"));
        maxNumberOfLoginAttempts = resourceManager.getInt("login.action.max.attempts");
        numberOfLoginAttempts = 0;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        loginDialog.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        Worker worker = new Worker(loginDialog.getUser(), loginDialog.getPassword());
        worker.execute();
    }

    private class Worker extends SwingWorker<Application, Void> {
        private String userName;
        private String password;

        private Worker(String userName, String password) {
            this.userName = userName;
            this.password = password;
        }

        @Override
        protected Application doInBackground() throws Exception {
            Application application = null;
            if (!isCancelled()) {
                application = stockScreenerUserServiceProxy.login(userName, password);
            }
            return application;
        }

        @Override
        protected void done() {
            numberOfLoginAttempts++;
            try {
                if (!isCancelled()) {
                    Application application = get();
                    if (application.isUserValid()) {
                        ApplicationProxy.instance.setApplication(application);
                        criteriaModelDialog.setCriteriaModels(application.getCriteriaModels());
                        indexModelDialog.setIndexModels(application.getIndexModels());
                        criteriaIndexModelsPanel.setModel(application.getCriteriaModels(), application.getIndexModels());
                        homeStatusBar.setUser(application.getUser().getName());
                        timeSeriesStatusBar.setUser(application.getUser().getName());
                        loginDialog.close();
                    } else if (numberOfLoginAttempts < maxNumberOfLoginAttempts) {
                        loginDialog.reset();    
                    } else {
                        JOptionPane.showMessageDialog(loginDialog,
                                                      resourceManager.getString("login.action.error"),
                                                      resourceManager.getString("login.action.error.title"),
                                                      JOptionPane.INFORMATION_MESSAGE);
                        System.exit(0);
                    }
                }
            } catch (Throwable t) {
                logger.error(t.getMessage(), t);
            } finally {
                loginDialog.setCursor(Cursor.getDefaultCursor());
            }
        }
    }
}