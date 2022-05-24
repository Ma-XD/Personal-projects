package chess.figures;

public class Rook extends LinearFigure {

    @Override
    protected boolean incorrectMoveImpl(int dRow, int dCol) {
        return Math.abs(dRow) == Math.abs(dCol);
    }

    @Override
    public FigureName getName() {
        return FigureName.ROOK;
    }

    @Override
    protected Figure copyImpl() {
        return new Rook();
    }
}
