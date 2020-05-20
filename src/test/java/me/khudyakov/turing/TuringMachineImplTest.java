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
    void executeBinaryIncrement() {
        String input = TestUtils.getTuringMachineExample(0);

        List<Token> tokens = lexicalAnalyzer.analyze(input);
        TuringMachine turingMachine = syntaxAnalyzer.analyze(tokens);

        testInput(turingMachine, "", "1", "done");
        testInput(turingMachine, "0", "1", "done");
        testInput(turingMachine, "1011", "1100", "done");
        testInput(turingMachine, "11111111", "100000000", "done");
    }

    @Test
    void executeEqualOnesAndZeros() {
        String input = TestUtils.getTuringMachineExample(1);

        List<Token> tokens = lexicalAnalyzer.analyze(input);
        TuringMachine turingMachine = syntaxAnalyzer.analyze(tokens);

        testInput(turingMachine, "", "accept");
        testInput(turingMachine, "1", "reject");
        testInput(turingMachine, "0", "reject");
        testInput(turingMachine, "10", "accept");
        testInput(turingMachine, "100110", "accept");
        testInput(turingMachine, "11110000", "accept");
        testInput(turingMachine, "101010100", "reject");
    }

    @Test
    void executePrimeLength() {
        String input = TestUtils.getTuringMachineExample(2);

        List<Token> tokens = lexicalAnalyzer.analyze(input);
        TuringMachine turingMachine = syntaxAnalyzer.analyze(tokens);

        testInput(turingMachine, "", "reject");
        testInput(turingMachine, "1", "reject");
        testInput(turingMachine, "11", "accept");
        testInput(turingMachine, "111", "accept");
        testInput(turingMachine, "1111", "reject");
        testInput(turingMachine, "11111", "accept");
        testInput(turingMachine, "111111", "reject");
        testInput(turingMachine, "1111111", "accept");
        testInput(turingMachine, "11111111", "reject");
        testInput(turingMachine, "111111111", "reject");
    }

    private void testInput(TuringMachine turingMachine, String input, String expectedOutput, String expectedEndState) {
        TuringMachine.Result result = turingMachine.execute(input);
        TuringMachine.Result expected = new TuringMachine.Result(expectedOutput, expectedEndState);
        assertEquals(expected, result);
        System.out.println(result.toString());
    }

    private void testInput(TuringMachine turingMachine, String input, String expectedEndState) {
        TuringMachine.Result result = turingMachine.execute(input);
        assertEquals(expectedEndState, result.getEndState());
        System.out.println("End state: " + result.getEndState());
    }
}