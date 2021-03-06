package me.khudyakov.turing.ui;

import me.khudyakov.turing.TuringMachine;
import me.khudyakov.turing.TuringMachineImpl;
import me.khudyakov.turing.lexis.LexicalAnalyzer;
import me.khudyakov.turing.lexis.LexicalAnalyzerException;
import me.khudyakov.turing.lexis.LexicalAnalyzerImpl;
import me.khudyakov.turing.lexis.Token;
import me.khudyakov.turing.syntax.SyntaxAnalyzer;
import me.khudyakov.turing.syntax.SyntaxAnalyzerException;
import me.khudyakov.turing.syntax.SyntaxAnalyzerImpl;
import me.khudyakov.turing.util.Example;
import me.khudyakov.turing.util.ExamplesParser;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.List;
import java.util.stream.IntStream;

public class EditorGUI extends JFrame {

    private final LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzerImpl();
    private final SyntaxAnalyzer syntaxAnalyzer = new SyntaxAnalyzerImpl();
    private final ExamplesParser examplesParser = new ExamplesParser();

    private ExecutionManager executionManager;

    private JTextArea codeArea;
    private JTextArea outputArea;
    private JTable tape;
    private JTextField inputField;
    private final Font font = new Font("Serif", Font.PLAIN, 20);

    public EditorGUI() {
        super("Turing Machine Interpreter");
        JPanel codeAreaPanel = createCodeAreaPanel();
        JScrollPane outputAreaScroll = createOutputArea();
        JPanel centerPane = createCenterPane();

        createEditorWindow(codeAreaPanel, outputAreaScroll, centerPane);
    }

    private void createEditorWindow(JPanel codeAreaPanel, JScrollPane outputAreaScroll, JPanel centerPane) {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setJMenuBar(createMenuBar());

        JPanel contentPane = new JPanel(new BorderLayout(0, 5));
        contentPane.setBorder(BorderFactory.createBevelBorder(0, Color.WHITE, Color.WHITE));
        setContentPane(contentPane);

        add(centerPane, BorderLayout.CENTER);
        add(codeAreaPanel, BorderLayout.NORTH);
        add(outputAreaScroll, BorderLayout.SOUTH);

        pack();
        // Centers application on screen
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

    private JPanel createCenterPane() {
        JPanel centerPane = new JPanel(new BorderLayout());

        tape = createTape();
        JPanel inputPane = createInputPanel();
        JPanel runButtons = createRunButtons();

        centerPane.add(tape, BorderLayout.CENTER);
        centerPane.add(inputPane, BorderLayout.NORTH);
        centerPane.add(runButtons, BorderLayout.SOUTH);

        return centerPane;
    }

    private JTable createTape() {
        JTable tape = new JTable(1, 13);
        tape.setEnabled(false);
        tape.setCellSelectionEnabled(false);
        tape.setRowHeight(50);
        IntStream.range(0, tape.getColumnCount())
                 .forEach(ind -> tape.getColumnModel().getColumn(ind).setMaxWidth(60));
        tape.setFont(new Font("Serif", Font.BOLD, 40));

        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
        cellRenderer.setHorizontalAlignment(JLabel.CENTER);
        tape.getColumnModel()
            .getColumns()
            .asIterator()
            .forEachRemaining(column -> column.setCellRenderer(cellRenderer));
        return tape;
    }

    private JPanel createInputPanel() {
        JPanel inputPane = new JPanel();

        JLabel inputLabel = new JLabel("Input string: ", JLabel.TRAILING);
        inputField = new JTextField(10);
        JButton loadButton = createLoadButton();
        inputLabel.setFont(font);
        inputField.setFont(font);


        inputPane.add(inputLabel);
        inputPane.add(inputField);
        inputPane.add(loadButton);

        return inputPane;
    }

    private JButton createLoadButton() {
        JButton loadButton = new JButton("Load");
        loadButton.setFont(font);
        loadButton.addActionListener(this::handleLoadButtonClick);

        return loadButton;
    }

    private JPanel createRunButtons() {
        JPanel panel = new JPanel();
        JButton stepButton = createStepButton();

        panel.add(stepButton);

        return panel;
    }

    private JButton createStepButton() {
        JButton step = new JButton("Step");
        step.setFont(font);
        step.addActionListener(event -> {
            if (executionManager != null) {
                if (!executionManager.doStep()) {
                    TuringMachine.Result result = executionManager.getResult();
                    outputArea.setText(result.toString());
                }
            }
        });

        return step;
    }

    private JPanel createCodeAreaPanel() {
        JPanel codeAreaPanel = new JPanel(new BorderLayout());

        codeArea = new JTextArea(10, 50);
        codeArea.setEditable(true);
        codeArea.setBorder(BorderFactory.createLoweredBevelBorder());
        codeArea.setFont(new Font("Consolas", Font.PLAIN, 16));
        JScrollPane codeAreaScroll = new JScrollPane(codeArea);

        JLabel codeAreaLabel = new JLabel("Define your Turing Machine:");
        codeAreaLabel.setFont(font);

        codeAreaPanel.add(codeAreaScroll, BorderLayout.CENTER);
        codeAreaPanel.add(codeAreaLabel, BorderLayout.NORTH);

        return codeAreaPanel;
    }

    private JScrollPane createOutputArea() {
        outputArea = new JTextArea(5, 50);
        outputArea.setEditable(false);
        outputArea.setBorder(BorderFactory.createLoweredBevelBorder());
        outputArea.setFont(new Font("Consolas", Font.PLAIN, 16));

        return new JScrollPane(outputArea);
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        menuBar.add(fileMenu());
        menuBar.add(examplesMenu());

        return menuBar;
    }

    private JMenu fileMenu() {
        JMenu fileMenu = new JMenu("File");
        fileMenu.setPreferredSize(new Dimension(40, 20));

        JMenuItem exit = new JMenuItem("Exit");
        exit.setPreferredSize(new Dimension(100, 20));
        exit.setEnabled(true);
        exit.addActionListener(e -> System.exit(0));

        fileMenu.add(exit);
        return fileMenu;
    }

    private JMenu examplesMenu() {
        JMenu examplesMenu = new JMenu("Examples");

        try {
            List<Example> examples = examplesParser.loadExamples();
            examples.stream()
                    .map(example -> {
                        JMenuItem menuItem = new JMenuItem(example.getName());
                        menuItem.addActionListener(event -> {
                            codeArea.setText(example.getDefinition());
                            inputField.setText(example.getInput());
                            handleLoadButtonClick(null);
                        });
                        return menuItem;
                    })
                    .forEach(examplesMenu::add);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return examplesMenu;
    }

    private void handleLoadButtonClick(ActionEvent actionEvent) {
        String input = inputField.getText();
        addStringToTape(input);

        TuringMachineImpl turingMachine = createTuringMachine();
        if (turingMachine != null) {
            executionManager = new ExecutionManager(turingMachine.executor(input), tape);
            outputArea.setText("");
        } else {
            executionManager = null;
        }
    }

    private TuringMachineImpl createTuringMachine() {
        String definition = codeArea.getText();
        TuringMachineImpl result = null;

        try {
            List<Token> tokens = lexicalAnalyzer.analyze(definition);
            result = (TuringMachineImpl) syntaxAnalyzer.analyze(tokens);
        } catch (LexicalAnalyzerException | SyntaxAnalyzerException ex) {
            outputArea.setText(ex.getMessage());
        }

        return result;
    }

    private void addStringToTape(String input) {
        int mid = tape.getColumnCount() / 2;
        clearTape();
        IntStream.range(mid, Math.min(mid + input.length(), tape.getColumnCount()))
                 .forEach(ind -> tape.setValueAt(input.charAt(ind - mid), 0, ind));
    }

    private void clearTape() {
        IntStream.range(0, tape.getColumnCount())
                 .forEach(ind -> tape.setValueAt(' ', 0, ind));
    }
}