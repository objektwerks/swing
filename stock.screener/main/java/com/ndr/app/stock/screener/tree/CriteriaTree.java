package com.ndr.app.stock.screener.tree;

import com.ndr.app.stock.screener.button.AddColumnButton;
import com.ndr.app.stock.screener.button.AddCriteriaButton;
import com.ndr.app.stock.screener.list.ColumnList;
import com.ndr.app.stock.screener.panel.CriteriaModelPanel;
import com.ndr.app.stock.screener.search.Searchable;
import com.ndr.app.stock.screener.text.CriteriaTextPane;
import com.ndr.model.stock.screener.Category;
import com.ndr.model.stock.screener.Criteria;
import com.ndr.model.stock.screener.Name;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.annotation.PostConstruct;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.jdesktop.swingx.JXTree;
import org.jdesktop.swingx.decorator.ColorHighlighter;
import org.jdesktop.swingx.decorator.HighlightPredicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public final class CriteriaTree extends JXTree implements Searchable {
    private static final long serialVersionUID = 5364729538938725720L;
    
	@Autowired private CriteriaModelPanel criteriaModelPanel;
    @Autowired private ColumnList<Name> columnList;
    @Autowired private CriteriaTextPane criteriaTextPane;
    @Autowired private AddCriteriaButton addCriteriaButton;
    @Autowired private AddColumnButton addColumnButton;

    private Map<Name, Criteria> criteriaMap;

    public void setModel(List<Criteria> prototypeCriteria, List<Criteria> userCriteria) {
        Collections.sort(prototypeCriteria);
        Collections.sort(userCriteria);
        setPrototypeCriteria(buildCategoryCriteriaMap(prototypeCriteria));
        setUserCriteria(userCriteria);
        setCriteriaLookupMap(prototypeCriteria, userCriteria);
    }

    private SortedMap<Category, List<Criteria>> buildCategoryCriteriaMap(List<Criteria> criteria) {
        SortedMap<Category, List<Criteria>> categoryCriteriaMap = new TreeMap<Category, List<Criteria>>();
        Set<Category> categories = new HashSet<Category>();
        for (Criteria criterion : criteria) {
            categories.add(criterion.getCategory());
        }
        for (Category category : categories) {
            List<Criteria> criteriaList = new ArrayList<Criteria>();
            for (Criteria criterion : criteria) {
                if (criterion.getCategory().equals(category)) {
                    criteriaList.add(criterion);
                }
            }
            categoryCriteriaMap.put(category, criteriaList);
        }
        return categoryCriteriaMap;
    }

    private void setPrototypeCriteria(SortedMap<Category, List<Criteria>> prototypeCriteria) {
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode();
        for (Map.Entry<Category, List<Criteria>> entry : prototypeCriteria.entrySet()) {
            Category category = entry.getKey();
            List<Criteria> criteria = entry.getValue();
            DefaultMutableTreeNode categoryNode = new DefaultMutableTreeNode(category);
            rootNode.add(categoryNode);
            for (Criteria criterion : criteria) {
                categoryNode.add(new DefaultMutableTreeNode(criterion));                
            }
        }
        setModel(new DefaultTreeModel(rootNode));
    }

    @SuppressWarnings("unchecked")
	private void setUserCriteria(List<Criteria> userCriteria) {
        Map<String, Criteria> userCriteriaMap = new HashMap<String, Criteria>();
        for (Criteria criterion : userCriteria) {
            userCriteriaMap.put(criterion.getNameAsText(), criterion);
        }
        DefaultTreeModel model = (DefaultTreeModel) getModel();
        DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) model.getRoot();
        Enumeration nodes = rootNode.children();
        while (nodes.hasMoreElements()) {
            DefaultMutableTreeNode categoryNode = (DefaultMutableTreeNode) nodes.nextElement();
            Enumeration criteriaLeafNodes = categoryNode.children();
            while (criteriaLeafNodes.hasMoreElements()) {
                DefaultMutableTreeNode criteriaLeafNode = (DefaultMutableTreeNode) criteriaLeafNodes.nextElement();
                Criteria criteria = (Criteria) criteriaLeafNode.getUserObject();
                Criteria userCriterion = userCriteriaMap.get(criteria.getNameAsText());
                if (userCriterion != null) {
                    criteriaLeafNode.setUserObject(userCriterion);
                    model.reload(criteriaLeafNode);
                }
            }
        }
    }

    private void setCriteriaLookupMap(List<Criteria> prototypeCriteria, List<Criteria> userCriteria) {
        criteriaMap.clear();
        for (Criteria criteria : prototypeCriteria) {
            criteriaMap.put(criteria.getName(), criteria);
        }
        for (Criteria criteria : userCriteria) {
            criteriaMap.put(criteria.getName(), criteria);
        }
    }

    public Criteria getSelectedCriteria() {
        addCriteriaButton.setEnabled(false);
        addColumnButton.setEnabled(false);
        return (Criteria) getSelectedNode().getUserObject();
    }

    @SuppressWarnings("unchecked")
	public Criteria getCriteriaByName(Name name) {
        Criteria criteria = criteriaMap.get(name);
        DefaultTreeModel model = (DefaultTreeModel) getModel();
        DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) model.getRoot();
        Enumeration nodes = rootNode.children();
        while (nodes.hasMoreElements()) {
            DefaultMutableTreeNode categoryNode = (DefaultMutableTreeNode) nodes.nextElement();
            Enumeration criteriaLeafNodes = categoryNode.children();
            while (criteriaLeafNodes.hasMoreElements()) {
                DefaultMutableTreeNode criteriaLeafNode = (DefaultMutableTreeNode) criteriaLeafNodes.nextElement();
                Criteria criterion = (Criteria) criteriaLeafNode.getUserObject();
                if (criterion.equals(criteria)) {
                    model.reload(criteriaLeafNode);
                    break;
                }
            }
        }
        return criteria;
    }

    public void deselectCriteriaByName(Name column) {
        Criteria criteria = criteriaMap.get(column);
        deselectCriteria(criteria);
    }

    @SuppressWarnings("unchecked")
	public void deselectCriteria(Criteria criteria) {
        String categoryName = criteria.getCategory().getName();
        DefaultTreeModel model = (DefaultTreeModel) getModel();
        DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) model.getRoot();
        Enumeration nodes = rootNode.children();
        while (nodes.hasMoreElements()) {
            DefaultMutableTreeNode categoryNode = (DefaultMutableTreeNode) nodes.nextElement();
            Category category = (Category) categoryNode.getUserObject();
            if (category.getName().equals(categoryName)) {
                Enumeration criteriaLeafNodes = categoryNode.children();
                while (criteriaLeafNodes.hasMoreElements()) {
                    DefaultMutableTreeNode criteriaLeafNode = (DefaultMutableTreeNode) criteriaLeafNodes.nextElement();
                    Criteria criterion = (Criteria) criteriaLeafNode.getUserObject();
                    if (criterion.equals(criteria)) {
                        model.reload(criteriaLeafNode);
                        DefaultMutableTreeNode selectedNode = getSelectedNode();
                        if (selectedNode != null && selectedNode.getUserObject() instanceof Criteria) {
                            if (selectedNode.equals(criteriaLeafNode)) {
                                handleSelectedNode(criteriaLeafNode);
                            }
                        }
                        break;
                    }
                }
            }
        }
    }

    public boolean search(String text) {
        return TreeSearcher.instance.search(this, text);
    }

    @PostConstruct
    protected void build() {
        criteriaMap = new HashMap<Name, Criteria>();        
        setModel(new DefaultTreeModel(new DefaultMutableTreeNode()));
        setAutoscrolls(true);
        setExpandsSelectedPaths(true);
        setRootVisible(false);
        setShowsRootHandles(true);
        setRolloverEnabled(true);
        addHighlighter(new ColorHighlighter(HighlightPredicate.ROLLOVER_ROW, null, Color.blue));
        getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        setCellRenderer(new CriteriaTreeCellRenderer());
        addTreeSelectionListener(new CriteriaTreeSelectionListener());
    }

    private void handleSelectedNode(DefaultMutableTreeNode node) {
        if (node.isLeaf()) {
            Criteria criteria = (Criteria) node.getUserObject();
            criteriaTextPane.setText(criteria.getDescriptionAsText());
            if (criteriaModelPanel.containsCriteria(criteria) || columnList.containsEntity(criteria.getName())) {
                addCriteriaButton.setEnabled(false);
                addColumnButton.setEnabled(false);
            } else {
                addCriteriaButton.setEnabled(true);
                addColumnButton.setEnabled(true);                                
            }
        } else {
            criteriaTextPane.setText("");
            addCriteriaButton.setEnabled(false);
            addColumnButton.setEnabled(false);
        }
    }

    private DefaultMutableTreeNode getSelectedNode() {
        TreePath treePath = getLeadSelectionPath();
        DefaultMutableTreeNode node = null;
        if (treePath != null) {
            node = (DefaultMutableTreeNode) treePath.getPathComponent(treePath.getPathCount() - 1);
        }
        return node;
    }

    private class CriteriaTreeCellRenderer extends DefaultTreeCellRenderer {
        private static final long serialVersionUID = -3952394378812450954L;

		public CriteriaTreeCellRenderer() {
            setPreferredSize(new Dimension(200, 20));
        }

        @Override
        public java.awt.Component getTreeCellRendererComponent(JTree tree,
                                                               Object value,
                                                               boolean isSelected,
                                                               boolean isExpanded,
                                                               boolean isLeaf,
                                                               int row,
                                                               boolean hasFocus) {
            super.getTreeCellRendererComponent(tree, value, isSelected, isExpanded, isLeaf, row, hasFocus);
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
            if (node != null) {
                if (!node.isLeaf()) {
                    Category category = (Category) node.getUserObject();
                    if (category != null) {
                        setText(category.getName());
                    }
                } else {
                    Criteria criteria = (Criteria) node.getUserObject();
                    if (criteria != null) {
                        if (criteriaModelPanel.containsCriteria(criteria) || columnList.containsEntity(criteria.getName())) {
                            setText("(" + criteria.getName() + ")");
                        } else {
                            setText(criteria.getNameAsText());
                        }
                    }
                }
            }
            return this;
        }
    }

    private class CriteriaTreeSelectionListener implements TreeSelectionListener {
        @Override
        public void valueChanged(TreeSelectionEvent event) {
            TreePath treePath = event.getNewLeadSelectionPath();
            if (treePath != null) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) treePath.getLastPathComponent();
                handleSelectedNode(node);
            }
        }
    }
}