package com.me.chess.board;

import com.me.chess.game.IllegalStateUpdate;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

import static com.me.chess.game.ChessGame.SQUARE_SIZE;

public class Highlight {
    public Rectangle highlightedLegalMoveRectangle;
    public Rectangle highlightedRectangle;
    public Square square;

    public Highlight(Square square) {
        this.square = square;

        this.highlightedLegalMoveRectangle = getHighlightedRectangle(true);
        this.highlightedRectangle = getHighlightedRectangle(false);
    }

    private Square.State getState() {
        return this.square.getState();
    }

    private Rectangle getHighlightedRectangle(boolean forLegalMoves) {
        Rectangle rect = new Rectangle(SQUARE_SIZE, SQUARE_SIZE, Color.TRANSPARENT);
        rect.setStroke(forLegalMoves ? Color.GREENYELLOW : Color.DARKVIOLET);
        rect.setStrokeType(StrokeType.INSIDE);
        rect.setStrokeWidth(2.5);

        return rect;
    }

    public void updateState(Square.State newState) {
        Square.State oldState = this.getState();

        if (newState == oldState) throw new IllegalStateUpdate(String.format("You shouldn't be able to update a state to the same one (%s)", newState));

        if (newState == Square.State.NONE) {
            if (oldState == Square.State.SELECTED) this.square.renderer.removeElement(highlightedRectangle);
            if (oldState == Square.State.LEGAL_MOVE) this.square.renderer.removeElement(highlightedLegalMoveRectangle);
        } else if (newState == Square.State.SELECTED) {
            this.square.renderer.addElement(highlightedRectangle);
        } else {
            this.square.renderer.addElement(highlightedLegalMoveRectangle);
        }
    }
}
