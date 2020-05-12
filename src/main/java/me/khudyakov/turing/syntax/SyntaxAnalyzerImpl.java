package me.khudyakov.turing.syntax;

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
 * ACTION -> MOVE_DIRECTION | { write : SYMBOL }
 *      | { write : SYMBOL, MOVE_DIRECTION }
 *      | { write : SYMBOL, MOVE_DIRECTION : ID }
 *      | { MOVE_DIRECTION : ID }
 * MOVE_DIRECTION -> L | R | H
 * INPUT_DEF -> input: [ INPUT_LIST ]
 * INPUT_LIST -> INPUT_STR | INPUT_LIST , INPUT_STR
 * INPUT_STR -> "SYMBOL_LIST"
 * SYMBOL_LIST -> SYMBOL | SYMBOL_LIST SYMBOL
 * SYMBOL -> ' ' | NOT_BLANK_SYMBOL
 * ID -> NOT_BLANK_SYMBOL | ID NOT_BLANK_SYMBOL
 * NOT_BLANK_SYMBOL -> any Unicode symbol except delimiters and brackets
 */
public class SyntaxAnalyzerImpl implements SyntaxAnalyzer {

}
