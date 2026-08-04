package com.me.chess;

import com.me.chess.board.Board;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;

public class ChessGame {
    public static final int SQUARE_SIZE = 100;
    public GridPane grid = new GridPane();
    public Scene scene = new Scene(grid);
    public Board board = new Board(grid);

    public ChessGame() {
        //grid.setGridLinesVisible(true);
    }
}
