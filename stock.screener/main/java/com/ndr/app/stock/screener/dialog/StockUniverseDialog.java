package com.ndr.app.stock.screener.dialog;

import com.ndr.app.stock.screener.border.CompoundBorderFactory;
import com.ndr.app.stock.screener.button.CloseButton;
import com.ndr.app.stock.screener.button.TreeExpandCollapseToggleButton;
import com.ndr.app.stock.screener.list.SelectableEntityList;
import com.ndr.app.stock.screener.resource.ResourceManager;
import com.ndr.app.stock.screener.search.SearchBar;
import com.ndr.app.stock.screener.tree.StockUniverseTree;
import com.ndr.model.stock.screener.CountryItem;
import com.ndr.model.stock.screener.IndexItem;
import com.ndr.model.stock.screener.RegionItem;
import com.ndr.model.stock.screener.SectorItem;
import com.ndr.model.stock.screener.StockUniverse;
import com.ndr.model.stock.screener.StyleItem;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.annotation.PostConstruct;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JViewport;
import javax.swing.WindowConstants;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public final class StockUniverseDialog extends JDialog {
    private static final long serialVersionUID = 8727670778208648561L;
    
	@Autowired private ResourceManager resourceManager;
    @Autowired private StockUniverseTree stockUniverseTree;
    @Autowired private SelectableEntityList<IndexItem> indexItemList;
    @Autowired private SelectableEntityList<SectorItem> sectorItemList;
    @Autowired private SelectableEntityList<CountryItem> countryItemList;
    @Autowired private SelectableEntityList<RegionItem> regionItemList;
    @Autowired private SelectableEntityList<StyleItem> styleItemList;
    @Autowired private TreeExpandCollapseToggleButton treeExpandCollapseToggleButton;
    @Autowired private SearchBar searchBar;
    @Autowired private CloseButton closeButton;
    private StockUniverseListDataListener stockUniverseListDataListener;

    public void view(JComponent owner) {
        setLocationRelativeTo(owner);
        setVisible(true);
    }

    public void setModel(StockUniverse stockUniverse) {
        stockUniverseTree.setModel(stockUniverse);
        indexItemList.setSelectables(stockUniverse.getIndexItems());
        sectorItemList.setSelectables(stockUniverse.getSectorItems());
        countryItemList.setSelectables(stockUniverse.getCountryItems());
        regionItemList.setSelectables(stockUniverse.getRegionItems());
        styleItemList.setSelectables(stockUniverse.getStyleItems());
        stockUniverseListDataListener.setStockUniverse(stockUniverse);
        treeExpandCollapseToggleButton.setSelected(true);
    }

    @PostConstruct
    protected void build() {
        addWindowListener(new HideDialogWindowAdapter(this));
        stockUniverseListDataListener = new StockUniverseListDataListener(stockUniverseTree);
        indexItemList.addListDataListener(stockUniverseListDataListener);
        sectorItemList.addListDataListener(stockUniverseListDataListener);
        countryItemList.addListDataListener(stockUniverseListDataListener);
        regionItemList.addListDataListener(stockUniverseListDataListener);
        styleItemList.addListDataListener(stockUniverseListDataListener);
        treeExpandCollapseToggleButton.setTree(stockUniverseTree);
        searchBar.setSearchable(stockUniverseTree);
        setIconImage(resourceManager.getImage("stock.universe.icon"));
        setTitle(resourceManager.getString("stock.universe"));
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setModal(true);
        add(buildSplitPane(), BorderLayout.CENTER);
        pack();
    }

    private JSplitPane buildSplitPane() {
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, buildTreePanel(), buildTabbedPanel());
        splitPane.setDividerLocation(0.75);
        splitPane.setResizeWeight(0.75);
        return splitPane;
    }

    private JPanel buildTreePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        CompoundBorderFactory.instance.create(panel, resourceManager.getString("stock.universe.selected"));
        panel.setPreferredSize(new Dimension(300, 400));
        panel.add(buildScrollPane(), BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildTabbedPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        CompoundBorderFactory.instance.create(panel, resourceManager.getString("stock.universe"));
        panel.setPreferredSize(new Dimension(360, 400));
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab(resourceManager.getString("index.tab.title"), new JScrollPane(indexItemList));
        tabbedPane.addTab(resourceManager.getString("sector.tab.title"), new JScrollPane(sectorItemList));
        tabbedPane.addTab(resourceManager.getString("country.tab.title"), new JScrollPane(countryItemList));
        tabbedPane.addTab(resourceManager.getString("region.tab.title"), new JScrollPane(regionItemList));
        tabbedPane.addTab(resourceManager.getString("style.tab.title"), new JScrollPane(styleItemList));
        panel.add(tabbedPane, BorderLayout.CENTER);
        return panel;
    }

    private JScrollPane buildScrollPane() {
        JScrollPane scrollPane = new JScrollPane(stockUniverseTree);
        JViewport viewport = new JViewport();
        viewport.setView(buildButtonPanel());
        scrollPane.setColumnHeader(viewport);
        return scrollPane;
    }

    private JPanel buildButtonPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.add(treeExpandCollapseToggleButton);
        panel.add(Box.createRigidArea(new Dimension(2,0)));
        panel.add(searchBar);
        panel.add(Box.createRigidArea(new Dimension(2,0)));
        panel.add(buildCloseButton());
        return panel;
    }

    private JButton buildCloseButton() {
        getRootPane().setDefaultButton(closeButton);
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                setVisible(false);
            }
        });
        return closeButton;
    }

    private class StockUniverseListDataListener implements ListDataListener {
        private StockUniverseTree stockUniverseTree;
        private StockUniverse stockUniverse;

        public StockUniverseListDataListener(StockUniverseTree stockUniverseTree) {
            this.stockUniverseTree = stockUniverseTree;
        }

        public void setStockUniverse(StockUniverse stockUniverse) {
            this.stockUniverse = stockUniverse;
        }

        @Override
        public void contentsChanged(ListDataEvent event) {
            setStockUniverseTreeModel();
        }

        @Override
        public void intervalAdded(ListDataEvent event) {
            setStockUniverseTreeModel();
        }

        @Override
        public void intervalRemoved(ListDataEvent event) {
            setStockUniverseTreeModel();
        }

        private void setStockUniverseTreeModel() {
            if (stockUniverseTree != null && stockUniverse != null) {
                stockUniverseTree.setModel(stockUniverse);                
                treeExpandCollapseToggleButton.setSelected(true);
            }
        }
    }
}