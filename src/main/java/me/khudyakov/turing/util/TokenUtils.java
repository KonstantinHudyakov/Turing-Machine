package me.khudyakov.turing.util;

import me.khudyakov.turing.lexis.Token;
import me.khudyakov.turing.lexis.TokenType;

import static me.khudyakov.turing.lexis.TokenType.IDENTIFIER;

public class TokenUtils {

    public static boolean isTokenOfType(Token token, TokenType type) {
        return token.getType() == type;
    }

    public static boolean isSymbol(Token token) {
        return token.getType() == IDENTIFIER
                && token.getValue().length() == 1;
    }

    public static boolean isDirection(Token token) {
        return token.getType() == IDENTIFIER
                && ("L".equals(token.getValue()) || "R".equals(token.getValue()));
    }
}
