package me.khudyakov.turing.lexis;

public enum TokenType {
    // Keywords
    START_STATE,
    RULES,
    INPUT,
    WRITE,

    // Delimiters
    COLON,
    COMMA,
    OPEN_BRACE,
    CLOSE_BRACE,
    OPEN_BRACKET,
    CLOSE_BRACKET,

    // Atom
    IDENTIFIER
}
