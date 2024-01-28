package chess.board;

import java.util.InputMismatchException;
import java.util.Objects;

import static chess.board.Board.SIZE;

public class Position {
    public final int row;
    public final int col;

    public Position(int row, int col) {
        if (row < 0 || col < 0 || row >= SIZE || col >= SIZE) {
            throw new InputMismatchException();
        }
        this.row = row;
        this.col = col;
    }

    public Position(String strPos) {
        this(SIZE - strPos.charAt(1) + '0',strPos.charAt(0) - 'A');
    }

    public static String strRow(int row) {
        return Integer.toString(SIZE - row);
    }

    public static String strCol(int col) {
        return Character.toString(col + 'A');
    }

    public String toString() {
        return strCol(col) + strRow(row);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof Position position) {
            return row == position.row &&
                    col == position.col;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }
}
