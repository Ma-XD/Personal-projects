package chess;

import chess.board.Board;
import chess.board.Colour;
import chess.figures.FigureName;
import chess.interfaces.ChessUI;
import chess.players.Player;

import java.util.ArrayDeque;

public class Game {
    private final Player player1;
    private final Player player2;
    private Board board = new Board();
    private final ChessUI UI;
    private final ArrayDeque<Board> history = new ArrayDeque<>();

    public Game(Player player1, Player player2, ChessUI ui) {
        this.player1 = player1;
        this.player2 = player2;
        this.UI = ui;
    }

    private enum Case {
        END, CONTINUE, CHANGE, START
    }

    public void play() {
        startPosition();
        Player player = player1;
        showBoard();

        while (true) {
            history.push(board.copy());
            Result result = player.makeMove(board, UI);
            Case aCase = getCase(result);

            switch (aCase) {
                case CHANGE -> player = (player == player1) ? player2 : player1;
                case START -> player = player1;
                case END -> {
                    return;
                }
                default -> throw new IllegalArgumentException("Unexpected value: " + aCase);
            }
        }
    }

    private Case getCase(Result result) {
        switch (result) {
            case ERROR -> {
                board = history.pop();
                return Case.CONTINUE;
            }

            case ROLLBACK -> {
                int steps = Integer.parseInt(result.getMessage());
                board = getPrevious(steps);
                showBoard();
                UI.whoseMove(board.getTurn());
                return Case.CONTINUE;
            }

            case RESTART -> {
                history.clear();
                board = new Board();
                startPosition();
                showBoard();
                return Case.START;
            }

            case EXIT -> {
                UI.endMessage("EXIT");
                return Case.END;
            }

            case CHECKMATE, STALEMATE, DRAW -> {
                showBoard();
                UI.endMessage(result.toString());
                return nextAction();
            }

            default -> {
                showBoard();
                UI.whoseMove(board.getTurn());
                return Case.CHANGE;
            }
        }
    }

    private Case nextAction() {
        history.push(board.copy());

        while (true) {
            Result result = UI.testAction();

            switch (result) {
                case ROLLBACK, RESTART, EXIT -> {
                    return getCase(result);
                }
            }
        }
    }

    private Board getPrevious(int steps) {
        for (int i = 0; i < steps; i++) {
            if (history.size() == 1) {
                break;
            }
            history.pop();
        }

        return history.pop();
    }

    private void startPosition() {
        board.makeStartPosition();
        //somePosition();
    }

    private void somePosition() {
        //board.createFigure(FigureName.QUEEN, Colour.BLACK, "A8");
        board.createFigure(FigureName.KING, Colour.BLACK, "A8", true);
        board.createFigure(FigureName.KING, Colour.WHITE, "B6", true);
        board.createFigure(FigureName.PAWN, Colour.WHITE, "F6", true);
    }

    private void showBoard() {
        UI.showBoard(board);
    }
}
