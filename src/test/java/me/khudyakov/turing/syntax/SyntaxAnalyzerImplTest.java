package me.khudyakov.turing.syntax;

import me.khudyakov.turing.TestUtils;
import me.khudyakov.turing.TuringMachine;
import me.khudyakov.turing.lexis.LexicalAnalyzer;
import me.khudyakov.turing.lexis.LexicalAnalyzerImpl;
import me.khudyakov.turing.lexis.Token;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SyntaxAnalyzerImplTest {

    private final LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzerImpl();
    private final SyntaxAnalyzer syntaxAnalyzer = new SyntaxAnalyzerImpl();

    @Test
    void analyzeBinaryIncrement() {
        String input = TestUtils.getTuringMachineExample(0);

        List<Token> tokens = lexicalAnalyzer.analyze(input);
        TuringMachine turingMachine = syntaxAnalyzer.analyze(tokens);

        assertNotNull(turingMachine);
    }

    @Test
    void analyzeEqualOnesAndZeros() {
        String input = TestUtils.getTuringMachineExample(1);

        List<Token> tokens = lexicalAnalyzer.analyze(input);
        TuringMachine turingMachine = syntaxAnalyzer.analyze(tokens);

        assertNotNull(turingMachine);
    }

    @Test
    void analyzePrimeLength() {
        String input = TestUtils.getTuringMachineExample(2);

        List<Token> tokens = lexicalAnalyzer.analyze(input);
        TuringMachine turingMachine = syntaxAnalyzer.analyze(tokens);

        assertNotNull(turingMachine);
    }
}