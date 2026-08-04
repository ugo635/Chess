package com.me.chess.game;

public class IllegalStateUpdate extends RuntimeException {
    public IllegalStateUpdate(String message) {
        super(message);
    }
}
