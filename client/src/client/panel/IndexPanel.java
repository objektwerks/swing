package client.panel;

import client.common.SpringLayoutManager;
import client.list.IndexList;
import client.listener.IndexModelListener;
import client.listener.RepositoryModelListener;
import client.model.DomainListModel;
import client.model.IndexModel;
import client.resource.ResourceManager;
import domain.Index;
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

public final class IndexPanel extends JPanel implements IndexModel, RepositoryModelListener {
    private ResourceManager resourceManager;
    private IndexList indexList;
    private DomainListModel domainListModel;
    private Action searchIndexListModelAction;
    private Action addIndexAction;
    private Action searchIndexAction;
    private IndexDomainPanel indexDomainPanel;
    private List<IndexModelListener> indexModelListeners;
    private JTextField nameTextField;
    private Repository repository;
    private Index index;

    public IndexPanel() {
        super();
        this.indexModelListeners = new ArrayList <IndexModelListener>();
    }

    public void build() {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
        JPanel indexPanel = new JPanel();
        indexPanel.setLayout(new BoxLayout(indexPanel, BoxLayout.Y_AXIS));
        indexPanel.setBorder(BorderFactory.createTitledBorder(resourceManager.getString("label.indexes")));
        buildIndexList(indexPanel);
        buildModelPanel(indexPanel);
        buildActionPanel(indexPanel);
        add(indexPanel);
        add(indexDomainPanel);
    }

    public Repository getRepository() {
        return repository;
    }

    public void setRepository(Repository repository) {
        this.repository = repository;
    }

    public Index getIndex() {
        index.setName(nameTextField.getText());
        index.setId(index.getId());
        index.setRepository(repository);
        return index;
    }

    public void setIndex(Index index) {
        this.index = index;
        nameTextField.setText(index.getName());
        for (IndexModelListener listener : indexModelListeners) {
            listener.onSelection(index);
        }
    }

    public void setIndexModelListeners(List listeners) {
        for (Object listener : listeners) {
            indexModelListeners.add((IndexModelListener) listener);
        }
    }

    public void onSelection(Repository repository) {
        setRepository(repository);
        indexList.setIndexes(repository.getIndexes());
        domainListModel.reset();
    }

    public void setResourceManager(ResourceManager resourceManager) {
        this.resourceManager = resourceManager;
    }

    public void setIndexList(IndexList indexList) {
        this.indexList = indexList;
    }

    public void setDomainListModel(DomainListModel domainListModel) {
        this.domainListModel = domainListModel;
    }

    public void setSearchIndexListModelAction(Action searchIndexListModelAction) {
        this.searchIndexListModelAction = searchIndexListModelAction;
    }

    public void setAddIndexAction(Action addIndexAction) {
        this.addIndexAction = addIndexAction;
    }

    public void setSearchIndexAction(Action searchIndexAction) {
        this.searchIndexAction = searchIndexAction;
    }

    public void setIndexDomainPanel(IndexDomainPanel indexDomainPanel) {
        this.indexDomainPanel = indexDomainPanel;
    }

    private void buildIndexList(JPanel indexPanel) {
        JScrollPane scrollPane = new JScrollPane(indexList);
        JViewport viewport = new JViewport();
        JTextField searchTextField = new JTextField(32);
        searchTextField.setAction(searchIndexListModelAction);
        searchTextField.setBorder(BorderFactory.createEtchedBorder());
        searchTextField.setToolTipText(resourceManager.getString("tool.tip.search"));
        viewport.setView(searchTextField);
        scrollPane.setColumnHeader(viewport);
        JPanel listPanel = new JPanel(new BorderLayout());
        listPanel.setBorder(BorderFactory.createEtchedBorder());
        listPanel.add(scrollPane, BorderLayout.CENTER);
        listPanel.setPreferredSize(new Dimension(120, 580));
        indexPanel.add(listPanel);
    }

    private void buildModelPanel(JPanel indexPanel) {
        JPanel modelPanel = new JPanel();
        modelPanel.setLayout(new SpringLayout());
        JLabel nameLabel = new JLabel(resourceManager.getString("label.name"), JLabel.TRAILING);
        nameTextField = new JTextField(32);
        nameTextField.setEnabled(false);
        JLabel searchLabel = new JLabel(resourceManager.getString("label.search"), JLabel.TRAILING);
        JTextField searchTextField = new JTextField(32);
        searchTextField.setAction(searchIndexAction);
        searchTextField.setToolTipText(resourceManager.getString("tool.tip.search"));
        JLabel [] labels = new JLabel [] { nameLabel, searchLabel};
        JTextField [] textFields = new JTextField [] { nameTextField, searchTextField };
        SpringLayoutManager.buildForm(modelPanel, labels, textFields);
        indexPanel.add(modelPanel);
    }

    private void buildActionPanel(JPanel indexPanel) {
        JPanel actionPanel = new JPanel();
        actionPanel.setLayout(new BorderLayout());
        JButton addButton = new JButton(addIndexAction);
        addButton.setPreferredSize(new Dimension(75, 25));
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        actionPanel.add(buttonPanel, BorderLayout.EAST);
        indexPanel.add(actionPanel);
    }
}