package client.model;

import domain.Domain;

import java.util.SortedSet;

public interface MonitorListModel {
    public SortedSet <Domain> getCrawlsInProgress();
    public void setCrawlsInProgress(SortedSet <Domain> crawlsInProgress);    
}