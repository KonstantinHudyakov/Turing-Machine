package me.khudyakov.turing;

import me.khudyakov.turing.util.Example;
import me.khudyakov.turing.util.ExamplesParser;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class TestUtils {

    public static final ExamplesParser exampleParser = new ExamplesParser();

    private static List<Example> examples = Collections.emptyList();

    static {
        try {
            examples = exampleParser.loadExamples();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getTuringMachineExample(int index) {
        return examples.get(index).getDefinition();
    }
}
