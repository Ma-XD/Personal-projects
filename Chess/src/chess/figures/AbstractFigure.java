package chess.figures;

import chess.board.Board;
import chess.board.Colour;
import chess.board.Position;

import java.util.List;

public abstract class AbstractFigure implements Figure {
    protected Colour colour;
    protected Position position;
    protected boolean isMoved = false;
    protected List<Position> moves;

    @Override
    public Colour getColour() {
        return colour;
    }

    @Override
    public void setColour(Colour colour) {
        this.colour = colour;
    }

    @Override
    public Position getPosition() {
        return position;
    }

    @Override
    public void setPosition(Position position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return colour + " " + getName();
    }

    @Override
    public void setMoveStatus(boolean status) {
        isMoved = status;
    }

    @Override
    public boolean getMoveStatus() {
        return isMoved;
    }

    @Override
    public List<Position> getMoves() {
        return moves;
    }

    @Override
    public Figure copy() {
        Figure newFigure = copyImpl();
        newFigure.setColour(colour);
        newFigure.setPosition(position);
        newFigure.setMoveStatus(isMoved);

        return newFigure;
    }

    protected boolean vacantPosition(Board board, int row, int col) {
        if (!board.isInside(row, col)) {
            return false;
        }

        Figure figure = board.getFigure(row, col);
        Position position = new Position(row, col);

        if (figure.getName() != FigureName.EMPTY) {

            if (figure.getColour() != this.colour) {
                moves.add(position);
            }

            return false;
        }

        moves.add(position);
        return true;
    }

    protected abstract Figure copyImpl();
}
