package client.listener;

import client.model.RepositoryModel;
import domain.Repository;

import javax.swing.JList;
import javax.swing.Action;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public final class RepositoryListSelectionListener implements ListSelectionListener {
    private RepositoryModel repositoryModel;
    private Action addGroupAction;
    private Action addIndexAction;

    public RepositoryListSelectionListener() {
    }

    public void valueChanged(ListSelectionEvent event) {
        JList list = (JList) event.getSource();
        int selectedIndex = list.getSelectedIndex();
        if (selectedIndex > -1) {
            Repository repository = (Repository) list.getModel().getElementAt(selectedIndex);
            repositoryModel.setRepository(repository);
            addGroupAction.setEnabled(true);
            addIndexAction.setEnabled(true);
        } else {
            addGroupAction.setEnabled(false);
            addIndexAction.setEnabled(false);
        }
    }

    public void setRepositoryModel(RepositoryModel repositoryModel) {
        this.repositoryModel = repositoryModel;
    }

    public void setAddGroupAction(Action addGroupAction) {
        this.addGroupAction = addGroupAction;
    }

    public void setAddIndexAction(Action addIndexAction) {
        this.addIndexAction = addIndexAction;
    }
}