package me.khudyakov.turing.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ExamplesParser {

    private final PropertyReader propertyReader = new PropertyReader();

    public List<Example> loadExamples() throws IOException {
        List<Example> examples = new ArrayList<>();
        String[] exampleFileNames = {"/examples/binaryIncrement.txt",
                "/examples/equalOnesAndZeros.txt",
                "/examples/primeLength.txt"};

        for (String fileName : exampleFileNames) {
            InputStream resourceStream = getResource(fileName);
            examples.add(readExample(resourceStream));
        }

        return examples;
    }

    private InputStream getResource(String fileName) throws FileNotFoundException {
        InputStream inputStream = this.getClass().getResourceAsStream(fileName);
        if (inputStream != null) {
            return inputStream;
        }

        throw new FileNotFoundException("Not found resource: " + fileName);
    }

    private Example readExample(InputStream inputStream) throws IOException {
        Map<String, String> exampleMap = propertyReader.read(inputStream);
        if (!exampleMap.containsKey("name")
                || !exampleMap.containsKey("definition")
                || !exampleMap.containsKey("input")) {
            throw new IOException("Not found needed fields in example file");
        }
        return new Example(exampleMap.get("name"), exampleMap.get("definition"), exampleMap.get("input"));
    }
}
