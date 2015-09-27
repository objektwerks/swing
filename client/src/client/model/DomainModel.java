package client.model;

import domain.Domain;
import domain.Group;

import java.util.List;

public interface DomainModel {
    public Group getGroup();
    public void setGroup(Group group);
    public Domain getDomain();
    public void setDomain(Domain domain);
    public void setDomainModelListeners(List domainModelListeners);
}