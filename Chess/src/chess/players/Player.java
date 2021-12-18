package chess.players;

import chess.Result;
import chess.board.Board;
import chess.interfaces.ChessUI;

public interface Player {
    Result makeMove(final Board board, final ChessUI UI);
}
