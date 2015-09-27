package client;

import client.frame.Frame;
import client.dialog.LoginDialog;
import client.resource.ResourceManager;
import service.DomainManager;

import java.awt.EventQueue;

import javax.swing.JOptionPane;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public final class Starter {
    public static void main(String [] args) throws Exception {
        LookAndFeelManager.getInstance().setDefaultLookAndFeel();
        final ApplicationContext context = new ClassPathXmlApplicationContext("/app-context.xml");
        final DomainManager domainManager = (DomainManager) context.getBean("domainManager");
        final LoginDialog loginDialog = (LoginDialog) context.getBean("loginDialog");
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    System.out.println("[Domain Manager] echo: " + domainManager.echo("echo"));
                    loginDialog.view();
                } catch (Exception e) {
                    Frame frame = (Frame) context.getBean("frame");
                    ResourceManager resourceManager = (ResourceManager) context.getBean("resourceManager");
                    JOptionPane.showMessageDialog(frame,
                                                  resourceManager.getString("app.failure"),
                                                  resourceManager.getString("frame.title"),
                                                  JOptionPane.WARNING_MESSAGE);
                    e.printStackTrace();
                    System.exit(0);
                }
            }
        });
    }
}