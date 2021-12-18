package chess.interfaces;

import chess.Result;
import chess.board.Board;
import chess.board.Cell;
import chess.board.Colour;
import chess.board.Position;
import chess.figures.Figure;
import chess.figures.FigureName;

import java.util.Map;
import java.util.Scanner;
import java.util.TreeSet;

public class CUI implements ChessUI {
    private static final Map<FigureName, Character> WHITE_FIGURES = Map.of(
            FigureName.EMPTY, '_',
            FigureName.KING, 'K',
            FigureName.QUEEN, 'Q',
            FigureName.BISHOP, 'B',
            FigureName.ROOK, 'R',
            FigureName.KNIGHT, 'H',
            FigureName.PAWN, 'P'
    );
    private static final Map<FigureName, Character> BLACK_FIGURES = Map.of(
            FigureName.EMPTY, '*',
            FigureName.KING, 'k',
            FigureName.QUEEN, 'q',
            FigureName.BISHOP, 'b',
            FigureName.ROOK, 'r',
            FigureName.KNIGHT, 'h',
            FigureName.PAWN, 'p'
    );

    private final Map<String, FigureName> INPUT_FIGURES = Map.of(
            "QUEEN", FigureName.QUEEN,
            "ROOK", FigureName.ROOK,
            "BISHOP", FigureName.BISHOP,
            "KNIGHT", FigureName.KNIGHT
    );
    private final  Map<String, Result> INPUT_ACTIONS = Map.of(
            "EXIT", Result.EXIT,
            "ROLLBACK", Result.ROLLBACK
    );

    private final TreeSet<String> cellNames = new TreeSet<>();
    private final Scanner in;

    public CUI(Scanner in) {
        this.in = in;
        createNames();
        showInterface();
    }

    public CUI() {
        this.in = new Scanner(System.in);
        createNames();
        showInterface();
    }

    private void createNames() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                cellNames.add(new Position(row, col).toString());
            }
        }
    }

    public void showInterface() {
        System.out.println("Game Interface:" + "\n\n" +
                "To make move you should write column and row of figure and cell where you want to move it." + "\n" +
                "Columns: A..H, rows: 1..8, example: E2 E4" + "\n" +
                "Keywords: EXIT (to exit), ROLLBACK <number> (roll back by number of steps)" + "\n" +
                "==================" + "\n" +
                "WHITE starts:");
    }

    @Override
    public void showBoard(Board board) {
        System.out.println(board.getStringMove());
        System.out.println("==================" + "\n" +
                stringBoard(board) + "\n" + "==================" + "\n"
        );
    }

    @Override
    public void whoseMove(Colour colour) {
        System.out.println(colour + "'s move:");
    }

    @Override
    public void endMessage(String message) {
        System.out.println("\n*" + message + "*\n");
    }

    @Override
    public void invalidMove(String message) {
        System.out.println("\n" + message + "\n" + "Try again" + "\n");
    }

    @Override
    public FigureName selectFigure(String pos) {
        while (true) {
            System.out.println("Set figure for pawn in " + pos);
            String figureName = in.next();

            if (INPUT_FIGURES.containsKey(figureName)) {
                return INPUT_FIGURES.get(figureName);
            }

            invalidMove("Invalid input for figure: " + figureName + ", choose QUEEN, ROOK, BISHOP or KNIGHT");
        }
    }

    @Override
    public Result testAction() {
        while (true) {
            System.out.println("Write move <from> <to>");
            String input = in.next();

            if (INPUT_ACTIONS.containsKey(input)) {
                return getAction(input);
            }

            String from = input;
            String to = in.next();

            if (correctInput(from, to)) {
                return Result.UNKNOWN.setMessage(from + to);
            }

            invalidMove("Invalid input: position consist of letter in A..H and number in 1..8");
        }
    }


    private Result getAction(String input) {
        if (INPUT_ACTIONS.get(input) == Result.ROLLBACK) {
            Result.ROLLBACK.setMessage(getSteps());
        }

        return INPUT_ACTIONS.get(input);
    }

    private String getSteps() {
        while (true) {
            String steps = in.next();

            if (correctSteps(steps)) {
                return steps;
            }

            invalidMove("Incorrect number of roll back steps: " + steps);
        }
    }

    private boolean correctSteps(String steps) {
        for (char ch: steps.toCharArray()) {
            if (!Character.isDigit(ch)) {
                return false;
            }
        }

        return true;
    }

    private boolean correctInput(String from, String to) {
        return cellNames.contains(from) && cellNames.contains(to);
    }

    private String stringBoard(final Board board) {
        final StringBuilder sb = new StringBuilder("   ");

        for (int col = 0; col < board.getSize(); col++) {
            sb.append(Position.strCol(col)).append(".");
        }

        for (int row = 0; row < board.getSize(); row++) {
            sb.append("\n");
            sb.append(Position.strRow(row)).append(". ");

            for (int col = 0; col < board.getSize(); col++) {
                Cell cell = board.getCell(row, col);
                Figure figure = cell.figure;
                char ch;

                if (figure.getColour() == Colour.WHITE ||
                        figure.getName() == FigureName.EMPTY && cell.colour == Colour.WHITE
                ) {
                    ch = WHITE_FIGURES.get(figure.getName());
                } else {
                    ch = BLACK_FIGURES.get(figure.getName());
                }

                sb.append(ch).append(' ');
            }
        }
        return sb.toString();
    }
}