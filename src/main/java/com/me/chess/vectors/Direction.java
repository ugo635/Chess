package com.me.chess.vectors;

public enum Direction {
    UP(new Point(0, 1)),
    DOWN(new Point(0, -1)),
    RIGHT(new Point(1, 0)),
    LEFT(new Point(-1, 0)),
    TOP_LEFT(new Point(-1, 1)),
    TOP_RIGHT(new Point(1, 1)),
    BOTTOM_RIGHT(new Point(1, -1)),
    BOTTOM_LEFT(new Point(-1, -1));

    private Point diff;

    Direction(Point diff) {
        this.diff = diff;
    }

    public Point getDiff() {
        return new Point(this.diff);
    }
}
