package me.khudyakov.turing.syntax;

import me.khudyakov.turing.TuringMachine;
import me.khudyakov.turing.TuringMachineImpl;
import me.khudyakov.turing.lexis.Token;
import me.khudyakov.turing.lexis.TokenType;
import me.khudyakov.turing.util.Pair;
import me.khudyakov.turing.util.TokenUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static me.khudyakov.turing.lexis.TokenType.*;
import static me.khudyakov.turing.util.TokenUtils.*;

/**
 * Grammar:
 * TURING_MACHINE -> START_DEF RULES_DEF INPUT_DEF
 * START_DEF -> startState : ID
 * RULES_DEF -> rules : { RULE_LIST }
 * RULE_LIST -> RULE | RULE_LIST RULE
 * RULE -> ID : { TRANSITION_LIST }
 * TRANSITION_LIST -> TRANSITION | TRANSITION_LIST TRANSITION
 * TRANSITION -> TRANSITION_SYMBOLS : ACTION
 * TRANSITION_SYMBOLS -> SYMBOL | [ SYMBOL_LIST ]
 * SYMBOL_LIST -> SYMBOL | SYMBOL_LIST , SYMBOL
 * ACTION -> MOVE_DIRECTION
 *      | { MOVE_DIRECTION : ID }
 *      | { write : SYMBOL, MOVE_DIRECTION }
 *      | { write : SYMBOL, MOVE_DIRECTION : ID }
 * MOVE_DIRECTION -> L | R
 * SYMBOL -> ' ' | NOT_BLANK_SYMBOL
 * ID -> NOT_BLANK_SYMBOL | ID NOT_BLANK_SYMBOL
 * NOT_BLANK_SYMBOL -> any Unicode symbol except delimiters and brackets
 */
public class SyntaxAnalyzerImpl implements SyntaxAnalyzer {

    @Override
    public TuringMachine analyze(List<Token> tokens) {
        return new TuringMachineBuilder(tokens).build();
    }

    private static class TuringMachineBuilder {

        private final List<Token> tokens;

        private int curInd = 0;

        public TuringMachineBuilder(List<Token> tokens) {
            this.tokens = tokens;
        }

        public TuringMachine build() {
            String startState = startState();
            Map<String, Map<Character, TuringMachineImpl.Action>> rules = rules();
            return new TuringMachineImpl(startState, rules);
        }

        private String startState() {
            checkTypeOfCurOrThrow(START_STATE);
            checkTypeOfCurOrThrow(COLON);
            Token cur = getCurOrThrow();
            if (!isTokenOfType(cur, IDENTIFIER)) {
                throwError(cur);
            }
            curInd++;
            return cur.getValue();
        }

        private Map<String, Map<Character, TuringMachineImpl.Action>> rules() {
            checkTypeOfCurOrThrow(RULES);
            checkTypeOfCurOrThrow(COLON);
            checkTypeOfCurOrThrow(OPEN_BRACE);
            Map<String, Map<Character, TuringMachineImpl.Action>> rules = new HashMap<>();
            while (checkTypeOfCur(IDENTIFIER)) {
                Pair<String, Map<Character, TuringMachineImpl.Action>> rule = rule();
                rules.put(rule.getFirst(), rule.getSecond());
            }
            checkTypeOfCurOrThrow(CLOSE_BRACE);

            return rules;
        }

        private Pair<String, Map<Character, TuringMachineImpl.Action>> rule() {
            Token cur = getCurOrThrow();
            checkTypeOfCurOrThrow(IDENTIFIER);
            String state = cur.getValue();
            checkTypeOfCurOrThrow(COLON);
            checkTypeOfCurOrThrow(OPEN_BRACE);
            Map<Character, TuringMachineImpl.Action> transitionsMap = new HashMap<>();
            while (!checkTypeOfCur(CLOSE_BRACE)) {
                List<Pair<Character, TuringMachineImpl.Action>> transitionList = transitionList(state);
                transitionList.forEach(pair -> transitionsMap.put(pair.getFirst(), pair.getSecond()));
            }
            checkTypeOfCurOrThrow(CLOSE_BRACE);

            return Pair.of(state, transitionsMap);
        }

        private List<Pair<Character, TuringMachineImpl.Action>> transitionList(String curState) {
            List<Character> symbols = transitionSymbols();
            checkTypeOfCurOrThrow(COLON);
            TuringMachineImpl.Action action = action(curState);

            return symbols.stream()
                          .map(symbol -> {
                              // action is always not null
                              Character newSymbol = action.getSymbolToWrite() == '\n' ? symbol : action.getSymbolToWrite();
                              TuringMachineImpl.Action newAction = new TuringMachineImpl.Action(newSymbol, action.getMoveDirection(), action.getNewState());
                              return Pair.of(symbol, newAction);
                          })
                          .collect(Collectors.toList());
        }

        private List<Character> transitionSymbols() {
            List<Character> symbols = new ArrayList<>();
            Token cur = getCurOrThrow();
            if (isSymbol(cur)) {
                symbols.add(cur.getValue().charAt(0));
                curInd++;
            } else if (isTokenOfType(cur, OPEN_BRACKET)) {
                symbols = symbolsList();
            } else {
                throwError(cur);
            }

            return symbols;
        }

        private List<Character> symbolsList() {
            List<Character> symbols = new ArrayList<>();
            checkTypeOfCurOrThrow(OPEN_BRACKET);
            Token cur = getCurOrThrow();
            checkTypeOfCurOrThrow(TokenUtils::isSymbol);
            symbols.add(cur.getValue().charAt(0));

            while (checkTypeOfCur(COMMA)) {
                curInd++;
                cur = getCurOrThrow();
                checkTypeOfCurOrThrow(TokenUtils::isSymbol);
                symbols.add(cur.getValue().charAt(0));
            }
            checkTypeOfCurOrThrow(CLOSE_BRACKET);

            return symbols;
        }

        private TuringMachineImpl.Action action(String curState) {
            Token cur = getCurOrThrow();
            if(isDirection(cur)) {
                curInd++;
                return new TuringMachineImpl.Action('\n', TuringMachineImpl.MoveDirection.valueOf(cur.getValue()), curState);
            } else if(isTokenOfType(cur, OPEN_BRACE)) {
                curInd++;
                cur = getCurOrThrow();
                if(isDirection(cur)) {
                    TuringMachineImpl.MoveDirection direction = TuringMachineImpl.MoveDirection.valueOf(cur.getValue());
                    curInd++;
                    checkTypeOfCurOrThrow(COLON);
                    cur = getCurOrThrow();
                    checkTypeOfCurOrThrow(IDENTIFIER);
                    checkTypeOfCurOrThrow(CLOSE_BRACE);
                    return new TuringMachineImpl.Action('\n', direction, cur.getValue());
                } else if(isTokenOfType(cur, WRITE)) {
                    curInd++;
                    checkTypeOfCurOrThrow(COLON);
                    cur = getCurOrThrow();
                    checkTypeOfCurOrThrow(TokenUtils::isSymbol);
                    Character symbolToWrite = cur.getValue().charAt(0);
                    checkTypeOfCurOrThrow(COMMA);
                    cur = getCurOrThrow();
                    checkTypeOfCurOrThrow(TokenUtils::isDirection);
                    TuringMachineImpl.MoveDirection direction = TuringMachineImpl.MoveDirection.valueOf(cur.getValue());
                    if(checkTypeOfCur(CLOSE_BRACE)) {
                        curInd++;
                        return new TuringMachineImpl.Action(symbolToWrite, direction, curState);
                    } else if(checkTypeOfCur(COLON)) {
                        curInd++;
                        cur = getCurOrThrow();
                        checkTypeOfCurOrThrow(IDENTIFIER);
                        checkTypeOfCurOrThrow(CLOSE_BRACE);
                        return new TuringMachineImpl.Action(symbolToWrite, direction, cur.getValue());
                    }
                }
            }

            throwError(cur);
            return null;
        }

        private boolean checkTypeOfCur(TokenType type) throws SyntaxAnalyzerException {
            return checkTypeOfCur(token -> isTokenOfType(token, type));
        }

        private boolean checkTypeOfCur(Function<Token, Boolean> condition) throws SyntaxAnalyzerException {
            Token cur = getCurOrThrow();
            return condition.apply(cur);
        }

        private void checkTypeOfCurOrThrow(TokenType type) throws SyntaxAnalyzerException {
            checkTypeOfCurOrThrow(token -> isTokenOfType(token, type));
        }

        private void checkTypeOfCurOrThrow(Function<Token, Boolean> condition) throws SyntaxAnalyzerException {
            Token cur = getCurOrThrow();
            throwIfNotTypeOf(cur, condition);
            curInd++;
        }

        private void throwIfNotTypeOf(Token token, Function<Token, Boolean> condition) throws SyntaxAnalyzerException {
            if (!condition.apply(token)) {
                throwError(token);
            }
        }

        private void throwIfNotTypeOf(Token token, TokenType type) throws SyntaxAnalyzerException {
            if (!isTokenOfType(token, type)) {
                throwError(token);
            }
        }

        private Token getCurOrThrow() throws SyntaxAnalyzerException {
            if (curInd >= tokens.size()) {
                throw new SyntaxAnalyzerException("Unexpected end of definition");
            }
            return tokens.get(curInd);
        }

        private void throwError(Token token) throws SyntaxAnalyzerException {
            throw new SyntaxAnalyzerException(String.format("Unexpected token: %s, on index: %d", token.toString(), curInd));
        }
    }
}
