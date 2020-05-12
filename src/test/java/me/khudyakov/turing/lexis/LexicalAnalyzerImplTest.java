package me.khudyakov.turing.lexis;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LexicalAnalyzerImplTest {

    private final LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzerImpl();

    @Test
    public void analyze() {
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
        String expected = "[startState, :, right, rules, :, {, right, :, {, [, 1, ,, 0, ], :, R,  , :, {, L, :, carry, }, }, carry, :, {, 1, :, {, write, :, 0, ,, L, }, [, 0, ,,  , ], :, {, write, :, 1, ,, L, :, done, }, }, done, :, {, }, }]";

        List<Token> tokens = lexicalAnalyzer.analyze(input);
        assertEquals(expected, tokens.toString());
    }

}