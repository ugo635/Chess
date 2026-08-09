package com.me.chess.game;

import com.me.chess.board.Board;
import com.me.chess.engine.Engine;
import com.me.chess.game.renderer.impl.BoardRenderer;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

public class ChessGame {
    public static final int SQUARE_SIZE = 100;
    public GridPane grid;
    public StackPane container;
    public Scene scene;
    public Board board;
    public BoardRenderer renderer;
    public Engine engine;

    public ChessGame() {
        this.grid = new GridPane();
        this.container = new StackPane(grid);
        this.scene = new Scene(container);
        this.board = new Board(this);
        this.renderer = new BoardRenderer(board, container, grid);
        this.engine = new Engine(board);

        this.renderer.render();
    }
}
