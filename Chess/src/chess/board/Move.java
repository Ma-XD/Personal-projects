package chess.board;

public record Move(Position from, Position to) {
    @Override
    public Position from() {
        return from;
    }

    @Override
    public Position to() {
        return to;
    }

    @Override
    public String toString() {
        return from + " -> " + to;
    }
}
