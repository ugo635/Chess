package com.me.chess.board;

import com.me.chess.board.pieces.Piece;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

import static com.me.chess.ChessGame.SQUARE_SIZE;

public class Square {
    public int x;
    public int y;
    public Board board;
    public Background background;
    public StackPane content;
    public boolean isHighlighted;
    public Rectangle highlightedRectangle;
    private Piece piece;

    public Square(Board board, Color color, int x, int y) {
        this.board = board;
        this.highlightedRectangle = getHighlightedRectangle();
        this.background = new Background(color);
        this.isHighlighted = false;
        this.x = x;
        this.y = y;

        this.content = new StackPane();
        this.content.getChildren().add(background.element);
    }

    public void onClick(Runnable runnable) {
        this.content.setOnMouseClicked(event -> runnable.run());
    }

    public void setHighlighted(boolean highlight) {
        isHighlighted = highlight;

        if (highlight) {
            this.addElement(highlightedRectangle);
        } else {
            this.removeElement(highlightedRectangle);
        }
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
        if (piece != null) this.piece.render(this.content);
    }

    public void empty() {
        this.setPiece(null);
    }

    public Piece getPiece() {
        return this.piece;
    }

    public void addElement(Node element) {
        this.content.getChildren().add(element);
    }

    public void removeElement(Node element) {
        this.content.getChildren().remove(element);
    }

    public boolean isEmpty() {
        return piece == null;
    }

    public Point getPosition() {
        return new Point(this.x, this.y);
    }

    private Rectangle getHighlightedRectangle() {
        Rectangle rect = new Rectangle(SQUARE_SIZE, SQUARE_SIZE, Color.TRANSPARENT);
        rect.setStroke(Color.DARKVIOLET);
        rect.setStrokeType(StrokeType.INSIDE);
        rect.setStrokeWidth(2.5);

        return rect;
    }

    public static class Background {
        public Rectangle element;

        public Background(Color color) {
            this.element = new Rectangle(SQUARE_SIZE, SQUARE_SIZE, color);
        }
    }

    public String toString() {
        return String.format("(%s, %s)", this.x, this.y);
    }
}
