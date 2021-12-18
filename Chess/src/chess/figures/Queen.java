package chess.figures;

public class Queen extends LinearFigure implements Figure {

    @Override
    protected boolean incorrectMoveImpl(int row, int col, int dRow, int dCol) {
        return false;
    }

    @Override
    public FigureName getName() {
        return FigureName.QUEEN;
    }

    @Override
    protected Figure copyImpl() {
        return new Queen();
    }
}
