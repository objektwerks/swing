package com.ndr.app.stock.screener.dialog;

import com.ndr.app.stock.screener.border.CompoundBorderFactory;
import com.ndr.app.stock.screener.button.ButtonSizer;
import com.ndr.app.stock.screener.button.CloseButton;
import com.ndr.app.stock.screener.button.TreeExpandCollapseToggleButton;
import com.ndr.app.stock.screener.panel.CriteriaModelPanel;
import com.ndr.app.stock.screener.panel.IndexModelPanel;
import com.ndr.app.stock.screener.resource.ResourceManager;
import com.ndr.app.stock.screener.table.PropertiesTable;
import com.ndr.app.stock.screener.text.NoteTextPane;
import com.ndr.app.stock.screener.tree.StockUniverseTree;
import com.ndr.model.stock.screener.Criteria;
import com.ndr.model.stock.screener.CriteriaModel;
import com.ndr.model.stock.screener.IndexModel;
import com.ndr.model.stock.screener.Name;
import com.ndr.model.stock.screener.RebalanceFrequencyDate;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;

import net.miginfocom.swing.MigLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public final class SummaryDialog extends JDialog {
    @Autowired private Frame frame;
    @Autowired private ResourceManager resourceManager;
    @Autowired private CriteriaModelPanel criteriaModelPanel;
    @Autowired private PropertiesTable criteriaModelPropertiesTable;
    @Autowired private NoteTextPane criteriaModelNoteTextPane;
    @Autowired private IndexModelPanel indexModelPanel;
    @Autowired private NoteTextPane indexModelNoteTextPane;
    @Autowired private PropertiesTable indexModelPropertiesTable;
    @Autowired private StockUniverseTree stockUniverseTree;
    @Autowired private TreeExpandCollapseToggleButton treeExpandCollapseToggleButton;
    @Autowired private CloseButton closeButton;

    public void view() {
        rebuild();
        setLocationRelativeTo(frame);
        setVisible(true);
    }

    @PostConstruct
    protected void build() {
        addWindowListener(new HideDialogWindowAdapter(this));
        setIconImage(resourceManager.getImage("summary.icon"));
        setTitle(resourceManager.getString("summary"));
        setLayout(new MigLayout());
        setModal(true);
        add(buildPanel(), "wrap");
        add(buildCloseButton(), "center");
        pack();
    }

    private void rebuild() {
        rebuildStockUniverse();
        rebuildCriteriaModel();
        rebuildIndexModel();
    }

    private void rebuildStockUniverse() {
        stockUniverseTree.setModel(criteriaModelPanel.getModel().getStockUniverse());
        treeExpandCollapseToggleButton.setSelected(true);
    }
    
    private void rebuildCriteriaModel() {
        CriteriaModel model = criteriaModelPanel.getModel();
        criteriaModelNoteTextPane.setText(model.getNote());
        String[] columns = new String[] { "Property", "Value" };
        List<Object[]> rows = new ArrayList<Object[]>();
        rows.add(new Object[] { "Name", model.getName() });
        rows.add(new Object[] { "Criteria:", "" });
        for (Criteria criteria : model.getCriteria()) {
            rows.add(new Object[] { "", criteria.getName() });            
        }
        rows.add(new Object[] { "Columns:", "" });
        for (Name column : model.getColumns()) {
            rows.add(new Object[] { "", column.getName() });            
        }
        criteriaModelPropertiesTable.setModel(columns, rows);
    }

    private void rebuildIndexModel() {
        IndexModel model = indexModelPanel.getModel();
        indexModelNoteTextPane.setText(model.getNote());
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        String[] columns = new String[] { "Property", "Value" };
        List<Object[]> rows = new ArrayList<Object[]>();
        rows.add(new Object[] { "Name", model.getName() });
        rows.add(new Object[] { "Start Date", dateFormatter.format(model.getStartDate()) });
        rows.add(new Object[] { "End Date", dateFormatter.format(model.getEndDate()) });
        rows.add(new Object[] { "Alternate Investment", model.getAlternateInvestment() });
        rows.add(new Object[] { "Weighting", model.getWeighting() });
        rows.add(new Object[] { "Total Return", model.isTotalReturn() });
        rows.add(new Object[] { "Rebalance Frequency", model.getRebalanceFrequency() });
        rows.add(new Object[] { "Dates:", "" });
        for (RebalanceFrequencyDate rebalanceFrequencyDate : model.getRebalanceFrequencyDates()) {
            rows.add(new Object[] { "", dateFormatter.format(rebalanceFrequencyDate.getDate()) });
        }
        indexModelPropertiesTable.setModel(columns, rows);
    }

    private JPanel buildPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.add(buildStockUniverseTreePanel());
        panel.add(buildCriteriaModelPanel());
        panel.add(buildIndexModelPanel());
        return panel;
    }

    private JPanel buildStockUniverseTreePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        CompoundBorderFactory.instance.create(panel, resourceManager.getString("stock.universe.selected"));
        panel.setPreferredSize(new Dimension(275, 400));
        panel.add(buildStockUniverseTreeScrollPane(), BorderLayout.CENTER);
        return panel;
    }

    private JScrollPane buildStockUniverseTreeScrollPane() {
        JScrollPane scrollPane = new JScrollPane(stockUniverseTree);
        JViewport viewport = new JViewport();
        viewport.setView(buildStockUniverseTreeButtonPanel());
        scrollPane.setColumnHeader(viewport);
        return scrollPane;
    }

    private JPanel buildStockUniverseTreeButtonPanel() {
        treeExpandCollapseToggleButton.setTree(stockUniverseTree);
        treeExpandCollapseToggleButton.setSelected(true);
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.add(treeExpandCollapseToggleButton);
        return panel;
    }

    private JPanel buildCriteriaModelPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        CompoundBorderFactory.instance.create(panel, resourceManager.getString("criteria.model.selected"));
        panel.setPreferredSize(new Dimension(325, 400));
        panel.add(new JScrollPane(criteriaModelPropertiesTable), BorderLayout.CENTER);
        panel.add(new JScrollPane(criteriaModelNoteTextPane), BorderLayout.SOUTH);
        return panel;
    }

    private JPanel buildIndexModelPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        CompoundBorderFactory.instance.create(panel, resourceManager.getString("index.model.selected"));
        panel.setPreferredSize(new Dimension(300, 400));
        panel.add(new JScrollPane(indexModelPropertiesTable), BorderLayout.CENTER);
        panel.add(new JScrollPane(indexModelNoteTextPane), BorderLayout.SOUTH);
        return panel;
    }

    private JButton buildCloseButton() {
        ButtonSizer.instance.setDefaultDialogSize(closeButton);
        getRootPane().setDefaultButton(closeButton);
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                setVisible(false);
            }
        });
        return closeButton;
    }
}