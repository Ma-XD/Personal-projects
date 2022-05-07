package chess.board;

import java.util.InputMismatchException;
import java.util.Objects;

public class Position {
    private static final int size = 8;
    public final int row;
    public final int col;

    public Position(int row, int col) {
        if (row < 0 || col < 0 || row >= size || col >= size) {
            throw new InputMismatchException();
        }
        this.row = row;
        this.col = col;
    }

    public Position(String strPos) {
        this(size - strPos.charAt(1) + '0',strPos.charAt(0) - 'A');
    }

    public static String strRow(int row) {
        return Integer.toString(size - row);
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
        if (o instanceof Position) {
            Position position = (Position) o;
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
