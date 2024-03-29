package chess;

import chess.interfaces.GUI;
import chess.players.HumanPlayer;
import chess.players.RandomPlayer;

public class Main {
    public static void main(String[] args) {
        Game game = new Game(new HumanPlayer(), new RandomPlayer(), new GUI());
        game.play();
    }
}
