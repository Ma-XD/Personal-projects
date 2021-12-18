package chess.interfaces;

import chess.Result;
import chess.board.Board;
import chess.board.Colour;
import chess.figures.FigureName;

public interface ChessUI {

    void showBoard(Board board);

    void whoseMove(Colour colour);

    void endMessage(String message);

    void invalidMove(String message);

    FigureName selectFigure(String pos);

    Result testAction();
}
