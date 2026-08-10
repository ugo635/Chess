package com.me.chess.engine.evaluator;

import com.me.chess.pieces.impl.Pawn;
import com.me.chess.vectors.Point;

public class PawnEvaluator {
    public static float getValue(Pawn pawn) {
        final Point pos = pawn.getPosition();
        final int x = pos.x;
        final int y = pos.y;

        float addedValue = 0;

        if (x == 2) addedValue += 0.025f;
        else if (x == 3) addedValue += 0.075f;
        else if (x == 4) addedValue += 0.125f;
        else if (x == 5) addedValue += 0.125f;
        else if (x == 6) addedValue += 0.075f;
        else if (x == 7) addedValue += 0.025f;

        if (y == (pawn.START_RANK + pawn.ONE)) addedValue += 0.075f; // 3
        else if (y == (pawn.START_RANK + pawn.TWO)) addedValue += 0.125f; // 4
        else if (y == (pawn.START_RANK + pawn.ONE * 3)) addedValue += 0.13f; // 5
        else if (y == (pawn.START_RANK + pawn.ONE * 4)) addedValue += 0.5f; // 6
        else if (y == (pawn.START_RANK + pawn.ONE * 5)) addedValue += 3f; // 7
        else addedValue += 9f; // 8

        return 1 + addedValue;
    }
}
