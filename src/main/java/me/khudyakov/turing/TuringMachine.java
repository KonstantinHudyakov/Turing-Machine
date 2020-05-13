package me.khudyakov.turing;

import java.util.Objects;

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

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Result result = (Result) o;
            return outputString.equals(result.outputString) &&
                    endState.equals(result.endState);
        }

        @Override
        public int hashCode() {
            return Objects.hash(outputString, endState);
        }

        @Override
        public String toString() {
            return "Result{" +
                    "outputString='" + outputString + '\'' +
                    ", endState='" + endState + '\'' +
                    '}';
        }
    }
}
