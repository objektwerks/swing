package client.action;

import client.model.RepositoryListModel;
import domain.Repository;

import java.awt.event.ActionEvent;
import java.util.SortedSet;

import javax.swing.AbstractAction;
import javax.swing.JTextField;

public final class SearchRepositoryListModelAction extends AbstractAction {
    private RepositoryListModel repositoryListModel;

    public SearchRepositoryListModelAction() {
        super();
    }

    public void actionPerformed(ActionEvent event) {
        JTextField searchTextField = (JTextField) event.getSource();
        String searchExpression = searchTextField.getText();
        SortedSet <Repository> repositories = repositoryListModel.getRepositories();
        for (Repository repository : repositories) {
            if (repository.getName().startsWith(searchExpression)) {
                repositoryListModel.setSelectedItem(repository);
                searchTextField.setText("");
            }
        }
    }

    public void setRepositoryListModel(RepositoryListModel repositoryListModel) {
        this.repositoryListModel = repositoryListModel;
    }
}