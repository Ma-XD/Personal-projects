package chess.figures;

public class King extends LinearFigure implements Figure {

    @Override
    protected boolean incorrectMoveImpl(int row, int col, int dRow, int dCol) {
        return Math.abs(position.row - row) > 1 || Math.abs(position.col - col) > 1;
    }

    @Override
    public FigureName getName() {
        return FigureName.KING;
    }

    @Override
    protected Figure copyImpl() {
        return new King();
    }
}
