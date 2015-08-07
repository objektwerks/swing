package com.ndr.app.stock.screener.report;

import java.awt.Font;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Row;
import org.jfree.chart.JFreeChart;

public abstract class Report {
    public abstract void build(File file) throws IOException;
    
    protected void addTable(HSSFWorkbook workbook, JTable table, String title) {
        HSSFSheet sheet = workbook.createSheet(title);
        addTitle(sheet, title, 0);
        addColumns(sheet, table.getColumnModel(), 1);
        addRows(workbook, sheet, table, 2);
        autoSizeColumns(sheet, table.getColumnModel());
    }

    protected void addColumns(HSSFSheet sheet, TableColumnModel columnModel, int currentRow) {
        Row row = sheet.createRow(currentRow);
        for (int i = 0; i < columnModel.getColumnCount(); i++) {
            String column = columnModel.getColumn(i).getHeaderValue().toString();
            Cell cell = row.createCell(i);
            cell.setCellValue(column);
        }
    }

    protected void autoSizeColumns(HSSFSheet sheet, TableColumnModel columnModel) {
        for (int i = 0; i < columnModel.getColumnCount(); i++) {
            sheet.autoSizeColumn(i);
        }
    }

    protected void addRows(HSSFWorkbook workbook, HSSFSheet sheet, JTable table, int currentRow) {
        int rowCount = table.getRowCount();
        int columnCount = table.getColumnCount();
        for (int tableRowIndex = 0; tableRowIndex < rowCount; tableRowIndex++) {
            for (int tableColumnIndex = 0; tableColumnIndex < columnCount; tableColumnIndex++) {
                addRow(workbook, table, tableRowIndex, tableColumnIndex, sheet, currentRow);
            }
            currentRow++;
        }
    }

    protected void addRow(HSSFWorkbook workbook,
                          JTable table,
                          int row,
                          int column,
                          HSSFSheet sheet,
                          int currentRow) {
        Object cellValue = table.getValueAt(row, column);
        TableCellRenderer renderer = table.getCellRenderer(row, column);
        JLabel label = (JLabel) renderer.getTableCellRendererComponent(table, cellValue, false, false, row, column);
        cellValue = label.getText();
        addRow(workbook, sheet, cellValue, currentRow, column);
    }

    protected void addRow(HSSFWorkbook workbook, HSSFSheet sheet, Object value, int row, int column) {
        HSSFRow hssfRow = sheet.getRow(row);
        hssfRow = (hssfRow == null) ? sheet.createRow(row) : hssfRow;
        HSSFCell cell = hssfRow.getCell(column);
        cell = (cell == null) ? hssfRow.createCell(column) : cell;
        String cellValue = (value == null) ? "" : value.toString();
        DecimalFormat decimalFormatter = new DecimalFormat("###,###,###,##0.00");
        try {
            double doubleValue = decimalFormatter.parse(cellValue).doubleValue();
            DataFormat dataFormat = workbook.createDataFormat();
            HSSFCellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setDataFormat(dataFormat.getFormat("###,###,###,##0.00"));
            cell.setCellValue(doubleValue);
        } catch (ParseException e) {
            cell.setCellValue(cellValue);
        }
        formatCell(workbook, cell);
    }

    protected void formatCell(HSSFWorkbook workbook, HSSFCell cell) {
        HSSFFont hssfFont = workbook.createFont();
        hssfFont.setFontName(Font.MONOSPACED);
        hssfFont.setFontHeightInPoints((short) 9);
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFont(hssfFont);
        cell.setCellStyle(cellStyle);
    }

    protected void addCharts(HSSFWorkbook workbook, Map<String, JFreeChart> charts) {
        for (Map.Entry<String, JFreeChart> entry : charts.entrySet()) {
            addChart(workbook, entry.getValue(), entry.getKey());
        }
    }

    protected void addChart(HSSFWorkbook workbook, JFreeChart chart, String name) {
        addChart(workbook, toByteArray(chart), name);
    }

    protected void addChart(HSSFWorkbook workbook, byte[] chart, String name) {
        int pictureIndex = workbook.addPicture(chart, HSSFWorkbook.PICTURE_TYPE_PNG);
        HSSFSheet sheet = workbook.createSheet(name);
        addTitle(sheet, name, 0);
        Drawing drawing = sheet.createDrawingPatriarch();
        CreationHelper helper = workbook.getCreationHelper();
        ClientAnchor anchor = helper.createClientAnchor();
        anchor.setRow1(1);
        anchor.setCol1(0);
        Picture picture = drawing.createPicture(anchor, pictureIndex);
        picture.resize();
    }

    protected void addTitle(HSSFSheet sheet, String title, int currentRow) {
        Row row = sheet.createRow(currentRow);
        row.setHeightInPoints(15);
        Cell cell = row.createCell(0);
        cell.setCellValue(title);
    }

    protected byte[] toByteArray(JFreeChart chart) {
        RenderedImage image = chart.createBufferedImage(900, 600);
        return toByteArray(image);
    }

    protected byte[] toByteArray(RenderedImage image) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "PNG", outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outputStream.toByteArray();
    }
}