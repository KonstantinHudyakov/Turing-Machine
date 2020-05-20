package me.khudyakov.turing.lexis;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static me.khudyakov.turing.lexis.TokenType.*;

public class LexicalAnalyzerImpl implements LexicalAnalyzer {

    public List<Token> analyze(String text) {
        List<Token> tokens = new ArrayList<>();
        text = text.replaceAll("[\t]", " ");
        int n = text.length();
        char[] arr = text.toCharArray();
        for (int i = 0; i < n; i++) {
            Token token;
            switch (arr[i]) {
                case ':': {
                    token = new Token(":", COLON);
                    break;
                }
                case '\'': {
                    String word = readUntil(text, i + 1, '\'');
                    if(" ".equals(word)) {
                        token = new Token(word, IDENTIFIER);
                    } else {
                        throw new LexicalAnalyzerException(String.format("Unexpected symbol '%c' on ind: %d", arr[i + 1], i + 1));
                    }
                    i += word.length() + 1;
                    break;
                }
                case '/': {
                    if(i + 1 > n || arr[i + 1] != '/') {
                        throw new LexicalAnalyzerException(String.format("Unexpected symbol '%c' on ind: %d", arr[i], i));
                    }
                    i++;
                    String comment = readUntil(text, i + 1, '\n');
                    token = new Token(comment, COMMENT);
                    i += comment.length() + 1;
                    break;
                }
                case ',': {
                    token = new Token(",", COMMA);
                    break;
                }
                case '[': {
                    token = new Token("[", OPEN_BRACKET);
                    break;
                }
                case ']': {
                    token = new Token("]", CLOSE_BRACKET);
                    break;
                }
                case '{': {
                    token = new Token("{", OPEN_BRACE);
                    break;
                }
                case '}': {
                    token = new Token("}", CLOSE_BRACE);
                    break;
                }
                default: {
                    if(arr[i] == ' ' || arr[i] == '\n') continue;

                    String word = readUntil(text, i, this::isDelimiter);
                    i += word.length() - 1;
                    switch (word) {
                        case "startState":
                            token = new Token(word, START_STATE);
                            break;
                        case "rules":
                            token = new Token(word, RULES);
                            break;
                        case "write":
                            token = new Token(word, WRITE);
                            break;
                        default:
                            token = new Token(word, IDENTIFIER);
                    }
                }
            }
            tokens.add(token);
        }
        return tokens;
    }

    private String readUntil(String text, int fromInd, char stopSymbol) {
        return readUntil(text, fromInd, ch -> ch == stopSymbol);
    }

    private String readUntil(String text, int fromInd, Function<Character, Boolean> stopCondition) {
        StringBuilder builder = new StringBuilder();
        int i = fromInd;
        while (i < text.length() && !stopCondition.apply(text.charAt(i))) {
            builder.append(text.charAt(i));
            i++;
        }
        if (i > text.length()) {
            throw new LexicalAnalyzerException("Unexpected end of input string on ind: " + i);
        }
        return builder.toString();
    }

    private boolean isDelimiter(char ch) {
        return ch == ':' || ch == ',' || ch == '\''
                || ch == '[' || ch == ']' || ch == '{'
                || ch == '}' || ch == '\n' || ch == '\t'
                || ch == ' ';
    }
}
