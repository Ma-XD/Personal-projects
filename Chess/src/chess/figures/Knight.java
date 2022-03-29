package chess.figures;

import chess.board.Board;

import java.util.ArrayList;

public class Knight extends AbstractFigure implements Figure {
    private static final int[] distance = new int[]{1, 2};
    private static final int[] direction = new int[]{-1, 1};

    @Override
    public FigureName getName() {
        return FigureName.KNIGHT;
    }

    @Override
    public void renewAccessible(Board board) {
        moves = new ArrayList<>();

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                for (int k = 0; k < 2; k++) {
                    int row = position.row + distance[i] * direction[j] ;
                    int col = position.col + distance[1 - i] * direction[k];

                    vacantPosition(board, row, col);
                }
            }
        }
    }

    @Override
    protected Figure copyImpl() {
        return new Knight();
    }
}
