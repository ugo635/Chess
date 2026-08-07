package com.me.chess.game.renderer.impl;

import com.me.chess.board.Highlight;
import com.me.chess.board.Square;
import com.me.chess.game.renderer.Renderer;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

import static com.me.chess.game.ChessGame.SQUARE_SIZE;

public class SquareRenderer implements Renderer {
    public Square square;
    public StackPane container;
    public Highlight highlight;

    public SquareRenderer(Square square) {
        this.square = square;
        this.container = new StackPane();
        this.highlight = new Highlight(this.square);
    }

    @Override
    public void render() {
        this.addElement(new Rectangle(SQUARE_SIZE, SQUARE_SIZE, square.backgroundColor));
        if (this.square.getPiece() != null) this.square.getPiece().render();
    }

    public void update() {
        if (this.square.getPiece() != null) this.square.getPiece().update();
    }

    public void onClick(Runnable runnable) {
        this.container.setOnMouseClicked(event -> runnable.run());
    }

    public void addElement(Node element) {
        this.container.getChildren().add(element);
    }

    public void removeElement(Node element) {
        this.container.getChildren().remove(element);
    }

    public void clearElements() {
        this.container.getChildren().clear();
        this.addElement(new Rectangle(SQUARE_SIZE, SQUARE_SIZE, square.backgroundColor));
    }
}
