package com.ndr.app.stock.screener.table;

import com.ndr.app.stock.screener.Colors;
import com.ndr.app.stock.screener.text.TextToHtmlSplitter;

import java.awt.Color;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.decorator.ColorHighlighter;
import org.jdesktop.swingx.decorator.HighlightPredicate;
import org.jdesktop.swingx.decorator.HighlighterFactory;
import org.jdesktop.swingx.table.DefaultTableColumnModelExt;
import org.jdesktop.swingx.table.TableColumnExt;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public final class TickerCompanyTable extends JXTable {
    private static final long serialVersionUID = 4500177548890866146L;

	public void setModel(Set<List<String>> tickersAndCompanies) {
        setColumnModel(new TickerCompanyTableColumnModel("Ticker", "Company"));
        TickerCompanyTableModel model = new TickerCompanyTableModel(tickersAndCompanies);
        setModel(model);
        TableRowSorter<TickerCompanyTableModel> rowSorter = new TableRowSorter<TickerCompanyTableModel>(model);
        setRowSorter(rowSorter);
        rowSorter.toggleSortOrder(0);
    }

    @PostConstruct
    protected void build() {
        setAutoCreateColumnsFromModel(false);
        setAutoCreateRowSorter(false);
        setEditable(false);
        addHighlighter(HighlighterFactory.createAlternateStriping(Color.white, Colors.tableRowColor));
        addHighlighter(new ColorHighlighter(HighlightPredicate.ROLLOVER_ROW, null, Color.blue));
    }

    private class TickerCompanyTableColumnModel extends DefaultTableColumnModelExt {
		private static final long serialVersionUID = -247917874216571920L;

		public TickerCompanyTableColumnModel(String... columns) {
            for (int i = 0; i < columns.length; i++) {
                TableColumnExt tableColumn = new TableColumnExt(i);
                tableColumn.setHeaderValue(TextToHtmlSplitter.instance.split(columns[i]));
                if (i == 0) {
                    tableColumn.setMaxWidth(75);
                }
                addColumn(tableColumn);
            }
        }
    }

    private class TickerCompanyTableModel extends DefaultTableModel {
        private static final long serialVersionUID = -150230533020891146L;

		public TickerCompanyTableModel(Set<List<String>> tickersAndCompanies) {
            for (List<String> tickerAndCompany : tickersAndCompanies) {
                if (tickerAndCompany != null) {
                    addRow(tickerAndCompany.toArray());                    
                }
            }
        }

        @Override
        public int getColumnCount() {
            return columnModel.getColumnCount();
        }
    }
}