package com.me.chess.vectors;

import com.me.chess.board.Board;

public class Point {
    public int x;
    public int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Point addX(int dx) {
        this.x += dx;
        return this;
    }

    public Point addY(int dy) {
        this.y += dy;
        return this;
    }

    public Point add(int dx, int dy) {
        this.x += dx;
        this.y += dy;

        return this;
    }

    public Point add(Point p) {
        this.x += p.x;
        this.y += p.y;

        return this;
    }

    public Point times(int multiplier) {
        this.x *= multiplier;
        this.y *= multiplier;

        return this;
    }

    public boolean isAnEmptySquare(Board board) {
        return board.getSquareAt(this).isEmpty();
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
