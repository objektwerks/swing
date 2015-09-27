package client.validator;

import java.awt.Component;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import javax.swing.JOptionPane;

public class Validator {
    private static final Pattern illegalCharsPattern = Pattern.compile("\\s|\\/|\\?|\\<|\\>|\\|\\:|\\*|\\|\"|\\!");

    public Validator() {
    }

    public boolean isNull(Object object) {
        return (null == object);
    }

    public boolean isNotValidName(String fileName) {
        int matches = 0;
        Matcher matcher = illegalCharsPattern.matcher(fileName);
        while (matcher.find()) {
            matches++;
        }
        return (matches != 0);
    }

    public boolean isNotMinLength(String string,
                                  int minLength) {
        boolean isNotMinLength = false;
        if (null == string || string.length() < minLength) {
            isNotMinLength = true;
        }
        return isNotMinLength;
    }

    public boolean isNotInList(String string,
                               String [] list) {
        boolean isNotInList = true;
        for (String item : list) {
            if (item.compareTo(string) == 0) {
                isNotInList = false;
                break;
            }
        }
        return isNotInList;
    }

    public boolean isNotUrl(String url) {
        boolean isNotUrl = false;
        try {
            new URL(url);
        } catch (MalformedURLException e) {
            isNotUrl = true;
        }
        return isNotUrl;
    }

    public static void showErrorDialog(String message,
                                       String title,
                                       Component parent) {
        JOptionPane.showMessageDialog(parent,
                                      message,
                                      title,
                                      JOptionPane.ERROR_MESSAGE);
    }

    public static void showErrorsDialog(List <String> errors,
                                        String title,
                                        Component parent) {
        String [] messages = errors.toArray(new String [] {});
        JOptionPane.showMessageDialog(parent,
                                      messages,
                                      title,
                                      JOptionPane.ERROR_MESSAGE);
    }
}