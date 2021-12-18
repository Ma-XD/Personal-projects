package chess.board;

import chess.figures.Figure;

public class Cell {
    public final Colour colour;
    public Figure figure;

    public Cell(Colour colour, Figure figure) {
        this.colour = colour;
        this.figure = figure;
    }
}
