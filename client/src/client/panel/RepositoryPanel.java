package client.panel;

import client.common.SpringLayoutManager;
import client.listener.RepositoryModelListener;
import client.model.RepositoryModel;
import client.resource.ResourceManager;
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
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.SpringLayout;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;

public final class RepositoryPanel extends JPanel implements RepositoryModel {
    private ResourceManager resourceManager;
    private Action listRepositoriesAction;
    private Action addRepositoryAction;
    private Action searchRepositoryListModelAction;
    private JList repositoryList;
    private JTextField nameTextField;
    private JTextField contextTextField;
    private List <RepositoryModelListener> repositoryModelListeners;
    private Repository repository;

    public RepositoryPanel() {
        super();
        this.repositoryModelListeners = new ArrayList <RepositoryModelListener> ();
    }

    public void build() {
        Border inner = BorderFactory.createTitledBorder(resourceManager.getString("label.repositories"));
        Border outer = BorderFactory.createEmptyBorder(6, 6, 6, 6);
        setBorder(new CompoundBorder(outer, inner));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        buildRepositoryList();
        buildModelPanel();
        buildActionPanel();
    }

    public Repository getRepository() {
        repository.setName(nameTextField.getText());
        repository.setContext(contextTextField.getText());
        return repository;
    }

    public void setRepository(Repository repository) {
        this.repository = repository;
        nameTextField.setText(repository.getName());
        contextTextField.setText(repository.getContext());
        for (RepositoryModelListener listener : repositoryModelListeners) {
            listener.onSelection(repository);
        }
    }

    public void setRepositoryModelListeners(List listeners) {
        for (Object listener : listeners) {
            repositoryModelListeners.add((RepositoryModelListener) listener);
        }
    }

    public void setResourceManager(ResourceManager resourceManager) {
        this.resourceManager = resourceManager;
    }

    public void setListRepositoriesAction(Action listRepositoriesAction) {
        this.listRepositoriesAction = listRepositoriesAction;
    }

    public void setAddRepositoryAction(Action addRepositoryAction) {
        this.addRepositoryAction = addRepositoryAction;
    }

    public void setSearchRepositoryListModelAction(Action searchRepositoryListModelAction) {
        this.searchRepositoryListModelAction = searchRepositoryListModelAction;
    }

    public void setRepositoryList(JList repositoryList) {
        this.repositoryList = repositoryList;
    }

    private void buildRepositoryList() {
        JScrollPane scrollPane = new JScrollPane(repositoryList);
        JViewport viewport = new JViewport();
        JTextField searchTextField = new JTextField(32);
        searchTextField.setAction(searchRepositoryListModelAction);
        searchTextField.setBorder(BorderFactory.createEtchedBorder());
        searchTextField.setToolTipText(resourceManager.getString("tool.tip.search"));
        viewport.setView(searchTextField);
        scrollPane.setColumnHeader(viewport);
        scrollPane.setPreferredSize(new Dimension(120, 580));
        add(scrollPane);
    }

    private void buildModelPanel() {
        JPanel modelPanel = new JPanel();
        modelPanel.setLayout(new SpringLayout());
        JLabel nameLabel = new JLabel(resourceManager.getString("label.name"), JLabel.TRAILING);
        nameTextField = new JTextField(32);
        nameTextField.setEnabled(false);
        JLabel contextLabel = new JLabel(resourceManager.getString("label.context"), JLabel.TRAILING);
        contextTextField = new JTextField(32);
        contextTextField.setEnabled(false);
        JLabel [] labels = new JLabel [] { nameLabel, contextLabel };
        JTextField [] textFields = new JTextField [] { nameTextField, contextTextField };
        SpringLayoutManager.buildForm(modelPanel, labels, textFields);
        add(modelPanel);
    }

    private void buildActionPanel() {
        JPanel actionPanel = new JPanel();
        actionPanel.setLayout(new BorderLayout());
        JButton listButton = new JButton(listRepositoriesAction);
        listButton.setPreferredSize(new Dimension(75, 25));
        JButton addButton = new JButton(addRepositoryAction);
        addButton.setPreferredSize(new Dimension(75, 25));
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(listButton);
        buttonPanel.add(addButton);
        actionPanel.add(buttonPanel, BorderLayout.EAST);
        add(actionPanel);
    }
}