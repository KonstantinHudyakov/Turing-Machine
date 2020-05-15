package me.khudyakov.turing.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ExamplesParser {

    private final PropertyReader propertyReader = new PropertyReader();

    public List<Example> loadExamples() throws IOException {
        List<Example> examples = new ArrayList<>();
        File examplesFolder = getExamplesFolder();

        File[] exampleFiles = examplesFolder.listFiles();
        if (exampleFiles != null) {
            for (File file : exampleFiles) {
                examples.add(readExample(file));
            }
        }

        return examples;
    }

    private File getExamplesFolder() throws FileNotFoundException {
        URL url = this.getClass().getClassLoader().getResource("examples");
        if (url != null) {
            return new File(url.getPath());
        }

        throw new FileNotFoundException("Not found folder: examples");
    }

    private Example readExample(File exampleFile) throws IOException {
        Map<String, String> exampleMap = propertyReader.read(exampleFile);
        if (!exampleMap.containsKey("name")
                || !exampleMap.containsKey("definition")
                || !exampleMap.containsKey("input")) {
            throw new IOException("Not found needed fields in example file: " + exampleFile.getName());
        }
        return new Example(exampleMap.get("name"), exampleMap.get("definition"), exampleMap.get("input"));
    }
}
