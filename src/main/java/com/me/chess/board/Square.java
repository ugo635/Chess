package com.me.chess.board;

import com.me.chess.game.renderer.Renderable;
import com.me.chess.game.renderer.impl.SquareRenderer;
import com.me.chess.pieces.Piece;
import com.me.chess.vectors.Point;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import org.jetbrains.annotations.Nullable;

public class Square implements Renderable {
    public int x;
    public int y;
    public Board board;
    public Color backgroundColor;
    public SquareRenderer renderer;
    private State state;
    private @Nullable Piece piece;

    public Square(Board board, Color color, int x, int y) {
        this.board = board;
        this.state = State.NONE;
        this.backgroundColor = color;
        this.renderer = null;
        this.x = x;
        this.y = y;

    }

    @Override
    public void render() {
        if (this.renderer == null) this.renderer = new SquareRenderer(this);
        this.renderer.render();
    }

    public void setState(State state) {
        if (this.renderer != null) this.renderer.highlight.updateState(state);
        this.state = state;
    }

    public void setPiece(@Nullable Piece piece) {
        this.piece = piece;
        if (this.getPiece() != null && this.renderer != null) this.renderer.update();
    }

    /**
     * Remove the piece on this square.
     */
    public void empty() {
        this.setPiece(null);
    }

    /**
     * Remove the piece on this square & remove the square from the piece.
     */
    public void clear() {
        this.getPiece().setSquare(null);
        this.setPiece(null);
    }

    public @Nullable Piece getPiece() {
        return this.piece;
    }

    public boolean isEmpty() {
        return this.getPiece() == null;
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

    @SuppressWarnings("DataFlowIssue")
    public void toggleShowingAttacks() {
        // Can't be null cuz if we got here it's that it passed the null check in the Board constructor
        this.getPiece()
                .getLegalMoves()
                .stream()
                .map(this.board::getSquareAt)
                .forEach(Square::toggleLegalMove);

    }

    public Square renderlessCopy(Board board) {
        Square sq = new Square(board, this.backgroundColor, this.x, this.y);
        sq.setState(this.getState());
        sq.setPiece(this.getPiece() == null ? null : this.getPiece().renderlessCopy(sq));

        return sq;
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
