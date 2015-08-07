package com.ndr.app.stock.screener.dnd;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.io.Serializable;

public class CriteriaColumnTransferable implements Serializable, Transferable {
    private static final long serialVersionUID = 5151823210999420548L;
	public static final DataFlavor criteriaColumnDataFlavor = new DataFlavor(CriteriaColumnTransferable.class, "application/x-java-jvm-local-objectref;class=com.ndr.app.stock.screener.dnd.CriteriaColumnTransferable");
    public static final DataFlavor[] dataFlavors = new DataFlavor[] { criteriaColumnDataFlavor };

    private String exporter;

    public CriteriaColumnTransferable(String exporter) {
        this.exporter = exporter;
    }

    public String getExporter() {
        return exporter;
    }

    @Override
    public Object getTransferData(DataFlavor dataFlavor) throws UnsupportedFlavorException, IOException {
        return this;
    }

    @Override
    public DataFlavor[] getTransferDataFlavors() {
        return dataFlavors;
    }

    @Override
    public boolean isDataFlavorSupported(DataFlavor dataFlavor) {
        return (dataFlavors[0].equals(dataFlavor));
    }

    @Override
    public String toString() {
        return exporter;
    }    
}