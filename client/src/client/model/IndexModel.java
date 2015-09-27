package client.model;

import domain.Index;
import domain.Repository;

import java.util.List;

public interface IndexModel {
    public Repository getRepository();
    public void setRepository(Repository repository);
    public Index getIndex();
    public void setIndex(Index index);
    public void setIndexModelListeners(List indexModelListeners);
}