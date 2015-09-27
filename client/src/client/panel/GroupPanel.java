package client.panel;

import client.common.SpringLayoutManager;
import client.list.GroupList;
import client.listener.GroupModelListener;
import client.listener.RepositoryModelListener;
import client.model.DomainListModel;
import client.model.GroupModel;
import client.resource.ResourceManager;
import domain.Group;
import domain.Repository;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.SpringLayout;

public final class GroupPanel extends JPanel implements GroupModel, RepositoryModelListener {
    private ResourceManager resourceManager;
    private GroupList groupList;
    private DomainListModel domainListModel;
    private Action searchGroupListModelAction;
    private Action addGroupAction;
    private GroupDomainPanel groupDomainPanel;
    private List <GroupModelListener> groupModelListeners;
    private JTextField nameTextField;
    private Repository repository;
    private Group group;

    public GroupPanel() {
        super();
        this.groupModelListeners = new ArrayList <GroupModelListener> ();
    }

    public void build() {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
        JPanel groupPanel = new JPanel();
        groupPanel.setLayout(new BoxLayout(groupPanel, BoxLayout.Y_AXIS));
        groupPanel.setBorder(BorderFactory.createTitledBorder(resourceManager.getString("label.groups")));
        buildGroupList(groupPanel);
        buildModelPanel(groupPanel);
        buildActionPanel(groupPanel);
        add(groupPanel);
        add(groupDomainPanel);
    }

    public Repository getRepository() {
        return repository;
    }

    public void setRepository(Repository repository) {
        this.repository = repository;
    }

    public Group getGroup() {
        group.setName(nameTextField.getText());
        group.setId(group.getId());
        group.setRepository(repository);
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
        nameTextField.setText(group.getName());
        for (GroupModelListener listener : groupModelListeners) {
            listener.onSelection(group);
        }
    }

    public void setGroupModelListeners(List listeners) {
        for (Object listener : listeners) {
            groupModelListeners.add((GroupModelListener) listener);
        }
    }

    public void onSelection(Repository repository) {
        setRepository(repository);
        groupList.setGroups(repository.getGroups());
        domainListModel.reset();
    }

    public void setResourceManager(ResourceManager resourceManager) {
        this.resourceManager = resourceManager;
    }

    public void setGroupList(GroupList groupList) {
        this.groupList = groupList;
    }

    public void setDomainListModel(DomainListModel domainListModel) {
        this.domainListModel = domainListModel;
    }

    public void setSearchGroupListModelAction(Action searchGroupListModelAction) {
        this.searchGroupListModelAction = searchGroupListModelAction;
    }

    public void setAddGroupAction(Action addGroupAction) {
        this.addGroupAction = addGroupAction;
    }

    public void setGroupDomainPanel(GroupDomainPanel groupDomainPanel) {
        this.groupDomainPanel = groupDomainPanel;
    }

    private void buildGroupList(JPanel groupPanel) {
        JScrollPane scrollPane = new JScrollPane(groupList);
        JViewport viewport = new JViewport();
        JTextField searchTextField = new JTextField(32);
        searchTextField.setAction(searchGroupListModelAction);
        searchTextField.setBorder(BorderFactory.createEtchedBorder());
        searchTextField.setToolTipText(resourceManager.getString("tool.tip.search"));
        viewport.setView(searchTextField);
        scrollPane.setColumnHeader(viewport);
        JPanel listPanel = new JPanel(new BorderLayout());
        listPanel.setBorder(BorderFactory.createEtchedBorder());
        listPanel.add(scrollPane, BorderLayout.CENTER);
        listPanel.setPreferredSize(new Dimension(120, 580));
        groupPanel.add(listPanel);
    }

    private void buildModelPanel(JPanel groupPanel) {
        JPanel modelPanel = new JPanel();
        modelPanel.setLayout(new SpringLayout());
        JLabel nameLabel = new JLabel(resourceManager.getString("label.name"), JLabel.TRAILING);
        nameTextField = new JTextField(32);
        nameTextField.setEnabled(false);
        JLabel [] labels = new JLabel [] { nameLabel };
        JTextField [] textFields = new JTextField [] { nameTextField };
        SpringLayoutManager.buildForm(modelPanel, labels, textFields);
        groupPanel.add(modelPanel);
    }

    private void buildActionPanel(JPanel groupPanel) {
        JPanel actionPanel = new JPanel();
        actionPanel.setLayout(new BorderLayout());
        JButton addButton = new JButton(addGroupAction);
        addButton.setPreferredSize(new Dimension(75, 25));
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        actionPanel.add(buttonPanel, BorderLayout.EAST);
        groupPanel.add(actionPanel);
    }
}