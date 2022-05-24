package chess.figures;

public class Bishop extends LinearFigure {

    @Override
    protected boolean incorrectMoveImpl(int dRow, int dCol) {
        return Math.abs(dRow) != Math.abs(dCol);
    }

    @Override
    public FigureName getName() {
        return FigureName.BISHOP;
    }

    @Override
    protected Figure copyImpl() {
        return new Bishop();
    }
}


