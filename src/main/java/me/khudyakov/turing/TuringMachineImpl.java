package me.khudyakov.turing;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

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
        Executor executor = new Executor(input);
        boolean canMove = true;
        while (canMove) {
            canMove = executor.doStep();
        }

        String output = executor.getTape()
                                .view()
                                .stream()
                                .collect(Collector.of(StringBuilder::new, StringBuilder::append, StringBuilder::append, StringBuilder::toString))
                                .trim();
        return new Result(output, executor.getCurState());
    }

    public class Executor {
        private final Tape tape;
        private String curState;

        public Executor(String input) {
            tape = new Tape(input);
            curState = startState;
        }

        public boolean doStep() {
            Map<Character, Action> curStateRules = rules.get(curState);
            char curSymbol = tape.getCur();
            Action action = curStateRules.get(curSymbol);
            if(action != null) {
                tape.typeAndMove(action.getSymbolToWrite(), action.getMoveDirection());
                curState = action.getNewState();
                return true;
            } else {
                return false;
            }
        }

        public Tape getTape() {
            return tape;
        }

        public String getCurState() {
            return curState;
        }
    }

    private static class Tape {
        private final List<Character> tape;
        private final ListIterator<Character> iterator;

        public Tape(String input) {
            tape = input.chars()
                        .mapToObj(ch -> (char) ch)
                        .collect(Collectors.toCollection(LinkedList::new));
            iterator = tape.listIterator();
        }

        public void typeAndMove(Character symbolToType, MoveDirection moveDirection) {
            iterator.set(symbolToType);
            if(moveDirection == MoveDirection.R) {
                moveToNext();
            } else {
                moveToPrev();
            }
        }

        private void moveToPrev() {
            if(!iterator.hasPrevious()) {
                iterator.add(' ');
            }
            iterator.previous();
        }

        private void moveToNext() {
            if(iterator.hasNext()) {
                iterator.next();
            } else {
                iterator.add(' ');
            }
        }

        public char getCur() {
            if(!iterator.hasNext()) {
                iterator.add(' ');
                iterator.previous();
            }
            Character cur = iterator.next();
            iterator.previous();
            return cur;
        }

        public List<Character> view() {
            return Collections.unmodifiableList(tape);
        }

        @Override
        public String toString() {
            return tape.toString();
        }
    }

    public static class Action {
        private final Character symbolToWrite;
        private final MoveDirection moveDirection;
        private final String newState;

        public Action(Character symbolToWrite, MoveDirection moveDirection, String newState) {
            this.symbolToWrite = symbolToWrite;
            this.moveDirection = moveDirection;
            this.newState = newState;
        }

        public Character getSymbolToWrite() {
            return symbolToWrite;
        }

        public MoveDirection getMoveDirection() {
            return moveDirection;
        }

        public String getNewState() {
            return newState;
        }
    }

    public enum MoveDirection {
        L, R
    }
}
