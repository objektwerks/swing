package com.ndr.app.stock.screener.list;

import com.ndr.app.stock.screener.search.Searchable;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import org.jdesktop.swingx.JXList;
import org.jdesktop.swingx.decorator.ColorHighlighter;
import org.jdesktop.swingx.decorator.HighlightPredicate;
import org.springframework.stereotype.Component;

@Component
public class EntityList<E> extends JXList implements Searchable {
    private static final long serialVersionUID = 6557635968782583367L;
    
	protected EntityListModel entityListModel;
    protected List<E> entityModel;
    private ImageIcon entityIcon;

    public EntityList() {
        entityListModel = new EntityListModel();
        entityModel = new ArrayList<E>();
        setModel(entityListModel);
        setAutoCreateRowSorter(false);
        setRolloverEnabled(true);
        addHighlighter(new ColorHighlighter(HighlightPredicate.ROLLOVER_ROW, null, Color.blue));
        setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        setCellRenderer(new EntityCellRenderer());
    }

    public EntityList(ImageIcon entityIcon) {
        this();
        this.entityIcon = entityIcon;
    }

    public void addListDataListener(ListDataListener listener) {
        entityListModel.addListDataListener(listener);
    }

    public void removeListDataListener(ListDataListener listener) {
        entityListModel.removeListDataListener(listener);
    }

    protected void notifyListDataListeners(Object source, int type, int firstIndex, int secondIndex) {
        ListDataEvent event = new ListDataEvent(source, type, firstIndex, secondIndex);
        for (ListDataListener listener : entityListModel.getListDataListeners()) {
            listener.contentsChanged(event);
        }
    }

    public void setEntityIcon(ImageIcon entityIcon) {
        this.entityIcon = entityIcon;
    }

    public List<E> getEntities() {
        return entityModel;
    }

    public void setEntities(List<E> entities) {
        entityModel = entities;
        for (E entity : entityModel) {
            entityListModel.addElement(entity);
        }
        sort();
    }

    public void addEntity(E entity) {
        entityListModel.addElement(entity);
        entityModel.add(entity);
        sort();
        setSelectedEntity(entity);
    }

    public void removeEntity(E entity) {
        entityListModel.removeElement(entity);
        entityModel.remove(entity);
        sort();
    }

    public void removeEntities() {
        entityListModel.removeAllElements();
        entityModel.clear();
    }

    @SuppressWarnings("unchecked")
    public void removeSelectedEntity() {
        removeEntity((E) getSelectedValue());
    }

    @SuppressWarnings("unchecked")
    public E getSelectedEntity() {
        return (E) getSelectedValue();
    }

    @SuppressWarnings("unchecked")
    public E getSelectedEntity(int index) {
        return (E) getElementAt(index);
    }

    public void setSelectedEntity(E entity) {
        setSelectedValue(entity, true);
    }

    public void updateEntity(E entity) {
        entityListModel.setElementAt(entity, getSelectedIndex());
    }

    public boolean containsEntity(E entity) {
        return entityListModel.contains(entity);
    }

    public boolean isEmpty() {
        return entityListModel.isEmpty();
    }
    
    public boolean isNotEmpty() {
        return entityListModel.getSize() > 0;
    }

    public boolean search(String text) {
        boolean textFound = false;
        for (int i = 0; i < entityListModel.size(); i++) {
            String entity = entityListModel.getElementAt(i).toString();
            if (entity != null && (entity.indexOf(text) > -1 || entity.toLowerCase().indexOf(text) > -1)) {
                setSelectedIndex(i);
                textFound = true;
                break;
            }
        }
        return textFound;
    }

    @SuppressWarnings("unchecked")
    public void sort() {
        Collections.sort(entityModel, EntityComparator.instance);
        entityListModel.removeAllElements();
        for (E entity : entityModel) {
            entityListModel.addElement(entity);
        }
        if (isNotEmpty()) {
            setSelectedIndex(0);
        }
    }

    protected class EntityListModel extends DefaultListModel {
        public EntityListModel() {
            super();
        }
    }

    private class EntityCellRenderer extends DefaultListCellRenderer {
        private static final long serialVersionUID = 6389061030452741660L;

		@Override
        @SuppressWarnings("unchecked")
        public java.awt.Component getListCellRendererComponent(JList list,
                                                               Object value,
                                                               int index,
                                                               boolean isSelected,
                                                               boolean cellHasFocus) {
            E entity = (E) entityListModel.getElementAt(index);
            if (entity != null && entity.toString() != null && !entity.toString().isEmpty()) {
                setText(entity.toString());
            } else {
                setText("");
            }
            if (entityIcon != null) {
                setIcon(entityIcon);
            }
            if (isSelected) {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            } else {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }
            return this;
        }
    }

    private static class EntityComparator<E> implements Comparator<E> {
        @SuppressWarnings("unchecked")
		public static final EntityComparator instance = new EntityComparator();

        @Override
        public int compare(E firstEntity, E secondEntity) {
            int result;
            try {
                result = firstEntity.toString().compareTo(secondEntity.toString());
            } catch (Throwable ignore) {
                result = 0;
            }
            return result;
        }
    }
}