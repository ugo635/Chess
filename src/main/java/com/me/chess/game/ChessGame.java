package com.me.chess.game;

import com.me.chess.board.Board;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

public class ChessGame {
    public static final int SQUARE_SIZE = 100;
    public GridPane grid = new GridPane();
    public StackPane container = new StackPane(grid);
    public Scene scene = new Scene(container);
    public Board board = new Board(container, grid);
}
