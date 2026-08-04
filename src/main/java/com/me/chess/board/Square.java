package com.me.chess.board;

import com.me.chess.pieces.Piece;
import com.me.chess.vectors.Point;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.jetbrains.annotations.Nullable;

import static com.me.chess.game.ChessGame.SQUARE_SIZE;

public class Square {
    public int x;
    public int y;
    public Board board;
    public Background background;
    public StackPane content;
    public Highlight highlight;
    private State state;
    private @Nullable Piece piece;

    public Square(Board board, Color color, int x, int y) {
        this.board = board;
        this.state = State.NONE;
        this.highlight = new Highlight(this);
        this.background = new Background(color);
        this.x = x;
        this.y = y;

        this.content = new StackPane();
        this.content.getChildren().add(background.element);
    }

    public void onClick(Runnable runnable) {
        this.content.setOnMouseClicked(event -> runnable.run());
    }

    public void setState(State state) {
        this.highlight.updateState(state);
        this.state = state;
    }

    public void setPiece(@Nullable Piece piece) {
        this.piece = piece;
        if (piece != null) this.piece.render(this.content);
    }

    public void empty() {
        this.setPiece(null);
    }

    public @Nullable Piece getPiece() {
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

    public State getState() {
        return this.state;
    }

    public void toggleSelect() {
        this.setState(this.state.toggleSelect());
    }

    public void toggleLegalMove() {
        this.setState(this.state.toggleLegalMove());
    }

    public void resetState() {
        this.setState(State.NONE);
    }

    public void toggleShowingAttacks() {
        // Can't be null cuz if we got here it's that it passed the null check in the Board constructor
        this.getPiece()
                .getLegalMoves()
                .stream()
                .map(this.board::getSquareAt)
                .forEach(Square::toggleLegalMove);

    }

    public static class Background {
        public Rectangle element;

        public Background(Color color) {
            this.element = new Rectangle(SQUARE_SIZE, SQUARE_SIZE, color);
        }
    }

    public String toString() {
        return String.format("Square(Point(%s, %s), State(%s))", this.x, this.y, this.state);
    }

    public enum State {
        NONE, SELECTED, LEGAL_MOVE;

        /**
         * Will replace NONE with SELECTED
         * Will replace SELECTED with NONE
         * Will replace LEGAL_MOVE with NONE
         */
        public State toggleSelect() {
            return this == NONE ? SELECTED : NONE;
        }

        public State toggleLegalMove() {
            return this == NONE ? LEGAL_MOVE : NONE;
        }
    }
}
