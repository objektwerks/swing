package com.ndr.app.stock.screener.tree;

import com.ndr.app.stock.screener.resource.ResourceManager;
import com.ndr.app.stock.screener.search.Searchable;
import com.ndr.model.stock.screener.Selectable;
import com.ndr.model.stock.screener.StockUniverse;

import java.awt.Color;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;

import org.jdesktop.swingx.JXTree;
import org.jdesktop.swingx.decorator.ColorHighlighter;
import org.jdesktop.swingx.decorator.HighlightPredicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class StockUniverseTree extends JXTree implements Searchable {
    @Autowired ResourceManager resourceManager;
    
    public void setModel(StockUniverse stockUniverse) {
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode();
        buildTreeNodes(rootNode, resourceManager.getString("index.tab.title"), stockUniverse.getIndexItems());
        buildTreeNodes(rootNode, resourceManager.getString("sector.tab.title"), stockUniverse.getSectorItems());
        buildTreeNodes(rootNode, resourceManager.getString("country.tab.title"), stockUniverse.getCountryItems());
        buildTreeNodes(rootNode, resourceManager.getString("region.tab.title"), stockUniverse.getRegionItems());
        buildTreeNodes(rootNode, resourceManager.getString("style.tab.title"), stockUniverse.getStyleItems());
        setModel(new DefaultTreeModel(rootNode));
    }

    public boolean search(String text) {
        return TreeSearcher.instance.search(this, text);
    }

    @PostConstruct
    protected void build() {
        setModel(new DefaultTreeModel(new DefaultMutableTreeNode()));
        setAutoscrolls(true);
        setExpandsSelectedPaths(true);
        setRootVisible(false);
        setShowsRootHandles(true);
        setRolloverEnabled(true);
        addHighlighter(new ColorHighlighter(HighlightPredicate.ROLLOVER_ROW, null, Color.blue));
        getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
    }

    private void buildTreeNodes(DefaultMutableTreeNode rootNode,
                                String category,
                                List<? extends Selectable> selectables) {
        DefaultMutableTreeNode categoryNode = new DefaultMutableTreeNode(category);
        rootNode.add(categoryNode);
        for (Selectable selectable : selectables) {
            if (selectable.isSelected()) {
                categoryNode.add(new DefaultMutableTreeNode(selectable.toString()));                
            }
        }
    }
}