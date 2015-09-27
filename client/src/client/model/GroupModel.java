package client.model;

import domain.Group;
import domain.Repository;

import java.util.List;

public interface GroupModel {
    public Repository getRepository();
    public void setRepository(Repository repository);
    public Group getGroup();
    public void setGroup(Group group);
    public void setGroupModelListeners(List groupModelListeners);
}