package com.me.chess.vectors;

import com.me.chess.board.Board;
import com.me.chess.board.Square;
import com.me.chess.pieces.Piece;
import org.jetbrains.annotations.Nullable;

public class Point {
    public int x;
    public int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Point(Point p) {
        this.x = p.x;
        this.y = p.y;
    }

    public Point addX(int dx) {
        return new Point(this.x + dx, this.y);
    }

    public Point addY(int dy) {
        return new Point(this.x, this.y + dy);
    }

    public Point add(int dx, int dy) {
        return new Point(this.x + dx, this.y + dy);
    }

    public Point add(Point p) {
        return new Point(this.x + p.x, this.y + p.y);
    }

    public Point times(int multiplier) {
        return new Point(this.x * multiplier, this.y * multiplier);
    }

    public Point minus(Point p) {
        return new Point(this.x - p.x, this.y - p.y);
    }

    public boolean isntWithinBounds() {
        return this.x <= 0 || this.x >= 9 || this.y <= 0 || this.y >= 9;
    }

    public boolean isAnEmptySquare(Board board) {
        if (isntWithinBounds()) return false;
        return board.getSquareAt(this).isEmpty();
    }

    @SuppressWarnings("DataFlowIssue")
    public boolean isOppositeColorPiece(Board board, Piece currentPiece) {
        if (isntWithinBounds()) return false;
        Square sq = board.getSquareAt(this);

        if (sq.isEmpty()) return false;
        return sq.getPiece().isOppositeColor(currentPiece);
    }

    @SuppressWarnings("DataFlowIssue")
    public boolean isOppositeColorPieceOrEmpty(Board board, Piece currentPiece) {
        if (isntWithinBounds()) return false;
        Square sq = board.getSquareAt(this);

        if (sq.isEmpty()) return true;
        return sq.getPiece().isOppositeColor(currentPiece);
    }

    public @Nullable Square getSquare(Board board) {
        if (isntWithinBounds()) return null;
        return board.getSquareAt(this);
    }

    public @Nullable Piece getPiece(Board board) {
        Square sq = this.getSquare(board);
        return sq == null ? null : sq.getPiece();
    }

    @Override
    public String toString() {
        return String.format("(%d, %d)", this.x, this.y);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Point p) {
            return this.x == p.x && this.y == p.y;
        } else {
            return false;
        }
    }
}
