package client.panel;

import client.model.EventListModel;
import client.resource.ResourceManager;
import client.table.EventTable;
import domain.Event;
import com.toedter.calendar.JDateChooser;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public final class EventPanel extends JPanel implements EventListModel {
    private static final String REMOVE_FILTER = "all";

    private ResourceManager resourceManager;
    private EventTable eventTable;
    private Action listEventsAction;
    private JDateChooser dateChooser;
    private SortedSet <Event> events;

    public EventPanel() {
        super();
    }

    public void build() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder(resourceManager.getString("label.events")));
        buildModelPanel();
        buildActionPanel();
    }

    public Date getDate() {
        return dateChooser.getDate();
    }

    public SortedSet <Event> getEvents() {
        if (null == events) {
            events = new TreeSet <Event> ();
        }
        return events;
    }

    public void setEvents(SortedSet <Event> events) {
        this.events = events;
        eventTable.set(events);
    }

    public void setResourceManager(ResourceManager resourceManager) {
        this.resourceManager = resourceManager;
    }

    public void setEventTable(EventTable eventTable) {
        this.eventTable = eventTable;
    }

    public void setListEventsAction(Action listEventsAction) {
        this.listEventsAction = listEventsAction;
    }

    private void buildModelPanel() {
        JScrollPane scrollPane = new JScrollPane(eventTable);
        JPanel modelPanel = new JPanel(new BorderLayout());
        modelPanel.setBorder(BorderFactory.createEtchedBorder());
        modelPanel.add(scrollPane, BorderLayout.CENTER);
        add(modelPanel, BorderLayout.CENTER);
    }

    private void buildActionPanel() {
        JPanel actionPanel = new JPanel();
        actionPanel.setLayout(new BorderLayout());
        JButton listButton = new JButton(listEventsAction);
        listButton.setPreferredSize(new Dimension(100, 25));
        dateChooser = new JDateChooser(new Date());
        dateChooser.setPreferredSize(new Dimension(110, 25));
        JPanel componentPanel = new JPanel();
        componentPanel.add(listButton);
        componentPanel.add(dateChooser);
        JLabel filterLabel = new JLabel(resourceManager.getString("label.filter"), JLabel.RIGHT);
        JComboBox filterComboBox = new JComboBox();
        filterComboBox.addItem(REMOVE_FILTER);
        for (String type : Event.TYPES) {
            filterComboBox.addItem(type);
        }
        filterComboBox.addActionListener(new FilterActionListener());
        JPanel filterPanel = new JPanel();
        filterPanel.add(filterLabel);
        filterPanel.add(filterComboBox);
        actionPanel.add(componentPanel, BorderLayout.WEST);
        actionPanel.add(filterPanel, BorderLayout.EAST);
        add(actionPanel, BorderLayout.SOUTH);
    }

    private final class FilterActionListener implements ActionListener {
        public FilterActionListener() {
        }

        public void actionPerformed(ActionEvent e) {
            JComboBox filterComboBox = (JComboBox)e.getSource();
            String type = (String)filterComboBox.getSelectedItem();
            if (type.equals(REMOVE_FILTER)) {
                eventTable.set(events);
            } else {
                SortedSet <Event> filteredEvents = new TreeSet <Event> ();
                for (Event event : events) {
                    if (type.equals(event.getType())) {
                        filteredEvents.add(event);
                    }
                }
                eventTable.set(filteredEvents);
            }
        }
    }
}