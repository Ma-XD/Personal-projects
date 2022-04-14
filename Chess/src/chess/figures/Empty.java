package chess.figures;

import chess.board.Board;
import chess.board.Colour;

public class Empty extends AbstractFigure {

    @Override
    public FigureName getName() {
        return FigureName.EMPTY;
    }

    @Override
    public void renewLegalMoves(Board board) {
    }

    @Override
    public Colour getColour() {
        return null;
    }

    @Override
    public void setColour(Colour colour) {
    }

    @Override
    public String toString() {
        return getName().toString();
    }

    @Override
    protected Figure copyImpl() {
        return new Empty();
    }
}
