package me.khudyakov.turing.ui;

import me.khudyakov.turing.TuringMachine;
import me.khudyakov.turing.TuringMachineImpl;

import javax.swing.*;
import java.util.List;
import java.util.stream.IntStream;

public class ExecutionManager {

    private final TuringMachineImpl.Executor turingMachine;
    private final JTable tapeView;

    public ExecutionManager(TuringMachineImpl.Executor turingMachine, JTable tapeView) {
        this.turingMachine = turingMachine;
        this.tapeView = tapeView;
    }

    public boolean doStep() {
        boolean result = turingMachine.doStep();
        editTapeView(turingMachine.getTape());
        return result;
    }

    private void editTapeView(TuringMachineImpl.Tape tape) {
        List<Character> tapeList = tape.view();
        int mid = tape.getCurIndex();
        int rowMid = tapeView.getColumnCount() / 2;

        IntStream.range(0, tapeView.getColumnCount())
                 .forEach(ind -> {
                     int tapeListCurInd = mid - rowMid + ind;
                     char curSymbol = tapeListCurInd >= 0 && tapeListCurInd < tapeList.size() ? tapeList.get(tapeListCurInd) : ' ';
                     tapeView.setValueAt(curSymbol, 0, ind);
                 });
    }

    public TuringMachine.Result getResult() {
        return turingMachine.getCurResult();
    }
}
