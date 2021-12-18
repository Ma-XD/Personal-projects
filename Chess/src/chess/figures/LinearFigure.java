package chess.figures;

import chess.board.Board;

import java.util.ArrayList;

public abstract class LinearFigure extends AbstractFigure implements Figure {

    @Override
    public void renewAccessible(Board board) {
        moves = new ArrayList<>();

        for (int dRow = -1; dRow <= 1; dRow++) {
            for (int dCol = -1; dCol <= 1; dCol++) {

                if (dCol != 0 || dRow != 0) {
                    checkRoute(board, dRow, dCol);
                }
            }
        }
    }

    private void checkRoute(final Board board, final int dRow, final int dCol) {
        int row = position.row;
        int col = position.col;

        while (true) {
            row += dRow;
            col += dCol;

            if (incorrectMoveImpl(row, col, dRow, dCol)) {
                return;
            }

            if (!validPosition(board, row, col)) {
                return;
            }
        }
    }

    abstract boolean incorrectMoveImpl(int row, int col, int dRow, int dCol);
}
