package client.panel;

import client.common.SpringLayoutManager;
import client.listener.DomainListModelListener;
import client.listener.DomainModelListener;
import client.listener.GroupModelListener;
import client.model.DomainModel;
import client.resource.ResourceManager;
import client.list.GroupDomainList;
import client.list.IndexComboBox;
import domain.Domain;
import domain.Group;
import domain.Index;
import com.toedter.calendar.JDateChooser;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.JViewport;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

public final class GroupDomainPanel extends JPanel implements DomainModel, DomainListModelListener, GroupModelListener {
    private ResourceManager resourceManager;
    private GroupDomainList groupDomainList;
    private IndexComboBox indexComboBox;
    private Action searchGroupDomainListModelAction;
    private Action addDomainAction;
    private Action updateDomainAction;
    private Action loadDomainsAction;
    private Action searchDomainAction;
    private List <DomainModelListener> domainModelListeners;
    private JTextField urlTextField;
    private SpinnerNumberModel crawlDepthSpinnerModel;
    private JTextField createdDateTextField;
    private JTextField crawlDateTextField;
    private JDateChooser nextCrawlDateChooser;
    private JComboBox typeComboBox;
    private Group group;
    private Domain domain;

    public GroupDomainPanel() {
        super();
        this.domainModelListeners = new ArrayList <DomainModelListener>();
    }

    public void build() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createTitledBorder(resourceManager.getString("label.domains")));
        buildGroupDomainList();
        buildModelPanel();
        buildActionPanel();
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public Domain getDomain() {
        domain.setType((String) typeComboBox.getSelectedItem());
        Index index = indexComboBox.getSelectedItem();
        if (!index.getId().equals(index.getName())) {
            domain.setIndex(index);
            index.addDomain(domain);
        } else {
            domain.setIndex(null);
        }
        return domain;
    }

    public void setDomain(Domain domain) {
        this.domain = domain;
        urlTextField.setText(domain.getUrl());
        crawlDepthSpinnerModel.setValue(domain.getCrawlDepth());
        createdDateTextField.setText(domain.format(domain.getCreatedDate()));
        crawlDateTextField.setText(domain.format(domain.getCrawlDate()));
        nextCrawlDateChooser.setDate(domain.getNextCrawlDate());
        typeComboBox.setSelectedItem(domain.getType());
        indexComboBox.setSelectedItem(domain.getIndex());
        for (DomainModelListener listener : domainModelListeners) {
            listener.onSelection(domain);
        }
    }

    public void setDomainModelListeners(List listeners) {
        for (Object listener : listeners) {
            domainModelListeners.add((DomainModelListener) listener);
        }
    }

    public void onAdd(Domain domain) {
        setDomain(domain);
    }

    public void onRemove(Domain domain) {
        this.domain = new Domain();
        reset();
    }

    public void onSelection(Group group) {
        setGroup(group);
        indexComboBox.setIndexes(group.getRepository().getIndexes());
        groupDomainList.setDomains(group.getDomains());
        reset();
    }

    public void setResourceManager(ResourceManager resourceManager) {
        this.resourceManager = resourceManager;
    }

    public void setGroupDomainList(GroupDomainList groupDomainList) {
        this.groupDomainList = groupDomainList;
    }

    public void setIndexComboBox(IndexComboBox indexComboBox) {
        this.indexComboBox = indexComboBox;
    }

    public void setSearchGroupDomainListModelAction(Action searchGroupDomainListModelAction) {
        this.searchGroupDomainListModelAction = searchGroupDomainListModelAction;
    }

    public void setAddDomainAction(Action addDomainAction) {
        this.addDomainAction = addDomainAction;
    }

    public void setUpdateDomainAction(Action updateDomainAction) {
        this.updateDomainAction = updateDomainAction;
    }

    public void setLoadDomainsAction(Action loadDomainsAction) {
        this.loadDomainsAction = loadDomainsAction;
    }

    public void setSearchDomainAction(Action searchDomainAction) {
        this.searchDomainAction = searchDomainAction;
    }

    private void buildGroupDomainList() {
        JScrollPane scrollPane = new JScrollPane(groupDomainList);
        JViewport viewport = new JViewport();
        JTextField searchTextField = new JTextField(32);
        searchTextField.setAction(searchGroupDomainListModelAction);
        searchTextField.setBorder(BorderFactory.createEtchedBorder());
        searchTextField.setToolTipText(resourceManager.getString("tool.tip.search"));
        viewport.setView(searchTextField);
        scrollPane.setColumnHeader(viewport);
        JPanel listPanel = new JPanel(new BorderLayout());
        listPanel.setBorder(BorderFactory.createEtchedBorder());
        listPanel.add(scrollPane, BorderLayout.CENTER);
        listPanel.setPreferredSize(new Dimension(400, 400));
        add(listPanel);
    }

    private void buildModelPanel() {
        JPanel modelPanel = new JPanel();
        modelPanel.setLayout(new SpringLayout());
        JLabel urlLabel = new JLabel(resourceManager.getString("label.url"), JLabel.TRAILING);
        urlTextField = new JTextField(32);
        urlTextField.setEnabled(false);
        JLabel crawlDepthLabel = new JLabel(resourceManager.getString("label.crawl.depth"), JLabel.TRAILING);
        JSpinner crawlDepthSpinner = new JSpinner();
        crawlDepthSpinnerModel = new SpinnerNumberModel(1, 1, 10, 1);
        crawlDepthSpinner.setModel(crawlDepthSpinnerModel);
        JLabel createdDateLabel = new JLabel(resourceManager.getString("label.created.date"), JLabel.TRAILING);
        createdDateTextField = new JTextField(32);
        createdDateTextField.setEnabled(false);
        JLabel crawlDateLabel = new JLabel(resourceManager.getString("label.crawl.date"), JLabel.TRAILING);
        crawlDateTextField = new JTextField(32);
        crawlDateTextField.setEnabled(false);
        JLabel nextCrawlDateLabel = new JLabel(resourceManager.getString("label.next.crawl.date"), JLabel.TRAILING);
        nextCrawlDateChooser = new JDateChooser(new Date());
        nextCrawlDateChooser.setDateFormatString("yyyy.MM.dd:HH.mm.ss");
        JLabel typeLabel = new JLabel(resourceManager.getString("label.type"), JLabel.TRAILING);
        typeComboBox = typeComboBox = new JComboBox(Domain.TYPES);
        JLabel indexLabel = new JLabel(resourceManager.getString("label.index"), JLabel.TRAILING);
        JLabel searchLabel = new JLabel(resourceManager.getString("label.search"), JLabel.TRAILING);
        JTextField searchTextField = new JTextField();
        searchTextField.setAction(searchDomainAction);
        searchTextField.setToolTipText(resourceManager.getString("tool.tip.search"));
        JLabel [] labels = new JLabel [] { urlLabel, crawlDepthLabel, createdDateLabel, crawlDateLabel, nextCrawlDateLabel, typeLabel, indexLabel, searchLabel };
        JComponent [] components = new JComponent [] { urlTextField, crawlDepthSpinner, createdDateTextField, crawlDateTextField, nextCrawlDateChooser, typeComboBox, indexComboBox, searchTextField };
        SpringLayoutManager.buildForm(modelPanel, labels, components);
        add(modelPanel);
    }

    private void buildActionPanel() {
        JPanel actionPanel = new JPanel();
        actionPanel.setLayout(new BorderLayout());
        JButton addButton = new JButton(addDomainAction);
        addButton.setPreferredSize(new Dimension(90, 25));
        JButton updateButton = new JButton(updateDomainAction);
        updateButton.setPreferredSize(new Dimension(90, 25));
        JButton loadButton = new JButton(loadDomainsAction);
        loadButton.setPreferredSize(new Dimension(90, 25));
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(loadButton);
        actionPanel.add(buttonPanel, BorderLayout.EAST);
        add(actionPanel);
    }

    private void reset() {
        urlTextField.setText("");
        crawlDepthSpinnerModel.setValue(1);
        typeComboBox.setSelectedItem(Domain.CANDIDATE);
        indexComboBox.setSelectedIndex(0);
    }
}