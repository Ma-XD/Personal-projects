package chess;

import chess.interfaces.GUI;
import chess.players.HumanPlayer;

public class Main {
    public static void main(String[] args) {
        Game game = new Game(new HumanPlayer(), new HumanPlayer(), new GUI());
        game.play();
    }
}
