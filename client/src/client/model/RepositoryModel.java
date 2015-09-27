package client.model;

import domain.Repository;

import java.util.List;

public interface RepositoryModel {
    public Repository getRepository();
    public void setRepository(Repository repository);
    public void setRepositoryModelListeners(List repositoryModelListeners);
}
