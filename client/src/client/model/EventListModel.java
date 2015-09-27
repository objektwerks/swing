package client.model;

import domain.Event;

import java.util.SortedSet;
import java.util.Date;

public interface EventListModel {
    public Date getDate();
    public SortedSet <Event> getEvents();
    public void setEvents(SortedSet <Event> events);
}