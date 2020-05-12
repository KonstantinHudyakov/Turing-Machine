package me.khudyakov.turing;

import java.util.Map;

public class TuringMachineImpl implements TuringMachine {

    private final String startState;
    // Map state and symbol to some action
    private final Map<String, Map<Character, Action>> rules;

    public TuringMachineImpl(String startState,
                             Map<String, Map<Character, Action>> rules) {
        this.startState = startState;
        this.rules = rules;
    }

    @Override
    public Result execute(String input) {
        // TODO
        return null;
    }

    public static class Action {
        private final Character symbolToWrite;
        private final Character moveDirection;
        private final String newState;

        public Action(Character symbolToWrite, Character moveDirection, String newState) {
            this.symbolToWrite = symbolToWrite;
            this.moveDirection = moveDirection;
            this.newState = newState;
        }

        public Character getSymbolToWrite() {
            return symbolToWrite;
        }

        public Character getMoveDirection() {
            return moveDirection;
        }

        public String getNewState() {
            return newState;
        }
    }
}
