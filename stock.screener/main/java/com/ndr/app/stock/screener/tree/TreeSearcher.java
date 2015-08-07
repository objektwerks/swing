package com.ndr.app.stock.screener.tree;

import java.util.Enumeration;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

public enum TreeSearcher {
    instance;
    
    public boolean search(JTree tree, String text) {
        boolean textFound = false;
        DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
        DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) model.getRoot();
        Enumeration nodes = rootNode.children();
        while (nodes.hasMoreElements()) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) nodes.nextElement();
            Enumeration leafNodes = node.children();
            while (leafNodes.hasMoreElements()) {
                DefaultMutableTreeNode leafNode = (DefaultMutableTreeNode) leafNodes.nextElement();
                Object userObject = leafNode.getUserObject();
                String userObjectName = userObject.toString();
                if (userObjectName.indexOf(text) > -1 || userObjectName.toLowerCase().indexOf(text) > -1) {
                    TreePath treePath = new TreePath(leafNode.getPath());
                    tree.setSelectionPath(treePath);
                    tree.scrollPathToVisible(treePath);
                    textFound = true;
                    break;
                }
            }
        }
        return textFound;
    }
}