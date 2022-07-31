package chess.board;

import chess.Result;
import chess.figures.*;

import java.util.*;

public class Board {
    public final Map<FigureName, Integer> WHITE_COUNTS = new HashMap<>();
    public final Map<FigureName, Integer> BLACK_COUNTS = new HashMap<>();

    private final int SIZE = 8;
    private final Cell[][] cells;
    private Colour turn;
    private int movesCnt = 0;
    private String strMove = "";

    public Board() {
        cells = new Cell[SIZE][SIZE];
        Colour colour = Colour.BLACK;

        for (int row = SIZE - 1; row >= 0; row--) {
            for (int col = 0; col < SIZE; col++) {
                cells[row][col] = new Cell(colour, new Empty());
                colour = getAnotherColour(colour);
            }
            colour = getAnotherColour(colour);
        }
        for (FigureName name : FigureName.values()) {
            WHITE_COUNTS.put(name, 0);
            BLACK_COUNTS.put(name, 0);
        }
        turn = Colour.WHITE;
    }

    public Board copy() {
        Board newBoard = new Board();
        newBoard.turn = turn;
        newBoard.movesCnt = movesCnt;
        newBoard.strMove = strMove;

        for (int row = SIZE - 1; row >= 0; row--) {
            for (int col = 0; col < SIZE; col++) {
                Figure newFigure = getFigure(row, col).copy();
                newBoard.putFigure(newFigure, new Position(row, col));
            }
        }
        return newBoard;
    }

    public int getSize() {
        return SIZE;
    }

    public Colour getTurn() {
        return turn;
    }

    public int getMovesCount() {
        return movesCnt;
    }

    public String getStringMove() {
        return strMove;
    }

    public Cell getCell(int row, int col) {
        return cells[row][col];
    }

    public Figure getFigure(int row, int col) {
        return cells[row][col].figure;
    }

    public Figure getFigure(Position position) {
        return getFigure(position.row, position.col);
    }

    public boolean isInside(int row, int col) {
        return 0 <= row && row < SIZE
                && 0 <= col && col < SIZE;
    }

    public void makeStartPosition() {
        fill(FigureName.ROOK, Colour.WHITE, new String[]{"A1", "H1"});
        fill(FigureName.KNIGHT, Colour.WHITE, new String[]{"B1", "G1"});
        fill(FigureName.BISHOP, Colour.WHITE, new String[]{"C1", "F1"});
        fill(FigureName.QUEEN, Colour.WHITE, new String[]{"D1"});
        fill(FigureName.KING, Colour.WHITE, new String[]{"E1"});
        fill(FigureName.PAWN, Colour.WHITE, new String[]{"A2", "B2", "C2", "D2", "E2", "F2", "G2", "H2"});

        fill(FigureName.ROOK, Colour.BLACK, new String[]{"A8", "H8"});
        fill(FigureName.KNIGHT, Colour.BLACK, new String[]{"B8", "G8"});
        fill(FigureName.BISHOP, Colour.BLACK, new String[]{"C8", "F8"});
        fill(FigureName.QUEEN, Colour.BLACK, new String[]{"D8"});
        fill(FigureName.KING, Colour.BLACK, new String[]{"E8"});
        fill(FigureName.PAWN, Colour.BLACK, new String[]{"A7", "B7", "C7", "D7", "E7", "F7", "G7", "H7"});
    }

    private void fill(FigureName figureName, Colour colour, String[] positions) {
        for (String pos : positions) {
            createFigure(figureName, colour, pos);
        }
    }

    public void createFigure(FigureName figureName, Colour colour, String cell) {
        createFigure(figureName, colour, cell, false);
    }

    public void createFigure(FigureName figureName, Colour colour, String cell, Boolean isMoved) {
        Figure figure = switch (figureName) {
            case KING -> new King();
            case QUEEN -> new Queen();
            case BISHOP -> new Bishop();
            case KNIGHT -> new Knight();
            case ROOK -> new Rook();
            case PAWN -> new Pawn();
            case EMPTY -> new Empty();
        };

        figure.setMoveStatus(isMoved);
        figure.setColour(colour);
        putFigure(figure, new Position(cell));
    }

    private void putFigure(Figure figure, Position position) {
        Figure old = getFigure(position);
        renewCounts(old, -1);
        renewCounts(figure, 1);
        figure.setPosition(position);
        cells[position.row][position.col].figure = figure;
        renewAllMoves();
    }

    private void renewCounts(Figure figure, int add) {
        FigureName name = figure.getName();

        if (name == FigureName.EMPTY) {
            return;
        }
        if (figure.getColour() == Colour.WHITE) {
            WHITE_COUNTS.replace(name, WHITE_COUNTS.get(name) + add);
        } else {
            BLACK_COUNTS.replace(name, BLACK_COUNTS.get(name) + add);
        }
    }

    private void renewAllMoves() {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                Figure figure = getFigure(row, col);
                if (figure.getName() != FigureName.KING) {
                    figure.renewLegalMoves(this);
                }
            }
        }
        getKing(Colour.WHITE).renewLegalMoves(this);
        getKing(Colour.BLACK).renewLegalMoves(this);
    }

    public Result move(Move move) {
        int rowFrom = move.from.row;
        int colFrom = move.from.col;
        Figure figure = getFigure(rowFrom, colFrom);

        if (figure.getColour() != turn) {
            return Result.ERROR.setMessage("Invalid figure for moving: " + figure.getName() + " on " + move.from);
        }
        String message = figure + ": " + move;

        if (findPosition(figure, move.to)) {
            return testMove(figure, move, message);
        }
        return Result.ERROR.setMessage("Invalid move: " + move);
    }

    private boolean findPosition(Figure figure, Position next) {
        List<Position> legalMoves = figure.getLegalMoves();

        for (Position pos : legalMoves) {
            if (pos.equals(next)) {
                return true;
            }
        }
        return false;
    }

    private Result testMove(Figure figure, Move move, String message) {
        makeMove(move);

        if (isCastling(figure, move)) {
            message += ", CASTLING";
        }
        Result result = testCheck(getKing(turn));

        if (result == Result.CHECK) {
            return Result.ERROR.setMessage("Invalid move: " + move + ", because check");
        }
        result.setMessage(message);
        result = testReplacement(figure);

        if (result == Result.REPLACE) {
            return result;
        }
        return changeOfMove("");
    }

    private void makeMove(Move move) {
        Figure figure = getFigure(move.from);
        figure.setMoveStatus(true);
        putFigure(new Empty(), move.from);
        putFigure(figure, move.to);
    }

    private boolean isCastling(Figure figure, Move move) {
        if (figure.getName() == FigureName.KING && Math.abs(move.from.col - move.to.col) == 2) {
            int dCol = (move.to.col - move.from.col) / 2;
            int rookCol = dCol < 0 ? 0 : SIZE - 1;
            Position rookFrom = new Position(move.from.row, rookCol);
            Position rookTo = new Position(move.from.row, move.to.col - dCol);
            makeMove(new Move(rookFrom, rookTo));
            return true;
        }
        return false;
    }

    private Figure getKing(Colour colour) {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                Figure figure = getFigure(row, col);

                if (figure.getName() == FigureName.KING && figure.getColour() == colour) {
                    return figure;
                }
            }
        }

        return new Empty();
    }

    public Result changeOfMove(String message) {
        movesCnt++;
        turn = getAnotherColour(turn);
        Result result;
        result = testCheck(getKing(turn));
        result = testMate(result);
        result = testDraw(result);
        message = result.getMessage() + message;

        if (result != Result.UNKNOWN) {
            message += ", " + result;
        }
        strMove = "Move " + movesCnt + ": " + message;
        return result;
    }

    private Result testReplacement(Figure pawn) {
        if (pawn.getName() != FigureName.PAWN) {
            return Result.UNKNOWN;
        }
        if (pawn.getColour() == Colour.WHITE && pawn.getPosition().row == 0 ||
                pawn.getColour() == Colour.BLACK && pawn.getPosition().row == SIZE - 1
        ) {
            return Result.REPLACE;
        }
        return Result.UNKNOWN;
    }

    private Result testCheck(Figure king) {
        return isCheck(king) ? Result.CHECK : Result.UNKNOWN;
    }

    public boolean isCheck(Figure king) {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                Figure figure = getFigure(row, col);

                if (figure.getColour() != getAnotherColour(king.getColour())) {
                    continue;
                }
                if (findPosition(figure, king.getPosition())) {
                    return true;
                }
            }
        }
        return false;
    }

    private Result testMate(Result currentResult) {
        Board temp = this.copy();
        Colour currentColour = temp.getTurn();

        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                Figure figure = temp.getFigure(row, col);

                if (figure.getColour() != currentColour) {
                    continue;
                }
                Position from = figure.getPosition();

                for (Position to : figure.getLegalMoves()) {
                    temp.makeMove(new Move(from, to));
                    Result tempResult = temp.testCheck(temp.getKing(currentColour));
                    temp = this.copy();
                    if (tempResult == Result.CHECK) {
                        continue;
                    }
                    return currentResult;
                }
            }
        }

        if (currentResult == Result.CHECK) {
            return Result.CHECKMATE;
        } else {
            return Result.STALEMATE;
        }
    }

    private Result testDraw(Result result) {
        if (result != Result.CHECKMATE && result != Result.STALEMATE) {
            if (drawC(Colour.WHITE) || drawC(Colour.BLACK)) {
                return Result.DRAW;
            }
        }
        return result;
    }

    private boolean drawC(Colour colour) {
        return onlyLightFigures(colour) &&
                onlyLightFigures(getAnotherColour(colour)) &&
                (kings(colour) ||
                        kingsBishop(colour) ||
                        kingsKnight(colour) ||
                        kingsBishops(colour) ||
                        kingsKnights(colour)
                );
    }

    private boolean onlyLightFigures(Colour colour) {
        return getCounts(colour, FigureName.QUEEN) == 0 &&
                getCounts(colour, FigureName.ROOK) == 0 &&
                getCounts(colour, FigureName.PAWN) == 0;
    }

    private boolean kings(Colour colour) {
        return getCounts(colour, FigureName.BISHOP) == 0 &&
                getCounts(colour, FigureName.KNIGHT) == 0 &&
                getCounts(getAnotherColour(colour), FigureName.BISHOP) == 0 &&
                getCounts(getAnotherColour(colour), FigureName.KNIGHT) == 0;
    }

    private boolean kingsBishop(Colour colour) {
        return getCounts(colour, FigureName.BISHOP) == 1 &&
                getCounts(colour, FigureName.KNIGHT) == 0 &&
                getCounts(getAnotherColour(colour), FigureName.BISHOP) == 0 &&
                getCounts(getAnotherColour(colour), FigureName.KNIGHT) == 0;
    }

    private boolean kingsKnight(Colour colour) {
        return getCounts(colour, FigureName.BISHOP) == 0 &&
                getCounts(colour, FigureName.KNIGHT) == 1 &&
                getCounts(getAnotherColour(colour), FigureName.BISHOP) == 0 &&
                getCounts(getAnotherColour(colour), FigureName.KNIGHT) == 0;
    }

    private boolean kingsBishops(Colour colour) {
        return getCounts(colour, FigureName.BISHOP) == 1 &&
                getCounts(colour, FigureName.KNIGHT) == 0 &&
                getCounts(getAnotherColour(colour), FigureName.BISHOP) == 1 &&
                getCounts(getAnotherColour(colour), FigureName.KNIGHT) == 0;
    }

    private boolean kingsKnights(Colour colour) {
        return getCounts(colour, FigureName.BISHOP) == 0 &&
                getCounts(colour, FigureName.KNIGHT) == 2 &&
                getCounts(getAnotherColour(colour), FigureName.BISHOP) == 0 &&
                getCounts(getAnotherColour(colour), FigureName.KNIGHT) == 0;
    }

    private Colour getAnotherColour(Colour colour) {
        return colour == Colour.BLACK ? Colour.WHITE : Colour.BLACK;
    }

    private int getCounts(Colour colour, FigureName name) {
        if (colour == Colour.WHITE) {
            return WHITE_COUNTS.get(name);
        } else {
            return BLACK_COUNTS.get(name);
        }
    }
}