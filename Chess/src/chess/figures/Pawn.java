package chess.figures;

import chess.board.Board;
import chess.board.Colour;
import chess.board.Position;

import java.util.ArrayList;

public class Pawn extends AbstractFigure implements Figure {
    private int dRow;

    public void setColour(Colour colour) {
        this.colour = colour;
        dRow = (colour == Colour.BLACK) ? 1 : -1;
    }

    @Override
    public FigureName getName() {
        return FigureName.PAWN;
    }

    @Override
    public void renewAccessible(Board board) {
        moves = new ArrayList<>();

        int row = position.row + dRow;

        for (int dCol = -1; dCol <= 1; dCol++) {
            checkRoute(board, row, dCol);
        }
    }

    private void checkRoute(final Board board, final int row, final int dCol) {
        int col = position.col + dCol;

        if (!board.isInside(row, col)) {
            return;
        }

        Figure figure = board.getFigure(row, col);
        Position position = new Position(row, col);

        if (dCol == 0 && figure.getName() == FigureName.EMPTY) {
            moves.add(position);
            int nextRow = row + dRow;

            if (board.isInside(nextRow, col) && !isMoved &&
                    board.getFigure(nextRow, col).getName() == FigureName.EMPTY
            ) {
                moves.add(new Position(nextRow, col));
            }

        } else if (dCol != 0 && figure.getName() != FigureName.EMPTY) {

            if (figure.getColour() != colour) {
                moves.add(position);
            }
        }
    }

    @Override
    protected Figure copyImpl() {
        return new Pawn();
    }
}
