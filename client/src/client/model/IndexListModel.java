package client.model;

import domain.Index;

import java.util.SortedSet;
import java.util.List;

public interface IndexListModel {
    public SortedSet <Index> getIndexes();
    public void setIndexes(SortedSet <Index> indexes);
    public void reset();
    public void addIndex(Index index);
    public void removeIndex(Index index);
    public Index getSelectedItem();
    public void setSelectedItem(Index index);
    public void setIndexListModelListeners(List indexListModelListeners);
}