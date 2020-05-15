package me.khudyakov.turing.util;

public class Example {

    private final String name;
    private final String definition;
    private final String input;

    public Example(String name, String definition, String input) {
        this.name = name;
        this.definition = definition;
        this.input = input;
    }

    public String getName() {
        return name;
    }

    public String getDefinition() {
        return definition;
    }

    public String getInput() {
        return input;
    }

    @Override
    public String toString() {
        return "Example{" +
                "name='" + name + '\'' +
                ", definition='" + definition + '\'' +
                ", input='" + input + '\'' +
                '}';
    }
}
