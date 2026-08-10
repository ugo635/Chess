package com.me.chess.engine.evaluator;

import com.me.chess.pieces.impl.Pawn;
import com.me.chess.vectors.Point;

public class PawnEvaluator {
    public static float getValue(Pawn pawn) {
        return 1 + getAddedValue(pawn);
    }

    private static float getAddedValue(Pawn pawn) {
        Point position = pawn.getPosition();

        int rank = (position.y - pawn.START_RANK) * pawn.ONE + 1;

        float rankValue = switch (rank) {
            case 1 -> 0.0f;
            case 2 -> 0.5f;
            case 3 -> 1.0f;
            case 4 -> 1.5f;
            case 5 -> 2.0f;
            case 6 -> 2.5f;
            case 7 -> 3.0f;
            case 8 -> 3.5f;
            default -> 0.0f;
        };

        float rowValue = switch (position.x) {
            case 1, 8 -> 0.0f;
            case 2, 7 -> 0.1f;
            case 3, 6 -> 0.2f;
            case 4, 5 -> 0.3f;
            default -> 0.0f;
        };

        return rankValue + rowValue;
    }
}
