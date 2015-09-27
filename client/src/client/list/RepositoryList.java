package client.list;

import client.listener.RepositoryListModelListener;
import client.listener.RepositoryListSelectionListener;
import client.model.RepositoryListModel;
import client.renderer.RepositoryListCellRenderer;
import domain.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.ListSelectionModel;

public final class RepositoryList extends JList implements RepositoryListModel {
    private RepositoryListSelectionListener repositoryListSelectionListener;
    private RepositoryListCellRenderer repositoryListCellRenderer;
    private List <RepositoryListModelListener> repositoryListModelListeners;
    private SortedSet <Repository> repositories;

    public RepositoryList() {
        super();
        this.repositoryListModelListeners = new ArrayList <RepositoryListModelListener> ();
    }

    public void build() {
        setModel(new DefaultListModel());
        getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        addListSelectionListener(repositoryListSelectionListener);
        setCellRenderer(repositoryListCellRenderer);
    }

    public SortedSet <Repository> getRepositories() {
        if (null == repositories) {
            repositories = new TreeSet <Repository> ();
        }
        return repositories;
    }

    public void setRepositories(SortedSet <Repository> repositories) {
        this.repositories = repositories;
        DefaultListModel listModel = (DefaultListModel) getModel();
        listModel.clear();
        for (Repository repository : repositories) {
            listModel.addElement(repository);
        }
        setModel(listModel);
        if (repositories.size() > 0) {
            setSelectedIndex(0);
        }
    }

    public void reset() {
        setRepositories(getRepositories());
    }

    public void addRepository(Repository repository) {
        getRepositories().add(repository);
        setRepositories(repositories);
        setSelectedValue(repository, true);
        for (RepositoryListModelListener listener : repositoryListModelListeners) {
            listener.onAdd(repository);
        }
    }

    public void removeRepository(Repository repository) {
        getRepositories().remove(repository);
        setRepositories(repositories);
        for (RepositoryListModelListener listener : repositoryListModelListeners) {
            listener.onRemove(repository);
        }
    }

    public Repository getSelectedItem() {
        int selectedIndex = getSelectedIndex();
        return (Repository) getModel().getElementAt(selectedIndex);
    }

    public void setSelectedItem(Repository repository) {
        setSelectedValue(repository, true);
    }

    public void setRepositoryListModelListeners(List listeners) {
        for (Object listener : listeners) {
            repositoryListModelListeners.add((RepositoryListModelListener) listener);
        }
    }

    public void setRepositoryListSelectionListener(RepositoryListSelectionListener repositoryListSelectionListener) {
        this.repositoryListSelectionListener = repositoryListSelectionListener;
    }

    public void setRepositoryListCellRenderer(RepositoryListCellRenderer repositoryListCellRenderer) {
        this.repositoryListCellRenderer = repositoryListCellRenderer;
    }
}