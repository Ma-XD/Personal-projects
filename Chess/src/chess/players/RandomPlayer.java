package chess.players;

import chess.Result;
import chess.board.Board;
import chess.board.Move;
import chess.board.Position;
import chess.figures.FigureName;
import chess.interfaces.ChessUI;

import java.util.Map;
import java.util.Random;

public class RandomPlayer implements Player {
    private final Random random = new Random();
    private final Map<Integer, FigureName> figureNumbers = Map.of(
            0, FigureName.QUEEN,
            1, FigureName.ROOK,
            2, FigureName.BISHOP,
            3, FigureName.KNIGHT
    );

    @Override
    public Result makeMove(Board board, final ChessUI UI) {
        int size = board.getSize();
        int rowFrom = random.nextInt(size);
        int colFrom = random.nextInt(size);
        int rowTo = random.nextInt(size);
        int colTo = random.nextInt(size);

        Position from = new Position(rowFrom, colFrom);
        Position to = new Position(rowTo, colTo);
        Result result = board.move(new Move(from, to));

        if (result == Result.REPLACE) {
            FigureName figureName = figureNumbers.get(random.nextInt(figureNumbers.size()));
            board.createFigure(figureName, board.getTurn(), to.toString());
            result = board.changeOfMove(", PAWN -> " + figureName);
        }
        return result;
    }
}
