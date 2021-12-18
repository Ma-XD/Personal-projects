package chess.figures;

import chess.board.Board;
import chess.board.Colour;
import chess.board.Position;

import java.util.List;

public interface Figure {
    FigureName getName();

    Colour getColour();

    void setColour(Colour colour);

    Position getPosition();

    void setPosition(Position position);

    String toString();

    void setMoveStatus(boolean status);

    boolean getMoveStatus();

    List<Position> getMoves();

    void renewAccessible(Board board);

    Figure copy();
}
