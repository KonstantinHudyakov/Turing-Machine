package me.khudyakov.turing;

public interface TuringMachine {

    Result execute(String input);

    class Result {
        private final String outputString;
        private final String endState;

        public Result(String outputString, String endState) {
            this.outputString = outputString;
            this.endState = endState;
        }

        public String getOutputString() {
            return outputString;
        }

        public String getEndState() {
            return endState;
        }
    }
}
