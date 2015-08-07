package com.ndr.app.stock.screener.text;

public enum TextToHtmlSplitter {
    instance;

    public String split(String text) {
        StringBuilder builder = new StringBuilder();
        if (text != null) {
            String[] items = text.split(" ");
            builder.append("<html>");
            for (String item : items) {
                builder.append("<center>");
                builder.append(item);
                builder.append("</center>");
            }
            builder.append("</html>");
        }
        return builder.toString();
    }
}