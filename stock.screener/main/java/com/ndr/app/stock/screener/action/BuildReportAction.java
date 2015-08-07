package com.ndr.app.stock.screener.action;

import com.ndr.app.stock.screener.report.DefaultReport;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public final class BuildReportAction extends ConfigurableAction {
    private static final long serialVersionUID = -583077503405246132L;

    @Autowired private DefaultReport defaultReport;
    private FileFilter xlsFileFilter;
    private JFileChooser fileChooser;

    @PostConstruct
    protected void build() {
        setSmallIcon(resourceManager.getImageIcon("report.icon"));
        setTooltip(resourceManager.getString("report.tooltip"));
        setEnabled(false);
        buildFileChooser();
    }

    private void buildFileChooser() {
        xlsFileFilter = new XlsFileFilter();
        fileChooser = new JFileChooser();
        fileChooser.setDialogTitle(resourceManager.getString("report.tooltip"));
        fileChooser.removeChoosableFileFilter(fileChooser.getAcceptAllFileFilter());
        fileChooser.addChoosableFileFilter(xlsFileFilter);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        JButton button = (JButton) event.getSource();
        int response = fileChooser.showSaveDialog(button);
        if (response == JFileChooser.APPROVE_OPTION) {
            try {
                File xlsFile = fileChooser.getSelectedFile();
                if (xlsFileFilter.accept(xlsFile)) {
                    defaultReport.build(xlsFile);                    
                } else {
                    JOptionPane.showMessageDialog(button, "Please, specify an Excel (.xls) file.", "Stock Screener", JOptionPane.INFORMATION_MESSAGE);                    
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(button, e.getMessage(), "Stock Screener", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class XlsFileFilter extends FileFilter {
        @Override
        public boolean accept(File file) {
            boolean isAcceptable = false;
            if (!file.isDirectory()) {
                String fileName = file.getName();
                if (fileName.endsWith(".xls")) {
                    isAcceptable = true;
                }
            }
            return isAcceptable;
        }

        @Override
        public String getDescription() {
            return "Excel (.xls) file.";
        }
    }
}