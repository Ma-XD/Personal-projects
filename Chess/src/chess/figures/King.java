package chess.figures;

import chess.board.Board;

import java.util.ArrayList;

public class King extends AbstractFigure {

    @Override
    public FigureName getName() {
        return FigureName.KING;
    }

    @Override
    public void renewLegalMoves(Board board) {
        legalMoves = new ArrayList<>();

        for (int dRow = -1; dRow <= 1; dRow++) {
            for (int dCol = -1; dCol <= 1; dCol++) {
                if (dCol != 0 || dRow != 0) {
                    vacantPosition(board, position.row + dRow, position.col + dCol);
                }
            }
        }
        castling(board, position.col + 2);
        castling(board, position.col - 2);
    }

    private void castling(Board board, int colTo) {
        int row = position.row;
        int col = position.col;
        if (getMoveStatus() || board.isCheck(this) || !board.isInside(row, colTo)) {
            return;
        }
        int dCol = (colTo - col) / 2;
        do {
            col += dCol;
            if (board.getFigure(row, col).getName() != FigureName.EMPTY) {
                return;
            }
        } while (col != colTo);

        if (dCol < 0 && board.getFigure(row, --col).getName() != FigureName.EMPTY) {
            return;
        }
        col += dCol;
        Figure rook = board.getFigure(row, col);

        if (rook.getName() != FigureName.ROOK || rook.getMoveStatus()) {
            return;
        }
        vacantPosition(board, row, colTo);
    }

    @Override
    protected Figure copyImpl() {
        return new King();
    }
}
