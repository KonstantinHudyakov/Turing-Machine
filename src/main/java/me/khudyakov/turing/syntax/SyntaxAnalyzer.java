package me.khudyakov.turing.syntax;

import me.khudyakov.turing.TuringMachine;
import me.khudyakov.turing.lexis.Token;

import java.util.List;

public interface SyntaxAnalyzer {

    TuringMachine analyze(List<Token> tokens);
}
