package client.dialog;

import client.frame.Frame;
import client.resource.ResourceManager;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public final class SearchDialog extends JDialog {
    private ResourceManager resourceManager;
    private Frame frame;
    private JEditorPane resultsEditorPane;

    public SearchDialog() {
        super((Frame)null, true);
    }

    public void build() {
        setTitle(resourceManager.getString("search.title"));
        buildModelPanel();
        buildActionPanel();
        pack();
    }

    public void view(String [] results) {
        resultsEditorPane.setText(toHtml(results));
        resultsEditorPane.select(0, 1);
        setLocationRelativeTo(frame);
        setVisible(true);
    }

    public void setResourceManager(ResourceManager resourceManager) {
        this.resourceManager = resourceManager;
    }

    public void setFrame(Frame frame) {
        this.frame = frame;
    }

    private void buildModelPanel() {
        resultsEditorPane = new JEditorPane();
        resultsEditorPane.setContentType("text/html");
        resultsEditorPane.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultsEditorPane);
        scrollPane.setPreferredSize(new Dimension(520, 520));
        JPanel modelPanel = new JPanel(new BorderLayout());
        modelPanel.setBorder(BorderFactory.createTitledBorder(resourceManager.getString("label.results")));
        modelPanel.setBorder(BorderFactory.createEtchedBorder());
        modelPanel.add(scrollPane, BorderLayout.CENTER);
        add(modelPanel, BorderLayout.CENTER);
    }

    private void buildActionPanel() {
        JPanel actionPanel = new JPanel();
        actionPanel.setLayout(new BorderLayout());
        JButton closeButton = new JButton(resourceManager.getString("label.close"));
        closeButton.setPreferredSize(new Dimension(100, 25));
        closeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                setVisible(false);
            }
        });
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(closeButton);
        actionPanel.add(buttonPanel, BorderLayout.EAST);
        add(actionPanel, BorderLayout.SOUTH);
    }

    private String toHtml(String [] results) {
        StringBuilder buffer = new StringBuilder();
        buffer.append("<html");
        buffer.append("<body text=\"navy\">");
        buffer.append("<b>");
        buffer.append("Number of results: ");
        buffer.append(results.length);
        buffer.append("</b>");
        if (results.length > 0) {
            buffer.append("<ol>");
            for (String result : results) {
                buffer.append("<li>");
                buffer.append("<p>");
                result = result.replaceAll("&lt;b&gt;", "<b>");
                result = result.replaceAll("&lt;/b&gt;", "</b>");
                buffer.append(result);
                buffer.append("</p>");
                buffer.append("</li>");
            }
            buffer.append("</ol>");
        }
        buffer.append("</body>");
        buffer.append("</html>");
        return buffer.toString();
    }
}