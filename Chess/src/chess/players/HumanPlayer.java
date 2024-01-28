package chess.players;

import chess.Result;
import chess.board.Board;
import chess.board.Move;
import chess.board.Position;
import chess.figures.FigureName;
import chess.interfaces.ChessUI;

public class HumanPlayer implements Player {

    @Override
    public Result makeMove(final Board board, final ChessUI UI) {
        Result result = UI.testAction();

        if (result != Result.MOVE) {
            return result;
        }
        String cellFrom = result.getMessage().substring(0, 2);
        String cellTo = result.getMessage().substring(2, 4);
        result = board.move(new Move(new Position(cellFrom), new Position(cellTo)));

        if (result == Result.ERROR) {
            UI.invalidMove(result.getMessage());
            return result;
        }
        if (result == Result.REPLACE) {
            FigureName figureName = UI.selectFigure(cellTo);
            board.createFigure(figureName, board.getTurn(), cellTo);
            result = board.changeOfMove(", PAWN -> " + figureName);
        }
        return result;
    }
}
