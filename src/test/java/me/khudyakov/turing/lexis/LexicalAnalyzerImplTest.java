package me.khudyakov.turing.lexis;

import me.khudyakov.turing.TestUtils;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LexicalAnalyzerImplTest {

    private final LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzerImpl();

    @Test
    public void analyzeBinaryIncrement() {
        String input = TestUtils.getTuringMachineExample(0);
        String expected = "[ Turing Machine that makes binary incrementation,  Example: '101111' -> '110000', startState, :, right, rules, :, {, right, :, {, [, 1, ,, 0, ], :, R,  , :, {, L, :, carry, }, }, carry, :, {, 1, :, {, write, :, 0, ,, L, }, [, 0, ,,  , ], :, {, write, :, 1, ,, L, :, done, }, }, done, :, {, }, }]";

        List<Token> tokens = lexicalAnalyzer.analyze(input);
        assertEquals(expected, tokens.toString());
    }

    @Test
    public void analyzeEqualOnesAndZeros() {
        String input = TestUtils.getTuringMachineExample(1);
        String expected = "[ Turing machine that accepts strings that contains equal number of ones and zeros,  Example: '101001' ending in state 'accept', startState, :, start, rules, :, {, start, :, {, 0, :, {, write, :, x, ,, R, :, findOne, }, 1, :, {, write, :, x, ,, R, :, findZero, },  , :, {, R, :, accept, }, }, findOne, :, {, [, 0, ,, x, ], :, R, 1, :, {, write, :, x, ,, L, :, moveBack, },  , :, {, R, :, reject, }, }, findZero, :, {, [, 1, ,, x, ], :, R, 0, :, {, write, :, x, ,, L, :, moveBack, },  , :, {, R, :, reject, }, }, moveBack, :, {, [, 0, ,, 1, ,, x, ], :, L,  , :, {, R, :, findNumber, }, }, findNumber, :, {, x, :, R, 0, :, {, write, :, x, ,, R, :, findOne, }, 1, :, {, write, :, x, ,, R, :, findZero, },  , :, {, R, :, accept, }, }, accept, :, {, }, reject, :, {, }, }]";

        List<Token> tokens = lexicalAnalyzer.analyze(input);
        assertEquals(expected, tokens.toString());
    }

    @Test
    public void analyzeEqualPrimeLength() {
        String input = TestUtils.getTuringMachineExample(2);
        String expected = "[ Turing Machine that accepts strings of ones with prime length,  Example: '11111' ending in state 'accept',  Example: '1111' ending in state 'reject', startState, :, start, rules, :, {, start, :, {, 1, :, {, R, :, checkOnlyOne, },  , :, {, L, :, reject, }, }, checkOnlyOne, :, {, 1, :, {, L, :, normalStart, },  , :, {, L, :, reject, }, }, normalStart, :, {, 1, :, R,  , :, {, write, :, d, ,, R, :, typeSecondD, }, }, typeSecondD, :, {,  , :, {, write, :, d, ,, R, :, startChecking, }, }, startChecking, :, {,  , :, {, L, :, getD, }, }, getD, :, {, d, :, {, write, :, D, ,, L, :, mark, }, m, :, {, L, :, checkAlreadyDivided, }, }, checkAlreadyDivided, :, {, m, :, L, 1, :, {, R, :, divisorCleanup, },  , :, {, R, :, checkEquals, }, }, mark, :, {, [, d, ,, m, ], :, L, 1, :, {, write, :, m, ,, R, :, goToEndAndGetD, },  , :, {, R, :, fullCleanup, }, }, goToEndAndGetD, :, {, [, m, ,, d, ], :, R, D, :, {, L, :, getD, }, }, divisorCleanup, :, {, m, :, R, D, :, {, write, :, d, ,, R, },  , :, {, L, :, getD, }, }, fullCleanup, :, {, d, :, R, m, :, {, write, :, 1, ,, R, }, D, :, {, write, :, d, ,, R, },  , :, {, L, :, increaseDivisor, }, }, increaseDivisor, :, {, [, 1, ,, d, ], :, R,  , :, {, write, :, d, ,, R, :, startChecking, }, }, checkEquals, :, {, m, :, {, write, :, 1, ,, R, :, findD2, }, }, findD2, :, {, [, m, ,, d, ,, 1, ], :, R, D, :, {, write, :, d, ,, L, :, findM2, },  , :, {, R, :, reject, }, }, findM2, :, {, [, 1, ,, d, ], :, L, m, :, {, write, :, 1, ,, R, :, findD2, },  , :, {, R, :, accept, }, }, accept, :, {, }, reject, :, {, }, }]";

        List<Token> tokens = lexicalAnalyzer.analyze(input);
        assertEquals(expected, tokens.toString());
    }

}