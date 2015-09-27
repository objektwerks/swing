package client.action;

import client.common.SwingWorker;
import client.frame.Frame;
import client.model.DomainListModel;
import client.model.GroupListModel;
import client.resource.ResourceManager;
import client.validator.Validator;
import client.dialog.LoadDomainsDialog;
import domain.Domain;
import domain.Group;
import service.DomainManager;

import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

public final class LoadDomainsAction extends AbstractAction {
    private ResourceManager resourceManager;
    private LoadDomainsDialog loadDomainsDialog;
    private Frame frame;
    private GroupListModel groupListModel;
    private DomainListModel domainListModel;
    private DomainManager domainManager;

    public LoadDomainsAction() {
        super();
    }

    public void build() {
        putValue(Action.SMALL_ICON, resourceManager.getImageIcon("icon.load.domains"));
        putValue(Action.NAME, resourceManager.getString("action.load.domains"));
    }

    public void setResourceManager(ResourceManager resourceManager) {
        this.resourceManager = resourceManager;
    }

    public void setLoadDomainsDialog(LoadDomainsDialog loadDomainsDialog) {
        this.loadDomainsDialog = loadDomainsDialog;
    }

    public void setFrame(Frame frame) {
        this.frame = frame;
    }

    public void setDomainListModel(DomainListModel domainListModel) {
        this.domainListModel = domainListModel;
    }

    public void setGroupListModel(GroupListModel groupListModel) {
        this.groupListModel = groupListModel;
    }

    public void setDomainManager(DomainManager domainManager) {
        this.domainManager = domainManager;
    }

    public void actionPerformed(ActionEvent event) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle(resourceManager.getString("load.domains.file.chooser.title"));
        fileChooser.setFileFilter(new UrlsFileFilter());
        int returnValue = fileChooser.showOpenDialog(frame);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            String url;
            List <String> urls = new ArrayList <String> ();
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new FileReader(file));
                while ((url = reader.readLine()) != null) {
                    urls.add(url);
                }
                reader.close();
                loadDomainsDialog.view(urls);
                if (!loadDomainsDialog.isCancelled()) {
                    List <Domain> domains = loadDomainsDialog.getDomains();
                    Group group = groupListModel.getSelectedItem();
                    for (Domain domain : domains) {
                        domain.setGroup(group);
                    }
                    new Worker(domains).start();
                }
            } catch (Throwable t) {
                String message = resourceManager.getString("load.domains.action.error" + t.getMessage());
                String title = resourceManager.getString("load.domains.action.error.title");
                Validator.showErrorDialog(message, title, frame);
            }
        }
    }

    private final class UrlsFileFilter extends FileFilter {
        public boolean accept(File file) {
            boolean isAcceptable = false;
            if (file.isDirectory()) {
                isAcceptable = true;
            } else {
                if (file.getName().endsWith(".urls")) {
                    isAcceptable = true;
                }
            }
            return isAcceptable;
        }

        public String getDescription() {
            return ".urls";
        }
    }

    private final class Worker extends SwingWorker {
        private List <Domain> domains;

        public Worker(List <Domain> domains) {
            this.domains = domains;
        }

        public Object construct() {
            return domainManager.loadDomains(domains);
        }

        public void finished() {
            List <String> domainNames = (List <String>) get();
            for (String domainName : domainNames) {
                for (Domain domain : domains) {
                    if (domainName.equals(domain.getName())) {
                        domainListModel.addDomain(domain);
                        System.out.println("[LoadDomainsAction] adding to domain list model: " + domainName);
                    }
                }
            }
            int total = domains.size();
            String [] messages = new String [5];
            messages[0] = resourceManager.getString("load.domains.dialog.results");
            messages[1] = resourceManager.getString("load.domains.dialog.submitted") + " " + total;
            messages[2] = resourceManager.getString("load.domains.dialog.valid") + " " + domainNames.size();
            messages[3] = resourceManager.getString("load.domains.dialog.invalid") + " " + (total - domainNames.size());
            messages[4] = resourceManager.getString("load.domains.dialog.instruction");
            String title = resourceManager.getString("load.domains.dialog.results.title");
            JOptionPane.showMessageDialog(frame,
                                          messages,
                                          title,
                                          JOptionPane.INFORMATION_MESSAGE);
            System.out.println("[LoadDomainsAction] loaded.");
        }
    }
}