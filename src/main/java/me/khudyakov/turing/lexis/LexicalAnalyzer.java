package me.khudyakov.turing.lexis;

import java.util.List;

public interface LexicalAnalyzer {

    List<Token> analyze(String text);
}
