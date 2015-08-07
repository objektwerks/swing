package com.ndr.app.stock.screener;

import com.ndr.app.stock.screener.dialog.LoginDialog;
import com.ndr.app.stock.screener.frame.Frame;

import java.awt.EventQueue;

import javax.swing.JOptionPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public final class Launcher {
    private static final Logger logger = LoggerFactory.getLogger(Launcher.class);

    public static void main(String[] args) {
        try {
            LookAndFeelManager.instance.configure();
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:test.context.xml");
                    LoginDialog loginDialog = context.getBean(LoginDialog.class);
                    loginDialog.view();
                    Frame frame = (Frame) context.getBean("frame");
                    frame.setVisible(true);
                }
            });
        } catch (Throwable t) {
            logger.error(t.getMessage(), t);
            JOptionPane.showMessageDialog(null, t.getMessage(), "Stock Screener", JOptionPane.ERROR_MESSAGE);
        }
    }
}