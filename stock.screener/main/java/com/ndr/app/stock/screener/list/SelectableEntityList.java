package com.ndr.app.stock.screener.list;

import com.ndr.model.stock.screener.Selectable;

import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;
import javax.swing.event.ListDataEvent;

import org.jdesktop.swingx.decorator.Highlighter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public final class SelectableEntityList<E extends Selectable> extends EntityList<E> {
    private static final long serialVersionUID = -1597192416551831674L;

	public SelectableEntityList() {
        super();
        for (Highlighter highlighter : getHighlighters()) {
            removeHighlighter(highlighter);            
        }
        addMouseListener(new SelectableEntityListMouseAdapter());
        setCellRenderer(new SelectableEntityListRenderer());
    }

    public List<E> getSelectables() {
        return getEntities();
    }

    public void setSelectables(List<E> selectables) {
        setEntities(selectables);
    }

    public void setSelectables(List<E> selectables, int[] selectedIndices) {
        setEntities(selectables);
        for (int i = 0; i < entityListModel.getSize(); i++) {
            Selectable selectable = (Selectable) entityListModel.getElementAt(i);
            for (int j = 0; j < selectedIndices.length; j++) {
                if (j == i) {
                    selectable.setSelected(true);
                }
            }
        }
    }

    private class SelectableEntityListMouseAdapter extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent event) {
            int index = locationToIndex(event.getPoint());
            Selectable selectable = (Selectable) getModel().getElementAt(index);
            selectable.setSelected(!selectable.isSelected());
            Rectangle rectangle = getCellBounds(index, index);
            repaint(rectangle);
            notifyListDataListeners(entityListModel, ListDataEvent.CONTENTS_CHANGED, index, index);
        }
    }

    private class SelectableEntityListRenderer extends JCheckBox implements ListCellRenderer {
        private static final long serialVersionUID = 7731732592026602267L;

		public SelectableEntityListRenderer() {
            setBackground(UIManager.getColor("List.textBackground"));
            setForeground(UIManager.getColor("List.textForeground"));
        }

        public java.awt.Component getListCellRendererComponent(JList list,
                                                               Object value,
                                                               int index,
                                                               boolean isSelected,
                                                               boolean hasFocus) {
            setSelected(((Selectable) value).isSelected());
            setText(value.toString());
            return this;
        }
    }
}