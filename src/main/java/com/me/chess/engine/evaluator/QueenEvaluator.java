package com.me.chess.engine.evaluator;

import com.me.chess.pieces.impl.Queen;
import com.me.chess.vectors.Point;

public class QueenEvaluator {
    public static float getValue(Queen queen) {
        return 9 + getAddedValue(queen);
    }

    private static float getAddedValue(Queen queen) {
        Point position = queen.getPosition();

        float rowValue = switch (position.x) {
            case 1, 8 -> 0.0f;
            case 2, 7 -> 0.1f;
            case 3, 6 -> 0.2f;
            case 4, 5 -> 0.3f;
            default -> 0.0f;
        };

        float rankValue = switch (position.y) {
            case 1, 8 -> 0.0f;
            case 2, 7 -> 0.1f;
            case 3, 6 -> 0.2f;
            case 4, 5 -> 0.3f;
            default -> 0.0f;
        };

        return rowValue + rankValue;
    }
}