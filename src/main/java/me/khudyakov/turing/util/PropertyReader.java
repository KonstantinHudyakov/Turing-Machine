package me.khudyakov.turing.util;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class PropertyReader {

    private static final String ERROR_FORMAT = "Wrong property format on ind %d:\n%s";

    public Map<String, String> read(File file) throws IOException {
        Map<String, String> properties = new HashMap<>();
        String content = readFileContent(file);
        int curInd = 0;
        int n = content.length();
        String key = readWord(content, curInd);
        curInd += key.length();
        while (!"".equals(key)) {
            curInd = skipDelimiters(content, curInd);
            if (curInd >= n || content.charAt(curInd) != ':') {
                throw new IOException(String.format(ERROR_FORMAT, curInd, content));
            }
            curInd++;
            curInd = skipDelimiters(content, curInd);
            String propertyValue = readQuotedProperty(content, curInd);
            curInd += propertyValue.length() + 2;
            properties.put(key, propertyValue);
            curInd = skipDelimiters(content, curInd);

            key = readWord(content, curInd);
            curInd += key.length();
        }

        return properties;
    }

    private String readFileContent(File file) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        String content = reader.lines().collect(Collectors.joining("\n"));
        reader.close();
        return content;
    }

    private String readWord(String text, int fromInd) {
        int curInd = fromInd;
        while (curInd < text.length() && Character.isLetterOrDigit(text.charAt(curInd))) {
            curInd++;
        }
        return text.substring(fromInd, curInd);
    }

    private String readQuotedProperty(String text, int fromInd) throws IOException {
        int curInd = fromInd;
        int n = text.length();
        if(curInd > n || text.charAt(curInd) != '"') {
            throw new IOException(String.format(ERROR_FORMAT, curInd, text));
        }
        curInd = text.indexOf('"', curInd + 1);
        if(curInd == -1) {
            throw new IOException(String.format(ERROR_FORMAT, fromInd, text));
        }

        return text.substring(fromInd + 1, curInd);
    }

    private int skipDelimiters(String text, int fromInd) {
        while (fromInd < text.length() &&
                (text.charAt(fromInd) == ' '
                || text.charAt(fromInd) == '\n'
                || text.charAt(fromInd) == '\t')) {
            fromInd++;
        }
        return fromInd;
    }
}
