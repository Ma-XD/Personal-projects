package chess;

public enum Result {
    CHECK, CHECKMATE, DRAW, STALEMATE, CASTLING, UNKNOWN, ERROR, REPLACE, EXIT, ROLLBACK, RESTART;

    private static String message = "";

    public Result setMessage(String message) {
        Result.message = message;
        return this;
    }

    public String getMessage() {
        return message;
    }
}
