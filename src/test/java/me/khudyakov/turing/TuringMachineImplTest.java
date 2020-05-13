package me.khudyakov.turing;

import me.khudyakov.turing.lexis.LexicalAnalyzer;
import me.khudyakov.turing.lexis.LexicalAnalyzerImpl;
import me.khudyakov.turing.lexis.Token;
import me.khudyakov.turing.syntax.SyntaxAnalyzer;
import me.khudyakov.turing.syntax.SyntaxAnalyzerImpl;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TuringMachineImplTest {

    private final LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzerImpl();
    private final SyntaxAnalyzer syntaxAnalyzer = new SyntaxAnalyzerImpl();

    @Test
    void execute() {
        String input = "startState: right\n" +
                "rules: {\n" +
                "  right: {\n" +
                "    [1,0]: R\n" +
                "    ' ': {L: carry}\n" +
                "  }\n" +
                "  carry: {\n" +
                "    1: {write: 0, L}\n" +
                "    [0, ' ']: {write: 1, L: done}\n" +
                "  }\n" +
                "  done: {}\n" +
                "}\n";

        List<Token> tokens = lexicalAnalyzer.analyze(input);
        TuringMachine turingMachine = syntaxAnalyzer.analyze(tokens);

        testInput(turingMachine, "1011", "1100", "done");
        testInput(turingMachine, "", "1", "done");
        testInput(turingMachine, "0", "1", "done");
        testInput(turingMachine, "11111111", "100000000", "done");
    }

    private void testInput(TuringMachine turingMachine, String input, String expectedOutput, String expectedEndState) {
        TuringMachine.Result result = turingMachine.execute(input);
        TuringMachine.Result expected = new TuringMachine.Result(expectedOutput, expectedEndState);
        assertEquals(expected, result);
    }
}