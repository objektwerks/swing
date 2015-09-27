package client.action;

import client.common.SwingWorker;
import client.model.EventListModel;
import client.resource.ResourceManager;
import domain.Event;
import service.DomainManager;

import java.awt.event.ActionEvent;
import java.util.SortedSet;
import java.util.Date;

import javax.swing.AbstractAction;
import javax.swing.Action;

public final class ListEventsAction extends AbstractAction {
    private ResourceManager resourceManager;
    private DomainManager domainManager;
    private EventListModel eventListModel;

    public ListEventsAction() {
        super();
    }

    public void build() {
        putValue(Action.SMALL_ICON, resourceManager.getImageIcon("icon.event"));
        putValue(Action.NAME, resourceManager.getString("action.list.events"));
    }

    public void actionPerformed(ActionEvent event) {
        Date date = eventListModel.getDate();
        new Worker(date).start();
    }

    public void setResourceManager(ResourceManager resourceManager) {
        this.resourceManager = resourceManager;
    }

    public void setDomainManager(DomainManager domainManager) {
        this.domainManager = domainManager;
    }

    public void setEventListModel(EventListModel eventListModel) {
        this.eventListModel = eventListModel;
    }

    private final class Worker extends SwingWorker {
        private Date date;

        public Worker(Date date) {
            this.date = date;
        }

        public Object construct() {
            return domainManager.listEvents(date);
        }

        public void finished() {
            SortedSet <Event> events = (SortedSet <Event>) get();
            eventListModel.setEvents(events);
            System.out.println("[ListEventsAction] : " + events.size());
        }
    }
}