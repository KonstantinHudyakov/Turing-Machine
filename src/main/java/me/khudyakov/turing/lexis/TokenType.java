package me.khudyakov.turing.lexis;

public enum TokenType {
    // Keywords
    START_STATE,
    RULES,
    INPUT,

    // Delimiters
    COLON,
    COMMA,
    OPEN_BRACE,
    CLOSE_BRACE,
    OPEN_BRACKET,
    CLOSE_BRACKET,

    // Atoms
    IDENTIFIER,
    INPUT_STR
}
