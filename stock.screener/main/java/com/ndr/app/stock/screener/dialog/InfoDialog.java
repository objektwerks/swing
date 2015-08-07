package com.ndr.app.stock.screener.dialog;

import com.ndr.app.stock.screener.button.ButtonSizer;
import com.ndr.app.stock.screener.resource.ResourceManager;

import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.NumberFormat;

import javax.annotation.PostConstruct;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;
import org.jdesktop.swingx.JXHyperlink;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public final class InfoDialog extends JDialog {
    private static final long serialVersionUID = 6716246247175367380L;
    
	@Autowired private Frame frame;
    @Autowired private ResourceManager resourceManager;

    public void view() {
        setLocationRelativeTo(frame);
        setVisible(true);
    }

    @PostConstruct
    protected void build() {
        addWindowListener(new HideDialogWindowAdapter(this));
        setIconImage(resourceManager.getImage("app.icon"));
        setTitle(resourceManager.getString("app.title"));
        setLayout(new MigLayout());
        setModal(true);
        add(buildPanel(), "wrap");
        add(buildCloseButton(), "center");
        pack();
    }

    private JPanel buildPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 2, 3, 3));
        panel.add(new JLabel(resourceManager.getString("company.name.label")));
        panel.add(new JLabel(resourceManager.getString("company.name")));
        panel.add(new JLabel(resourceManager.getString("company.web.site.label")));
        JXHyperlink link = new JXHyperlink();
        try {
            link.setURI(new URI(resourceManager.getString("company.web.site")));
            panel.add(link);
        } catch (URISyntaxException e) {
            panel.add(new JLabel(resourceManager.getString("company.web.site")));
        }
        panel.add(new JLabel(resourceManager.getString("app.version.label")));
        panel.add(new JLabel(resourceManager.getString("app.version")));
        panel.add(new JLabel(resourceManager.getString("java.version.label")));
        panel.add(new JLabel(System.getProperty("java.version")));
        panel.add(new JLabel(resourceManager.getString("os.label")));
        panel.add(new JLabel(System.getProperty("os.name")));
        panel.add(new JLabel(resourceManager.getString("memory.usage")));
        panel.add(new JLabel(getMemoryUsage()));
        return panel;
    }

    private JButton buildCloseButton() {
        JButton button = new JButton(resourceManager.getString("close"), resourceManager.getImageIcon("close.icon"));
        ButtonSizer.instance.setDefaultDialogSize(button);
        button.setDefaultCapable(true);
        getRootPane().setDefaultButton(button);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                setVisible(false);
            }
        });
        return button;
    }

    private String getMemoryUsage() {
        MemoryMXBean mbean = ManagementFactory.getMemoryMXBean();
        MemoryUsage memoryUsage = mbean.getHeapMemoryUsage();
        long usedMemory = (memoryUsage.getUsed() / 1024) / 1000;
        return NumberFormat.getInstance().format(usedMemory) + "MB";
    }
}