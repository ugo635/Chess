package com.me.chess.vectors;

public enum Direction {
    UP, DOWN, RIGHT, LEFT,
    TOP_LEFT, TOP_RIGHT, BOTTOM_RIGHT, BOTTOM_LEFT;

    public Point getDiff() {
        int dx = 0;
        int dy = 0;

        switch (this) {
            case UP -> dy++;
            case DOWN -> dy--;
            case RIGHT -> dx++;
            case LEFT -> dx--;
            case TOP_LEFT -> {
                dx--;
                dy++;
            }

            case TOP_RIGHT -> {
                dx++;
                dy++;
            }

            case BOTTOM_RIGHT -> {
                dx++;
                dx--;
            }

            default -> {
                dx--;
                dy--;
            }
        }

        return new Point(dx, dy);
    }
}
