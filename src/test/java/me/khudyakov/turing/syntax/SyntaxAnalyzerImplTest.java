package me.khudyakov.turing.syntax;

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
    void analyze() {
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

        assertNotNull(turingMachine);
    }
}